package service;

import model.Account;
import model.UsageRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DataService {
    private static final String DELIMITER = ",";
    private SimpleDateFormat userDateFormat = new SimpleDateFormat("yyyymmdd");
    private SimpleDateFormat usageDateFormat = new SimpleDateFormat("mm/dd/yyyy");

    boolean dataImported = false;
    boolean usersImported = false;

    private List<Account> accountList;
    private List<UsageRecord> usageRecordList;

    public DataService() {
        accountList = new ArrayList<>();
        usageRecordList = new ArrayList<>();
    }

    public void importUsers() throws Exception {
        String inputFile = "CellPhone.csv"; //TODO: Replace with file open dialog
        Integer employeeIdColumn = null;
        Integer employeeNameColumn = null;
        Integer purchaseDateColumn = null;
        Integer modelColumn = null;

        BufferedReader fileReader = new BufferedReader(new FileReader(inputFile));
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
    }

    public void importData() throws Exception {
        String inputFile = "CellPhoneUsageByMonth.csv"; //TODO: Replace with file open dialog
        Integer employeeIdColumn = null;
        Integer dateColumn = null;
        Integer minutesColumn = null;
        Integer dataColumn = null;

        BufferedReader fileReader = new BufferedReader(new FileReader(inputFile));
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
                case "date":
                    dateColumn = i;
                    break;
                case "totalMinutes":
                    minutesColumn = i;
                    break;
                case "totalData":
                    dataColumn = i;
            }
        }
        if(employeeIdColumn == null || dateColumn == null || minutesColumn == null || dataColumn == null) {
            throw new Exception("Required column was not found");
        }

        String line;
        while ((line = fileReader.readLine()) != null)
        {
            UsageRecord usageRecord = new UsageRecord();
            String[] tokens = line.split(DELIMITER);

            if(employeeIdColumn < tokens.length) {
                usageRecord.setEmployeeId(Integer.parseInt(tokens[employeeIdColumn]));
            }
            if(dateColumn < tokens.length) {
                usageRecord.setDate(usageDateFormat.parse(tokens[dateColumn]));
            }
            if(minutesColumn < tokens.length) {
                usageRecord.setTotalMinutes(Integer.parseInt(tokens[minutesColumn]));
            }
            if(dataColumn < tokens.length) {
                usageRecord.setTotalData(Float.parseFloat(tokens[dataColumn]));
            }
            usageRecordList.add(usageRecord);
        }
        dataImported = true;
    }

    public boolean getDataLoaded() {
        return dataImported && usersImported;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public List<UsageRecord> getUsageRecordList() {
        return usageRecordList;
    }
}
