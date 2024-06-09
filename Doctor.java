package HospitalManagementSystem;

import java.sql.*;

import javax.swing.JOptionPane;

class Doctor {
    private Connection connection;

    public Doctor(Connection connection) {
        this.connection = connection;
    }

    public void addDoctor(String name, String specialization) {
        try {
            String query = "INSERT INTO doctors (name, specialization) VALUES (?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, specialization);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Doctor added successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewDoctors() {
        try {
            String query = "SELECT * FROM doctors";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            StringBuilder doctors = new StringBuilder();
            while (rs.next()) {
                doctors.append("ID: ").append(rs.getInt("id")).append(", Name: ").append(rs.getString("name"))
                        .append(", Specialization: ").append(rs.getString("specialization"))
                        .append("\n");
            }
            JOptionPane.showMessageDialog(null, doctors.toString(), "Doctors", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteDoctor(int id) {
        try {
            String query = "DELETE FROM doctors WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Doctor deleted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateDoctor(int id, String name, String specialization) {
        try {
            String query = "UPDATE doctors SET name = ?, specialization = ? WHERE id = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, specialization);
            ps.setInt(3, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Doctor updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
