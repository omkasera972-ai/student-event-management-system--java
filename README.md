<div align="center">
  
# 🎓 Student Event Management System (SCNEMS)

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)
![JDBC](https://img.shields.io/badge/JDBC-FF0000?style=for-the-badge&logo=java&logoColor=white)

*A robust, console-based Java application to seamlessly manage campus events, student registrations, and faculty coordination.*

</div>

---

## 🌟 Overview

The **Student Event Management System (SCNEMS)** is an enterprise-level Java application designed to streamline the workflow of college events. From organizing technical workshops to tracking student participation, this system handles it all seamlessly with a secure MySQL database integration.

## ✨ Key Features

- 👤 **Role-Based Management:** Independent modules for Students, Faculty, and Admins.
- 📅 **Event Organization:** Create, schedule, and manage college events efficiently.
- 🎟️ **Seamless Registrations:** Students can book slots for events (subject to seat availability).
- 📊 **Robust Reporting:** Generate insightful reports using dedicated Data Access Objects (DAO).
- 🛡️ **Validation & Exceptions:** Custom exception handling (e.g., `NoSeatsAvailableException`, `DuplicateEntityException`) ensures data integrity.
- 🗄️ **Database Driven:** Full CRUD operations powered by JDBC & MySQL.

---

## 🏗️ Architecture & Tech Stack

- **Language:** Core Java (JDK 8+)
- **Database:** MySQL
- **Driver:** MySQL Connector/J (`mysql-connector-j-26.7.0.jar`)
- **Design Pattern:** DAO (Data Access Object) Pattern & Service Layer Pattern

### 📁 Project Structure

```text
SCNEMS/
├── dao/          # Database operations (CRUD)
├── exceptions/   # Custom error handling
├── interfaces/   # System contracts (Bookable, Navigable)
├── models/       # POJO classes (Student, Event, Faculty)
├── services/     # Business logic layer
├── utils/        # Helper utilities (IDGenerator, ValidationUtils)
└── Main.java     # Application Entry Point
```

---

## 🚀 Getting Started

Follow these steps to run the project on your local machine.

### Prerequisites

1. **Java Development Kit (JDK):** Ensure Java is installed (`java -version`).
2. **MySQL Server:** Ensure MySQL is running locally.

### Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/omkasera972-ai/student-event-management-system--java.git
   cd student-event-management-system--java
   ```

2. **Database Setup:**
   - Open MySQL Workbench or terminal.
   - Execute the SQL queries (refer to `AlterTable.java` or DB setup scripts if available) to create the necessary tables for Students, Events, Faculty, and Registrations.
   - Update `dao/DBConnection.java` with your MySQL credentials (username and password).

3. **Add Dependencies:**
   - Ensure `lib/mysql-connector-j-26.7.0.jar` is added to your project's build path/classpath.

4. **Run the Application:**
   - Compile and run `Main.java` from your preferred IDE (VS Code, IntelliJ, Eclipse) or command line.

---

## 🎯 Usage

Once the application is running in the console, you will be presented with an interactive menu:
1. **Manage Students:** Add new students, view details.
2. **Manage Events:** Create new events, set capacity.
3. **Manage Faculty:** Assign faculty to events.
4. **Registrations:** Book events for students.
5. **Reports:** View analytics and registration lists.

---

## 🤝 Contributing

Contributions, issues, and feature requests are welcome! 
Feel free to check [issues page](https://github.com/omkasera972-ai/student-event-management-system--java/issues).

---

<div align="center">
  <b>Built with ❤️ by Om Kasera</b>
</div>
