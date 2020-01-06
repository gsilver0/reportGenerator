package model;

import java.util.Date;

public class UsageRecord implements Comparable{
    private Integer employeeId;
    private Date date;
    private Integer totalMinutes;
    private Float totalData;

    public UsageRecord() {

    }

    public UsageRecord(Integer employeeId, Date date, Integer totalMinutes, Float totalData) {
        this.employeeId = employeeId;
        this.date = date;
        this.totalMinutes = totalMinutes;
        this.totalData = totalData;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    @Override
    public String toString() {
        return employeeId + " : " + date + " : Minutes: " + totalMinutes + " : Data: " + totalData;
    }

    @Override
    public int compareTo(Object o) {
        return Long.compare(date.toInstant().toEpochMilli(), ((UsageRecord) o).getDate().toInstant().toEpochMilli());
    }
}
