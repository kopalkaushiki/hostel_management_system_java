package com.hostel.ui;

import com.hostel.dao.RoomDAO;
import com.hostel.dao.StudentDAO;
import com.hostel.model.Student;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class RoomDeallocationPanel extends JPanel {
    private JComboBox<String> studentCombo;
    private JButton deallocateButton;
    private JButton refreshButton;
    private RoomDAO roomDAO;
    private StudentDAO studentDAO;

    public RoomDeallocationPanel() {
        roomDAO = new RoomDAO();
        studentDAO = new StudentDAO();
        
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add components
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Select Student:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        studentCombo = new JComboBox<>();
        formPanel.add(studentCombo, gbc);

        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        deallocateButton = new JButton("Deallocate Room");
        refreshButton = new JButton("Refresh");

        buttonPanel.add(deallocateButton);
        buttonPanel.add(refreshButton);

        // Add panels to main panel
        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        // Add action listeners
        deallocateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedStudent = (String) studentCombo.getSelectedItem();
                
                if (selectedStudent == null) {
                    JOptionPane.showMessageDialog(RoomDeallocationPanel.this,
                            "Please select a student to deallocate",
                            "Selection Required",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int studentId = Integer.parseInt(selectedStudent.split(" - ")[0]);

                if (roomDAO.deallocateRoom(studentId)) {
                    JOptionPane.showMessageDialog(RoomDeallocationPanel.this,
                            "Room deallocated successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    refreshData();
                } else {
                    JOptionPane.showMessageDialog(RoomDeallocationPanel.this,
                            "Failed to deallocate room",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshData();
            }
        });

        // Load initial data
        loadData();
    }

    private void loadData() {
        // Load students with rooms
        List<Student> students = studentDAO.getStudentsWithRooms();
        studentCombo.removeAllItems();
        for (Student student : students) {
            studentCombo.addItem(student.getId() + " - " + student.getName());
        }
    }

    private void refreshData() {
        loadData();
    }
}