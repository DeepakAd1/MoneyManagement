package BudgetManagementServices;

import BudgetManagementClasses.DbConnection;
import BudgetManagementClasses.UserCurrency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserCurrencyService {

    public void addUserCurrency(UserCurrency userCurrency) throws SQLException {
        Connection connection = DbConnection.getConnecton();
        String sql = "INSERT INTO user_currency (user_id, currency_id, updated_on) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userCurrency.user_id);
            stmt.setInt(2, userCurrency.currency_id);
            stmt.setTimestamp(3, new java.sql.Timestamp(userCurrency.updatedOn.getTime()));
            stmt.executeUpdate();
            System.out.println("User currency added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUserCurrency(UserCurrency userCurrency) throws SQLException {
        Connection connection = DbConnection.getConnecton();
        String sql = "UPDATE user_currency SET currency_id = ?, updated_on = ? WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userCurrency.currency_id);
            stmt.setTimestamp(2, new java.sql.Timestamp(userCurrency.updatedOn.getTime()));
            stmt.setInt(3, userCurrency.user_id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User currency updated successfully.");
            } else {
                System.out.println("No user currency found with the given user ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
