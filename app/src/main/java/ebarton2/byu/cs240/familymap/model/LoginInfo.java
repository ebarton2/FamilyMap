package ebarton2.byu.cs240.familymap.model;

public class LoginInfo {
    private String hostName;
    private String portNumber;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;



    public LoginInfo() {
        hostName = null;
        portNumber = null;
        userName = null;
        password = null;
        firstName = null;
        lastName = null;
        email = null;
        gender = null;
    }

    public boolean loginCheckStatus() {
        if (hostName != null &&
                portNumber != null &&
                userName != null &&
                password != null) {
           if (loginNotNullCheckStatus()) {
               return true;
           }
        }
        return false;
    }

    public boolean registerCheckStatus() {
        if (hostName != null &&
                portNumber != null &&
                userName != null &&
                password != null &&
                firstName != null &&
                lastName != null &&
                email != null &&
                gender != null) {
            if (registerNotNullCheckStatus()) {
                return true;
            }
        }
        return false;
    }
    private boolean loginNotNullCheckStatus() {
        if (!hostName.isEmpty() &&
                !portNumber.isEmpty() &&
                !userName.isEmpty() &&
                !password.isEmpty()) {
            return true;
        }
        return false;
    }

    private boolean registerNotNullCheckStatus() {
        if (!hostName.isEmpty() &&
                !portNumber.isEmpty() &&
                !userName.isEmpty() &&
                !password.isEmpty() &&
                !firstName.isEmpty() &&
                !lastName.isEmpty() &&
                !email.isEmpty() &&
                !gender.isEmpty()) {
            return true;
        }
        return false;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(String portNumber) {
        this.portNumber = portNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
