package model;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ReportData {
    Date runDate;
    Integer numberOfPhones;
    Float totalData;
    Integer totalMinutes;
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

    public Integer getNumberOfPhones() {
        return numberOfPhones;
    }

    public void setNumberOfPhones(Integer numberOfPhones) {
        this.numberOfPhones = numberOfPhones;
    }

    public Integer getTotalMinutes() {
        return totalMinutes;
    }

    public void setTotalMinutes(Integer totalMinutes) {
        this.totalMinutes = totalMinutes;
    }

    public Float getTotalData() {
        return totalData;
    }

    public void setTotalData(Float totalData) {
        this.totalData = totalData;
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
