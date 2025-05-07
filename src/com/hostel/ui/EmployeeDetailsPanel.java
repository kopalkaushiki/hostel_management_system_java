package com.hostel.ui;

import com.hostel.dao.EmployeeDAO;
import com.hostel.model.Employee;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EmployeeDetailsPanel extends JPanel {
    private JTable employeeTable;
    private JScrollPane tableScrollPane;
    private JButton refreshButton;
    private JButton addButton;
    private JButton deleteButton;
    private EmployeeDAO employeeDAO;

    public EmployeeDetailsPanel() {
        employeeDAO = new EmployeeDAO();
        
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        refreshButton = new JButton("Refresh");
        addButton = new JButton("Add Employee");
        deleteButton = new JButton("Delete");

        buttonPanel.add(refreshButton);
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        // Create table
        String[] columnNames = {
            "ID", "Name", "Email", "Phone", "Position", "Salary", "Status"
        };
        Object[][] data = {}; // Will be populated with actual data
        employeeTable = new JTable(data, columnNames);
        tableScrollPane = new JScrollPane(employeeTable);

        // Add panels to main panel
        add(buttonPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);

        // Add action listeners
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshData();
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openEmployeeRegistrationFrame();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = employeeTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(EmployeeDetailsPanel.this,
                            "Please select an employee to delete",
                            "No Selection",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int employeeId = (int) employeeTable.getValueAt(selectedRow, 0);
                String employeeName = (String) employeeTable.getValueAt(selectedRow, 1);

                int choice = JOptionPane.showConfirmDialog(
                    EmployeeDetailsPanel.this,
                    "Are you sure you want to delete " + employeeName + "?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
                );

                if (choice == JOptionPane.YES_OPTION) {
                    if (employeeDAO.deleteEmployee(employeeId)) {
                        JOptionPane.showMessageDialog(EmployeeDetailsPanel.this,
                                "Employee deleted successfully!",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                        refreshData();
                    } else {
                        JOptionPane.showMessageDialog(EmployeeDetailsPanel.this,
                                "Failed to delete employee",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Load initial data
        loadData();
    }

    private void loadData() {
        List<Employee> employees = employeeDAO.getAllEmployees();
        Object[][] data = new Object[employees.size()][7];
        
        for (int i = 0; i < employees.size(); i++) {
            Employee employee = employees.get(i);
            data[i][0] = employee.getId();
            data[i][1] = employee.getName();
            data[i][2] = employee.getEmail();
            data[i][3] = employee.getPhoneNumber();
            data[i][4] = employee.getPosition();
            data[i][5] = employee.getSalary();
            data[i][6] = employee.isActive() ? "Active" : "Inactive";
        }

        String[] columnNames = {
            "ID", "Name", "Email", "Phone", "Position", "Salary", "Status"
        };
        employeeTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }

    private void refreshData() {
        loadData();
    }

    private void openEmployeeRegistrationFrame() {
        EmployeeRegistrationFrame registrationFrame = new EmployeeRegistrationFrame();
        registrationFrame.setVisible(true);
    }
}