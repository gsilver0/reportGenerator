package service;

import model.Account;
import model.MonthlyUsagePerDevice;
import model.ReportData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PrintService {
    private SimpleDateFormat usageDateFormat = new SimpleDateFormat("MM/dd/yyyy");

    public PrintService() {

    }

    //Generates a report, opens the print dialog, and outputs to a local printer
    //Precondition: accountMap and filteredUsageRecordList contain data only for the year specified
    public void printReport(ReportData reportData, int year) throws Exception {
        System.out.println("Cell phone usage report for year: " + reportData.getRunDate().getYear() + "\n");
        if(reportData.getMonthlyUsagePerDeviceMap().isEmpty()) {
            throw new Exception("No data to export for the specified year");
        }
        generateHeader(reportData, year);
        generateData(reportData, year);
    }

    private void generateHeader(ReportData reportData, int year) {
        System.out.println("Report Run Date: " + usageDateFormat.format(reportData.getRunDate()));
        System.out.println("Year for report: " + year);

        Calendar cal = Calendar.getInstance();
        cal.set(year, 0, 1);
        Date firstDayOfYear = cal.getTime();

        List<Integer> employeeIds = reportData.getAccountMap().keySet().stream().sorted().collect(Collectors.toList());

        for(Integer emoloyeeId : employeeIds) {
            List<Account> accounts = reportData.getAccountMap().get(emoloyeeId);
            int totalMinutes = 0;
            float totalData = 0;
            int numberOfMonths = 12;
            if(accounts.get(0).getPurchaseDate().after(firstDayOfYear)) {
                numberOfMonths = (12 - accounts.get(0).getPurchaseDate().getMonth());
            }

            System.out.println("\nEmployee id: " + accounts.get(0).getEmployeeId());
            System.out.println("Employee name: " + accounts.get(0).getEmployeeName());
            for(Account account : accounts) {
                if(reportData.getMonthlyUsagePerDeviceMap().get(account.getAccountHashKey()) != null) {
                    MonthlyUsagePerDevice monthlyUsagePerDevice = reportData.getMonthlyUsagePerDeviceMap().get(account.getAccountHashKey());
                    totalData += monthlyUsagePerDevice.getTotalData();
                    totalMinutes += monthlyUsagePerDevice.getTotalMinutes();
                }
            }

            System.out.println("Number of phones: " + accounts.size());
            System.out.println("Total Minutes:" + totalMinutes);
            System.out.printf("Total Data: %.2f\n", totalData);
            System.out.println("Average minutes: " + totalMinutes / numberOfMonths);
            System.out.printf("Average Data: %.2f\n", totalData / (float) numberOfMonths);
        }
    }

    private void generateData(ReportData reportData, int year) {
        List<Integer> employeeIds = reportData.getAccountMap().keySet().stream().sorted().collect(Collectors.toList());
    }
}
