package guis;

import db_objs.MyJDBC;
import db_objs.User;
import dialog.AutoSaveDialog;
import utils.Constant;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LoginGuis extends BaseFrame{
    public LoginGuis() {
        super("Banking App Login");
    }
    @Override
    protected void addGuiComponents() {
        JLabel bankLabel = new JLabel("Banking App Login");
        bankLabel.setBounds(0, 20, super.getWidth(), 40);
        bankLabel.setFont(new Font("Dialog", Font.BOLD, 24));
        bankLabel.setBackground(new Color(0x772A77));
        bankLabel.setForeground(Color.WHITE);
        bankLabel.setOpaque(true);
        bankLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(bankLabel);

        JButton searchButton = new JButton(loadImage("src/assets/settings.png",true));
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(355, 75, 45, 45);
        searchButton.addActionListener(e -> {
            new AutoSaveDialog(this).setVisible(true);
        });
        add(searchButton);



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

        // login button
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(20, 300, getWidth()-40, 50);
        loginButton.setFont(new Font("Dialog", Font.BOLD, 14));
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = String.valueOf(passwordField.getPassword());
            User user = MyJDBC.validateLogin(username, password);
            if (user != null) {
                new BankingAppGuis(user).setVisible(true);
                System.out.println("Status:"+ Constant.isAutoSaveEnabled);

                dispose();
                // success dialog
                JOptionPane.showMessageDialog(this, "Login successful", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(loginButton);

        // register label under line text
        JLabel registerLabel = new JLabel("<html>Don't have an account? <u>Register</u></html>");
        registerLabel.setBounds(20, 420, getWidth()-30, 24);
        registerLabel.setFont(new Font("Dialog", Font.ITALIC, 14));
        // center the text
        registerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        //mouse cursor change to hand
        registerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new RegisterGuis().setVisible(true);
                dispose();
            }
        });
        add(registerLabel);


    }

    private ImageIcon loadImage(String path,boolean isSize) {
        try {
            BufferedImage image = ImageIO.read(new File(path));
            if (isSize){
                ImageIcon imageIcon = new ImageIcon(image);
                Image img = imageIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
            else {
                return new ImageIcon(image);
            }
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
}
