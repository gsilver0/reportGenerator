package service;

import model.Account;
import model.MonthlyUsagePerDevice;
import model.ReportData;
import model.UsageRecord;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DataService {
    private static final String DELIMITER = ",";
    private SimpleDateFormat userDateFormat = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat usageDateFormat = new SimpleDateFormat("MM/dd/yyyy");

    private boolean dataImported = false;
    private boolean usersImported = false;

    private List<Account> accountList;
    private List<UsageRecord> usageRecordList;

    public DataService() {
        accountList = new ArrayList<>();
        usageRecordList = new ArrayList<>();
    }

    public void importUsers(File inputFile) throws Exception {
        //String inputFile = "CellPhone2.csv"; //Test file
        Integer employeeIdColumn = null;
        Integer employeeNameColumn = null;
        Integer purchaseDateColumn = null;
        Integer modelColumn = null;

        BufferedReader fileReader =  new BufferedReader(new FileReader(inputFile));
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

        //Ensure that devices are sorted by purchase date
        accountList = accountList.stream().sorted().collect(Collectors.toList());
        usersImported = true;
    }

    public void importData(File inputFile) throws Exception {
        //String inputFile = "CellPhoneUsageByMonth.csv"; //Test file
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


    public Map<Integer, List<Account>> getAccountsForYear(int year) {
        if(!usersImported) {
            return Collections.EMPTY_MAP;
        }
        Map<Integer, List<Account>> sortedAccountMap = new HashMap<>();
        Calendar cal = Calendar.getInstance();
        cal.set(year, 0, 2);
        Date secondDayOfYear = cal.getTime();
        cal.set(year, 11, 31);
        Date lastDayOfYear = cal.getTime();
        for(Account account : accountList) {
            if(account.getPurchaseDate().after(lastDayOfYear)) {
                continue;
            }
            //Look for devices replaced on or prior to the beginning of the year and remove
            if(account.getPurchaseDate().before(secondDayOfYear)
                    && sortedAccountMap.get(account.getEmployeeId()) != null
                    && !sortedAccountMap.get(account.getEmployeeId()).isEmpty()
            ) {
                sortedAccountMap.put(account.getEmployeeId(), new ArrayList<>());
            }
            if(sortedAccountMap.get(account.getEmployeeId()) == null) {
                sortedAccountMap.put(account.getEmployeeId(), new ArrayList<>());
            }
            sortedAccountMap.get(account.getEmployeeId()).add(account);
        }
        return sortedAccountMap;
    }

    public List<UsageRecord> getUsageRecordListForYear(int year) {
        return usageRecordList.stream().filter(r -> (r.getDate().getYear() + 1900) == year).sorted().collect(Collectors.toList());
    }

    public ReportData getReportDataForYear(int year) {
        ReportData reportData = new ReportData();
        reportData.setRunDate(new Date());
        reportData.setAccountMap(getAccountsForYear(year));
        setMonthlyUsageData(reportData, year);

        return reportData;
    }

    public void setMonthlyUsageData(ReportData reportData, int year) {
        Map<Integer, UsageRecord> employeeUsageSummary;
        List<UsageRecord> usageRecordList = getUsageRecordListForYear(year);
        Map<Long, MonthlyUsagePerDevice> monthlyUsagePerDeviceMap = new HashMap<>();
        Map<Integer, Integer> currentDeviceForEmployeeMap = new HashMap<>();

        for(UsageRecord usageRecord : usageRecordList) {
            //Determine which device the employee is using at the time
            Integer employeeId = usageRecord.getEmployeeId();
            if(currentDeviceForEmployeeMap.get(employeeId) == null) {
                currentDeviceForEmployeeMap.put(employeeId, 0);
            }
            Integer currentDeviceForEmployee = currentDeviceForEmployeeMap.get(employeeId);

            if((currentDeviceForEmployee + 1 < reportData.getAccountMap().get(employeeId).size())
              && usageRecord.getDate().after(reportData.getAccountMap().get(employeeId).get(currentDeviceForEmployee + 1).getPurchaseDate())) {
                currentDeviceForEmployee++;
                currentDeviceForEmployeeMap.put(employeeId, currentDeviceForEmployee);
            }
            Account currentAccount = reportData.getAccountMap().get(employeeId).get(currentDeviceForEmployee);
            Long lookupKey = currentAccount.getAccountHashKey();
            if(monthlyUsagePerDeviceMap.get(lookupKey) == null) {
                monthlyUsagePerDeviceMap.put(lookupKey, new MonthlyUsagePerDevice(currentAccount));
            }
            monthlyUsagePerDeviceMap.get(lookupKey).addDataUsage(usageRecord.getDate().getMonth(), usageRecord.getTotalData());
            monthlyUsagePerDeviceMap.get(lookupKey).addMinutesUsage(usageRecord.getDate().getMonth(), usageRecord.getTotalMinutes());
        }
        reportData.setMonthlyUsagePerDeviceMap(monthlyUsagePerDeviceMap);
    }
}
