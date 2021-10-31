package ebarton2.byu.cs240.familymap.userInterface;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ebarton2.byu.cs240.familymap.DataCache;
import ebarton2.byu.cs240.familymap.R;
import model.Event;
import model.Person;

public class PersonActivity extends AppCompatActivity {

    private String firstName;
    private String lastName;
    private String gender;
    private String personID;

    private TextView first;
    private TextView last;
    private TextView genderAssignment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        firstName = getIntent().getStringExtra("FIRST_NAME");
        lastName = getIntent().getStringExtra("LAST_NAME");
        gender = getIntent().getStringExtra("GENDER");
        personID = getIntent().getStringExtra("PERSON_ID");

        first = findViewById(R.id.personFirstName);
        TextView firstNameDesc = findViewById(R.id.personFirstNameTitle);
        firstNameDesc.setText(R.string.firstName);
        last = findViewById(R.id.personLastName);
        TextView lastNameDesc = findViewById(R.id.personLastNameTitle);
        lastNameDesc.setText(R.string.lastName);
        genderAssignment = findViewById(R.id.personGender);
        TextView genderDesc = findViewById(R.id.personGenderTitle);
        genderDesc.setText(R.string.gender);

        first.setText(firstName);
        last.setText(lastName);
        switch(gender) {
            case "m":
                genderAssignment.setText("Male");
                break;
            case "f":
                genderAssignment.setText("Female");
                break;
            default:
                genderAssignment.setText("Non-Binary");
                break;
        }


        ExpandableListView expandableListView = findViewById(R.id.expandableList);

        List<Person> personList = composeFamily(personID);
        List<Event> eventList = DataCache.instance().getPersonEvents(personID);

        expandableListView.setAdapter(new PersonExpandableAdapter(eventList, personList));

    }

    private List<Person> composeFamily(String personID) {
        List<Person> temp = new ArrayList<>();
        Person person = DataCache.instance().getPersonById(personID);
        String fatherID = person.getFatherID();
        String motherID = person.getMotherID();
        String spouseID = person.getSpouseID();

        if (fatherID != null) {
            temp.add(DataCache.instance().getPersonById(fatherID));
        }
        if (motherID != null) {
            temp.add(DataCache.instance().getPersonById(motherID));
        }
        if (spouseID != null) {
            temp.add(DataCache.instance().getPersonById(spouseID));
        }
        temp.addAll(DataCache.getPersonChildren(personID));
        return temp;
    }

    // TODO private void

    private class PersonExpandableAdapter extends BaseExpandableListAdapter {

        private static final int EVENT_GROUP_POSITION = 0;
        private static final int PERSON_GROUP_POSITION = 1;

        private final List<Event> eventList;
        private final List<Person> personList;

        PersonExpandableAdapter(List<Event> eventList, List<Person> personList) {
            this.eventList = eventList;
            this.personList = personList;
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case EVENT_GROUP_POSITION:
                    return eventList.size();
                case PERSON_GROUP_POSITION:
                    return personList.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch (groupPosition) {
                case EVENT_GROUP_POSITION:
                    return getString(R.string.life_events);
                case PERSON_GROUP_POSITION:
                    return getString(R.string.family);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (groupPosition) {
                case EVENT_GROUP_POSITION:
                    return eventList.get(childPosition);
                case PERSON_GROUP_POSITION:
                    return personList.get(childPosition);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_group, parent, false);
            }

            TextView titleView = convertView.findViewById(R.id.listTitle);

            switch (groupPosition) {
                case EVENT_GROUP_POSITION:
                    titleView.setText(R.string.life_events);
                    break;
                case PERSON_GROUP_POSITION:
                    titleView.setText(R.string.family);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView;

            switch(groupPosition) {
                case EVENT_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.event_item, parent, false);
                    initializeEventView(itemView, childPosition);
                    break;
                case PERSON_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.person_item, parent, false);
                    initializePersonView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return itemView;
        }

        private void initializeEventView(View eventItemView, final int childPosition) {
            ImageView eventImage = eventItemView.findViewById(R.id.markerIcon);
            eventImage.setImageDrawable(new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_map_marker).colorRes(R.color.black).actionBarSize());

            TextView eventDescriptionView = eventItemView.findViewById(R.id.eventDetails);
            eventDescriptionView.setText(eventList.get(childPosition).getEventType().toUpperCase(Locale.US) + ": " + eventList.get(childPosition).getCity()
                    + ", " + eventList.get(childPosition).getCountry() + " (" + eventList.get(childPosition).getYear() + ")");

            TextView eventNameView = eventItemView.findViewById(R.id.eventNameDetails);
            eventNameView.setText(DataCache.instance().getPersonById(eventList.get(childPosition).getPersonID()).getFirstName()
                    + " " + DataCache.instance().getPersonById(eventList.get(childPosition).getPersonID()).getLastName());

            eventItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PersonActivity.this, EventActivity.class);
                    intent.putExtra("EVENT_ID", eventList.get(childPosition).getEventID());
                    startActivity(intent);

                    Toast.makeText(PersonActivity.this, "You clicked on an Event", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void initializePersonView(View personItemView, final int childPosition) {
            Person tempPerson = DataCache.instance().getPersonById(personID);
            Person relative = personList.get(childPosition);

            ImageView personImage = personItemView.findViewById(R.id.personIcon);
            switch (relative.getGender()) {
                case "m":
                    personImage.setImageDrawable(new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_male).colorRes(R.color.blue).actionBarSize());
                    break;
                case "f":
                    personImage.setImageDrawable(new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_female).colorRes(R.color.pink).actionBarSize());
                    break;
                default:
                    personImage.setImageDrawable(new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_android).colorRes(R.color.green).actionBarSize());
            }

            TextView personDescriptionView = personItemView.findViewById(R.id.personName);
            personDescriptionView.setText(relative.getFirstName() + " " + relative.getLastName());

            TextView personRelationView = personItemView.findViewById(R.id.personRelation);
            if (relative.getPersonID().equals(tempPerson.getFatherID())) {
                personRelationView.setText("Father");
            } else if (relative.getPersonID().equals(tempPerson.getMotherID())) {
                personRelationView.setText("Mother");
            } else if (relative.getPersonID().equals(tempPerson.getSpouseID())) {
                personRelationView.setText("Spouse");
            } else {
                personRelationView.setText("Child");
            }

            personItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
                    intent.putExtra("FIRST_NAME", relative.getFirstName());
                    intent.putExtra("LAST_NAME", relative.getLastName());
                    intent.putExtra("GENDER", relative.getGender());
                    intent.putExtra("PERSON_ID", relative.getPersonID());
                    startActivity(intent);

                    //Toast.makeText(PersonActivity.this, "You clicked on a Person", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}