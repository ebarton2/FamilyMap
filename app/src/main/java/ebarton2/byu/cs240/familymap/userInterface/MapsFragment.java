package ebarton2.byu.cs240.familymap.userInterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import ebarton2.byu.cs240.familymap.DataCache;
import ebarton2.byu.cs240.familymap.R;
import ebarton2.byu.cs240.familymap.model.Settings;
import model.Event;
import model.Person;

public class MapsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {

    private GoogleMap map;
    private Event selectedEvent;
    private List<Marker> mMarkerList = new ArrayList<>();
    private List<Polyline> mPolylineList = new ArrayList<>();
    private float LINE_SIZE = 16;
    private float ANCESTOR_LINE = 3;
    private Bundle args;

    ImageView eventIcon;
    TextView eventPersonName;
    TextView eventDetails;
    LinearLayout details;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        args = getArguments();
        /*if (args.containsKey("CURRENT_EVENT")) {
            selectedEvent = DataCache.instance().getEventById(args.getString("CURRENT_EVENT"));
            onResume();
        }*/

        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        eventIcon = (ImageView)view.findViewById(R.id.genderIcon);
        setEventIcon();

        eventPersonName = (TextView)view.findViewById(R.id.eventPersonName);

        eventDetails = (TextView)view.findViewById(R.id.eventInfo);

        details = (LinearLayout)view.findViewById(R.id.eventMarkerSelection);
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedEvent != null) {
                    Person person = DataCache.instance().getPersonById(selectedEvent.getPersonID());
                    Intent intent = new Intent(getActivity(), PersonActivity.class);
                    intent.putExtra("FIRST_NAME", person.getFirstName());
                    intent.putExtra("LAST_NAME", person.getLastName());
                    intent.putExtra("GENDER", person.getGender());
                    intent.putExtra("PERSON_ID", person.getPersonID());
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Something's wrong....", Toast.LENGTH_SHORT).show();
                }
            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapLoadedCallback(this);
        map.setOnMarkerClickListener(markerClickListener);

        populateMarkers();

        if (args.containsKey("CURRENT_EVENT")) {
            selectedEvent = DataCache.instance().getEventById(args.getString("CURRENT_EVENT"));
            onResume();
            LatLng latLng = new LatLng(selectedEvent.getLatitude(), selectedEvent.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    @Override
    public void onMapLoaded() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (map == null) {
            return;
        }

        map.clear();
        mMarkerList.clear();
        mPolylineList.clear();

        populateMarkers();

        if (selectedEvent != null) {
            if (isEventLegit(selectedEvent)) {
                updateEventInfo(selectedEvent);
                setPolyLines(selectedEvent);
            }
        }
    }

    private boolean isEventLegit(Event selectedEvent) {
        boolean test = true;
        Person person = DataCache.instance().getPersonById(selectedEvent.getPersonID());

        if (person == null) return false;

        switch (person.getGender()) {
            case "m":
                if (Settings.instance().getMaleEvents() == false) {
                    test = false;
                }
                break;
            case "f":
                if (Settings.instance().getFemaleEvents() == false) {
                    test = false;
                }
                break;
            default:
                test = false;
        }

        if (DataCache.instance().isPaternalAncestor(person) && !Settings.instance().getPaternalLines()) test = false;
        if (DataCache.instance().isMaternalAncestor(person) && !Settings.instance().getMaternalLines()) test = false;

        return test;
    }

    private GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            selectedEvent = (Event)marker.getTag();
            updateEventInfo(selectedEvent);
            LatLng latLng = new LatLng(selectedEvent.getLatitude(), selectedEvent.getLongitude());
            setPolyLines(selectedEvent);
            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            return true;
        }
    };

    private void populateMarkers() {
        Map<String, Event> events = DataCache.instance().getEvents();

        //Set<String> eventTypes = DataCache.instance().getEventTypes();
        Iterator<String> it = events.keySet().iterator();

        while (it.hasNext()) {
            Event event = events.get(it.next());
            Person person = DataCache.instance().getPersonById(event.getPersonID());

            if (Settings.instance().getMaleEvents() && Settings.instance().getFemaleEvents()) { // All Events are good to go
                if ((Settings.instance().getPaternalLines() && DataCache.instance().isPaternalAncestor(person)) // Father's side is good to go
                || (Settings.instance().getMaternalLines() && DataCache.instance().isMaternalAncestor(person)) // Mother's side is good to go
                || (!DataCache.instance().isPaternalAncestor(person) && !DataCache.instance().isMaternalAncestor(person))) { // Isn't an ancestor
                    LatLng latLng = new LatLng(event.getLatitude(), event.getLongitude());
                    Marker marker = map.addMarker(new MarkerOptions().position(latLng)
                            .icon(BitmapDescriptorFactory.defaultMarker(DataCache.instance().getEventTypeColors().get(event.getEventType().toLowerCase())))
                            .title(event.getEventType()));
                    marker.setTag(event);
                    mMarkerList.add(marker);
                }

            } else if (Settings.instance().getMaleEvents() && !Settings.instance().getFemaleEvents()) { // Only Male events are good
                if ((Settings.instance().getPaternalLines() && DataCache.instance().isPaternalAncestor(person)) // Father's side is good to go
                        || (Settings.instance().getMaternalLines() && DataCache.instance().isMaternalAncestor(person)) // Mother's side is good to go
                        || (!DataCache.instance().isPaternalAncestor(person) && !DataCache.instance().isMaternalAncestor(person))) { // Isn't an ancestor
                    if (DataCache.instance().getPersonById(event.getPersonID()).getGender().equals("m")) {
                        LatLng latLng = new LatLng(event.getLatitude(), event.getLongitude());
                        Marker marker = map.addMarker(new MarkerOptions().position(latLng)
                                .icon(BitmapDescriptorFactory.defaultMarker(DataCache.instance().getEventTypeColors().get(event.getEventType().toLowerCase())))
                                .title(event.getEventType()));
                        marker.setTag(event);
                        mMarkerList.add(marker);
                    }
                }

            } else if (!Settings.instance().getMaleEvents() && Settings.instance().getFemaleEvents()) { // Only Female events are good
                if ((Settings.instance().getPaternalLines() && DataCache.instance().isPaternalAncestor(person)) // Father's side is good to go
                        || (Settings.instance().getMaternalLines() && DataCache.instance().isMaternalAncestor(person)) // Mother's side is good to go
                        || (!DataCache.instance().isPaternalAncestor(person) && !DataCache.instance().isMaternalAncestor(person))) { // Isn't an ancestor
                    if (DataCache.instance().getPersonById(event.getPersonID()).getGender().equals("f")) {
                        LatLng latLng = new LatLng(event.getLatitude(), event.getLongitude());
                        Marker marker = map.addMarker(new MarkerOptions().position(latLng)
                                .icon(BitmapDescriptorFactory.defaultMarker(DataCache.instance().getEventTypeColors().get(event.getEventType().toLowerCase())))
                                .title(event.getEventType()));
                        marker.setTag(event);
                        mMarkerList.add(marker);
                    }
                }

            } else { // No Events are good
                System.out.println("Nothing to display");
            }
        }
    }

    private void setPolyLines(Event event) {
        for (Polyline polyline : mPolylineList) {
            polyline.remove();
        }
        mPolylineList.clear();

        Person person = DataCache.instance().getPersonById(event.getPersonID());
        if (Settings.instance().getLifeStoryLines()) {
            lifeEvents(person.getPersonID(), event);
        }
        LatLng selectedEventLoc = new LatLng(event.getLatitude(), event.getLongitude());

        String spouse = person.getSpouseID();

        if (spouse != null) {
            Event spouseEvent = getEarliestEvent(spouse);
            Person spousePerson = DataCache.instance().getPersonById(spouse);

            if ((!Settings.instance().getPaternalLines() && DataCache.instance().isPaternalAncestor(spousePerson))
            || (!Settings.instance().getMaternalLines() && DataCache.instance().isMaternalAncestor(spousePerson))) {
                System.out.println("Something is wonky with the parents");

            } else {

                if (spouseEvent != null && Settings.instance().getSpouseLines()) {
                    LatLng spouseLoc = new LatLng(spouseEvent.getLatitude(), spouseEvent.getLongitude());
                    Polyline polyline = map.addPolyline(new PolylineOptions().add(selectedEventLoc, spouseLoc).width(LINE_SIZE).color(Color.RED));
                    mPolylineList.add(polyline);
                }

            }
        }

        String fatherID = person.getFatherID();
        String motherID = person.getMotherID();

        if (Settings.instance().getFamilyTreeLines()) {
            if (fatherID != null) {
                if (Settings.instance().getMaleEvents()) {
                    Person father = DataCache.instance().getPersonById(person.getFatherID());
                    if ((DataCache.instance().isPaternalAncestor(father) && Settings.instance().getPaternalLines()) || DataCache.instance().isMaternalAncestor(father)) {
                        drawAncestorLines(fatherID, event, LINE_SIZE - ANCESTOR_LINE);
                    }
                }
            }
            if (motherID != null) {
                if (Settings.instance().getFemaleEvents()) {
                    Person mother = DataCache.instance().getPersonById(person.getMotherID());
                    if ((DataCache.instance().isMaternalAncestor(mother) && Settings.instance().getMaternalLines()) || DataCache.instance().isPaternalAncestor(mother)) {
                        drawAncestorLines(motherID, event, LINE_SIZE - ANCESTOR_LINE);
                    }
                }
            }
        }
    }

    private void lifeEvents(String personID, Event event) {
        List<Event> listLifeEvents = DataCache.instance().getPersonEvents(personID);

        for (int i = 1; i < listLifeEvents.size(); ++i) {
            LatLng firstEvent = new LatLng(listLifeEvents.get(i-1).getLatitude(), listLifeEvents.get(i-1).getLongitude());
            LatLng secondEvent = new LatLng(listLifeEvents.get(i).getLatitude(), listLifeEvents.get(i).getLongitude());
            Polyline polyline = map.addPolyline(new PolylineOptions().add(firstEvent, secondEvent).width(LINE_SIZE).color(Color.BLUE));
            mPolylineList.add(polyline);
        }
    }

    private Event getEarliestEvent(String personID) {
        List<Event> tempList = DataCache.instance().getPersonEvents(personID);
        Event chosenEvent = null;

        if (!tempList.isEmpty()) {
            chosenEvent = tempList.get(0);
            for (Event temp : tempList) {
                if (temp.getEventType().toLowerCase().equals("birth")) {
                    chosenEvent = temp;
                    return chosenEvent;
                }
                if (temp.getYear() < chosenEvent.getYear()) {
                    chosenEvent = temp;
                }
            }
        }
        return chosenEvent;
    }

    private void drawAncestorLines(String personID, Event event, float thiccLines) {
        if (thiccLines <= 0) {
            thiccLines = 1;
        }

        Person ancestor = DataCache.instance().getPersonById(personID);
        Event ancestorFirst = getEarliestEvent(personID);
        if (ancestorFirst != null) {
            LatLng selectedEventLoc = new LatLng(event.getLatitude(), event.getLongitude());
            LatLng ancestorEventLoc = new LatLng(ancestorFirst.getLatitude(), ancestorFirst.getLongitude());
            Polyline polyline = map.addPolyline(new PolylineOptions().add(selectedEventLoc, ancestorEventLoc).width(thiccLines).color(Color.YELLOW));
            mPolylineList.add(polyline);
        }
        String fatherID = ancestor.getFatherID();
        String motherID = ancestor.getMotherID();

        if (fatherID != null) {
            drawAncestorLines(fatherID, ancestorFirst, thiccLines - ANCESTOR_LINE);
        }
        if (motherID != null) {
            drawAncestorLines(motherID, ancestorFirst, thiccLines - ANCESTOR_LINE);
        }

    }

    private void setEventIcon() { eventIcon.setImageDrawable(new IconDrawable(getContext(), FontAwesomeIcons.fa_android).colorRes(R.color.green).actionBarSize());}

    private void updateEventInfo(Event selectedEvent) {
        Person person = DataCache.instance().getPersonById(selectedEvent.getPersonID());

        switch (person.getGender()) {
            case "m":
                eventIcon.setImageDrawable(new IconDrawable(getContext(), FontAwesomeIcons.fa_male).colorRes(R.color.blue).actionBarSize());
                break;
            case "f":
                eventIcon.setImageDrawable(new IconDrawable(getContext(), FontAwesomeIcons.fa_female).colorRes(R.color.pink).actionBarSize());
                break;
            default:
                eventIcon.setImageDrawable(new IconDrawable(getContext(), FontAwesomeIcons.fa_android).colorRes(R.color.green).actionBarSize());
        }

        eventPersonName.setText(person.getFirstName() + " " + person.getLastName());
        eventDetails.setText(selectedEvent.getEventType().toUpperCase(Locale.US) + ":\n" + selectedEvent.getCity() + ", " + selectedEvent.getCountry() + "\n(" + selectedEvent.getYear() + ")");
    }
}