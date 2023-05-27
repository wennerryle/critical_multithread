import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Main {
    private static final String[] COLUMN_NAMES = {"Текущий баланс", "Шаг в последовательности", "Время выполнения, сек."};

    private JFrame frame;
    private JTextArea textArea;
    private JButton btnWithCritSection;
    private JButton btnWithoutCritSection;
    private JTable table;
    private DefaultTableModel tableModel;

    public Main() {
        frame = new JFrame("Пример многопоточности");
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setMinimumSize(new Dimension(800, 500));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        textArea = new JTextArea("Эта программа отображает работу с многопоточностью,\nа " +
                "именно один из её аспектов в языках программирования -\nкритическая секция.\n\n" +
                "Критическая секция необходима для синхронизации последовательной работы различных " +
                "потоков, с целью сохранения порядка их выполнения.\n\n" +
                "Симуляция различного времени работы потока достигается методом Thread.sleep, " +
                "который блокирует работу потока на неопределенное время, от 0 - 5 секунд при " +
                "помощи метода Math.random()\n\nПример кода - банковское приложение. Для успешной транзакции," +
                "порядок действий должен быть одинаковым. Нельзя снять деньги с баланса, если на счету не" +
                "достаточно такой суммы\n\nПервоначальная сумма = 0;\n\nПервый шаг добавляет 1000 к сумме\n\n" +
                "Второй шаг отнимает 500 у суммы");

        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        panel.add(textArea, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnWithCritSection = new JButton("Выполнить с критической секцией");
        btnWithoutCritSection = new JButton("Выполнить без критической секции");
        buttonPanel.add(btnWithCritSection);
        buttonPanel.add(btnWithoutCritSection);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        tableModel = new DefaultTableModel(COLUMN_NAMES, 0);

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.EAST);

        btnWithCritSection.addActionListener(e -> {
            BankAccount bankAccount = new BankAccount(0, tableModel);

            // Код с критической секцией
            synchronized (bankAccount) {
                // Пополняем счёт на 1000 рублей
                SharedThreads.getExecutorService().submit(() -> {
                    try {
                        bankAccount.deposit(1000);
                    } catch (InterruptedException ignored) {}
                });

                // Снятие со счета на 500 рублей
                SharedThreads.getExecutorService().submit(() -> {
                    try {
                        bankAccount.withdraw(500);
                    } catch (InterruptedException ignored) {}
                });
            }
        });

        btnWithoutCritSection.addActionListener(e -> {
            BankAccount bankAccount = new BankAccount(0, tableModel);
            // Пополняем счёт на 1000 рублей
            SharedThreads.getExecutorService().submit(() -> {
                try {
                    bankAccount.depositWithoutCriticalSection(1000);
                } catch (InterruptedException ignored) {}
            });

            // Снятие со счета на 500 рублей
            SharedThreads.getExecutorService().submit(() -> {
                try {
                    bankAccount.withdrawWithoutCriticalSection(500);
                } catch (InterruptedException ignored) {}
            });
        });

        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}