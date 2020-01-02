package service;

public class DataService {
    boolean dataImported = false;
    boolean usersImported = false;

    public DataService() {

    }

    public void importUsers() {
        System.out.println("Users imported");
        usersImported = true;
    }

    public void importData() {
        System.out.println("Data imported");
        dataImported = true;
    }

    public boolean getDataLoaded() {
        return dataImported && usersImported;
    }
}
