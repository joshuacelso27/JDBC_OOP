# Student Record Management System

A beginner-friendly JavaFX CRUD application using SceneBuilder FXML, JDBC, and PostgreSQL.

## Recommended Project Structure

```text
student-record-management-system/
├── pom.xml
├── README.md
└── src/
    └── main/
        ├── java/
        │   ├── MainApp.java
        │   ├── Controller.java
        │   ├── Student.java
        │   ├── YearLevel.java
        │   └── DBConnection.java
        └── resources/
            └── main.fxml
```

## PostgreSQL Database Setup

Open PostgreSQL, pgAdmin, or the PostgreSQL SQL Shell, then run:

```sql
CREATE DATABASE studentdb;
```

Connect to the `studentdb` database and create the table:

```sql
CREATE TABLE students (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    course VARCHAR(50),
    year_level INT
);
```

The `year_level` column uses `INT` to match the activity handout. The JavaFX app displays the values as `1st Year`, `2nd Year`, `3rd Year`, and `4th Year`.

## Database Connection

Open `src/main/java/DBConnection.java` and replace:

```java
private static final String PASSWORD = "your_password";
```

with your actual PostgreSQL password.

## PostgreSQL JDBC Driver Setup

### Option 1: Maven

This project already includes the PostgreSQL JDBC driver in `pom.xml`:

```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.4</version>
</dependency>
```

If you use Maven, IntelliJ or Eclipse can download the driver automatically.

### Option 2: Manual JAR

1. Download the PostgreSQL JDBC driver from `https://jdbc.postgresql.org/download/`.
2. In IntelliJ: `File > Project Structure > Modules > Dependencies > + > JARs or Directories`.
3. In Eclipse: right-click the project, then `Build Path > Configure Build Path > Libraries > Add External JARs`.
4. Select the downloaded PostgreSQL JDBC `.jar` file.

## How to Run in IntelliJ IDEA

1. Open the project folder.
2. Wait for Maven dependencies to load.
3. Make sure PostgreSQL is running.
4. Update the password in `DBConnection.java`.
5. Run this Maven command:

```bash
mvn javafx:run
```

You can also create a run configuration for `Launcher`. In IntelliJ, running `Launcher` is usually easier than running `MainApp` directly because JavaFX dependencies from Maven are placed on the classpath.

## How to Run in Eclipse

1. Import the project using `File > Import > Maven > Existing Maven Projects`.
2. Wait for Maven dependencies to load.
3. Make sure PostgreSQL is running.
4. Update the password in `DBConnection.java`.
5. Run:

```bash
mvn javafx:run
```

You can also run the `Launcher` class from Eclipse after Maven dependencies are loaded.

## IntelliJ JavaFX Runtime Error

If IntelliJ shows:

```text
Error: JavaFX runtime components are missing, and are required to run this application
```

use one of these fixes:

1. Run the project with Maven:

```bash
mvn javafx:run
```

2. Or run the `Launcher` class instead of `MainApp`.

3. Or add JavaFX VM options manually if you downloaded the JavaFX SDK:

```text
--module-path "C:\path\to\javafx-sdk\lib" --add-modules javafx.controls,javafx.fxml
```

## SceneBuilder Notes

Open `src/main/resources/main.fxml` in SceneBuilder. The required `fx:id` values are already set:

- `txtName`
- `txtCourse`
- `cbYear`
- `table`
- `colId`
- `colName`
- `colCourse`
- `colYear`

The button actions are also already connected:

- `#addStudent`
- `#updateStudent`
- `#deleteStudent`
- `#clearFields`

## Application Features

- Add student records
- Display all records in a `TableView`
- Update the selected student record
- Delete the selected student record
- Clear input fields
- Validate empty fields and missing selection
- Use `PreparedStatement` for SQL operations
- Use JavaFX properties for `TableView` binding
