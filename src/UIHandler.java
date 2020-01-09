import service.DataService;
import service.PrintService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class UIHandler implements ActionListener {
    public static final String IMPORT_USERS = "importUsers";
    public static final String IMPORT_DATA = "importData";
    public static final String RUN_REPORT = "runReport";
    private DataService dataService;
    private PrintService printService;
    JButton runReportButton;
    JComboBox<Integer> yearDropdown;
    JLabel statusLabel;
    JPanel panel;

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

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String action = actionEvent.getActionCommand();
        try {
            switch (action) {
                case IMPORT_USERS:
                    File userFile = getFile();
                    dataService.importUsers(userFile);
                    statusLabel.setForeground(Color.BLUE);
                    statusLabel.setText("User and device data imported");
                    break;
                case IMPORT_DATA:
                    File dataFile = getFile();
                    dataService.importData(dataFile);
                    statusLabel.setForeground(Color.BLUE);
                    statusLabel.setText("Usage data imported");
                    break;
                case RUN_REPORT:
                    Integer year = Integer.parseInt(yearDropdown.getSelectedItem().toString());
                    printService.printReport(dataService.getReportDataForYear(year), year);
                    statusLabel.setForeground(Color.BLUE);
                    statusLabel.setText("Export complete");
            }
            if (dataService.getDataLoaded()) {
                runReportButton.setEnabled(true);
            }
        } catch (Exception e) {
            statusLabel.setForeground(Color.RED);
            statusLabel.setText(e.getMessage());
            System.out.println(e.getStackTrace());
        }
    }

    private File getFile() throws Exception {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(panel);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        } else {
            throw new Exception("No file selected");
        }
    }
}
