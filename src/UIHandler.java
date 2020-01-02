import service.DataService;
import service.PrintService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UIHandler implements ActionListener {
    public static final String IMPORT_USERS = "importUsers";
    public static final String IMPORT_DATA = "importData";
    public static final String RUN_REPORT = "runReport";
    private DataService dataService;
    private PrintService printService;
    JButton runReportButton;

    public UIHandler() {
        dataService = new DataService();
        printService = new PrintService();
    }

    public void setRunReportButton(JButton runReportButton) {
        this.runReportButton = runReportButton;
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

        }
        if(dataService.getDataLoaded()) {
            runReportButton.setEnabled(true);
        }
    }
}
