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
        System.out.println("Счет " + id + " пополнен на " + amount + " текущий баланс: " + getBalance());
    }

    public synchronized void withdraw(int amount) {
        if (balance < amount) {
            throw new IllegalArgumentException("Недостаточно средств для вывода");
        }

        balance -= amount;
        System.out.println("Со счета " + id + " списано " + amount + " текущий баланс: " + getBalance());
    }

    public synchronized int getBalance() {
        return balance;
    }

    public long getId() {
        return id;
    }
}
