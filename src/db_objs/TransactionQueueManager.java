package db_objs;
import utils.Constant;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class TransactionQueueManager {
    private static final Queue<Transaction> transactionQueue = new LinkedBlockingQueue<>();
    private static final Thread workerThread;

    static {
        workerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (Constant.isAutoSaveEnabled && !transactionQueue.isEmpty()) {
                            Transaction transaction = transactionQueue.poll();
                            if (transaction != null) {
                                processTransaction(transaction);
                            }
                        }
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        workerThread.start();
    }

    public static void setAutoSaveEnabled(boolean enabled) {
        Constant.isAutoSaveEnabled = enabled;
    }

    public static void addTransactionToQueue(Transaction transaction,User user) {
        transactionQueue.add(transaction);
        MyJDBC.updateCurrentBalance(user);

    }

    private static void processTransaction(Transaction transaction) {
        // Process the transaction and save it to the database
        if (MyJDBC.addTransactionToDb(transaction)) {
            System.out.println("Transaction processed and saved: " + transaction);
        } else {
            System.out.println("Failed to save transaction: " + transaction);
        }
    }
}
