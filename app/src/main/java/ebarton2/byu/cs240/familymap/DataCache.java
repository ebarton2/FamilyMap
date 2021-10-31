package ebarton2.byu.cs240.familymap;

import java.util.*;

import ebarton2.byu.cs240.familymap.model.Settings;
import model.Event;
import model.Person;

public class DataCache {
    private static DataCache _instance = new DataCache();

    private Map<String, Person> people; // Links a PersonID to a Person object
    private Map<String, Event> events; // Links an EventID to an Event object
    private List<Event> allEvents;
    private Map<String, List<Event>> personEvents; // Links a PersonID to an Event Object
    private Settings settings; // TODO
    private Set<String> eventTypes; // Stores all possible event types
    private Map<String, Float> eventTypeColors; // Links an event to a color
    private Person user; // Has been added to at server connection success
    private Set<String> paternalAncestors; // Stores all paternal ancestors of user
    private Set<String> maternalAncestors; // Stores all maternal ancestors of user
    private Map<String, List<Person>> personChildren; // Links each person to their children
    private static float[] COLORS = {(float)210.0, (float) 240.0, (float) 180.0, (float) 120.0,
            (float) 300.0, (float) 30.0, (float) 0.0, (float) 330.0,
            (float) 270.0, (float) 60.0};
    private boolean loginState;
    private List<Event> maleEvents;
    private List<Event> femaleEvents;

    private DataCache() {
        people = new HashMap<>();
        events = new HashMap<>();
        personEvents = new HashMap<>();
        allEvents = new ArrayList<>();
        eventTypes = new HashSet<>();
        eventTypeColors = new HashMap<>();
        user = null;
        paternalAncestors = new HashSet<>();
        maternalAncestors = new HashSet<>();
        personChildren = new HashMap<>();
        loginState = false;
        maleEvents = new ArrayList<>();
        femaleEvents = new ArrayList<>();
    }

    public void clear() { _instance._clear(); }

    private void _clear() { _instance = new DataCache(); }

    public void fillPersonEvents() {
        _instance._fillPersonEvents();
    }

    private void _fillPersonEvents() {
        Iterator<String> iter = people.keySet().iterator();
        while (iter.hasNext()) {
            String personID = iter.next();
            List<Event> eventList = new ArrayList<>();
            personEvents.put(personID, eventList);
        }

        Iterator<String> it = events.keySet().iterator();
        while (it.hasNext()) {
            String eventID = it.next();
            String personalID = events.get(eventID).getPersonID();
            personEvents.get(personalID).add(events.get(eventID));
        }

        Iterator<String> anotherOne = personEvents.keySet().iterator();
        while (anotherOne.hasNext()) {
            String personID = anotherOne.next();
            sortPersonEvents(personID);

            switch (people.get(personID).getGender()) {
                case "m":
                    maleEvents.addAll(personEvents.get(personID));
                    break;
                case "f":
                    femaleEvents.addAll(personEvents.get(personID));
                    break;
                default:
                    System.out.println("Somethings wrong");
            }
        }
    }

    private void sortPersonEvents(String personID) {
        List<Event> temp1 = personEvents.get(personID);
        List<Event> sorted = new ArrayList<>();
        Event death = null;

        for (int i = 0; i < temp1.size(); ++i) { // Finds birth
            if (temp1.get(i).getEventType().toLowerCase().equals("birth")) {
                sorted.add(temp1.get(i));
                temp1.remove(i);
                break;
            }
        }

        for (int i = 0; i < temp1.size(); ++i) { // Finds death
            if (temp1.get(i).getEventType().toLowerCase().equals("death")) {
                death = temp1.get(i);
                temp1.remove(i);
                break;
            }
        }

        while (!temp1.isEmpty()) {
            Event chosenOne = temp1.get(0);
            int index = 0;

            for (int i = 0; i < temp1.size(); ++i) {

                if (chosenOne.getYear() > temp1.get(i).getYear()) {
                    chosenOne = temp1.get(i);
                    index = i;

                } else if (chosenOne.getYear() == temp1.get(i).getYear()) {
                    if (!chosenOne.getEventID().equals(temp1.get(i).getEventID())) {
                        if (chosenOne.getEventType().compareToIgnoreCase(temp1.get(i).getEventType()) > 0) {
                            chosenOne = temp1.get(i);
                        }
                    }
                }

            }
            sorted.add(chosenOne);
            temp1.remove(index);
        }

        if (death != null) {
            sorted.add(death);
        }

        personEvents.get(personID).clear();
        personEvents.get(personID).addAll(sorted);
    }



    public void setLoginState(boolean loginState) {
        _instance.setLogin(loginState);
    }

    private void setLogin(boolean login) {
        this.loginState = login;
    }

    public boolean getLoginState() {
        return _instance.getLogin();
    }

    private boolean getLogin() {
        return loginState;
    }



    public static DataCache instance() {
        return _instance;
    }



    public static void setUser(Person p) {
        _instance._setUser(p);
    }

    private void _setUser(Person p) {
        user = p;
    }

    public static Person findUser(String personID) {
        return _instance._findUser(personID);
    }

    private Person _findUser(String personID) {
        return people.get(personID);
    }

    public static Person getUser() {
        return _instance._getUser();
    }

    private Person _getUser() {
        return user;
    }

    public static boolean hasUser() {
        return _instance._hasUser();
    }

    private boolean _hasUser() {
        if (user != null) {
            return true;
        }
        return false;
    }



    public static void addPerson(String personID, Person p) {
        _instance._addPerson(personID, p);
    }

    private void _addPerson(String personID, Person p) {
        people.put(personID, p);
    }



    public static List<Person> getAllPeople() {
        return _instance._getAllPeople();
    }

    private List<Person> _getAllPeople() {
        List<Person> tempPeople = new ArrayList<>();

        Iterator<String> iter = people.keySet().iterator();

        while(iter.hasNext()) {
            tempPeople.add(people.get(iter.next()));
        }

        return tempPeople;
    }



    public static Person getPersonById(String id) {
        return _instance._getPersonById(id);
    }

    private Person _getPersonById(String id) {
        return people.get(id);
    }



    public static void addEvent(String eventID, Event e) {
        _instance._addEvent(eventID, e);
    }

    private void _addEvent(String eventID, Event e) {
        events.put(eventID, e);
        allEvents.add(e);
    }

    public Map<String, Event> getEvents() {
        return _instance._getEvents();
    }

    private Map<String, Event> _getEvents() {
        return events;
    }

    public static List<Event> getAllEvents() {
        return _instance._getAllEvents();
    }

    private List<Event> _getAllEvents() {
        List<Event> tempPeople = new ArrayList<>();

        Iterator<String> iter = events.keySet().iterator();

        while(iter.hasNext()) {
            tempPeople.add(events.get(iter.next()));
        }

        return tempPeople;
    }


    public static List<Person> getAllSearchedPeople(String search) {
        return _instance._getAllSearchedPeople(search);
    }

    private List<Person> _getAllSearchedPeople(String query) {
        List<Person> temp = new ArrayList<>();
        String search = query.toLowerCase();

        Iterator<String> iter = people.keySet().iterator();
        while (iter.hasNext()) {
            Person tempPerson = people.get(iter.next());
            if (tempPerson.getFirstName().toLowerCase().contains(search) || tempPerson.getLastName().toLowerCase().contains(search)) {
                temp.add(tempPerson);
            }
        }
        return temp;
    }

    public static List<Event> getAllSearchedEvents(String search) {
        return _instance._getAllSearchedEvents(search);
    }

    private List<Event> _getAllSearchedEvents(String search) {
        List<Event> temp = new ArrayList<>();

        if (Settings.instance().getMaleEvents() && Settings.instance().getFemaleEvents()) { // Male and Female are good
            Iterator<String> iter = events.keySet().iterator();
            while (iter.hasNext()) {
                Event tempEvent = events.get(iter.next());
                if (tempEvent.getEventType().toLowerCase().contains(search.toLowerCase())
                        || tempEvent.getCountry().toLowerCase().contains(search.toLowerCase())
                        || tempEvent.getCity().toLowerCase().contains(search.toLowerCase())
                        || Integer.toString(tempEvent.getYear()).toLowerCase().contains(search.toLowerCase())) {

                    if ((Settings.instance().getPaternalLines() && _isPaternalAncestor(people.get(tempEvent.getPersonID())))
                            || (Settings.instance().getMaternalLines() && _isMaternalAncestor(people.get(tempEvent.getPersonID())))
                            || (!_isPaternalAncestor(people.get(tempEvent.getPersonID())) && !_isMaternalAncestor(people.get(tempEvent.getPersonID())))) {
                        temp.add(tempEvent);
                    }
                }
            }

        } else if (Settings.instance().getMaleEvents() && !Settings.instance().getFemaleEvents()) { // Male is good
            Iterator<Event> iter = maleEvents.iterator();
            while (iter.hasNext()) {
                Event tempEvent = iter.next();

                if (tempEvent.getEventType().toLowerCase().contains(search.toLowerCase())
                        || tempEvent.getCountry().toLowerCase().contains(search.toLowerCase())
                        || tempEvent.getCity().toLowerCase().contains(search.toLowerCase())
                        || Integer.toString(tempEvent.getYear()).toLowerCase().contains(search.toLowerCase())) {

                    if ((Settings.instance().getPaternalLines() && _isPaternalAncestor(people.get(tempEvent.getPersonID())))
                            || (Settings.instance().getMaternalLines() && _isMaternalAncestor(people.get(tempEvent.getPersonID())))
                            || (!_isPaternalAncestor(people.get(tempEvent.getPersonID())) && !_isMaternalAncestor(people.get(tempEvent.getPersonID())))) {
                        temp.add(tempEvent);
                    }
                }
            }

        } else if (!Settings.instance().getMaleEvents() && Settings.instance().getFemaleEvents()) { // Female is good
            Iterator<Event> iter = femaleEvents.iterator();
            while (iter.hasNext()) {
                Event tempEvent = iter.next();

                if (tempEvent.getEventType().toLowerCase().contains(search.toLowerCase())
                        || tempEvent.getCountry().toLowerCase().contains(search.toLowerCase())
                        || tempEvent.getCity().toLowerCase().contains(search.toLowerCase())
                        || Integer.toString(tempEvent.getYear()).toLowerCase().contains(search.toLowerCase())) {

                    if ((Settings.instance().getPaternalLines() && _isPaternalAncestor(people.get(tempEvent.getPersonID())))
                            || (Settings.instance().getMaternalLines() && _isMaternalAncestor(people.get(tempEvent.getPersonID())))
                            || (!_isPaternalAncestor(people.get(tempEvent.getPersonID())) && !_isMaternalAncestor(people.get(tempEvent.getPersonID())))) {
                        temp.add(tempEvent);
                    }
                }
            }

        }
        return temp;
    }



    public static Event getEventById(String id) {
        return _instance._getEventById(id);
    }

    private Event _getEventById(String id) {
        return events.get(id);
    }



    public static Set<String> getEventTypes() { // For Accessing the eventTypes Set object
        return _instance._getEventTypes();
    }

    private Set<String> _getEventTypes() {
        return eventTypes;
    }

    public void addEventType(String eventType) { // For Adding to Event Types
        _instance._addEventType(eventType);
    }

    private void _addEventType(String eventType) {
        eventTypes.add(eventType.toLowerCase());
    }



    public static Map<String, Float> getEventTypeColors() {
        return _instance._getEventTypeColors();
    }

    private Map<String, Float> _getEventTypeColors() {
        return eventTypeColors;
    }

    public void fillEventColors() {
        _instance._fillEventColors();
    }

    private void _fillEventColors() {
        Iterator<String> it = eventTypes.iterator();
        int i = 0;
        while (it.hasNext()) {
            String eventType = it.next();
            eventTypeColors.put(eventType, COLORS[i % COLORS.length]);
            System.out.println(eventType + " " + COLORS[i % COLORS.length]);
            ++i;
        }
    }



    public static boolean isPaternalAncestor(Person p) {
        return _instance._isPaternalAncestor(p);
    }

    private boolean _isPaternalAncestor(Person p) {
        boolean test = false;
        Iterator<String> iter = paternalAncestors.iterator();
        while (iter.hasNext()) {
            if (p.getPersonID().equals(iter.next())) {
                return true;
            }
        }

        return test;
    }

    public static boolean isMaternalAncestor(Person p) {
        return _instance._isMaternalAncestor(p);
    }

    private boolean _isMaternalAncestor(Person p) {
        boolean test = false;
        Iterator<String> iter = maternalAncestors.iterator();
        while (iter.hasNext()) {
            if (p.getPersonID().equals(iter.next())) {
                return true;
            }
        }
        return test;
    }

    public void setAncestors() {
        _instance._setAncestors();
    } // TODO CALL IN LOGIN

    private void _setAncestors() {
        recursivePaternalAncestor(user.getFatherID());
        recursiveMaternalAncestor(user.getMotherID());
    }

    private void recursivePaternalAncestor(String personID) {
        if (personID == null) return;
        System.out.println("Paternal: " + personID);
        paternalAncestors.add(personID);
        recursivePaternalAncestor(people.get(personID).getFatherID());
        recursivePaternalAncestor(people.get(personID).getMotherID());
    }

    private void recursiveMaternalAncestor(String personID) {
        if (personID == null) return;
        System.out.println("Maternal: " + personID);
        maternalAncestors.add(personID);
        recursiveMaternalAncestor(people.get(personID).getMotherID());
        recursiveMaternalAncestor(people.get(personID).getFatherID());
    }


    // Gets a list of Events connected to a personID
    public static List<Event> getPersonEvents(String personID) {
        return _instance._getPersonEvents(personID);
    }

    private List<Event> _getPersonEvents(String personID) { // TODO Do stuff here next!!!!!!!!!!!!!!!!
        List<Event> temp = new ArrayList<>();
        List<Event> stuff = personEvents.get(personID);
        Person person = people.get(personID);

        switch (person.getGender()) {
            case "m":
                System.out.println("Is considered");
                if (Settings.instance().getMaleEvents()) {
                    System.out.println(person.getFirstName() + " was able to get events.");
                    if ((_instance._isPaternalAncestor(person) && Settings.instance().getPaternalLines())
                    || (_instance._isMaternalAncestor(person) && Settings.instance().getMaternalLines())
                    || (!_instance._isMaternalAncestor(person) && !_instance._isPaternalAncestor(person))) {
                        return stuff;
                    }
                }
                break;
            case "f":
                System.out.println("Females are considered");
                if (Settings.instance().getFemaleEvents()) {
                    System.out.println();
                    if ((_instance._isPaternalAncestor(person) && Settings.instance().getPaternalLines())
                            || (_instance._isMaternalAncestor(person) && Settings.instance().getMaternalLines())
                            || (!_instance._isMaternalAncestor(person) && !_instance._isPaternalAncestor(person))) {
                        return stuff;
                    }
                }
                break;
            default:
                return temp;
        }
        return temp;
    }



    public static List<Event> getFilteredEvents() {
        return _instance._getFilteredEvents();
    }

    private List<Event> _getFilteredEvents() {
        return null;
    }


    public void setPersonChildren() { // TODO CALL IN LOGIN
        _instance._setPersonChildren();
    }

    private void _setPersonChildren() {
        Iterator<String> iter = people.keySet().iterator();
        while (iter.hasNext()) {
            String personID = iter.next();
            List<Person> childList = new ArrayList<>();
            personChildren.put(personID, childList);
        }

        Iterator<String> it = people.keySet().iterator();
        while (it.hasNext()) {
            String personID = it.next();
            String fatherID = people.get(personID).getFatherID();
            String motherID = people.get(personID).getMotherID();
            if (fatherID != null) {
                personChildren.get(fatherID).add(people.get(personID));
            }
            if (motherID != null) {
                personChildren.get(motherID).add(people.get(personID));
            }
        }
    }

    public static List<Person> getPersonChildren(String personID) {
        return _instance._getPersonChildren(personID);
    }

    private List<Person> _getPersonChildren(String personID) {
        return personChildren.get(personID);
    }

}