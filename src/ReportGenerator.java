import service.DataService;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;

public class ReportGenerator extends Frame {
    public static void main(String[] args) {
        reportGeneratorUI();
    }

    public static void reportGeneratorUI() {
        UIHandler uiHandler = new UIHandler();

        JFrame frame = new JFrame("Cell phone usage report generator");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 175);
        //frame.setLocation(500, 500);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        frame.add(panel);

        JButton importRecordsBtn = new JButton("Import employee records");
        importRecordsBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        importRecordsBtn.addActionListener(uiHandler);
        importRecordsBtn.setActionCommand(UIHandler.IMPORT_USERS);
        panel.add(importRecordsBtn);

        JButton importDataBtn = new JButton("Import employee usage data");
        importDataBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        importDataBtn.addActionListener(uiHandler);
        importDataBtn.setActionCommand(UIHandler.IMPORT_DATA);
        panel.add(importDataBtn);

        JLabel label = new JLabel("Select a year to generate a report for:");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);

        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);

        //Populate year dropdown
        Integer[] years = new Integer[5];
        int startYear = cal.get(Calendar.YEAR);
        for(int i = 0; i < 5; i++) {
            years[i] = startYear - i;
        }

        JComboBox<Integer> yearDropdown = new JComboBox<Integer>(years);

        yearDropdown.setMaximumSize(yearDropdown.getPreferredSize());
        yearDropdown.setAlignmentX(Component.CENTER_ALIGNMENT);
        uiHandler.setYearDropdown(yearDropdown);
        panel.add(yearDropdown);


        JButton runReportButton = new JButton("Generate report");
        runReportButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        runReportButton.setEnabled(false);
        runReportButton.addActionListener(uiHandler);
        runReportButton.setActionCommand(UIHandler.RUN_REPORT);
        uiHandler.setRunReportButton(runReportButton);

        panel.add(runReportButton);
        //frame.pack();
        frame.setVisible(true);
    }
}
