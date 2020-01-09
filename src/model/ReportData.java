package model;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ReportData {
    Date runDate;
    Map<Integer, List<Account>> accountMap;
    Map<Long, MonthlyUsagePerDevice> monthlyUsagePerDeviceMap;

    public ReportData() {

    }

    public Date getRunDate() {
        return runDate;
    }

    public void setRunDate(Date runDate) {
        this.runDate = runDate;
    }

    public Map<Integer, List<Account>> getAccountMap() {
        return accountMap;
    }

    public void setAccountMap(Map<Integer, List<Account>> accountMap) {
        this.accountMap = accountMap;
    }

    public Map<Long, MonthlyUsagePerDevice> getMonthlyUsagePerDeviceMap() {
        return monthlyUsagePerDeviceMap;
    }

    public void setMonthlyUsagePerDeviceMap(Map<Long, MonthlyUsagePerDevice> monthlyUsagePerDeviceMap) {
        this.monthlyUsagePerDeviceMap = monthlyUsagePerDeviceMap;
    }
}
