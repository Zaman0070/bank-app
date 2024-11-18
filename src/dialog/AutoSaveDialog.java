package dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;

public class AutoSaveDialog {

    // To store user's auto-save preference
    private static final String AUTO_SAVE_KEY = "auto_save_enabled";

    public static void showAutoSaveDialog() {
        Preferences pref = Preferences.userNodeForPackage(AutoSaveDialog.class);
        boolean isAutoSaveEnabled = pref.getBoolean(AUTO_SAVE_KEY, false);
        if (isAutoSaveEnabled) {
            return;
        }
        int option = JOptionPane.showConfirmDialog(
                null,
                "Would you like to enable auto-save?",
                "Enable Auto-Save",
                JOptionPane.YES_NO_OPTION
        );
        pref.putBoolean(AUTO_SAVE_KEY, option == JOptionPane.YES_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            startAutoSave();
        }
    }

    private static void startAutoSave() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(100000);
                    System.out.println("Auto-save triggered...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
