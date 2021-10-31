package ebarton2.byu.cs240.familymap.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import request.LoginRequest;
import request.RegisterRequest;
import result.EventsResult;
import result.LoginResult;
import result.PersonsResult;
import result.RegisterResult;

public class ServerProxy {
    public static String serverHostName;
    public static int serverPortNumber;

    public ServerProxy() {}

    public ServerProxy(String serverHostName, int serverPortNumber) {
        this.serverHostName = serverHostName;
        this.serverPortNumber = serverPortNumber;
    }


    public LoginResult login(LoginRequest r) {
        try {
            URL url = new URL("http://" + serverHostName + ":" + serverPortNumber + "/user/login");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.addRequestProperty("Content-Type", "application/json");

            http.connect();

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(r);

            OutputStream reqBody = http.getOutputStream();
            writeString(json, reqBody);
            reqBody.close();

            if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStream respBody = http.getInputStream();
                LoginResult loginResult = gson.fromJson(readInput(respBody), LoginResult.class);
                return loginResult;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new LoginResult("Connection failed to login.", false);
    }

    public RegisterResult register(RegisterRequest r) {

        try {
            URL url = new URL("http://" + serverHostName + ":" + serverPortNumber + "/user/register");
            System.out.println(serverHostName + " + " + serverPortNumber);
            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.addRequestProperty("Content-Type", "application/json");

            http.connect();

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(r);

            OutputStream reqBody = http.getOutputStream();
            writeString(json, reqBody);
            reqBody.close();

            if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {

                System.out.println("We are in the clear");
                InputStream respBody = http.getInputStream();
                RegisterResult registerResult = gson.fromJson(readInput(respBody), RegisterResult.class);
                return registerResult;
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new RegisterResult("Connection failed to register.", false);
    }

    public PersonsResult getAllPeople(String authToken){

        try {
            URL url = new URL("http://" + serverHostName + ":" + serverPortNumber + "/person");

            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("GET");
            http.setDoOutput(false);

            http.addRequestProperty("Authorization", authToken);
            http.addRequestProperty("Accept", "application/json");

            http.connect();
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStream respBody = http.getInputStream();
                Gson gson = new GsonBuilder().setPrettyPrinting().create();

                PersonsResult personsResult = gson.fromJson(readInput(respBody), PersonsResult.class);
                return personsResult;
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return new PersonsResult("Connection to get all persons failed.", false);
    }

    public EventsResult getAllEvents(String authToken) {

        try {
            URL url = new URL("http://" + serverHostName + ":" + serverPortNumber + "/event");

            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("GET");
            http.setDoOutput(false);

            http.addRequestProperty("Authorization", authToken);
            http.addRequestProperty("Accept", "application/json");

            http.connect();
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStream respBody = http.getInputStream();
                Gson gson = new GsonBuilder().setPrettyPrinting().create();

                EventsResult eventsResult = gson.fromJson(readInput(respBody), EventsResult.class);
                return eventsResult;
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return new EventsResult("Connection to get all events failed.", false);
    }

    private static String readInput(InputStream is) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            stringBuilder.append(buf, 0, len);
        }
        return stringBuilder.toString();
    }

    private static void writeString(String string, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(string);
        sw.flush();
    }
}