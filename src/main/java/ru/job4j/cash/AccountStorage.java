package ru.job4j.cash;

import java.util.HashMap;
import java.util.Optional;

public final class AccountStorage {
    private final HashMap<Integer, Account> accounts = new HashMap<>();

    public synchronized boolean add(Account account) {
        return accounts.putIfAbsent(account.id(), account) == null;
    }

    public synchronized boolean update(Account account) {
        return accounts.replace(account.id(), account) != null;
    }

    public synchronized void delete(int id) {
        accounts.remove(id);
    }

    public synchronized Optional<Account> getById(int id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public synchronized boolean transfer(int fromId, int toId, int amount) {
        Account from = accounts.get(fromId);
        Account to = accounts.get(toId);
        if (from == null || to == null || from.amount() < amount) {
            return false;
        }
        accounts.replace(fromId, new Account(fromId, from.amount() - amount));
        accounts.replace(toId, new Account(toId, to.amount() + amount));
        return true;
    }
}