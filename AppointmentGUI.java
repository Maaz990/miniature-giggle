package HospitalManagementSystem;
//This is Change in my Code
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Scanner;

public class AppointmentGUI {
    private static Connection connection;

    public AppointmentGUI(Connection connection) {
        this.connection = connection;
        createGUI();
    }

    private void createGUI() {
        JFrame frame = new JFrame("Hospital Management System - Appointments");
        frame.setSize(1000, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JPanel panel1 = new JPanel(new GridLayout(10, 2, 10, 10));

        JButton viewAppointmentsButton = new JButton("View Appointments");
        JButton bookAppointmentButton = new JButton("Book Appointment");
        JButton updateAppointmentButton = new JButton("Update Appointment");
        JButton deleteAppointmentButton = new JButton("Delete Appointment");
        JButton addPatientButton = new JButton("Add Patient");

        viewAppointmentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAppointments();
            }
        });

        bookAppointmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a new Scanner object to read user input
                Scanner scanner = new Scanner(System.in);
                scanner.useDelimiter("\\n");

                // Call the bookAppointment method with the new Scanner object
                bookAppointment(0, 0, scanner);
            }
        });


        updateAppointmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateAppointment();
            }
        });

        deleteAppointmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteAppointment();
            }
        });

        addPatientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPatient();
            }
        });

        panel1.add(viewAppointmentsButton);
        panel1.add(bookAppointmentButton);
        panel1.add(updateAppointmentButton);
        panel1.add(deleteAppointmentButton);
        panel1.add(addPatientButton);

        frame.add(panel1);
        frame.setVisible(true);
    }

    void viewAppointments() {
        JFrame frame = new JFrame("View Appointments");
        frame.setSize(500, 400);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        String query = "SELECT * FROM appointments";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            StringBuilder sb = new StringBuilder();
            sb.append("AppointmentID | Patient ID | Doctor ID | Appointment Date | fee\n");
            sb.append("-----------------------------------------------------------------\n");
            while (resultSet.next()) {
                int appointmentId = resultSet.getInt("id");
                int patientId = resultSet.getInt("patient_id");
                int doctorId = resultSet.getInt("doctor_id");
                Date appointmentDate = resultSet.getDate("appointment_date");
                Double fee = resultSet.getDouble("fee");
                sb.append(String.format("%-13s | %-10s | %-9s | %-17s | %-10s\n", appointmentId, patientId, doctorId, appointmentDate, fee));
            }
            textArea.setText(sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane);

        frame.setVisible(true);
    }
    	
    	
    void bookAppointment(int patientId, int doctorId, Scanner scanner) {
        JFrame frame = new JFrame("Book Appointment");
        frame.setSize(500, 400);
        frame.setLayout(new GridLayout(6, 4));

        JLabel patientIdLabel = new JLabel("Patient ID:");
        JTextField patientIdField = new JTextField();
        JLabel doctorIdLabel = new JLabel("Doctor ID:");
        JTextField doctorIdField = new JTextField();
        JLabel dateLabel = new JLabel("Appointment Date (YYYY-MM-DD):");
        JTextField dateField = new JTextField();
        JLabel dateLabel1 = new JLabel("Fee:");
        JTextField feeField1 = new JTextField();

        JButton bookButton = new JButton("Book");
        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int patientId = Integer.parseInt(patientIdField.getText());
                    int doctorId = Integer.parseInt(doctorIdField.getText());
                    String appointmentDate = dateField.getText();
                    double fee = Double.parseDouble(feeField1.getText());

                    // Check if patientId and doctorId are valid integers
                    if (patientId <= 0 || doctorId <= 0) {
                        JOptionPane.showMessageDialog(null, "Please enter valid Patient ID and Doctor ID.");
                        return;
                    }

                    // Check if the doctor and patient exist
                    if (!checkExistence("patients", "id", patientId)) {
                        JOptionPane.showMessageDialog(null, "Patient with ID " + patientId + " does not exist!");
                        return;
                    }

                    if (!checkExistence("doctors", "id", doctorId)) {
                        JOptionPane.showMessageDialog(null, "Doctor with ID " + doctorId + " does not exist!");
                        return;
                    }

                    // Check if the doctor is available on the specified date
                    if (!checkDoctorAvailability(doctorId, appointmentDate)) {
                        JOptionPane.showMessageDialog(null, "Doctor is not available on " + appointmentDate + "!");
                        return;
                    }

                    // Book the appointment
                    String insertQuery = "INSERT INTO appointments(patient_id, doctor_id, appointment_date, fee) VALUES(?, ?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                    preparedStatement.setInt(1, patientId);
                    preparedStatement.setInt(2, doctorId);
                    preparedStatement.setDate(3, Date.valueOf(appointmentDate));
                    preparedStatement.setDouble(4, fee);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Appointment Booked Successfully!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to Book Appointment!");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter valid Patient ID and Doctor ID.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        frame.add(patientIdLabel);
        frame.add(patientIdField);
        frame.add(doctorIdLabel);
        frame.add(doctorIdField);
        frame.add(dateLabel);
        frame.add(dateField);
        frame.add(dateLabel1);
        frame.add(feeField1);
        frame.add(new JLabel());
        frame.add(bookButton);

        frame.setVisible(true);
    }


    private void updateAppointment() {
        JFrame frame = new JFrame("Update Appointment");
        frame.setSize(300, 200);
        frame.setLayout(new GridLayout(3, 2));

        JLabel appointmentIdLabel = new JLabel("Appointment ID:");
        JTextField appointmentIdField = new JTextField();
        JLabel dateLabel = new JLabel("New Appointment Date (YYYY-MM-DD):");
        JTextField dateField = new JTextField();

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int appointmentId = Integer.parseInt(appointmentIdField.getText());
                String newAppointmentDate = dateField.getText();

                try {
                    // Check if the appointment exists
                    if (!checkExistence("appointments", "id", appointmentId)) {
                        JOptionPane.showMessageDialog(null, "Appointment with ID " + appointmentId + " does not exist!");
                        return;
                    }

                    // Update the appointment date
                    String updateQuery = "UPDATE appointments SET appointment_date = ? WHERE id = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
                    preparedStatement.setDate(1, Date.valueOf(newAppointmentDate));
                    preparedStatement.setInt(2, appointmentId);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Appointment Updated Successfully!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to Update Appointment!");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        frame.add(appointmentIdLabel);
        frame.add(appointmentIdField);
        frame.add(dateLabel);
        frame.add(dateField);
        frame.add(new JLabel());
        frame.add(updateButton);

        frame.setVisible(true);
    }

    private void deleteAppointment() {
        JFrame frame = new JFrame("Delete Appointment");
        frame.setSize(300, 150);
        frame.setLayout(new GridLayout(2, 2));

        JLabel appointmentIdLabel = new JLabel("Appointment ID:");
        JTextField appointmentIdField = new JTextField();

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int appointmentId = Integer.parseInt(appointmentIdField.getText());

                try {
                    // Check if the appointment exists
                    if (!checkExistence("appointments", "id", appointmentId)) {
                        JOptionPane.showMessageDialog(null, "Appointment with ID " + appointmentId + " does not exist!");
                        return;
                    }

                    // Delete the appointment
                    String deleteQuery = "DELETE FROM appointments WHERE id = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
                    preparedStatement.setInt(1, appointmentId);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Appointment Deleted Successfully!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to Delete Appointment!");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        frame.add(appointmentIdLabel);
        frame.add(appointmentIdField);
        frame.add(new JLabel());
        frame.add(deleteButton);

        frame.setVisible(true);
    }

    private void addPatient() {
        JFrame frame = new JFrame("Add Patient");
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(5, 2));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JLabel ageLabel = new JLabel("Age:");
        JTextField ageField = new JTextField();
        JLabel genderLabel = new JLabel("Gender:");
        JTextField genderField = new JTextField();
        JLabel contactLabel = new JLabel("Contact Number:");
        JTextField contactField = new JTextField();

        JButton addButton = new JButton("Add Patient");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                int age = Integer.parseInt(ageField.getText());
                String gender = genderField.getText();
                String contactNumber = contactField.getText();

                try {
                    String query = "INSERT INTO patients (name, age, gender, contact_number) VALUES (?, ?, ?, ?)";
                    PreparedStatement ps = connection.prepareStatement(query);
                    ps.setString(1, name);
                    ps.setInt(2, age);
                    ps.setString(3, gender);
                    ps.setString(4, contactNumber);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Patient added successfully.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        frame.add(nameLabel);
        frame.add(nameField);
        frame.add(ageLabel);
        frame.add(ageField);
        frame.add(genderLabel);
        frame.add(genderField);
        frame.add(contactLabel);
        frame.add(contactField);
        frame.add(new JLabel());
        frame.add(addButton);

        frame.setVisible(true);
    }

    private boolean checkExistence(String tableName, String columnName, int id) throws SQLException {
        String query = "SELECT * FROM " + tableName + " WHERE " + columnName + " = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

    private boolean checkDoctorAvailability(int doctorId, String appointmentDate) throws SQLException {
        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, doctorId);
        preparedStatement.setDate(2, Date.valueOf(appointmentDate));
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        return count == 0;
    }

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_database", "username", "password");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        new AppointmentGUI(connection);
    }
}
