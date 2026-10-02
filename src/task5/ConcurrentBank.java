package task5;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class ConcurrentBank {

    private final List<BankAccount> accounts = new ArrayList<>();
    private static final AtomicLong count = new AtomicLong(1);

    public BankAccount createAccount(int balance) {
        BankAccount account = new BankAccount(count.getAndIncrement(), balance);
        accounts.add(account);
        return account;
    }

    public void transfer(BankAccount from, BankAccount to, int amount) {
        BankAccount first, second;
        if (from.getId() < to.getId()) {
            first = from;
            second = to;
        } else {
            first = to;
            second = from;
        }

        synchronized (first) {
            synchronized (second) {
                from.withdraw(amount);
                to.deposit(amount);
            }
        }
    }

    public int getTotalBalance() {
        return accounts.stream().map(BankAccount::getBalance).reduce(0, Integer::sum);
    }
}
