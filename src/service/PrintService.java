package service;

import model.Account;
import model.MonthlyUsagePerDevice;
import model.ReportData;

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PrintService implements Printable {
    private SimpleDateFormat usageDateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private DecimalFormat df = new DecimalFormat("#.##");
    private ReportData reportData;
    private int year;
    private final int LEFT_MARGIN = 30;
    private final int TOP_MARGIN = 30;
    private final int PIXELS_PER_LINE = 14;
    private final int MONTH_COL_WIDTH = 30;
    private final int[] HEADER_COLS = new int[]{
            0, //Employee id
            70, //Employee name
            165, //Number of phones
            215, //Total minutes
            265, //Total data
            315, //Average minutes
            365 //Average data
    };

    private final int[] DETAIL_COLS = new int[]{
            0, //Model
            70, //Purchase Date
            165, //Minutes Usage
            300, //Data Usage
    };

    private final String[] MONTHS = new String[] {"JAN","FEB", "MAR", "APR","MAY", "JUN", "JUL", "AUG", "SEP","OCT", "NOV", "DEC"};

    public PrintService() {

    }


    //Generates a report, opens the print dialog, and outputs to a local printer
    //Precondition: accountMap and filteredUsageRecordList contain data only for the year specified
    public boolean printReport(ReportData reportData, int year) throws Exception {
        this.reportData = reportData;
        this.year = year;

        if(reportData.getMonthlyUsagePerDeviceMap().isEmpty()) {
            throw new Exception("No data to export for the specified year");
        }

        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);
        if(job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException ex) {
                throw new Exception("Print job did not complete");
            }
            return true;
        } else {
            return false;
        }
    }

    public int print(Graphics g, PageFormat formmat, int page) {
        if(page > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(formmat.getImageableX(), formmat.getImageableY());
        g2d.setFont(new Font("TimesRoman", Font.PLAIN, 12));
        int lineNumber = 0;

        lineNumber = generateHeader(g2d, lineNumber);
        lineNumber = generateData(g2d, lineNumber);

        return PAGE_EXISTS;
    }

    private int generateHeader(Graphics2D g2d, int lineNumber) {
        Font headerFont = new Font("TimesRoman", Font.PLAIN, 12);
        g2d.setFont(headerFont);

        g2d.drawString("Cell phone usage report for year: " + year + "\n", LEFT_MARGIN, TOP_MARGIN);
        lineNumber++;
        g2d.drawString("Report Run Date: " + usageDateFormat.format(reportData.getRunDate()), LEFT_MARGIN, TOP_MARGIN + (lineNumber * PIXELS_PER_LINE));
        lineNumber++;


        Calendar cal = Calendar.getInstance();
        cal.set(year, 0, 1);
        Date firstDayOfYear = cal.getTime();

        List<Integer> employeeIds = reportData.getAccountMap().keySet().stream().sorted().collect(Collectors.toList());
        lineNumber++; //Blank line
        //Write column headers

        g2d.drawString("Employee Id", LEFT_MARGIN + HEADER_COLS[0], TOP_MARGIN + (lineNumber * PIXELS_PER_LINE));
        g2d.drawString("Employee Name", LEFT_MARGIN + HEADER_COLS[1], TOP_MARGIN + (lineNumber * PIXELS_PER_LINE));

        g2d.drawString("Number", LEFT_MARGIN + HEADER_COLS[2], TOP_MARGIN + (lineNumber * PIXELS_PER_LINE));
        g2d.drawString("of Phones", LEFT_MARGIN + HEADER_COLS[2], TOP_MARGIN + ((lineNumber + 1) * PIXELS_PER_LINE));

        g2d.drawString("Total", LEFT_MARGIN + HEADER_COLS[3], TOP_MARGIN + (lineNumber * PIXELS_PER_LINE));
        g2d.drawString("Minutes", LEFT_MARGIN + HEADER_COLS[3], TOP_MARGIN + ((lineNumber + 1)* PIXELS_PER_LINE));

        g2d.drawString("Total", LEFT_MARGIN + HEADER_COLS[4], TOP_MARGIN + (lineNumber * PIXELS_PER_LINE));
        g2d.drawString("Data", LEFT_MARGIN + HEADER_COLS[4], TOP_MARGIN + ((lineNumber + 1) * PIXELS_PER_LINE));

        g2d.drawString("Average", LEFT_MARGIN + HEADER_COLS[5], TOP_MARGIN + (lineNumber * PIXELS_PER_LINE));
        g2d.drawString("Minutes", LEFT_MARGIN + HEADER_COLS[5], TOP_MARGIN + ((lineNumber + 1) * PIXELS_PER_LINE));

        g2d.drawString("Average", LEFT_MARGIN + HEADER_COLS[6], TOP_MARGIN + (lineNumber * PIXELS_PER_LINE));
        g2d.drawString("Data", LEFT_MARGIN + HEADER_COLS[6], TOP_MARGIN + ((lineNumber + 1) * PIXELS_PER_LINE));

        lineNumber+=2;

        for(Integer emoloyeeId : employeeIds) {
            List<Account> accounts = reportData.getAccountMap().get(emoloyeeId);
            int totalMinutes = 0;
            float totalData = 0;
            int numberOfMonths = 12;
            if(accounts.get(0).getPurchaseDate().after(firstDayOfYear)) {
                numberOfMonths = (12 - accounts.get(0).getPurchaseDate().getMonth());
            }

            g2d.drawString(accounts.get(0).getEmployeeId().toString(), LEFT_MARGIN + HEADER_COLS[0], TOP_MARGIN + (lineNumber * PIXELS_PER_LINE));
            g2d.drawString(accounts.get(0).getEmployeeName(), LEFT_MARGIN + HEADER_COLS[1], TOP_MARGIN + (lineNumber * PIXELS_PER_LINE));

            for(Account account : accounts) {
                if(reportData.getMonthlyUsagePerDeviceMap().get(account.getAccountHashKey()) != null) {
                    MonthlyUsagePerDevice monthlyUsagePerDevice = reportData.getMonthlyUsagePerDeviceMap().get(account.getAccountHashKey());
                    totalData += monthlyUsagePerDevice.getTotalData();
                    totalMinutes += monthlyUsagePerDevice.getTotalMinutes();
                }
            }

            g2d.drawString(String.valueOf(accounts.size()), LEFT_MARGIN + HEADER_COLS[2], TOP_MARGIN + (lineNumber * PIXELS_PER_LINE));
            g2d.drawString(String.valueOf(totalMinutes), LEFT_MARGIN + HEADER_COLS[3], TOP_MARGIN + (lineNumber * PIXELS_PER_LINE));
            g2d.drawString(String.valueOf(totalData), LEFT_MARGIN + HEADER_COLS[4], TOP_MARGIN + (lineNumber * PIXELS_PER_LINE));
            g2d.drawString(String.valueOf(totalMinutes / numberOfMonths), LEFT_MARGIN + HEADER_COLS[5], TOP_MARGIN + (lineNumber * PIXELS_PER_LINE));
            g2d.drawString(df.format(totalData / (float) numberOfMonths), LEFT_MARGIN + HEADER_COLS[6], TOP_MARGIN + (lineNumber * PIXELS_PER_LINE));
            lineNumber++;
        }
        return lineNumber;
    }

    private int generateData(Graphics2D g2d, int lineNumber) {
        Font headerFont = new Font("TimesRoman", Font.PLAIN, 10);
        g2d.setFont(headerFont);

        List<Integer> employeeIds = reportData.getAccountMap().keySet().stream().sorted().collect(Collectors.toList());

        for(Integer emoloyeeId : employeeIds) {
            lineNumber++;
            List<Account> accounts = reportData.getAccountMap().get(emoloyeeId);
            g2d.drawString("Employee Id: " + emoloyeeId, LEFT_MARGIN, TOP_MARGIN + (lineNumber * PIXELS_PER_LINE));
            lineNumber++;
            g2d.drawString("Employee Name: " + accounts.get(0).getEmployeeName(), LEFT_MARGIN, TOP_MARGIN + (lineNumber * PIXELS_PER_LINE));
            lineNumber++;

            drawDetailsHeader(g2d, lineNumber);
        }
        return lineNumber;
    }

    private int drawDetailsHeader(Graphics2D g2d, int lineNumber) {
        g2d.drawString("Model", LEFT_MARGIN + DETAIL_COLS[0], TOP_MARGIN + (lineNumber * PIXELS_PER_LINE));
        g2d.drawString("Purchase Date", LEFT_MARGIN + DETAIL_COLS[1], TOP_MARGIN + (lineNumber * PIXELS_PER_LINE));
        lineNumber++;
        return lineNumber;
    }
}
