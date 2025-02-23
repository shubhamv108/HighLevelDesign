package SystemDesign.Architecture.Microservices.DDD.TacticalDesign;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionScript {

    public class Account {
        private final Integer id;
        private Double balance;

        public Account(int id, Double balance) {
            this.id = id;
            this.balance = balance;
        }

        public int getId() {
            return id;
        }

        public Double getBalance() {
            return balance;
        }
    }

    public class Transaction {
        private final String code;
        private final Integer accountId;
        private final Integer amount;

        public Transaction(String code, Integer accountId, Integer amount) {
            this.code = code;
            this.accountId = accountId;
            this.amount = amount;
        }

        public String getCode() {
            return code;
        }

        public int getAccountId() {
            return accountId;
        }

        public Integer getAmount() {
            return amount;
        }
    }

    public class BankTransactionExecutor {

        private final TransactionService transactionService;
        private final AccountService accountService;

        public BankTransactionExecutor(TransactionService transactionService, AccountService accountService) {
            this.transactionService = transactionService;
            this.accountService = accountService;
        }

        public void executeTransaction(Transaction transaction, Connection conn) throws Exception {
            conn.setAutoCommit(false);
            try {
                Account account = accountService.getAccountById(transaction.getAccountId(), conn);
                if ("W".equals(transaction.getCode()) ) {
                    if (account.getBalance() >= transaction.getAmount() && transaction.getAmount() > 0) {
                        transactionService.updateBalance(conn, account, account.getBalance() - transaction.getAmount());
                    } else {
                        throw new Exception("Insufficient balance or invalid withdrawal amount.");
                    }
                } else if ("D".equals(transaction.getCode())) {
                    if (transaction.getAmount() > 0) {
                        throw new Exception("Invalid deposit amount.");
                    }
                    transactionService.updateBalance(conn, account, account.getBalance() + transaction.getAmount());
                }
                transactionService.createTransaction(conn, transaction.getAccountId(), transaction.getAmount(), transaction.getCode());
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public class AccountService {
        public Account getAccountById(int accountId, Connection conn) throws SQLException {
            String sql = "SELECT * FROM account WHERE id = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, accountId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return new Account(resultSet.getInt("id"), resultSet.getDouble("balance"));
                    }
                }
            }
            throw new SQLException("Account not found");
        }
    }

    public class TransactionService {
        public void updateBalance(Connection conn, Account account, double newBalance) throws SQLException {
            String sql = "UPDATE account SET balance = ? WHERE id = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setDouble(1, newBalance);
                statement.setInt(2, account.getId());
                statement.executeUpdate();
            }
        }

        public void createTransaction(Connection conn, int accountId, double amount, String code) throws SQLException {
            String sql = "INSERT INTO transaction (account_id, amount, code) VALUES (?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setInt(1, accountId);
                statement.setDouble(2, amount);
                statement.setString(3, code);
                statement.executeUpdate();
            }
        }
    }

}
