package service;

import model.Account;
import model.UsageRecord;

import java.util.List;
import java.util.stream.Collectors;

public class PrintService {
    public PrintService() {

    }

    public void printReport(List<Account> accountList, List<UsageRecord> usageRecordList, int year) throws Exception {
        System.out.println(year);
        accountList.stream().forEach( a -> System.out.println(a));
        List<UsageRecord> filteredUsageRecords = usageRecordList.stream().filter(r -> (r.getDate().getYear() + 1900) == year).collect(Collectors.toList());
        filteredUsageRecords.stream().forEach( r -> System.out.println(r));
        if(filteredUsageRecords.size() == 0) {
            throw new Exception("No data to export for the specified year");
        }
    }
}
