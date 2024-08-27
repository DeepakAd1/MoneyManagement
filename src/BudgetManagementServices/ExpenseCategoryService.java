package BudgetManagementServices;

import BudgetManagementClasses.DbConnection;
import BudgetManagementClasses.ExpenseCategory;

import java.sql.*;

public class ExpenseCategoryService {
    public void addExpenseCategory(ExpenseCategory expenseCategory) throws SQLException {
        if (isCategoryNameExists(expenseCategory.category_name, expenseCategory.user_id)) {
            System.out.println("The category is already present.");
            return;
        }

        Connection connection = DbConnection.getConnecton();
        String sql = "INSERT INTO expense_category (user_id, category_name, description, created_on, updated_on) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, expenseCategory.user_id);
            stmt.setString(2, expenseCategory.category_name);
            stmt.setString(3, expenseCategory.description);
            stmt.setTimestamp(4, new Timestamp(expenseCategory.createdOn.getTime()));
            stmt.setTimestamp(5, new Timestamp(expenseCategory.updatedOn.getTime()));
            stmt.executeUpdate();
            System.out.println("Expense category added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateExpenseCategory(ExpenseCategory expenseCategory) throws SQLException {
        if (isCategoryNameExists(expenseCategory.category_name, expenseCategory.user_id)) {
            System.out.println("The category is already present.");
            return;
        }

        Connection connection = DbConnection.getConnecton();
        String sql = "UPDATE expense_category SET category_name = ?, description = ?, updated_on = ? WHERE expense_category_id = ? AND user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, expenseCategory.category_name);
            stmt.setString(2, expenseCategory.description);
            stmt.setTimestamp(3, new Timestamp(expenseCategory.updatedOn.getTime()));
            stmt.setInt(4, expenseCategory.expense_category_id);
            stmt.setInt(5, expenseCategory.user_id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Expense category updated successfully.");
            } else {
                System.out.println("No expense category found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isCategoryNameExists(String categoryName, int userId) throws SQLException {
        Connection connection = DbConnection.getConnecton();
        String sql = "SELECT COUNT(*) FROM expense_category WHERE category_name = ? AND user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, categoryName);
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

    public static int getExpenseCategoryId(String categoryName, int userId) throws SQLException {
        Connection connection=DbConnection.getConnecton();
        String query = "SELECT expense_category_id FROM expense_category WHERE category_name = ? AND user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, categoryName);
            statement.setInt(2, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("expense_category_id");
                } else {
                    // Handle case where category is not found
                    throw new SQLException("Category not found");
                }
            }
        }
    }

    public static void  showUserExpenseCategories(int user_id) throws SQLException {
        Connection connection=DbConnection.getConnecton();
        String query="SELECT * from expense_category where user_id=?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1,user_id);
        ResultSet rs=statement.executeQuery();
// Print the header with proper spacing
        System.out.printf("%-20s %-20s %-30s%n", "expense_category_id", "category_name", "description");

        // Print each row of the result set
        while (rs.next()) {
            System.out.printf("%-20d %-20s %-30s%n",
                    rs.getInt("expense_category_id"),
                    rs.getString("category_name"),
                    rs.getString("description"));
        }
    }
}
