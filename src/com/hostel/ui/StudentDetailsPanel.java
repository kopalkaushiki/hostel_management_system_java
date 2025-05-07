package com.hostel.ui;

import com.hostel.dao.StudentDAO;
import com.hostel.dao.RoomDAO;
import com.hostel.model.Student;
import com.hostel.model.Room;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;

public class StudentDetailsPanel extends JPanel {
    private JTable studentTable;
    private JScrollPane tableScrollPane;
    private JButton refreshButton;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private StudentDAO studentDAO;
    private RoomDAO roomDAO;

    public StudentDetailsPanel() {
        studentDAO = new StudentDAO();
        roomDAO = new RoomDAO();
        
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        refreshButton = new JButton("Refresh");
        addButton = new JButton("Add Student");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");

        buttonPanel.add(refreshButton);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // Create table
        String[] columnNames = {
            "ID", "Name", "Email", "Phone", "Room Number", "Room Type", "Status"
        };
        Object[][] data = {}; // Will be populated with actual data
        studentTable = new JTable(data, columnNames);
        tableScrollPane = new JScrollPane(studentTable);

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
                openRegistrationFrame();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = studentTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(StudentDetailsPanel.this,
                            "Please select a student to edit",
                            "No Selection",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Get the selected student's data
                int studentId = (int) studentTable.getValueAt(selectedRow, 0);
                String name = (String) studentTable.getValueAt(selectedRow, 1);
                String email = (String) studentTable.getValueAt(selectedRow, 2);
                String phone = (String) studentTable.getValueAt(selectedRow, 3);

                // Create a dialog for editing
                JTextField nameField = new JTextField(name);
                JTextField emailField = new JTextField(email);
                JTextField phoneField = new JTextField(phone);

                JPanel panel = new JPanel(new GridLayout(3, 2));
                panel.add(new JLabel("Name:"));
                panel.add(nameField);
                panel.add(new JLabel("Email:"));
                panel.add(emailField);
                panel.add(new JLabel("Phone:"));
                panel.add(phoneField);

                int result = JOptionPane.showConfirmDialog(
                    StudentDetailsPanel.this,
                    panel,
                    "Edit Student",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
                );

                if (result == JOptionPane.OK_OPTION) {
                    // Get the student from database to update
                    Student student = studentDAO.getStudentById(studentId);
                    if (student != null) {
                        // Update the student's information
                        student.setName(nameField.getText());
                        student.setEmail(emailField.getText());
                        student.setPhoneNumber(phoneField.getText());

                        // Save the changes
                        if (studentDAO.updateStudent(student)) {
                            JOptionPane.showMessageDialog(StudentDetailsPanel.this,
                                    "Student information updated successfully!",
                                    "Success",
                                    JOptionPane.INFORMATION_MESSAGE);
                            refreshData();
                        } else {
                            JOptionPane.showMessageDialog(StudentDetailsPanel.this,
                                    "Failed to update student information",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = studentTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(StudentDetailsPanel.this,
                            "Please select a student to delete",
                            "No Selection",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int studentId = (int) studentTable.getValueAt(selectedRow, 0);
                String studentName = (String) studentTable.getValueAt(selectedRow, 1);

                int choice = JOptionPane.showConfirmDialog(
                    StudentDetailsPanel.this,
                    "Are you sure you want to delete " + studentName + "?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
                );

                if (choice == JOptionPane.YES_OPTION) {
                    if (studentDAO.deleteStudent(studentId)) {
                        JOptionPane.showMessageDialog(StudentDetailsPanel.this,
                                "Student deleted successfully!",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                        refreshData();
                    } else {
                        JOptionPane.showMessageDialog(StudentDetailsPanel.this,
                                "Failed to delete student",
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
        List<Student> students = studentDAO.getAllStudents();
        Object[][] data = new Object[students.size()][7];
        
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            data[i][0] = student.getId();
            data[i][1] = student.getName();
            data[i][2] = student.getEmail();
            data[i][3] = student.getPhoneNumber();
            
            // Get room information if student has a room allocated
            if (student.getRoomId() > 0) {
                Room room = roomDAO.getRoomById(student.getRoomId());
                if (room != null) {
                    data[i][4] = room.getRoomNumber();
                    data[i][5] = room.getRoomType();
                    data[i][6] = "Allocated";
                } else {
                    data[i][4] = "N/A";
                    data[i][5] = "N/A";
                    data[i][6] = "Not Allocated";
                }
            } else {
                data[i][4] = "N/A";
                data[i][5] = "N/A";
                data[i][6] = "Not Allocated";
            }
        }

        String[] columnNames = {
            "ID", "Name", "Email", "Phone", "Room Number", "Room Type", "Status"
        };
        studentTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }

    private void refreshData() {
        loadData();
    }

    private void openRegistrationFrame() {
        RegistrationFrame registrationFrame = new RegistrationFrame();
        registrationFrame.setVisible(true);
    }
}