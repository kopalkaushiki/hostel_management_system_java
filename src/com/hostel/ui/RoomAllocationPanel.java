package com.hostel.ui;

import com.hostel.dao.RoomDAO;
import com.hostel.dao.StudentDAO;
import com.hostel.model.Room;
import com.hostel.model.Student;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class RoomAllocationPanel extends JPanel {
    private JComboBox<String> studentCombo;
    private JComboBox<String> roomCombo;
    private JTable roomTable;
    private JScrollPane tableScrollPane;
    private JButton allocateButton;
    private JButton refreshButton;
    private RoomDAO roomDAO;
    private StudentDAO studentDAO;

    public RoomAllocationPanel() {
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

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Select Room:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        roomCombo = new JComboBox<>();
        formPanel.add(roomCombo, gbc);

        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        allocateButton = new JButton("Allocate Room");
        refreshButton = new JButton("Refresh");

        buttonPanel.add(allocateButton);
        buttonPanel.add(refreshButton);

        // Create table
        String[] columnNames = {
            "Room Number", "Capacity", "Current Occupancy", "Status"
        };
        Object[][] data = {}; // Will be populated with actual data
        roomTable = new JTable(data, columnNames);
        tableScrollPane = new JScrollPane(roomTable);

        // Add panels to main panel
        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(tableScrollPane, BorderLayout.SOUTH);

        // Add action listeners
        allocateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedStudent = (String) studentCombo.getSelectedItem();
                String selectedRoom = (String) roomCombo.getSelectedItem();
                
                if (selectedStudent == null || selectedRoom == null) {
                    JOptionPane.showMessageDialog(RoomAllocationPanel.this,
                            "Please select both student and room",
                            "Selection Required",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Extract IDs from the selected items
                int studentId = Integer.parseInt(selectedStudent.split(" - ")[0]);
                int roomId = Integer.parseInt(selectedRoom.split(" - ")[0]);

                if (roomDAO.allocateRoom(studentId, roomId)) {
                    JOptionPane.showMessageDialog(RoomAllocationPanel.this,
                            "Room allocated successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    refreshData();
                } else {
                    JOptionPane.showMessageDialog(RoomAllocationPanel.this,
                            "Failed to allocate room",
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
        // Load students
        List<Student> students = studentDAO.getAllStudents();
        studentCombo.removeAllItems();
        for (Student student : students) {
            studentCombo.addItem(student.getId() + " - " + student.getName());
        }

        // Load available rooms
        List<Room> rooms = roomDAO.getAvailableRooms();
        roomCombo.removeAllItems();
        for (Room room : rooms) {
            roomCombo.addItem(room.getId() + " - " + room.getRoomNumber());
        }

        // Load room table data
        Object[][] data = new Object[rooms.size()][4];
        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            data[i][0] = room.getRoomNumber();
            data[i][1] = room.getCapacity();
            data[i][2] = room.getCurrentOccupancy();
            data[i][3] = room.getCurrentOccupancy() < room.getCapacity() ? "Available" : "Full";
        }

        String[] columnNames = {
            "Room Number", "Capacity", "Current Occupancy", "Status"
        };
        roomTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }

    private void refreshData() {
        loadData();
    }
}