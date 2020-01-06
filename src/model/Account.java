package model;

import java.util.Date;

public class Account implements Comparable{
    private Integer employeeId;
    private String employeeName;
    private Date purchaseDate;
    private String model;

    public Account() {

    }

    public Account(Integer employeeId, String employeeName, Date purchaseDate, String model) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.purchaseDate = purchaseDate;
        this.model = model;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return employeeId + " : " + employeeName + " : " + purchaseDate + " : " + model;
    }

    @Override
    public int compareTo(Object o) {
        return Long.compare(purchaseDate.toInstant().toEpochMilli(), ((Account) o).getPurchaseDate().toInstant().toEpochMilli());
    }

    public Long getAccountHashKey() {
        //Just a simple hash key to help with lookups...
        return Long.valueOf(employeeId) * 3L + Long.valueOf(purchaseDate.toInstant().toEpochMilli()) * 5L;
    }
}
