package ebarton2.byu.cs240.familymap;

import org.junit.Test;

import java.util.List;

import ebarton2.byu.cs240.familymap.userInterface.PersonActivity;
import model.Event;
import model.Person;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */



public class DataCacheTest {
    private Person me = new Person("ME_1", "Groot", "McKay", "Barton", "m", "DAD_1", "MOM_1", "SPOUSE_1");
    private Person mom = new Person("MOM_1", "Groot", "Nikki", "Barton", "f", null, null, "DAD_1");
    private Person dad = new Person("DAD_1", "Groot", "Ed", "Barton", "m", null, null, "MOM_1");
    private Person spouse = new Person("SPOUSE_1", "Groot", "Future", "Spouse", "f", null, null, "ME_1");

    private Event myBirth = new Event("MY_BIRTH", "Groot", "ME_1", 10.0f, 10.0f, "Provo", "UT", "birth", 1999);
    private Event myDeath = new Event("MY_DEATH", "Groot", "ME_1", 100.0f, 100.0f, "Lehi", "UT", "death", 2021);
    private Event myMarriage = new Event("MY_MARRIAGE", "Groot", "ME_1", 190.0f, 190.0f, "Payson", "UT", "marriage", 1999);
    private Event dadBirth = new Event("DAD_BIRTH", "Groot", "DAD_1", 20.0f, 20.0f, "Orem", "UT", "birth", 1999);
    private Event spouseBirth = new Event("SPOUSE_BIRTH", "Groot", "SPOUSE_1", 70.0f, 70.0f, "Nashville", "TN", "birth", 1999);


    @Test
    public void insertPeopleTest() {
        DataCache.instance().setUser(me);
        DataCache.instance().addPerson(me.getPersonID(), me);
        DataCache.instance().addPerson(mom.getPersonID(), mom);
        DataCache.instance().addPerson(dad.getPersonID(), dad);
        DataCache.instance().addPerson(spouse.getPersonID(),spouse);

        Person myself = DataCache.instance().getPersonById(me.getPersonID());
        assertEquals(me.getPersonID(), myself.getPersonID());
        assertEquals(mom.getPersonID(), DataCache.instance().getPersonById(mom.getPersonID()).getPersonID());
        assertEquals(dad.getPersonID(), DataCache.instance().getPersonById(dad.getPersonID()).getPersonID());
        assertEquals(spouse.getPersonID(), DataCache.instance().getPersonById(spouse.getPersonID()).getPersonID());

        DataCache.instance().clear();
    }

    @Test
    public void insertEventsTest() {
        DataCache.instance().addEvent(myBirth.getEventID(), myBirth);
        DataCache.instance().addEvent(myDeath.getEventID(), myDeath);
        DataCache.instance().addEvent(dadBirth.getEventID(), dadBirth);
        DataCache.instance().addEvent(spouseBirth.getEventID(), spouseBirth);

        assertEquals(myBirth.getEventID(), DataCache.instance().getEventById(myBirth.getEventID()).getEventID());
        assertEquals(myDeath.getEventID(), DataCache.instance().getEventById(myDeath.getEventID()).getEventID());
        assertEquals(dadBirth.getEventID(), DataCache.instance().getEventById(dadBirth.getEventID()).getEventID());
        assertEquals(spouseBirth.getEventID(), DataCache.instance().getEventById(spouseBirth.getEventID()).getEventID());

        DataCache.instance().clear();
    }

    @Test
    public void insertPersonFail() {
        DataCache.instance().addPerson(me.getPersonID(), me);

        assertNotEquals(dad.getPersonID(), DataCache.instance().getPersonById(me.getPersonID()).getPersonID());
        assertEquals(null, DataCache.instance().getPersonById(dad.getPersonID()));

        DataCache.instance().clear();
    }

    @Test
    public void insertEventFail() {
        DataCache.instance().addEvent(myDeath.getEventID(), myDeath);

        assertNotEquals(dadBirth.getEventID(), DataCache.instance().getEventById(myDeath.getEventID()).getEventID());
        assertEquals(null, DataCache.instance().getEventById(myBirth.getEventID()));

        DataCache.instance().clear();
    }

    @Test
    public void filterTest() {
        DataCache.instance().addPerson(me.getPersonID(), me);

        DataCache.instance().addEvent(myMarriage.getEventID(), myMarriage);
        DataCache.instance().addEvent(myDeath.getEventID(), myDeath);
        DataCache.instance().addEvent(myBirth.getEventID(), myBirth);

        DataCache.instance().fillPersonEvents();

        List<Event> list = DataCache.instance().getPersonEvents(me.getPersonID());

        assertEquals(myBirth.getEventID(), list.get(0).getEventID());
        assertEquals(myMarriage.getEventID(), list.get(1).getEventID());
        assertEquals(myDeath.getEventID(), list.get(2).getEventID());
        DataCache.instance().clear();
    }

    @Test
    public void filterTestFail() {
        DataCache.instance().addPerson(me.getPersonID(), me);

        DataCache.instance().addEvent(myMarriage.getEventID(), myMarriage);
        DataCache.instance().addEvent(myDeath.getEventID(), myDeath);
        DataCache.instance().addEvent(myBirth.getEventID(), myBirth);

        DataCache.instance().fillPersonEvents();

        List<Event> list = DataCache.instance().getPersonEvents(me.getPersonID());

        assertNotEquals(myBirth.getEventID(), list.get(2).getEventID());
        assertNotEquals(myMarriage.getEventID(), list.get(0).getEventID());
        assertNotEquals(myDeath.getEventID(), list.get(1).getEventID());
        DataCache.instance().clear();
    }

    @Test
    public void checkRelationsTest() {
        DataCache.instance().setUser(me);
        DataCache.instance().addPerson(me.getPersonID(), me);
        DataCache.instance().addPerson(mom.getPersonID(), mom);
        DataCache.instance().addPerson(dad.getPersonID(), dad);
        DataCache.instance().addPerson(spouse.getPersonID(),spouse);
        DataCache.instance().setAncestors();

        assertEquals(spouse.getPersonID(), DataCache.instance().getPersonById(me.getPersonID()).getSpouseID());
        assertEquals(true, DataCache.instance().isPaternalAncestor(dad));
        assertEquals(true, DataCache.instance().isMaternalAncestor(mom));

        assertEquals(null, DataCache.instance().getPersonById(dad.getFatherID()));

        DataCache.instance().clear();
    }

    @Test
    public void searchTest() {
        DataCache.instance().addPerson(me.getPersonID(), me);
        DataCache.instance().addPerson(mom.getPersonID(), mom);
        DataCache.instance().addPerson(dad.getPersonID(), dad);
        DataCache.instance().addPerson(spouse.getPersonID(),spouse);
        DataCache.instance().addEvent(myBirth.getEventID(), myBirth);
        DataCache.instance().addEvent(myDeath.getEventID(), myDeath);
        DataCache.instance().addEvent(dadBirth.getEventID(), dadBirth);
        DataCache.instance().addEvent(spouseBirth.getEventID(), spouseBirth);
        DataCache.instance().addEvent(myMarriage.getEventID(), myMarriage);

        List<Person> searchedPeople;
        List<Event> searchedEvents;

        searchedPeople = DataCache.instance().getAllSearchedPeople("m");
        searchedEvents = DataCache.instance().getAllSearchedEvents("d");

        assertEquals(1, searchedPeople.size());
        assertEquals(1, searchedEvents.size());

        searchedPeople = DataCache.instance().getAllSearchedPeople("b");
        assertEquals(3, searchedPeople.size());


        DataCache.instance().clear();
    }

    @Test
    public void noResultsTest() {
        DataCache.instance().addPerson(me.getPersonID(), me);
        DataCache.instance().addPerson(mom.getPersonID(), mom);
        DataCache.instance().addPerson(dad.getPersonID(), dad);
        DataCache.instance().addPerson(spouse.getPersonID(),spouse);
        DataCache.instance().addEvent(myBirth.getEventID(), myBirth);
        DataCache.instance().addEvent(myDeath.getEventID(), myDeath);
        DataCache.instance().addEvent(dadBirth.getEventID(), dadBirth);
        DataCache.instance().addEvent(spouseBirth.getEventID(), spouseBirth);
        DataCache.instance().addEvent(myMarriage.getEventID(), myMarriage);

        List<Person> searchedPeople;
        List<Event> searchedEvents;

        searchedEvents = DataCache.instance().getAllSearchedEvents("Aztec");
        assertEquals(0, searchedEvents.size());

        searchedPeople = DataCache.instance().getAllSearchedPeople("MAYA");
        assertEquals(0, searchedPeople.size());

        DataCache.instance().clear();
    }
}