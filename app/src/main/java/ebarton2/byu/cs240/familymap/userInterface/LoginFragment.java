package ebarton2.byu.cs240.familymap.userInterface;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ebarton2.byu.cs240.familymap.DataCache;
import ebarton2.byu.cs240.familymap.R;
import ebarton2.byu.cs240.familymap.model.LoginInfo;
import ebarton2.byu.cs240.familymap.network.ServerProxy;
import model.Person;
import request.LoginRequest;
import request.RegisterRequest;
import result.EventsResult;
import result.LoginResult;
import result.PersonsResult;
import result.RegisterResult;

public class LoginFragment extends Fragment {
    private LoginInfo mLoginInfo;

    private static final String LOGIN_RESULT = "LoginResult";
    private static final String REGISTER_RESULT = "RegisterResult";
    private static final String ACCESS_RESULT = "AccessResult";
    private static final String AUTH_TOKEN = "Auth";
    private static final String PERSON_ID = "ID";

    private EditText serverHostName;
    private EditText serverPortNumber;
    private EditText userName;
    private EditText password;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private RadioGroup genders;

    private Button loginButton;
    private Button registerButton;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginInfo = new LoginInfo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String authToken;
        String personID;
        boolean success;

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        serverHostName = (EditText) v.findViewById(R.id.serverHostEditText);
        serverHostName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mLoginInfo.setHostName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0) {
                    loginButton.setEnabled(false);
                    registerButton.setEnabled(false);
                }
                else {
                    loginSet();
                    registerSet();
                }
            }
        });

        serverPortNumber = (EditText) v.findViewById(R.id.serverPortEditText);
        serverPortNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mLoginInfo.setPortNumber(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0) {
                    loginButton.setEnabled(false);
                    registerButton.setEnabled(false);
                }
                else {
                    loginSet();
                    registerSet();
                }
            }
        });

        userName = (EditText) v.findViewById(R.id.userNameEditText);
        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mLoginInfo.setUserName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0) {
                    loginButton.setEnabled(false);
                    registerButton.setEnabled(false);
                }
                else {
                    loginSet();
                    registerSet();
                }
            }
        });

        password = (EditText) v.findViewById(R.id.passwordEditText);
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mLoginInfo.setPassword(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0) {
                    loginButton.setEnabled(false);
                    registerButton.setEnabled(false);
                }
                else {
                    loginSet();
                    registerSet();
                }
            }
        });

        firstName = (EditText) v.findViewById(R.id.firstNameEditText);
        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mLoginInfo.setFirstName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0) {
                    registerButton.setEnabled(false);
                }
                else {
                    registerSet();
                }
            }
        });


        lastName = (EditText) v.findViewById(R.id.lastNameEditText);
        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mLoginInfo.setLastName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0) {
                    registerButton.setEnabled(false);
                }
                else {
                    registerSet();
                }
            }
        });


        email = (EditText) v.findViewById(R.id.emailEditText);
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mLoginInfo.setEmail(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0) {
                    registerButton.setEnabled(false);
                }
                else {
                    registerSet();
                }
            }
        });

        genders = (RadioGroup) v.findViewById(R.id.genderButtons);
        genders.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.maleButton) {
                    mLoginInfo.setGender("m");
                    System.out.println("Male");
                }
                if (checkedId == R.id.femaleButton) {
                    mLoginInfo.setGender("f");
                    System.out.println("Female");
                }
                registerSet();
            }
        });

        loginButton = (Button) v.findViewById(R.id.loginButton);
        loginButton.setEnabled(false);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Handler uiThreadMessageHandler = new Handler() {
                        @Override
                        public void handleMessage(Message message) {
                            System.out.println("In the login message handler.");
                            Bundle bundle = message.getData();
                            boolean successReport = bundle.getBoolean(LOGIN_RESULT);
                            Toast toast;

                            if (successReport) {
                                toast = Toast.makeText(getContext(), R.string.loginSuccess, Toast.LENGTH_SHORT);

                            } else {
                                toast = Toast.makeText(getContext(), R.string.loginFail, Toast.LENGTH_SHORT);
                            }
                            toast.show();
                            loginDealtWith(bundle.getString(AUTH_TOKEN), bundle.getString(PERSON_ID));
                        }
                    };

                    AsyncLogin task = new AsyncLogin(uiThreadMessageHandler, mLoginInfo);
                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    System.out.println("About to run the login async task");
                    executorService.submit(task);


                    System.out.println("About to check if task was successful");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        registerButton = (Button) v.findViewById(R.id.registerButton);
        registerButton.setEnabled(false);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Handler uiThreadMessageHandler = new Handler() {
                        @Override
                        public void handleMessage(Message message) {
                            Bundle bundle = message.getData();
                            boolean taskSuccess = bundle.getBoolean(REGISTER_RESULT);

                            Toast toast;
                            if (taskSuccess) {
                                toast = Toast.makeText(getContext(), R.string.registerSuccess, Toast.LENGTH_SHORT);
                            } else {
                                toast = Toast.makeText(getContext(), R.string.registerFail, Toast.LENGTH_SHORT);
                            }
                            toast.show();

                            loginDealtWith(bundle.getString(AUTH_TOKEN), bundle.getString(PERSON_ID));
                        }
                    };

                    AsyncRegister task = new AsyncRegister(uiThreadMessageHandler, mLoginInfo);
                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    executorService.submit(task);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return v;
    }

    private void loginDealtWith(String authToken, String personID) {
        try {
            Handler dataHandler = new Handler() {
                @Override
                public void handleMessage(Message message) {
                    Bundle bundle = message.getData();
                    boolean dataSuccess = bundle.getBoolean(ACCESS_RESULT);
                    Toast toast;

                    if (dataSuccess) {
                        System.out.println("True");
                    }
                    else { System.out.println("False"); }

                    if (dataSuccess) {
                        String toastText = DataCache.instance().getUser().getFirstName() + " " + DataCache.instance().getUser().getLastName();
                        toast = Toast.makeText(getContext(), toastText, Toast.LENGTH_SHORT);
                        System.out.println("Yes it worked register");
                    } else {
                        toast = Toast.makeText(getContext(), R.string.dataAccessFail, Toast.LENGTH_SHORT);
                        System.out.println("No it didn't register");
                    }
                    toast.show();

                    if (dataSuccess) {
                        ((MainActivity) Objects.requireNonNull(getActivity())).swapFragment(DataCache.instance().getLoginState());
                    }
                }
            };

            AsyncDataFill dataTask = new AsyncDataFill(dataHandler, authToken, personID, mLoginInfo);
            ExecutorService service = Executors.newSingleThreadExecutor();
            service.submit(dataTask);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class AsyncLogin implements Runnable {

        private final Handler messageHandler;
        private final LoginInfo mLoginInfo;

        private String authToken;
        private boolean success;
        private String personID;

        public AsyncLogin(Handler messageHandler, LoginInfo loginInfo) {
            this.messageHandler = messageHandler;
            mLoginInfo = loginInfo;
        }

        @Override
        public void run() {
            LoginRequest lr = new LoginRequest(mLoginInfo.getUserName(), mLoginInfo.getPassword());

            System.out.println("Running in AsyncLogin");

            ServerProxy proxy = new ServerProxy(mLoginInfo.getHostName(), Integer.parseInt(mLoginInfo.getPortNumber()));
            LoginResult result = proxy.login(lr);

            success = result.getSuccess();

            if (success) {
                authToken = result.getAuthtoken();
                personID = result.getPersonID();
            }

            sendMessage(success);
        }

        private void sendMessage(Boolean pass) {
            Message message = Message.obtain();

            Bundle messageBundle = new Bundle();
            messageBundle.putBoolean(LOGIN_RESULT, pass);
            if (pass) {
                messageBundle.putString(AUTH_TOKEN, authToken);
                messageBundle.putString(PERSON_ID, personID);
            }
            message.setData(messageBundle);

            messageHandler.sendMessage(message);
        }

        public boolean getSuccess() { return success; }
        public String getAuthToken() { return authToken; }
        public String getPersonID() { return personID; }

    }

    private static class AsyncRegister implements Runnable {

        private final Handler messageHandler;
        private final LoginInfo mLoginInfo;

        private String authToken;
        private String personID;
        private boolean success;

        public AsyncRegister(Handler messageHandler, LoginInfo loginInfo) {
            this.messageHandler = messageHandler;
            mLoginInfo = loginInfo;
        }

        @Override
        public void run() {
            RegisterRequest rr = new RegisterRequest(mLoginInfo.getUserName(), mLoginInfo.getPassword(),
                    mLoginInfo.getEmail(), mLoginInfo.getFirstName(),
                    mLoginInfo.getLastName(), mLoginInfo.getGender());

            ServerProxy proxy = new ServerProxy(mLoginInfo.getHostName(), Integer.parseInt(mLoginInfo.getPortNumber()));
            RegisterResult result = proxy.register(rr);

            success = result.getSuccess();

            if (success) {
                authToken = result.getAuthtoken();
                personID = result.getPersonID();
            }
            sendMessage(success);
        }

        private void sendMessage(boolean pass) {
            Message message = Message.obtain();

            Bundle messageBundle = new Bundle();
            messageBundle.putBoolean(REGISTER_RESULT, pass);
            if (pass) {
                messageBundle.putString(AUTH_TOKEN, authToken);
                messageBundle.putString(PERSON_ID, personID);
            }
            message.setData(messageBundle);

            messageHandler.sendMessage(message);
        }

        public boolean getSuccess() { return success; }
        public String getAuthToken() { return authToken; }
        public String getPersonID() { return personID; }

    }

    private static class AsyncDataFill implements Runnable {

        private final Handler messageHandler;
        private final String authToken;
        private final String personID;
        private final LoginInfo loginInfo;

        public AsyncDataFill(Handler messageHandler, String authToken, String personID, LoginInfo loginInfo) {
            this.messageHandler = messageHandler;
            this.authToken = authToken;
            this.loginInfo = loginInfo;
            this.personID = personID;
        }

        @Override
        public void run() {
            ServerProxy proxy = new ServerProxy(loginInfo.getHostName(), Integer.parseInt(loginInfo.getPortNumber()));
            PersonsResult personsResult = proxy.getAllPeople(authToken);
            EventsResult eventsResult = proxy.getAllEvents(authToken);

            if (personsResult.isSuccess() && eventsResult.isSuccess()) {
                for (int i = 0; i < personsResult.getData().size(); ++i) {
                    DataCache.instance().addPerson(personsResult.getData().get(i).getPersonID(), personsResult.getData().get(i));
                }
                for (int j = 0; j < eventsResult.getData().size(); ++j) {
                    DataCache.instance().addEvent(eventsResult.getData().get(j).getEventID(), eventsResult.getData().get(j));
                    DataCache.instance().addEventType(eventsResult.getData().get(j).getEventType());
                }
                DataCache.instance().fillPersonEvents();
                DataCache.instance().setUser(DataCache.instance().findUser(personID));
                DataCache.instance().setAncestors();
                DataCache.instance().setPersonChildren();
                DataCache.instance().fillEventColors();
                DataCache.instance().setLoginState(true);
                sendMessage(true);

            } else {
                sendMessage(false);
            }
        }

        private void sendMessage(boolean pass) {
            Message message = Message.obtain();

            Bundle messageBundle = new Bundle();
            messageBundle.putBoolean(ACCESS_RESULT, pass);
            //if (pass) {
            //    messageBundle.putString(PERSON_ID, personID);
            //}
            message.setData(messageBundle);

            messageHandler.sendMessage(message);
        }
    }

    private void loginSet() {
        loginButton.setEnabled(mLoginInfo.loginCheckStatus());
    }

    private void registerSet() {
        registerButton.setEnabled(mLoginInfo.registerCheckStatus());
    }
}