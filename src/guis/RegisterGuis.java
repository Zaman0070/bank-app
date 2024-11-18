package guis;

import db_objs.MyJDBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterGuis extends BaseFrame{
    public RegisterGuis() {
        super("Banking App Register");
    }
    @Override
    protected void addGuiComponents() {
        JLabel bankLabel = new JLabel("Banking App Register");
        bankLabel.setBounds(0, 20, super.getWidth(), 40);
        bankLabel.setFont(new Font("Dialog", Font.BOLD, 24));
        bankLabel.setBackground(new Color(0x772A77));
        bankLabel.setForeground(Color.WHITE);
        bankLabel.setOpaque(true);
        bankLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(bankLabel);

        // username label
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(20, 120, getWidth()-30, 24);
        usernameLabel.setFont(new Font("Dialog", Font.ITALIC, 14));
        add(usernameLabel);

        // username textfield
        JTextField usernameField = new JTextField();
        usernameField.setBounds(20, 150, getWidth()-40, 40);
        usernameField.setFont(new Font("Dialog", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        usernameField.setBorder(BorderFactory.createSoftBevelBorder(10));
        add(usernameField);

        // password label
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(20, 200, getWidth()-30, 24);
        passwordLabel.setFont(new Font("Dialog", Font.ITALIC, 14));
        add(passwordLabel);

        // password textfield
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(20, 230, getWidth()-40, 40);
        passwordField.setFont(new Font("Dialog", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        passwordField.setBorder(BorderFactory.createSoftBevelBorder(10));
        add(passwordField);

        // confirm password label
        JLabel confirmPasswordLabel = new JLabel("Confirm Password");
        confirmPasswordLabel.setBounds(20, 280, getWidth()-30, 24);
        confirmPasswordLabel.setFont(new Font("Dialog", Font.ITALIC, 14));
        add(confirmPasswordLabel);

        // confirm password textfield
        JPasswordField confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(20, 310, getWidth()-40, 40);
        confirmPasswordField.setFont(new Font("Dialog", Font.PLAIN, 14));
        confirmPasswordField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        confirmPasswordField.setBorder(BorderFactory.createSoftBevelBorder(10));
        add(confirmPasswordField);

        // login button
        JButton registerButton = new JButton("Register");
        registerButton.setBounds(20, 380, getWidth()-40, 50);
        registerButton.setFont(new Font("Dialog", Font.BOLD, 14));
        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = String.valueOf(passwordField.getPassword());
            String confirmPassword = String.valueOf(confirmPasswordField.getPassword());
            if (validateUserInput(username, password, confirmPassword)) {
                if (MyJDBC.registerUser(username, password)) {
                    JOptionPane.showMessageDialog(this, "User registered successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    new LoginGuis().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "User already exist", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(registerButton);

        // register label under line text
        JLabel registerLabel = new JLabel("<html>Already have an account? <u>Login</u></html>");
        registerLabel.setBounds(20, 500, getWidth()-30, 24);
        registerLabel.setFont(new Font("Dialog", Font.ITALIC, 14));
        registerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        registerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LoginGuis().setVisible(true);
                dispose();
            }
        });
        add(registerLabel);

    }
    private boolean validateUserInput(String username, String password, String confirmPassword) {
       if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
           JOptionPane.showMessageDialog(this, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
           return false;
       }
       if (username.length()<4) {
           JOptionPane.showMessageDialog(this, "Username must be at least 6 characters", "Error", JOptionPane.ERROR_MESSAGE);
           return false;
       }
       if (password.length()<6) {
           JOptionPane.showMessageDialog(this, "Password must be at least 6 characters", "Error", JOptionPane.ERROR_MESSAGE);
           return false;
       }
         if (!password.equals(confirmPassword)) {
              JOptionPane.showMessageDialog(this, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
              return false;
         }
            return true;
    }
}
