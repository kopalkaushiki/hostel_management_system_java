package com.hostel.ui;

import com.hostel.model.Student;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private Student student;
    private JTabbedPane tabbedPane;
    private StudentDetailsPanel studentDetailsPanel;
    private RoomAllocationPanel roomAllocationPanel;
    private RoomDeallocationPanel roomDeallocationPanel;
    private EmployeeDetailsPanel employeeDetailsPanel;
    private JPanel welcomePanel;

    public MainFrame(Student student) {
        this.student = student;
        
        setTitle("Hostel Management System - Welcome " + student.getName());
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Create welcome panel
        welcomePanel = new JPanel();
        welcomePanel.setLayout(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome to Hostel Management System", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomePanel.add(welcomeLabel, BorderLayout.CENTER);
        
        // Create other panels
        studentDetailsPanel = new StudentDetailsPanel();
        roomAllocationPanel = new RoomAllocationPanel();
        roomDeallocationPanel = new RoomDeallocationPanel();
        employeeDetailsPanel = new EmployeeDetailsPanel();
        
        // Add tabs
        tabbedPane.addTab("Welcome", new ImageIcon("icons/home.png"), welcomePanel);
        tabbedPane.addTab("Students", new ImageIcon("icons/students.png"), studentDetailsPanel);
        tabbedPane.addTab("Room Allocation", new ImageIcon("icons/room.png"), roomAllocationPanel);
        tabbedPane.addTab("Room Deallocation", new ImageIcon("icons/room.png"), roomDeallocationPanel);
        tabbedPane.addTab("Employees", new ImageIcon("icons/employees.png"), employeeDetailsPanel);
        
        // Add tabbed pane to frame
        add(tabbedPane);

        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem logoutItem = new JMenuItem("Logout");
        fileMenu.add(logoutItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // Add action listener for logout
        logoutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(
                    MainFrame.this,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION
                );
                
                if (choice == JOptionPane.YES_OPTION) {
                    openLoginFrame();
                }
            }
        });
    }

    private void openLoginFrame() {
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
        this.dispose();
    }
}