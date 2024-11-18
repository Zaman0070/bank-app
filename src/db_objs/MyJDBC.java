package db_objs;

import utils.Queries;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;


public class MyJDBC {
    public static User validateLogin(String username, String password) {
        try {
            Connection connection =DatabaseInitializer.dbConnection();
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(Queries.LOGIN_QUERY);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new User(resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("password"),
                        resultSet.getBigDecimal("current_balance"));
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean registerUser(String username, String password) {
        if (checkUserExists(username)) {
            return false;
        }
        try {
            Connection connection = DatabaseInitializer.dbConnection();
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(Queries.REGISTER_QUERY);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setBigDecimal(3, new BigDecimal("0.00"));
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean checkUserExists(String username) {
        try {
            Connection connection = DatabaseInitializer.dbConnection();
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(Queries.CHECK_USER_EXISTS);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean addTransactionToDb(Transaction transaction){
        try {
            Connection connection = DatabaseInitializer.dbConnection();
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(Queries.INSERT_TRANSACTION);
            preparedStatement.setInt(1, transaction.userId());
            preparedStatement.setString(2, transaction.transactionType());
            preparedStatement.setBigDecimal(3, transaction.transactionAmount());
            preparedStatement.setDate(4, new Date(transaction.transactionDate().getTime()));
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateCurrentBalance(User user){
        try {
            Connection connection = DatabaseInitializer.dbConnection();
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(Queries.UPDATE_CURRENT_BALANCE);
            preparedStatement.setBigDecimal(1, user.getCurrentBalance());
            preparedStatement.setInt(2, user.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean transfer(User user, String transferUser, float amount) {
        Connection connection = null;
        try {
            connection = DatabaseInitializer.dbConnection();
            assert connection != null;
            // Disable auto-commit to start a transaction
            connection.setAutoCommit(false);

            // Fetch the user to transfer to
            PreparedStatement preparedStatement = connection.prepareStatement(Queries.USER_BY_NAME);
            preparedStatement.setString(1, transferUser);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                User transferToUser = new User(
                        resultSet.getInt("id"),
                        transferUser,
                        resultSet.getString("password"),
                        resultSet.getBigDecimal("current_balance")
                );

                // Create transaction objects
                Transaction transaction = new Transaction(
                        user.getId(),
                        "Transfer",
                        BigDecimal.valueOf(-amount),
                        new java.util.Date()
                );

                Transaction receivedTransaction = new Transaction(
                        transferToUser.getId(),
                        "Transfer",
                        BigDecimal.valueOf(amount),
                        new java.util.Date()
                );

                // Update balances
                transferToUser.setCurrentBalance(
                        transferToUser.getCurrentBalance().add(BigDecimal.valueOf(amount))
                );
                user.setCurrentBalance(
                        user.getCurrentBalance().subtract(BigDecimal.valueOf(amount))
                );

                // Update database balances
                if (!updateCurrentBalance( transferToUser) ||
                        !updateCurrentBalance( user)) {
                    connection.rollback(); // Rollback in case of failure
                    return false;
                }

                if (!addTransactionToDb(transaction) ||
                        !addTransactionToDb(receivedTransaction)) {
                    connection.rollback();
                    return false;
                }

                // Commit the transaction
                connection.commit();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (connection != null) {
                    connection.rollback(); // Rollback in case of exception
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true); // Re-enable auto-commit
                    connection.close();
                }
            } catch (SQLException closeEx) {
                closeEx.printStackTrace();
            }
        }
        return false;
    }


    public static ArrayList<Transaction> getPastTransactions(User user) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        try {
            Connection connection = DatabaseInitializer.dbConnection();
            assert connection != null;
            PreparedStatement preparedStatement = connection.prepareStatement(Queries.TRANSACTION_BY_USER_ID);
            preparedStatement.setInt(1, user.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                transactions.add(new Transaction(resultSet.getInt("user_id"),
                        resultSet.getString("transaction_type"),
                        resultSet.getBigDecimal("transaction_amount"),
                        resultSet.getDate("transaction_date")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }
}
