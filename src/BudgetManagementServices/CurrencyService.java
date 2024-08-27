package BudgetManagementServices;

import BudgetManagementClasses.Currencies;
import BudgetManagementClasses.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CurrencyService {

    public void addCurrency(Currencies currency) throws SQLException {
        Connection connection = DbConnection.getConnecton();
        String sql = "INSERT INTO currencies (currency_code, currency_name, created_on) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, currency.currency_code);
            stmt.setString(2, currency.currency_name);
            stmt.setTimestamp(3, new java.sql.Timestamp(currency.createdOn.getTime()));
            stmt.executeUpdate();
            System.out.println("Currency added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
