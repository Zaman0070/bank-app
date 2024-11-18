package db_objs;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TransactionQueue {
    private static final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
    private static final Thread workerThread;

    static {
        workerThread = new Thread(() -> {
            while (true) {
                try {
                    Runnable task = queue.take();  // Block until a task is available
                    task.run();  // Process the task
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        workerThread.setDaemon(true);  // Allow this thread to exit when the application closes
        workerThread.start();
    }

    public static void addTask(Runnable task) {
        queue.offer(task);
        System.out.println("Task added to the queue.");
        System.out.println("Queue size: " + queue.size());
    }
}
