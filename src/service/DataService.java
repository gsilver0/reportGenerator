package service;

import model.Account;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DataService {
    private static final String DELIMITER = ",";
    private SimpleDateFormat userDateFormat = new SimpleDateFormat("yyyymmdd");

    boolean dataImported = false;
    boolean usersImported = false;

    private List<Account> accountList;


    //User headers:


    public DataService() {
        accountList = new ArrayList<>();
    }

    public void importUsers() {
        String inputFile = "CellPhone.csv"; //TODO: Replace with file open dialog
        BufferedReader fileReader = null;
        //employeeId,employeeName,purchaseDate,model
        Integer employeeIdColumn = null;
        Integer employeeNameColumn = null;
        Integer purchaseDateColumn = null;
        Integer modelColumn = null;
        try {
            fileReader = new BufferedReader(new FileReader(inputFile));
            String headerLine = fileReader.readLine();
            String[] headers = headerLine.split(DELIMITER);
            if(headers.length < 4) {
                throw new Exception("Insufficient number of headers");
            }
            for(int i = 0; i < headers.length; i++) {
                switch(headers[i]) {
                    case "employeeId":
                        employeeIdColumn = i;
                        break;
                    case "employeeName":
                        employeeNameColumn = i;
                        break;
                    case "purchaseDate":
                        purchaseDateColumn = i;
                        break;
                    case "model":
                        modelColumn = i;
                }
            }
            if(employeeIdColumn == null || employeeNameColumn == null || purchaseDateColumn == null || modelColumn == null) {
                throw new Exception("Required column was not found");
            }

            String line;
            while ((line = fileReader.readLine()) != null)
            {
                Account account = new Account();
                String[] tokens = line.split(DELIMITER);

                if(employeeIdColumn < tokens.length) {
                    account.setEmployeeId(Integer.parseInt(tokens[employeeIdColumn]));
                }
                if(employeeNameColumn < tokens.length) {
                    account.setEmployeeName(tokens[employeeNameColumn]);
                }
                if(purchaseDateColumn < tokens.length) {
                    account.setPurchaseDate(userDateFormat.parse(tokens[purchaseDateColumn]));
                }
                if(modelColumn < tokens.length) {
                    account.setModel(tokens[modelColumn]);
                }
                accountList.add(account);
            }
            usersImported = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void importData() {
        System.out.println("Data imported");
        dataImported = true;
    }

    public boolean getDataLoaded() {
        return dataImported && usersImported;
    }

    public List<Account> getAccountList() {
        return this.accountList;
    }
}
