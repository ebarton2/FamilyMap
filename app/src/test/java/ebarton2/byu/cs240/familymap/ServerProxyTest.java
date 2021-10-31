package ebarton2.byu.cs240.familymap;

import org.junit.Test;

import ebarton2.byu.cs240.familymap.network.ServerProxy;
import request.LoginRequest;
import request.RegisterRequest;
import result.EventsResult;
import result.PersonsResult;

import static org.junit.Assert.*;

public class ServerProxyTest {
    private ServerProxy mServerProxy = new ServerProxy("localhost", 8080);
    private LoginRequest mLoginRequest = new LoginRequest("sheila", "parker");
    private LoginRequest mLoginFail = new LoginRequest("shella", "patrick");
    private RegisterRequest mRegisterRequest = new RegisterRequest("yi", "wubwub", "@gmail.com", "Bard", "of Ionia", "m");
    private RegisterRequest registerFail = new RegisterRequest("sheila", "wubwub", "@gmail.com", "Bard", "of Ionia", "m");

    @Test
    public void loginTest() {
        assertEquals(true, mServerProxy.login(mLoginRequest).getSuccess());
    }

    @Test
    public void loginTestFail() {
        assertEquals(false, mServerProxy.login(mLoginFail).getSuccess());
    }

    @Test
    public void registerTest() {
        assertEquals(true, mServerProxy.register(mRegisterRequest).getSuccess());
    }

    @Test
    public void registerTestFail() {
        assertEquals(false, mServerProxy.register(registerFail).getSuccess());
    }

    @Test
    public void getLoggedPeople() {
        String auth = mServerProxy.login(mLoginRequest).getAuthtoken();
        PersonsResult personsResult = mServerProxy.getAllPeople(auth);

        assertEquals(true, personsResult.isSuccess());
    }

    @Test
    public void getLoggedEvents() {
        String auth = mServerProxy.login(mLoginRequest).getAuthtoken();
        EventsResult eventsResult = mServerProxy.getAllEvents(auth);

        assertEquals(true, eventsResult.isSuccess());
    }

}
