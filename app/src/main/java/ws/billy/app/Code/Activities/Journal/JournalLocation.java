package ws.billy.app.Code.Activities.Journal;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import ws.billy.app.Code.Activities.JournalList.JournalList;
import ws.billy.app.Code.Utility.Journal;
import ws.billy.app.Code.Utility.JournalManager;
import ws.billy.app.R;

public class JournalLocation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng position;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Loads once the map is ready, zooms to the journal location
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        // fetch the clicked post
        Journal clickedJournal = JournalManager.journals.get(JournalList.selectedID);

        position = new LatLng(clickedJournal.getGeolocation().getLatitude(), clickedJournal.getGeolocation().getLongitude());

        mMap = googleMap;
        //changing map type
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        marker = mMap.addMarker( new MarkerOptions().position(position)
                .title("Journal Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 2));

    }
}

