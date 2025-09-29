# Multilayer Architecture  

This project implements a **basic Library System** following the **multilayer architecture (N-tier)** approach. It was developed in **Java** with **Oracle Database** as the persistence engine.  

The system was designed as part of the **Information Systems Design** course, with the objective of applying principles of **modularity, separation of concerns, and object-oriented design**, leading to scalable and maintainable software.  

---

## Objectives  

- Apply **multilayer architecture (N-tier)** in a real-world case study.  
- Separate data persistence, business logic, and user interface into independent modules.  
- Implement a complete flow of library operations: **users, books (items), loans, reservations, and fines**.  
- Strengthen the understanding of the relationship between **conceptual, logical, and physical models** in information systems.  

---

## System Architecture  

The system follows the **multilayer design pattern**, where each layer has a clear and decoupled responsibility:
- View → Console (App.java) / Swing (optional)
- Controller → Orchestrate communication between View and Services
- Service → Business logic and validations
- DAO → Data access with JDBC and Oracle
- Model → Entities: User, Item, Loan, etc.

## Setup

### Clone the repository

```bash
git clone https://github.com/angelariasus/multilayer-architecture
cd multilayer-architecture
```
### Configure Oracle connection
Edit dato/Conexion.java:

```java
private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
private static final String USER = "user";
private static final String PASSWORD = "pass";
```

### Build and run (console mode)
```bash
mvn clean compile exec:java
```

## Key Learning Outcomes

- Practical application of object-oriented design in an academic project.
- Understanding and implementing multilayer architecture in Java.
- Integration of Java applications with Oracle Database via JDBC.
- Development of a complete information flow in a library management system.

Project developed by **angelariasus**