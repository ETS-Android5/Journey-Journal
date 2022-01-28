package ws.billy.app.Code.Activities.Journal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import ws.billy.app.Code.Activities.JournalList.JournalList;
import ws.billy.app.Code.LoginSignup.LoginSignup.Login;
import ws.billy.app.Code.Utility.Journal;
import ws.billy.app.Code.Utility.JournalManager;
import ws.billy.app.R;

import java.util.ArrayList;
import java.util.Objects;

public class JournalView extends AppCompatActivity implements View.OnClickListener {
    ArrayList<Comment> rowListItem;
    JournalAdapter rcAdapter;
    Journal clickedJournal;

    private EditText postText;

    private android.app.AlertDialog dialog;

    @Override
    public void onBackPressed () {
        if(dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
                return;
            }
        }

        // fixes being able to go back to this screen
        Intent intent = new Intent(this, JournalList.class);
        startActivity(intent);

    }

    public android.app.AlertDialog createEditDialog() {
        // create the alert dialog
        android.app.AlertDialog dialog;
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogForm = inflater.inflate(R.layout.edit_dialog, null, false);

        // register buttons
        Button buttonCreate = (Button) dialogForm.findViewById(R.id.buttonEditConfirm);
        postText = (EditText) dialogForm.findViewById(R.id.journalInputEditText);

        // set text so it can be edited by user
        postText.setText(clickedJournal.getText());

        // register this class to listen to the dialog responses
        buttonCreate.setOnClickListener(this);

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
        setContentView(R.layout.specificjournalview_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }


        final NavigationView navigationViewLeft = (NavigationView) findViewById(R.id.nav_view);
        View navLeftLay = navigationViewLeft.getHeaderView(0);
        Space spaceLeftTop = (Space) navLeftLay.findViewById(R.id.spaceLeftTop);
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
            spaceLeftTop.setVisibility(View.VISIBLE);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // fetch the clicked post
        clickedJournal = JournalManager.journals.get(JournalList.selectedID);

        ImageView img1 = (ImageView) findViewById(R.id.image1);
        String urlPost = clickedJournal.getUrl();
        loadImageRequest(img1, urlPost);

        String friend1Url = clickedJournal.getProurl();

        if(friend1Url == null) {
            friend1Url = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png"; // default avatar
        } else {
            friend1Url = clickedJournal.getProurl();
        }

        ImageView imgProfile1 = (ImageView) findViewById(R.id.imgProfile1);
        loadImageSquareRequest(imgProfile1, friend1Url);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        TextView content = findViewById(R.id.editTextContent);
        content.setText(clickedJournal.getText());

        // hide location button if no geotag
        if(clickedJournal.getGeolocation() == null) {
            TextView loc = findViewById(R.id.buttonLocation);
            loc.setVisibility(View.GONE);
        }

        // Hide buttons if not the owner
        System.out.println(clickedJournal.getUserid());
        System.out.println(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        if(!clickedJournal.getUserid().equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())) {
            findViewById(R.id.buttonEdit).setVisibility(View.GONE);
            findViewById(R.id.buttonDelete).setVisibility(View.GONE);
        }
    }

    private void loadImageSquareRequest(ImageView img, String url){
        Glide.with(this)
                .load(url)
                .into(img);
    }

    private void loadImageRequest(ImageView bg, String url) {
        Glide.with(this)
                .load(url)
                .thumbnail(0.01f)
                .centerCrop()
                .crossFade()
                .into(bg);
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.buttonEdit) {
            // show dialog
            dialog = createEditDialog();
            dialog.show();
        }

        if(view.getId() == R.id.buttonLocation) {

            Intent intent = new Intent(this, JournalLocation.class);
            startActivity(intent);

        }

        if(view.getId() == R.id.buttonEditConfirm) {
            JournalManager.editJournal(clickedJournal, postText.getText().toString());
            dialog.dismiss();
            Intent intent = new Intent(this, JournalView.class);
            startActivity(intent);
        }

        if(view.getId() == R.id.buttonDelete) {
            JournalManager.deleteJournal(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(), clickedJournal);
            Intent intent = new Intent(this, JournalList.class);
            startActivity(intent);
        }

    }
}
