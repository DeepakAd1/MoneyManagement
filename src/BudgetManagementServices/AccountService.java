package BudgetManagementServices;

import BudgetManagementClasses.DbConnection;
import BudgetManagementClasses.Accounts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountService {
    public void addAccount(Accounts account) throws SQLException {
        if (isAccountNameExists(account.account_type, account.user_id)) {
            System.out.println("The account type is already present.");
            return;
        }

        Connection connection = DbConnection.getConnecton();
        String sql = "INSERT INTO accounts (user_id, account_type, balance, created_on, updated_on) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, account.user_id);
            stmt.setString(2, account.account_type);
            stmt.setDouble(3, account.balance);
            stmt.setDate(4, new java.sql.Date(account.createdOn.getTime()));
            stmt.setDate(5, new java.sql.Date(account.updatedOn.getTime()));
            stmt.executeUpdate();
            System.out.println("Account added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAccount(Accounts account) throws SQLException {
        if (isAccountNameExists(account.account_type, account.user_id)) {
            System.out.println("The account type is already present.");
            return;
        }

        Connection connection = DbConnection.getConnecton();
        String sql = "UPDATE accounts SET account_type = ?, updated_on = ?,balance=? WHERE account_id = ? AND user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, account.account_type);
            stmt.setDate(2, new java.sql.Date(account.updatedOn.getTime()));
            stmt.setInt(4, account.account_id);
            stmt.setInt(5, account.user_id);
            stmt.setDouble(3, account.balance);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Account updated successfully.");
            } else {
                System.out.println("No account found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isAccountNameExists(String accountType, int userId) throws SQLException {
        Connection connection = DbConnection.getConnecton();
        String sql = "SELECT COUNT(*) FROM accounts WHERE account_type = ? AND user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, accountType);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int getAccountId(String accountType, int userId) throws SQLException {
        Connection connection=DbConnection.getConnecton();
        String query = "SELECT account_id FROM accounts WHERE account_type = ? AND user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, accountType);
            statement.setInt(2, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("account_id");
                } else {
                    // Handle case where account is not found
                    throw new SQLException("Account not found");
                }
            }
        }
    }

    public static void showUserAccounts(int user_id) throws SQLException {
        Connection connection=DbConnection.getConnecton();
        String query = "SELECT * FROM accounts WHERE user_id = ?";
        PreparedStatement pst=connection.prepareStatement(query);
        pst.setInt(1,user_id);
        ResultSet rs=pst.executeQuery();
        System.out.println("account_id "+" account_type "+"   balance");
        while(rs.next()){
            System.out.println(rs.getInt("account_id")+"       "+rs.getString("account_type")+"  "+rs.getDouble("balance"));
        }
    }
}
