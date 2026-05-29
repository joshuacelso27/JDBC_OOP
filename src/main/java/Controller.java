import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Controller {

    @FXML private TextField txtName;
    @FXML private TextField txtCourse;
    @FXML private ChoiceBox<YearLevel> cbYear;

    @FXML private TableView<Student> table;
    @FXML private TableColumn<Student, Integer> colId;
    @FXML private TableColumn<Student, String> colName;
    @FXML private TableColumn<Student, String> colCourse;
    @FXML private TableColumn<Student, String> colYear;

    private final ObservableList<Student> studentList = FXCollections.observableArrayList();
    private int selectedId = -1;

    @FXML
    public void initialize() {
        // Load enum values into the ChoiceBox for a fixed set of year levels.
        cbYear.getItems().setAll(YearLevel.values());

        // Bind Student properties to the TableView columns.
        colId.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        colName.setCellValueFactory(data -> data.getValue().nameProperty());
        colCourse.setCellValueFactory(data -> data.getValue().courseProperty());
        colYear.setCellValueFactory(data -> data.getValue().yearLevelProperty());

        table.setItems(studentList);

        // Populate the form whenever the user selects a table row.
        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selectedStudent) -> {
            if (selectedStudent != null) {
                selectedId = selectedStudent.getId();
                txtName.setText(selectedStudent.getName());
                txtCourse.setText(selectedStudent.getCourse());
                cbYear.setValue(YearLevel.fromDisplayText(selectedStudent.getYearLevel()));
            }
        });

        loadData();
    }

    public void loadData() {
        studentList.clear();

        // PreparedStatement is used for consistency and safe SQL execution.
        String query = "SELECT id, name, course, year_level FROM students ORDER BY id";

        try (Connection conn = DBConnection.connect();
             PreparedStatement pst = conn.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                YearLevel yearLevel = YearLevel.fromValue(rs.getInt("year_level"));
                studentList.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("course"),
                        yearLevel == null ? "" : yearLevel.toString()
                ));
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to load student records.\n" + e.getMessage());
        }
    }

    @FXML
    private void addStudent() {
        if (!validateInputs()) {
            return;
        }

        String query = "INSERT INTO students(name, course, year_level) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.connect();
             PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setString(1, cleanInput(txtName.getText()));
            pst.setString(2, cleanInput(txtCourse.getText()));
            pst.setInt(3, cbYear.getValue().getValue());
            pst.executeUpdate();

            loadData();
            clearFields();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Student record added successfully.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to add student record.\n" + e.getMessage());
        }
    }

    @FXML
    private void updateStudent() {
        if (selectedId == -1) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please select a student record first.");
            return;
        }

        if (!validateInputs()) {
            return;
        }

        String query = "UPDATE students SET name = ?, course = ?, year_level = ? WHERE id = ?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setString(1, cleanInput(txtName.getText()));
            pst.setString(2, cleanInput(txtCourse.getText()));
            pst.setInt(3, cbYear.getValue().getValue());
            pst.setInt(4, selectedId);
            pst.executeUpdate();

            loadData();
            clearFields();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Student record updated successfully.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to update student record.\n" + e.getMessage());
        }
    }

    @FXML
    private void deleteStudent() {
        if (selectedId == -1) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please select a student record first.");
            return;
        }

        String query = "DELETE FROM students WHERE id = ?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setInt(1, selectedId);
            pst.executeUpdate();

            loadData();
            clearFields();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Student record deleted successfully.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to delete student record.\n" + e.getMessage());
        }
    }

    @FXML
    private void clearFields() {
        txtName.clear();
        txtCourse.clear();
        cbYear.setValue(null);
        selectedId = -1;
        table.getSelectionModel().clearSelection();
    }

    private boolean validateInputs() {
        if (cleanInput(txtName.getText()).isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Name field is required.");
            txtName.requestFocus();
            return false;
        }

        if (cleanInput(txtCourse.getText()).isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Course field is required.");
            txtCourse.requestFocus();
            return false;
        }

        if (cbYear.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please select a year level.");
            cbYear.requestFocus();
            return false;
        }

        return true;
    }

    private String cleanInput(String value) {
        // Trim leading/trailing spaces and collapse repeated spaces inside the text.
        return value == null ? "" : value.trim().replaceAll("\\s+", " ");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
