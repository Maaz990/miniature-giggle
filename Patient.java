package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class Patient {
    private Connection connection;

    public Patient(Connection connection) {
        this.connection = connection;
    }

    public void addPatient(String name, int age, String gender, String contactNumber) {
        try {
            String query = "INSERT INTO patients (name, age, gender, contact_number) VALUES (?,?,?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, gender);
            ps.setString(4, contactNumber);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Patient added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    public void viewPatients() {
        try {
            String query = "SELECT * FROM patients";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            StringBuilder patients = new StringBuilder();
            while (rs.next()) {
                patients.append("ID: ").append(rs.getInt("id")).append(", Name: ").append(rs.getString("name"))
                        .append(", Age: ").append(rs.getInt("age")).append(", Gender: ").append(rs.getString("gender"))
                        .append("\n");
            }
            JOptionPane.showMessageDialog(null, patients.toString(), "Patients", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePatient(int id) {
        try {
            String query = "DELETE FROM patients WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Patient deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePatient(int id, String name, int age, String gender, String contactNumber) {
        try {
            String query = "UPDATE patients SET name = ?, age = ?, gender = , contact_number = ? WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.setString(3, gender);
            ps.setString(4, contactNumber);
            ps.setInt(5, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Patient updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
