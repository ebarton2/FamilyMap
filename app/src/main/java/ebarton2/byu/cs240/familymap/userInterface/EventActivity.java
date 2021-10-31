package ebarton2.byu.cs240.familymap.userInterface;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import ebarton2.byu.cs240.familymap.DataCache;
import ebarton2.byu.cs240.familymap.R;
import model.Event;

public class EventActivity extends AppCompatActivity /*implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback*/ {

    //private GoogleMap mMap;

    private MapsFragment mMapsFragment;
    private FragmentManager fm;

    private Event currentEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Will fit hit the shan");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event2);

        System.out.println("We are trying our best");

        currentEvent = DataCache.getEventById(getIntent().getStringExtra("EVENT_ID"));

        //System.out.println();

        Iconify.with(new FontAwesomeModule());

        fm = this.getSupportFragmentManager();


        mMapsFragment = (MapsFragment) fm.findFragmentById(R.id.fragmentContainer2);
        if (mMapsFragment == null) {

            mMapsFragment = new MapsFragment();
            Bundle args = new Bundle();
            args.putString("CURRENT_EVENT", currentEvent.getEventID());
            mMapsFragment.setArguments(args);
        }
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fm.beginTransaction().add(R.id.fragmentContainer2, mMapsFragment).commit();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    /*@Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }*/

    /*@Override
    public void onMapLoaded() {

    }*/
}