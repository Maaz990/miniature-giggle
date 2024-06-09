package HospitalManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Billing {
    private Connection connection;

    public Billing(Connection connection) {
        this.connection = connection;
    }

    public void generateBill(int appointmentId) {
        String query = "SELECT a.id, p.name AS patient_name, d.name AS doctor_name, a.appointment_date, a.fee " +
                "FROM appointments a " +
                "JOIN patients p ON a.patient_id = p.id " +
                "JOIN doctors d ON a.doctor_id = d.id " +
                "WHERE a.id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, appointmentId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String patientName = resultSet.getString("patient_name");
                String doctorName = resultSet.getString("doctor_name");
                String appointmentDate = resultSet.getString("appointment_date");
                double fee = resultSet.getDouble("fee");

                // Create and display the billing GUI
                JFrame frame = new JFrame("Billing Information");
                frame.setSize(400, 300);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JPanel panel = new JPanel();
                panel.setLayout(new GridLayout(5, 2));

                panel.add(new JLabel("Appointment ID:"));
                panel.add(new JLabel(String.valueOf(id)));
                panel.add(new JLabel("Patient Name:"));
                panel.add(new JLabel(patientName));
                panel.add(new JLabel("Doctor Name:"));
                panel.add(new JLabel(doctorName));
                panel.add(new JLabel("Appointment Date:"));
                panel.add(new JLabel(appointmentDate));
                panel.add(new JLabel("Fee:"));
                panel.add(new JLabel("$" + fee));

                frame.add(panel);
                frame.setVisible(true);
            } else {
                System.out.println("No billing information found for Appointment ID: " + appointmentId);
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("doesn't have a default value")) {
                System.out.println("Billing information not available for Appointment ID: " + appointmentId);
            } else {
                e.printStackTrace();
            }
        }
    }
}
