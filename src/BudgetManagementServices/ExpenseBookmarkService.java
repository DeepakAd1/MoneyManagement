package BudgetManagementServices;

import BudgetManagementClasses.DbConnection;
import BudgetManagementClasses.ExpenseBookmark;

import java.sql.*;

public class ExpenseBookmarkService {

    public void addExpenseBookmark(ExpenseBookmark expenseBookmark) throws SQLException {
        if (isBookmarkExists(expenseBookmark.user_id, expenseBookmark.expense_category_id)) {
            System.out.println("The expense bookmark is already present.");
            return;
        }

        Connection connection = DbConnection.getConnecton();
        String sql = "INSERT INTO expense_bookmark (user_id, expense_category_id, created_on) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, expenseBookmark.user_id);
            stmt.setInt(2, expenseBookmark.expense_category_id);
            stmt.setTimestamp(3, new java.sql.Timestamp(expenseBookmark.createdOn.getTime()));
            stmt.executeUpdate();
            System.out.println("Expense bookmark added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeExpenseBookmark(int expenseBookmarkId) throws SQLException {
        Connection connection = DbConnection.getConnecton();
        String sql = "DELETE FROM expense_bookmark WHERE expense_bookmark_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, expenseBookmarkId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Expense bookmark removed successfully.");
            } else {
                System.out.println("No expense bookmark found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isBookmarkExists(int userId, int expenseCategoryId) throws SQLException {
        Connection connection = DbConnection.getConnecton();
        String sql = "SELECT COUNT(*) FROM expense_bookmark WHERE user_id = ? AND expense_category_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, expenseCategoryId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void showExpenseBookmark(int userId) throws SQLException {
        String query = "SELECT eb.user_id, ec.category_name, eb.created_on " +
                "FROM expense_bookmark eb " +
                "JOIN expense_category ec ON eb.expense_category_id = ec.expense_category_id " +
                "WHERE eb.user_id = ?";

        try (Connection connection = DbConnection.getConnecton();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, userId);
            try (ResultSet resultSet = stmt.executeQuery()) {
                System.out.println("+---------+---------------------+---------------------+");
                System.out.println("| User ID | Expense Category    | Created On          |");
                System.out.println("+---------+---------------------+---------------------+");

                while (resultSet.next()) {
                    int user_id = resultSet.getInt("user_id");
                    String category_name = resultSet.getString("category_name");
                    Timestamp created_on = resultSet.getTimestamp("created_on");

                    System.out.printf("| %-7d | %-19s | %-19s |%n", user_id, category_name, created_on);
                }

                System.out.println("+---------+---------------------+---------------------+");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

