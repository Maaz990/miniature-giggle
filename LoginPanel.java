package HospitalManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;

public class LoginPanel {

    // Assume 'connection' is a valid database connection
    private static Connection connection;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Hospital Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel mainPanel = new JPanel(new CardLayout());
        JPanel loginPanel = createLoginPanel(mainPanel);
        JPanel menuPanel = new JPanel(); // Placeholder for menu panel
        mainPanel.add(loginPanel, "login");
        mainPanel.add(menuPanel, "menu");

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static JPanel createLoginPanel(JPanel mainPanel) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");
        JTextField userField = new JTextField(15);
        JPasswordField passField = new JPasswordField(15);
        JButton loginButton = new JButton("Login");
        JCheckBox showPassword = new JCheckBox("Show Password");
        JButton resetPasswordButton = new JButton("Reset Password");
        JButton createNewPasswordButton = new JButton("Create New Password");

        // Username Label and Field
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(userLabel, gbc);
        gbc.gridx = 1;
        panel.add(userField, gbc);

        // Password Label and Field
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passLabel, gbc);
        gbc.gridx = 1;
        panel.add(passField, gbc);

        // Show Password Checkbox
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(showPassword, gbc);

        // Login Button
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(loginButton, gbc);

        // Reset Password Button
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(resetPasswordButton, gbc);

        // Create New Password Button
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(createNewPasswordButton, gbc);

        // Show Password ActionListener
        showPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showPassword.isSelected()) {
                    passField.setEchoChar((char) 0);
                } else {
                    passField.setEchoChar('*');
                }
            }
        });

        // Login Button ActionListener
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = new String(passField.getPassword());

                User user = new User(connection);
                if (user.authenticate(username, password)) {
                    CardLayout cl = (CardLayout) (mainPanel.getLayout());
                    cl.show(mainPanel, "menu");
                } else {
                    JOptionPane.showMessageDialog(panel, "Invalid Credentials!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Reset Password Button ActionListener
        resetPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                if (!username.isEmpty()) {
                    // Add logic to reset password, e.g., send an email with a reset link
                    JOptionPane.showMessageDialog(panel, "Password reset instructions have been sent to your email.", "Reset Password", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(panel, "Please enter your username.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Create New Password Button ActionListener
        createNewPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newPassword = JOptionPane.showInputDialog(panel, "Enter your new password:");
                if (newPassword != null && !newPassword.isEmpty()) {
                    // Add logic to create a new password in the database
                    JOptionPane.showMessageDialog(panel, "Your password has been successfully created.", "New Password", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(panel, "Password creation cancelled.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return panel;
    }
}


