import db_objs.DatabaseInitializer;
import db_objs.MyJDBC;
import db_objs.User;
import dialog.AutoSaveDialog;
import guis.BankingAppGuis;
import guis.LoginGuis;
import guis.RegisterGuis;

import javax.swing.*;
import java.math.BigDecimal;

public class AppLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                DatabaseInitializer.initializeDatabase();
                new LoginGuis().setVisible(true);
//                AutoSaveDialog.showAutoSaveDialog();

            }
        });
    }
}
