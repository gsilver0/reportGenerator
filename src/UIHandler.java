import model.Account;
import model.UsageRecord;
import service.DataService;
import service.PrintService;

import javax.swing.*;
import java.awt.*;
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
    JLabel statusLabel;

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

    public void setStatusLabel(JLabel statusLabel) {
        this.statusLabel = statusLabel;
    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String action = actionEvent.getActionCommand();
        try {
            switch (action) {
                case IMPORT_USERS:
                    dataService.importUsers();
                    statusLabel.setForeground(Color.BLUE);
                    statusLabel.setText("User and device data imported");
                    break;
                case IMPORT_DATA:
                    dataService.importData();
                    statusLabel.setForeground(Color.BLUE);
                    statusLabel.setText("Usage data imported");
                    break;
                case RUN_REPORT:
                    List<Account> accountList = dataService.getAccountList();
                    List<UsageRecord> usageRecordList = dataService.getUsageRecordList();
                    printService.printReport(accountList, usageRecordList, Integer.parseInt(yearDropdown.getSelectedItem().toString()));
                    statusLabel.setForeground(Color.BLUE);
                    statusLabel.setText("Export complete");
            }
            if (dataService.getDataLoaded()) {
                runReportButton.setEnabled(true);
            }
        } catch (Exception e) {
            statusLabel.setForeground(Color.RED);
            statusLabel.setText(e.getMessage());
        }
    }
}
