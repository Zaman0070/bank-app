package dialog;
import db_objs.MyJDBC;
import db_objs.Transaction;
import db_objs.TransactionQueueManager;
import db_objs.User;
import guis.BankingAppGuis;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.IntStream;

public class Dialog extends JDialog implements ActionListener {
    private User user;
    private BankingAppGuis bankingAppGuis;
    private JLabel balanceLabel, enterAmountLabel, enterUserLabel;
    private JTextField enterAmountField, enterUserField;
    private JButton button;
    private JPanel pastTransactionsPanel;
    private ArrayList<Transaction> transactions;

    public Dialog(BankingAppGuis bankingAppGuis, User user) {
        setSize(400, 400);
        setModal(true);
        setLocationRelativeTo(bankingAppGuis);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(null);
        this.user = user;
        this.bankingAppGuis = bankingAppGuis;
    }

    public void addCurrentBalanceDialog() {
        balanceLabel = new JLabel("Current Balance: " + user.getCurrentBalance());
        balanceLabel.setBounds(0, 10, getWidth() - 20, 24);
        balanceLabel.setFont(new Font("Dialog", Font.ITALIC, 22));
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(balanceLabel);

        enterAmountLabel = new JLabel("Enter Amount");
        enterAmountLabel.setBounds(20, 50, getWidth() - 30, 24);
        enterAmountLabel.setFont(new Font("Dialog", Font.ITALIC, 14));
        enterAmountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterAmountLabel);

        enterAmountField = new JTextField();
        enterAmountField.setBounds(20, 80, getWidth() - 40, 40);
        enterAmountField.setFont(new Font("Dialog", Font.PLAIN, 14));
        enterAmountField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        enterAmountField.setBorder(BorderFactory.createSoftBevelBorder(10));
        enterAmountField.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterAmountField);
    }

    public void actionButton(String action) {
        button = new JButton(action);
        button.setBounds(20, 250, getWidth() - 40, 50);
        button.setFont(new Font("Dialog", Font.BOLD, 14));
        button.addActionListener(this);
        add(button);
    }

    public void addUserField() {
        enterUserLabel = new JLabel("Enter Username");
        enterUserLabel.setBounds(20, 130, getWidth() - 30, 24);
        enterUserLabel.setFont(new Font("Dialog", Font.ITALIC, 14));
        enterUserLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterUserLabel);

        enterUserField = new JTextField();
        enterUserField.setBounds(20, 160, getWidth() - 40, 40);
        enterUserField.setFont(new Font("Dialog", Font.PLAIN, 14));
        enterUserField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        enterUserField.setBorder(BorderFactory.createSoftBevelBorder(10));
        enterUserField.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterUserField);
    }

    public void handleTransaction(String transactionType, float amount) {
        Transaction transaction;
        if (transactionType.equalsIgnoreCase("Deposit")) {
            user.setCurrentBalance(user.getCurrentBalance().add(new BigDecimal(amount)));
            transaction = new Transaction(user.getId(), transactionType, new BigDecimal(amount), new Date());
        } else {
            user.setCurrentBalance(user.getCurrentBalance().subtract(new BigDecimal(amount)));
            transaction = new Transaction(user.getId(), transactionType, BigDecimal.valueOf(-amount), new Date());
        }
        TransactionQueueManager.addTransactionToQueue(transaction, user);
        JOptionPane.showMessageDialog(this, transactionType + " successful", "Success", JOptionPane.INFORMATION_MESSAGE);
        resetFieldsAndUpdateCurrentBalance();
    }


    private void resetFieldsAndUpdateCurrentBalance() {
        enterAmountField.setText("");
        if (enterUserField != null) {
            enterUserField.setText("");
        }
        balanceLabel.setText("Current Balance: " + user.getCurrentBalance());
        bankingAppGuis.getCurrentBalanceField().setText("$" + user.getCurrentBalance());
    }

    private void handleTransfer(User user, String transferUser, float amount) {
        boolean success = MyJDBC.transfer(user, transferUser, amount);
        if (success) {
            Transaction transaction = new Transaction(user.getId(), "Transfer", BigDecimal.valueOf(-amount), new Date());
            TransactionQueueManager.addTransactionToQueue(transaction, user);
            JOptionPane.showMessageDialog(this, "Transfer successful", "Success", JOptionPane.INFORMATION_MESSAGE);
            resetFieldsAndUpdateCurrentBalance();
        } else {
            JOptionPane.showMessageDialog(this, "Transfer failed", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

        public void pastTransactionComponents(){
        pastTransactionsPanel = new JPanel();
        pastTransactionsPanel.setLayout(new BoxLayout(pastTransactionsPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(pastTransactionsPanel);
        scrollPane.setBounds(0, 20, getWidth()-15, getHeight()-15);
        transactions = MyJDBC.getPastTransactions(user);
        IntStream.range(0, transactions.size()).forEach(i -> {
            Transaction transaction = transactions.get(i);
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            JLabel label = new JLabel(transaction.transactionType());
            label.setFont(new Font("Dialog", Font.BOLD, 14));
            label.setHorizontalAlignment(SwingConstants.CENTER);


            JLabel amountLabel = new JLabel(transaction.transactionAmount().toString());
            amountLabel.setFont(new Font("Dialog", Font.BOLD, 14));

            JLabel dateLabel = new JLabel(transaction.transactionDate().toString());
            dateLabel.setFont(new Font("Dialog", Font.ITALIC, 11));

            panel.add(label, BorderLayout.WEST);
            panel.add(amountLabel, BorderLayout.EAST);
            panel.add(dateLabel, BorderLayout.SOUTH);

            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            pastTransactionsPanel.add(panel);
        });
        add(scrollPane);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        float amount = Float.parseFloat(enterAmountField.getText());
        if (action.equalsIgnoreCase("Deposit")) {
            handleTransaction(action, amount);
        } else {
            int result = user.getCurrentBalance().compareTo(BigDecimal.valueOf(amount));
            if (result < 0) {
                JOptionPane.showMessageDialog(this, "Insufficient funds", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (action.equalsIgnoreCase("Withdraw")) {
                handleTransaction(action, amount);
            } else {
                String transferTo = enterUserField.getText();
                if (MyJDBC.checkUserExists(transferTo)) {
                    handleTransfer(user, transferTo, amount);
                } else {
                    JOptionPane.showMessageDialog(this, "User does not exist", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
