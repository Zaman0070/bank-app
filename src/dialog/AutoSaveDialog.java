package dialog;

import db_objs.TransactionQueueManager;
import utils.Constant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AutoSaveDialog extends JDialog {
    private final JCheckBox autoSaveCheckBox;

    public AutoSaveDialog(JFrame parent) {
        setTitle("Settings");
        setSize(300, 200);
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));

        autoSaveCheckBox = new JCheckBox("Enable Auto Save", Constant.isAutoSaveEnabled);
        autoSaveCheckBox.setBounds(0, 100, 60, 30);
        panel.add(autoSaveCheckBox);

        JButton saveButton = new JButton("Save Settings");
        saveButton.setBounds(0, 0, 60, 30);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Constant.isAutoSaveEnabled = autoSaveCheckBox.isSelected();
                TransactionQueueManager.setAutoSaveEnabled(Constant.isAutoSaveEnabled);
                System.out.println("Auto Save is " + (Constant.isAutoSaveEnabled ? "enabled" : "disabled"));
                dispose();
            }
        });
        panel.add(saveButton);
        add(panel);
    }

}
