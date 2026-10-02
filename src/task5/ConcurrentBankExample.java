package task5;

public class ConcurrentBankExample {
    public static void main(String[] args) {
        ConcurrentBank bank = new ConcurrentBank();

        // Создание счетов
        BankAccount account1 = bank.createAccount(1000);
        BankAccount account2 = bank.createAccount(500);
        BankAccount account3 = bank.createAccount(1000);

        // Перевод между счетами
        Thread transferThread1 = new Thread(() -> bank.transfer(account1, account2, 200));
        Thread transferThread2 = new Thread(() -> bank.transfer(account2, account1, 100));
        Thread depositThread = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                account3.deposit(100);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Thread withdrawThread = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                account3.withdraw(100);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        transferThread1.start();
        transferThread2.start();
        depositThread.start();
        withdrawThread.start();

        try {
            transferThread1.join();
            transferThread2.join();
            depositThread.join();
            withdrawThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Вывод общего баланса
        System.out.println("Total balance: " + bank.getTotalBalance());
    }
}
