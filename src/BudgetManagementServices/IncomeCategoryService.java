package BudgetManagementServices;

import BudgetManagementClasses.DbConnection;
import BudgetManagementClasses.IncomeCategory;

import java.sql.*;

public class IncomeCategoryService {
    public void addIncomeCategory(IncomeCategory incomeCategory) throws SQLException {
        if (isCategoryNameExists(incomeCategory.category_name, incomeCategory.user_id)) {
            System.out.println("The category is already present.");
            return;
        }

        Connection connection = DbConnection.getConnecton();
        String sql = "INSERT INTO income_category (user_id, category_name, description, created_on, updated_on) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, incomeCategory.user_id);
            stmt.setString(2, incomeCategory.category_name);
            stmt.setString(3, incomeCategory.description);
            stmt.setTimestamp(4, new  Timestamp(incomeCategory.createdOn.getTime()));
            stmt.setTimestamp(5, new  Timestamp(incomeCategory.updatedOn.getTime()));
            stmt.executeUpdate();
            System.out.println("Income category added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateIncomeCategory(IncomeCategory incomeCategory) throws SQLException {
        if (isCategoryNameExists(incomeCategory.category_name, incomeCategory.user_id)) {
            System.out.println("The category is already present.");
            return;
        }

        Connection connection = DbConnection.getConnecton();
        String sql = "UPDATE income_category SET category_name = ?, description = ?, updated_on = ? WHERE income_category_id = ? AND user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, incomeCategory.category_name);
            stmt.setString(2, incomeCategory.description);
            stmt.setTimestamp(3, new Timestamp(incomeCategory.updatedOn.getTime()));
            stmt.setInt(4, incomeCategory.income_category_id);
            stmt.setInt(5, incomeCategory.user_id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Income category updated successfully.");
            } else {
                System.out.println("No income category found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isCategoryNameExists(String categoryName, int userId) throws SQLException {
        Connection connection = DbConnection.getConnecton();
        String sql = "SELECT COUNT(*) FROM income_category WHERE category_name = ? AND user_id = ?";
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
}
