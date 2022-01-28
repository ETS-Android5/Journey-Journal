package ws.billy.app.Code.Activities.JournalList;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Space;
import android.widget.Toast;

import ws.billy.app.Code.Activities.Journal.JournalView;
import ws.billy.app.Code.LoginSignup.SplashScreen.SplashScreen;
import ws.billy.app.Code.Utility.Journal;
import ws.billy.app.Code.Utility.JournalManager;
import ws.billy.app.MainActivity;
import ws.billy.app.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;

public class JournalList extends AppCompatActivity implements View.OnClickListener, JournalClickListener {
    ArrayList<Journal> rowListItem;

    private android.app.AlertDialog dialog;
    private EditText postText;
    private Calendar FirebaseDatabase;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference journalRef = db.collection("journals");
    private JournalListAdapter adapter;
    private CheckBox geolocationenabled;
    private FusedLocationProviderClient fusedLocationClient;


    public static int selectedID = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity3_menu, menu);
        return true;
    }

    public android.app.AlertDialog createPostDialog() {
        // create the alert dialog
        android.app.AlertDialog dialog;
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogForm = inflater.inflate(R.layout.create_dialog, null, false);

        // register buttons
        Button buttonCreate = (Button) dialogForm.findViewById(R.id.buttonCreate);
        Button buttonUpload = (Button) dialogForm.findViewById(R.id.buttonImage);
        geolocationenabled = (CheckBox) dialogForm.findViewById(R.id.checkBox);
        postText = (EditText) dialogForm.findViewById(R.id.journalInputText);

        // register this class to listen to the dialog responses
        buttonCreate.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);

        // build and return
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setView(dialogForm);
        builder.create();
        dialog = builder.create();
        return dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journallist_layout);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }

        // ADD SPACE TOP DRAWER ON LOLLIPOP AND UP
        final NavigationView navigationViewLeft = (NavigationView) findViewById(R.id.nav_view);
        View navLeftLay = navigationViewLeft.getHeaderView(0);
        Space spaceLeftTop = (Space) navLeftLay.findViewById(R.id.spaceLeftTop);
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            spaceLeftTop.setVisibility(View.VISIBLE);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        RecyclerView rView = (RecyclerView) findViewById(R.id.recyclerView);
        rView.setHasFixedSize(false);
        rView.setLayoutManager(layoutManager);
        rView.setNestedScrollingEnabled(false);

        Query query = FirebaseFirestore.getInstance().collection("journals").orderBy("timestamp", Query.Direction.DESCENDING).limit(10);

        FirestoreRecyclerOptions<Journal> options = new FirestoreRecyclerOptions.Builder<Journal>().setQuery(query, Journal.class).build();
        adapter = new JournalListAdapter(this, options);
        rView.setAdapter(adapter);
        adapter.setClickListener(this);

        if (JournalManager.journals == null) {
            JournalManager.journals = new ArrayList<>();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {

            // show dialog
            dialog = createPostDialog();
            dialog.show();

        }

        if (item.getItemId() == R.id.action_logout) {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        // cancels back pressed on this screen unless it's dialog
    }

    @Override
    protected void onStart() {
        super.onStart();
        JournalManager.journals.clear();
        adapter.startListening();
    }

    @Override
    public void itemClicked(View view, int position) {

        // open specific journal
        selectedID = position;

        try {
            Intent intent = new Intent(this, JournalView.class);
            startActivity(intent);
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void onClick(View v) {

        // create post button
        if (v.getId() == R.id.buttonCreate) {

            String imageUrl = "https://i.imgur.com/2qLwcGJ.jpg";
            final GeoPoint[] geolocation = {null};

            if (geolocationenabled.isChecked()) {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // If we don't have permissions, tell the person they have it disabled.
                    Toast.makeText(JournalList.this, "Location permissions disabled", Toast.LENGTH_LONG).show();
                } else {

                    geolocation[0] = new GeoPoint(54.978252, -1.61778);

                    fusedLocationClient.getLastLocation()
                            .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    // Got last known location. In some rare situations this can be null.
                                    if (location != null) {
                                        geolocation[0] = new GeoPoint(location.getLatitude(), location.getLongitude());
                                    }
                                }
                            });

                }
            }

            JournalManager.newJournal(imageUrl, postText.getText().toString(), Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()), geolocation[0]);
            dialog.dismiss();
            Toast.makeText(JournalList.this, "Created a journal entry!", Toast.LENGTH_LONG).show();

        }

    }
}
