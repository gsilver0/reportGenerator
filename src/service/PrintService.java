package service;

import model.Account;
import model.LineOffsets;
import model.MonthlyUsagePerDevice;
import model.ReportData;

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PrintService implements Printable {
    private SimpleDateFormat usageDateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private DecimalFormat df = new DecimalFormat("#.##");
    private ReportData reportData;
    private LineOffsets lineOffsets;
    private int year;
    private final int LEFT_MARGIN = 30;
    private final int TOP_MARGIN = 30;
    private final int LINES_PER_PAGE_LANDSCAPE = 25;
    private final int LINES_PER_PAGE_PORTRAIT = 50;

    private final int FIRST_PAGE_HEADER_LINES = 5;
    private final int HEADER_LINES = 2;
    private final int DETAILS_HEADER_LINES = 3;
    private final int DETAILS_DATA_LINES = 2;

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
            130, //Usage label
            170 //First months column
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
                lineOffsets = null; //Recompute once per print job
                job.print();
            } catch (PrinterException ex) {
                throw new Exception("Print job did not complete");
            }
            return true;
        } else {
            return false;
        }
    }

    public int print(Graphics g, PageFormat format, int page) {
        int linesPerPage;
        if(format.getOrientation() == PageFormat.LANDSCAPE) {
            linesPerPage = LINES_PER_PAGE_LANDSCAPE;
        } else {
            linesPerPage = LINES_PER_PAGE_PORTRAIT;
        }

        Integer headerOffset = 0;
        Integer dataOffset = 0;

        if(lineOffsets == null) {
            lineOffsets = calculateNumberOfPages(linesPerPage);
        }
        if(page > 0) {
            if(page > lineOffsets.getNumberOfPages()) {
                return NO_SUCH_PAGE;
            }
            if(page < lineOffsets.getHeaderAccountOffsetPerPage().size()) {
                headerOffset = lineOffsets.getHeaderAccountOffsetPerPage().get(page);
            } else {
                headerOffset = -1;
            }
            dataOffset = lineOffsets.getAccountOffsetPerPage().get(page);
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(format.getImageableX(), format.getImageableY());
        g2d.setFont(new Font("TimesRoman", Font.PLAIN, 12));
        int lineNumber = 0;


        if(headerOffset > -1) {
            lineNumber = generateHeader(g2d, lineNumber, linesPerPage, headerOffset);
        }
        if(lineNumber < linesPerPage && lineOffsets.getAccountOffsetPerPage().get(page) > -1) {
            lineNumber = generateData(g2d, lineNumber, linesPerPage, dataOffset);
        }

        return PAGE_EXISTS;
    }

    private int generateHeader(Graphics2D g2d, int lineNumber, int linesPerPage, int headerOffset) {
        Font headerFont = new Font("TimesRoman", Font.PLAIN, 12);
        g2d.setFont(headerFont);

        if(headerOffset == 0) {
            drawText(g2d, "Cell phone usage report for year: " + year + "\n", 0, 0);
            lineNumber++;
            drawText(g2d, "Report Run Date: " + usageDateFormat.format(reportData.getRunDate()), 0, lineNumber);
            lineNumber+= 2; //Blank line
        }

        Calendar cal = Calendar.getInstance();
        cal.set(year, 0, 1);
        Date firstDayOfYear = cal.getTime();

        List<Integer> employeeIds = reportData.getAccountMap().keySet().stream().sorted().collect(Collectors.toList());

        //Write column headers

        drawText(g2d, "Employee Id", HEADER_COLS[0], lineNumber);
        drawText(g2d, "Employee Name", HEADER_COLS[1], lineNumber);

        drawText(g2d, "Number", HEADER_COLS[2], lineNumber);
        drawText(g2d, "of Phones", HEADER_COLS[2], lineNumber + 1);

        drawText(g2d, "Total", HEADER_COLS[3], lineNumber);
        drawText(g2d, "Minutes", HEADER_COLS[3], lineNumber + 1);

        drawText(g2d, "Total", HEADER_COLS[4], lineNumber);
        drawText(g2d, "Data", HEADER_COLS[4], lineNumber + 1);

        drawText(g2d, "Average", HEADER_COLS[5], lineNumber);
        drawText(g2d, "Minutes", HEADER_COLS[5], lineNumber + 1);

        drawText(g2d, "Average", HEADER_COLS[6], lineNumber);
        drawText(g2d, "Data", HEADER_COLS[6], lineNumber + 1);

        lineNumber+=2;

        int currentEmployee = 0;
        for(Integer emoloyeeId : employeeIds) {
            if(currentEmployee < headerOffset) {
                currentEmployee++;
                continue;
            }
            List<Account> accounts = reportData.getAccountMap().get(emoloyeeId);
            int totalMinutes = 0;
            float totalData = 0;
            int numberOfMonths = 12;
            if(accounts.get(0).getPurchaseDate().after(firstDayOfYear)) {
                numberOfMonths = (12 - accounts.get(0).getPurchaseDate().getMonth());
            }

            drawText(g2d, accounts.get(0).getEmployeeId().toString(), HEADER_COLS[0], lineNumber);
            drawText(g2d, accounts.get(0).getEmployeeName(), HEADER_COLS[1], lineNumber);

            for(Account account : accounts) {
                if(reportData.getMonthlyUsagePerDeviceMap().get(account.getAccountHashKey()) != null) {
                    MonthlyUsagePerDevice monthlyUsagePerDevice = reportData.getMonthlyUsagePerDeviceMap().get(account.getAccountHashKey());
                    totalData += monthlyUsagePerDevice.getTotalData();
                    totalMinutes += monthlyUsagePerDevice.getTotalMinutes();
                }
            }

            drawText(g2d, String.valueOf(accounts.size()), HEADER_COLS[2], lineNumber);
            drawText(g2d, String.valueOf(totalMinutes), HEADER_COLS[3], lineNumber);
            drawText(g2d, String.valueOf(df.format(totalData)), HEADER_COLS[4], lineNumber);
            drawText(g2d, String.valueOf(totalMinutes / numberOfMonths), HEADER_COLS[5], lineNumber);
            float averageData = totalData / (float) numberOfMonths;
            drawText(g2d, df.format(averageData), HEADER_COLS[6], lineNumber);
            lineNumber++;

            currentEmployee++;
            if(lineNumber >= linesPerPage) {
                return lineNumber;
            }
        }
        return lineNumber;
    }

    private int generateData(Graphics2D g2d, int lineNumber, int linesPerPage, int dataOffset) {
        Font headerFont = new Font("TimesRoman", Font.PLAIN, 10);
        g2d.setFont(headerFont);

        List<Integer> employeeIds = reportData.getAccountMap().keySet().stream().sorted().collect(Collectors.toList());
        int currentEmployee = 0;
        for(Integer emoloyeeId : employeeIds) {
            if(currentEmployee < dataOffset) {
                currentEmployee++;
                continue;
            }
            List<Account> accounts = reportData.getAccountMap().get(emoloyeeId);
            if(lineNumber + DETAILS_HEADER_LINES + (DETAILS_DATA_LINES * accounts.size()) > linesPerPage) {
                return lineNumber; //End of page reached
            }
            lineNumber++;
            drawText(g2d, "Employee: " + emoloyeeId + " : " + accounts.get(0).getEmployeeName(), 0, lineNumber);
            lineNumber++;
            lineNumber = drawDetailsHeader(g2d, lineNumber);
            lineNumber = drawAccountDetails(accounts, g2d, lineNumber);
            currentEmployee++;
            //lineNumber++; //Blank line below current employee
        }
        return lineNumber;
    }

    private int drawDetailsHeader(Graphics2D g2d, int lineNumber) {
        drawText(g2d, "Model", DETAIL_COLS[0], lineNumber);
        drawText(g2d, "Purchase Date", DETAIL_COLS[1], lineNumber);
        drawText(g2d, "Usage", DETAIL_COLS[2], lineNumber);
        for(int i = 0; i < 12; i++) {
            drawText(g2d, MONTHS[i], DETAIL_COLS[3] + (i * MONTH_COL_WIDTH), lineNumber);
        }
        lineNumber++;
        return lineNumber;
    }

    private int drawAccountDetails( List<Account> accounts, Graphics2D g2d, int lineNumber) {
        for(Account account : accounts) {
            MonthlyUsagePerDevice deviceUsage = reportData.getMonthlyUsagePerDeviceMap().get(account.getAccountHashKey());
            drawTextWrap(g2d, account.getModel(), DETAIL_COLS[0], lineNumber, 10);
            drawText(g2d, usageDateFormat.format(account.getPurchaseDate()), DETAIL_COLS[1], lineNumber);
            drawText(g2d, "Minutes", DETAIL_COLS[2], lineNumber);
            drawText(g2d, "Data", DETAIL_COLS[2], lineNumber + 1);
            if(deviceUsage != null) {
                for(int i = 0; i < 12; i++) {
                    drawText(g2d, String.valueOf(deviceUsage.getMinutesUsage()[i]), DETAIL_COLS[3] + (i * MONTH_COL_WIDTH), lineNumber);
                    drawText(g2d, df.format(deviceUsage.getDataUsage()[i]), DETAIL_COLS[3] + (i * MONTH_COL_WIDTH), lineNumber + 1);
                }
            }
            lineNumber += 2;
        }
        return lineNumber;
    }

    private LineOffsets calculateNumberOfPages(int linesPerPage) {
        //Calculate header lines
        int pages = 0;
        int headerAccountOffset = 0;
        int line = FIRST_PAGE_HEADER_LINES + reportData.getAccountMap().size();
        int firstPageAccounts = linesPerPage - FIRST_PAGE_HEADER_LINES;
        int subsequentPageAccounts = linesPerPage - HEADER_LINES;

        List<Integer> headerOffsetPerPage = new ArrayList<>();
        List<Integer> accountOffsetPerPage = new ArrayList<>();
        while (line > linesPerPage) {
            if(pages == 0) {
                headerAccountOffset = 0;
            } else if(pages == 1) {
                headerAccountOffset += firstPageAccounts;
            } else {
                headerAccountOffset += subsequentPageAccounts;
            }
            headerOffsetPerPage.add(headerAccountOffset);
            accountOffsetPerPage.add(-1);
            pages++;
            line -= linesPerPage;
            line += HEADER_LINES;
        }
        headerOffsetPerPage.add(headerAccountOffset);
        int firstDetailsPage = pages;
        int fistDetailsLine = line;
        int accountOffset = 0;

        //Calculate data lines
        accountOffsetPerPage.add(0);
        for(List<Account> accounts : reportData.getAccountMap().values()) {
            if((line + DETAILS_HEADER_LINES + (accounts.size() * DETAILS_DATA_LINES)) > linesPerPage) {
                accountOffsetPerPage.add(accountOffset);
                pages++;
                line = DETAILS_HEADER_LINES + (accounts.size() * DETAILS_DATA_LINES); //Put account on new page
            } else {
                line += DETAILS_HEADER_LINES + (accounts.size() * DETAILS_DATA_LINES); //Put on current page
            }
            accountOffset++;
        }
        return new LineOffsets(pages, firstDetailsPage, fistDetailsLine, headerOffsetPerPage, accountOffsetPerPage);
    }

    private void drawText(Graphics2D g2d, String text, int xOffset, int lineNumber) {
        g2d.drawString(text, LEFT_MARGIN + xOffset, TOP_MARGIN + (lineNumber * PIXELS_PER_LINE));
    }

    private void drawTextWrap(Graphics2D g2d, String text, int xOffset, int lineNumber, int maxChars) {
        if(text.length() > maxChars) {
            String[] splitText = text.split(" ");
            //We could find the best pivot point, but this is a quick fix
            g2d.drawString(splitText[0], LEFT_MARGIN + xOffset, TOP_MARGIN + (lineNumber * PIXELS_PER_LINE));
            g2d.drawString(String.join(" ", Arrays.copyOfRange(splitText, 1, splitText.length)), LEFT_MARGIN + xOffset, TOP_MARGIN + ((lineNumber + 1) * PIXELS_PER_LINE));
        } else {
            g2d.drawString(text, LEFT_MARGIN + xOffset, TOP_MARGIN + (lineNumber * PIXELS_PER_LINE));
        }

    }
}
