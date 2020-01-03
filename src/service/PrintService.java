package service;

import model.Account;
import model.UsageRecord;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class PrintService {
    private SimpleDateFormat usageDateFormat = new SimpleDateFormat("mm/dd/yyyy");

    public PrintService() {

    }

    //Generates a report, opens the print dialog, and outputs to a local printer
    //Precondition: accountMap and filteredUsageRecordList contain data only for the year specified
    public void printReport(Map<Integer, List<Account>> accountMap, List<UsageRecord> filteredUsageRecords, int year) throws Exception {
        System.out.println("Cell phone usage report for year: " + year + "\n");
        if(filteredUsageRecords.size() == 0) {
            throw new Exception("No data to export for the specified year");
        }
        generateHeader(accountMap, filteredUsageRecords, year);
    }

    private void generateHeader(Map<Integer, List<Account>> accountMap, List<UsageRecord> usageRecordList, int year) {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);

        int phones = 0;
        for(Collection<Account> accounts : accountMap.values()) {
            phones += accounts.size();
        }
        System.out.println("Report Run Date: " + usageDateFormat.format(today));
        System.out.println("Number of users: " + accountMap.size());
        System.out.println("Number of phones: " + phones);

        AtomicReference<Float> totalData = new AtomicReference<>((float) 0);
        AtomicReference<Integer> totalMinutes = new AtomicReference<>(0);
        usageRecordList.forEach( r -> {
            if(r.getTotalData() != null) {
                totalData.updateAndGet(v -> new Float((float) (v + r.getTotalData())));
            }
            if(r.getTotalMinutes() != null) {
                totalMinutes.updateAndGet(v -> new Integer((int) (v + r.getTotalMinutes())));
            }
        });
        System.out.println("Total Minutes:" + totalMinutes.get());
        System.out.printf("Total Data: %.2f\n", totalData.get() );
        System.out.println("Average minutes: " + totalMinutes.get() / accountMap.size());
        System.out.printf("Average Data: %.2f\n", (totalData.get() / (float) accountMap.size()));






    }
}
