package db_objs;

import java.math.BigDecimal;
import java.util.Date;

public record Transaction(int userId, String transactionType, BigDecimal transactionAmount, Date transactionDate) {
}
