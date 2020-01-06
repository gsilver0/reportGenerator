package model;

import java.util.Date;

public class MonthlyUsagePerDevice {
    Account account;
    int[] minutesUsage = new int[12];
    float[] dataUsage = new float[12];
    int totalMinutes = 0;
    float totalData = 0;

    public MonthlyUsagePerDevice(Account account) {
        this.account = account;
        for(int i = 0; i < 12; i++) {
            minutesUsage[i] = 0;
            dataUsage[i] = 0;
        }
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public int[] getMinutesUsage() {
        return minutesUsage;
    }

    public int getTotalMinutes() {
        return totalMinutes;
    }

    public int addMinutesUsage(int month, int minutes) {
        if(month < 0 || month > 11) {
            return 0;
        }
        totalMinutes += minutes;
        return minutesUsage[month] += minutes;
    }

    public float[] getDataUsage() {
        return dataUsage;
    }

    public float getTotalData() {
        return totalData;
    }

    public float addDataUsage(int month, float data) {
        if(month < 0 || month > 11) {
            return 0;
        }
        totalData += data;
        return dataUsage[month] += data;
    }
}
