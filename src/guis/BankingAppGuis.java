package guis;

import db_objs.User;
import dialog.Dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BankingAppGuis extends BaseFrame implements ActionListener {
    private JTextField currentBalanceField;
    public JTextField getCurrentBalanceField(){return currentBalanceField;}
    public BankingAppGuis(User user) {
        super("Banking App", user);
    }
    @Override
    protected void addGuiComponents() {

        String hello =
                "<html>" +
                        "<body style='text-align:center'>" +
                        "<b>Hello, " + user.getUsername() + "</b><br>" +
                        "What would you like to do today?" +
                        "</body>" +
                        "</html>";
        JLabel welcomeLabel = new JLabel(hello);
        welcomeLabel.setBounds(0, 20, super.getWidth(), 60);
        welcomeLabel.setFont(new Font("Dialog", Font.ITALIC, 16));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setBackground(new Color(0x772A77));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setOpaque(true);
        add(welcomeLabel);

        // current balance label
        JLabel balanceLabel = new JLabel("Current Balance");
        balanceLabel.setBounds(20, 100, getWidth()-30, 24);
        balanceLabel.setFont(new Font("Dialog", Font.ITALIC, 22));
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(balanceLabel);

        // current balance text field
        currentBalanceField = new JTextField("$"+user.getCurrentBalance());
        currentBalanceField.setBounds(20, 140, getWidth()-40, 40);
        currentBalanceField.setFont(new Font("Dialog", Font.PLAIN, 22));
        currentBalanceField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        currentBalanceField.setBorder(BorderFactory.createSoftBevelBorder(10));
        currentBalanceField.setHorizontalAlignment(SwingConstants.CENTER);
        currentBalanceField.setEditable(false);
        add(currentBalanceField);

        // deposit Button
        JButton depositButton = new JButton("Deposit");
        depositButton.setBounds(20, 200, getWidth()-40, 50);
        depositButton.setFont(new Font("Dialog", Font.BOLD, 14));
        depositButton.addActionListener(this);
        add(depositButton);

        // withdraw Button
        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.setBounds(20, 260, getWidth()-40, 50);
        withdrawButton.setFont(new Font("Dialog", Font.BOLD, 14));
        withdrawButton.addActionListener(this);
        add(withdrawButton);

        // past transactions Button
        JButton pastTransactionsButton = new JButton("Past Transactions");
        pastTransactionsButton.setBounds(20, 320, getWidth()-40, 50);
        pastTransactionsButton.setFont(new Font("Dialog", Font.BOLD, 14));
        pastTransactionsButton.addActionListener(this);
        add(pastTransactionsButton);

        // transfer Button
        JButton transferButton = new JButton("Transfer");
        transferButton.setBounds(20, 380, getWidth()-40, 50);
        transferButton.setFont(new Font("Dialog", Font.BOLD, 14));
        transferButton.addActionListener(this);
        add(transferButton);

        // logout Button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(20, 440, getWidth()-40, 50);
        logoutButton.setFont(new Font("Dialog", Font.BOLD, 14));
        logoutButton.addActionListener(this);
        add(logoutButton);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
       String action = e.getActionCommand();

         if (action.equalsIgnoreCase("Logout")) {
                dispose();
                new LoginGuis().setVisible(true);
         }
        dialog.Dialog dialog = new Dialog(this, user);
        dialog.setTitle(action);
        if (action.equalsIgnoreCase("Deposit") || action.equalsIgnoreCase("Withdraw")|| action.equalsIgnoreCase("Transfer")) {
            dialog.addCurrentBalanceDialog();
            dialog.actionButton(action);
            if(action.equalsIgnoreCase("Transfer")){
                dialog.addUserField();
            }
            dialog.setVisible(true);
        }else if(action.equalsIgnoreCase("Past Transactions")){
            dialog.pastTransactionComponents();
            dialog.setVisible(true);
        }

    }
}
