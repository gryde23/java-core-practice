package task5;

public class BankAccount {
    private final long id;
    private int balance;

    public BankAccount(long id, int balance) {
        this.id = id;
        this.balance = balance;
    }

    public synchronized void deposit(int amount) {
        balance += amount;
    }

    public synchronized void withdraw(int amount) {
        if (balance < amount) {
            throw new IllegalArgumentException("Недостаточно средств для вывода");
        }

        balance -= amount;
    }

    public synchronized int getBalance() {
        return balance;
    }
}
