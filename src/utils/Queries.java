package utils;

public class Queries {
    public static final String LOGIN_QUERY = "SELECT * FROM users WHERE name = ? AND password = ?";
    public static final String REGISTER_QUERY = "INSERT INTO users (name, password, current_balance) VALUES (?, ?, ?)";
    public static final String CHECK_USER_EXISTS = "SELECT * FROM users WHERE name = ?";
    public static final String INSERT_TRANSACTION = "INSERT INTO transactions (user_id, transaction_type, transaction_amount, transaction_date) VALUES (?, ?, ?, ?)";
    public static final String UPDATE_CURRENT_BALANCE = "UPDATE users SET current_balance = ? WHERE id = ?";
    public static final String USER_BY_NAME = "SELECT * FROM users WHERE name = ?";
    public static final String TRANSACTION_BY_USER_ID = "SELECT * FROM transactions WHERE user_id = ?";


}
