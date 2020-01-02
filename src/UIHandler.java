import model.Account;
import model.UsageRecord;
import service.DataService;
import service.PrintService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UIHandler implements ActionListener {
    public static final String IMPORT_USERS = "importUsers";
    public static final String IMPORT_DATA = "importData";
    public static final String RUN_REPORT = "runReport";
    private DataService dataService;
    private PrintService printService;
    JButton runReportButton;
    JComboBox<Integer> yearDropdown;

    public UIHandler() {
        dataService = new DataService();
        printService = new PrintService();
    }

    public void setRunReportButton(JButton runReportButton) {
        this.runReportButton = runReportButton;
    }

    public void setYearDropdown(JComboBox<Integer> yearDropdown) {
        this.yearDropdown = yearDropdown;
    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String action = actionEvent.getActionCommand();
        switch(action) {
            case IMPORT_USERS:
                dataService.importUsers();
                break;
            case IMPORT_DATA:
                dataService.importData();
                break;
            case RUN_REPORT:
                List<Account> accountList = dataService.getAccountList();
                List<UsageRecord> usageRecordList = dataService.getUsageRecordList();
                printService.printReport(accountList, usageRecordList, Integer.parseInt(yearDropdown.getSelectedItem().toString()));
        }
        if(dataService.getDataLoaded()) {
            runReportButton.setEnabled(true);
        }
    }
}
