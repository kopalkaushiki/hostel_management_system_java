# Hostel Management System (Java + MySQL)

A desktop-based Hostel Room Allocation System built with Java Swinv and MySQL. 
This application allows hostel administrators to manage room assignments, student data, and employee records through a user-friendly interface.

## 🛠 Features

- Room allocation and deallocation
- Student registration and tracking
- Employee data management
- MySQL database integration
- NetBeans-based GUI using Swing

## 📁 Project Structure
hostel_management_system_java/
├── src/
│ └── main/
│ └── java/
│ └── com/
│ └── hostel/
│ ├── model/
│ ├── db/
│ ├── dao/
│ └── ui/
├── lib/ (MySQL connector JAR)
└── README.md

## ⚙️ Requirements

- **Java JDK 11 or later**
- **NetBeans IDE** (recommended)
- **MySQL Server 8.0 or newer**
- **MySQL Connector/J** (JAR file added to project libraries)

## 🧩 Database Setup

1. Open **MySQL Workbench** or any MySQL client.
2. Run the SQL script below to create the required schema and tables:

```sql
CREATE DATABASE IF NOT EXISTS hostel_management;
USE hostel_management;

-- Run the rest of the SQL from your SQL script (students, rooms, employees)
String url = "jdbc:mysql://localhost:3306/hostel_management";
String user = "your_mysql_username";
String password = "your_mysql_password";

