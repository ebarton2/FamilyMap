package ebarton2.byu.cs240.familymap.userInterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.List;
import java.util.Locale;

import ebarton2.byu.cs240.familymap.DataCache;
import ebarton2.byu.cs240.familymap.R;
import model.Event;
import model.Person;

public class SearchActivity extends AppCompatActivity {

    private SearchView search;
    private List<Person> allPeople;
    private List<Event> allEvents;

    private static final int PERSON_ITEM_VIEW_TYPE = 0;
    private static final int EVENT_ITEM_VIEW_TYPE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        RecyclerView recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        search = findViewById(R.id.search_bar);
        search.setSubmitButtonEnabled(true);
        search.setIconifiedByDefault(false);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                if (allEvents != null) {
                    allEvents.clear();
                }
                if (allPeople != null) {
                    allPeople.clear();
                }

                allPeople = DataCache.instance().getAllSearchedPeople(query);
                allEvents = DataCache.instance().getAllSearchedEvents(query);

                SearchAdapter adapter = new SearchAdapter(allPeople, allEvents);
                recyclerView.setAdapter(adapter);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (allEvents != null) {
                    allEvents.clear();
                }
                if (allPeople != null) {
                    allPeople.clear();
                }

                allPeople = DataCache.instance().getAllSearchedPeople(newText);
                allEvents = DataCache.instance().getAllSearchedEvents(newText);

                SearchAdapter adapter = new SearchAdapter(allPeople, allEvents);
                recyclerView.setAdapter(adapter);
                return true;
            }
        });
    }

    private class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {
        private List<Person> allPeople;
        private List<Event> allEvents;

        public SearchAdapter(List<Person> allPeople, List<Event> allEvents) {
            this.allPeople = allPeople;
            this.allEvents = allEvents;
        }

        @Override
        public int getItemViewType(int position) {
            return position < allPeople.size() ? PERSON_ITEM_VIEW_TYPE : EVENT_ITEM_VIEW_TYPE;
        }

        @NonNull
        @Override
        public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;

            if (viewType == PERSON_ITEM_VIEW_TYPE) {
                view = getLayoutInflater().inflate(R.layout.person_item, parent, false);
            } else {
                view = getLayoutInflater().inflate(R.layout.event_item, parent, false);
            }
            return new SearchViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
            if (position < allPeople.size()) {
                holder.bind(allPeople.get(position));
            } else {
                holder.bind(allEvents.get(position - allPeople.size()));
            }
        }

        @Override
        public int getItemCount() {
            return allPeople.size() + allEvents.size();
        }
    }

    private class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView icon;
        private final TextView name;
        private final TextView eventDetails;

        private Person person;
        private Event event;

        private final int viewType;

        public SearchViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;

            itemView.setOnClickListener(this);

            if(viewType == PERSON_ITEM_VIEW_TYPE) {
                icon = itemView.findViewById(R.id.personIcon);
                name = itemView.findViewById(R.id.personName);
                eventDetails = null;
            } else {
                icon = itemView.findViewById(R.id.markerIcon);
                name = itemView.findViewById(R.id.eventNameDetails);
                eventDetails = itemView.findViewById(R.id.eventDetails);
            }
        }

        private void bind(Person person) {
            this.person = person;
            name.setText(person.getFirstName() + " " + person.getLastName());
            switch (person.getGender()) {
                case "m":
                    icon.setImageDrawable(new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_male).colorRes(R.color.blue).actionBarSize());
                    break;
                case "f":
                    icon.setImageDrawable(new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_female).colorRes(R.color.pink).actionBarSize());
                    break;
                default:
                    icon.setImageDrawable(new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_android).colorRes(R.color.green).actionBarSize());
            }
        }

        private void bind(Event event) {
            this.event = event;
            name.setText(DataCache.instance().getPersonById(event.getPersonID()).getFirstName() + " " + DataCache.instance().getPersonById(event.getPersonID()).getLastName());
            eventDetails.setText(event.getEventType().toUpperCase(Locale.US) + ": " + event.getCity() + ", " + event.getCountry() + " (" + event.getYear() + ")");
            icon.setImageDrawable(new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_map_marker).colorRes(R.color.black).actionBarSize());
        }

        @Override
        public void onClick(View v) {
            if (viewType == PERSON_ITEM_VIEW_TYPE) {
                Intent intent = new Intent(SearchActivity.this, PersonActivity.class);
                intent.putExtra("FIRST_NAME", person.getFirstName());
                intent.putExtra("LAST_NAME", person.getLastName());
                intent.putExtra("GENDER", person.getGender());
                intent.putExtra("PERSON_ID", person.getPersonID());
                startActivity(intent);

            } else {
                Intent intent = new Intent(SearchActivity.this, EventActivity.class);
                intent.putExtra("EVENT_ID", event.getEventID());
                startActivity(intent);

            }
        }
    }
}