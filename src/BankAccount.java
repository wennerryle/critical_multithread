import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.concurrent.ThreadLocalRandom;

public class BankAccount {
    private int balance;
    DefaultTableModel tableModel;

    public BankAccount(int balance, DefaultTableModel tableModel) {
        this.balance = balance;
        this.tableModel = tableModel;

        createTableRow(String.valueOf(balance), "-", "-");
    }

    private void createTableRow(String currentResult, String step, String executionTime) {
        Object[] row = new Object[3];

        row[0] = currentResult;
        row[1] = step;
        row[2] = executionTime;

        tableModel.addRow(row);
    }

    public synchronized void deposit(int amount) throws InterruptedException {
        int executionTimeInMS = ThreadLocalRandom.current().nextInt(5) * 1000;
        Thread.sleep(executionTimeInMS);
        balance += amount;
        createTableRow(String.valueOf(balance), String.valueOf(1), String.valueOf(executionTimeInMS / 1000));
    }

    public synchronized void withdraw(int amount) throws InterruptedException {
        int executionTimeInMS = ThreadLocalRandom.current().nextInt(5) * 1000;
        Thread.sleep(executionTimeInMS);
        balance -= amount;
        createTableRow(String.valueOf(balance), String.valueOf(2), String.valueOf(executionTimeInMS / 1000));
    }

    public void depositWithoutCriticalSection(int amount) throws InterruptedException {
        int executionTimeInMS = ThreadLocalRandom.current().nextInt(5) * 1000;
        Thread.sleep(executionTimeInMS);
        balance += amount;
        createTableRow(String.valueOf(balance), String.valueOf(1), String.valueOf(executionTimeInMS / 1000));
    }

    public void withdrawWithoutCriticalSection(int amount) throws InterruptedException {
        int executionTimeInMS = ThreadLocalRandom.current().nextInt(5) * 1000;
        Thread.sleep(executionTimeInMS);
        balance -= amount;
        createTableRow(String.valueOf(balance), String.valueOf(2), String.valueOf(executionTimeInMS / 1000));
    }
}
