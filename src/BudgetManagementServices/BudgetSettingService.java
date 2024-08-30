package BudgetManagementServices;

import BudgetManagementClasses.DbConnection;
import BudgetManagementClasses.BudgetSetting;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BudgetSettingService {

    // Method to add a new budget setting
    public void addBudgetSetting(BudgetSetting budgetSetting) throws SQLException {
        Connection connection = DbConnection.getConnecton();
        String sql = "INSERT INTO budget_setting (user_id, expense_category_id, budget_amount, start_date, end_date, created_on, updated_on) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, budgetSetting.user_id);
            stmt.setInt(2, budgetSetting.expense_category_id);
            stmt.setDouble(3, budgetSetting.budget_amount);
            stmt.setDate(4, new java.sql.Date(budgetSetting.start_date.getTime()));
            stmt.setDate(5, new java.sql.Date(budgetSetting.end_date.getTime()));
            stmt.setTimestamp(6, new java.sql.Timestamp(budgetSetting.createdOn.getTime()));
            stmt.setTimestamp(7, new java.sql.Timestamp(budgetSetting.updatedOn.getTime()));
            stmt.executeUpdate();
            System.out.println("Budget setting added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update an existing budget setting
    public void updateBudgetSetting(BudgetSetting budgetSetting) throws SQLException {
        Connection connection = DbConnection.getConnecton();
        String sql = "UPDATE budget_setting SET user_id = ?, expense_category_id = ?, budget_amount = ?, start_date = ?, end_date = ?, updated_on = ? WHERE budget_setting_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, budgetSetting.user_id);
            stmt.setInt(2, budgetSetting.expense_category_id);
            stmt.setDouble(3, budgetSetting.budget_amount);
            stmt.setDate(4, new java.sql.Date(budgetSetting.start_date.getTime()));
            stmt.setDate(5, new java.sql.Date(budgetSetting.end_date.getTime()));
            stmt.setTimestamp(6, new java.sql.Timestamp(budgetSetting.updatedOn.getTime()));
            stmt.setInt(7, budgetSetting.budget_setting_id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Budget setting updated successfully.");
            } else {
                System.out.println("No budget setting found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void DeleteBudget(int budget_id) throws SQLException {
        Connection connection=DbConnection.getConnecton();
        String query = "delete from budget_setting where budget_setting_id=? ";
        PreparedStatement pst=connection.prepareStatement(query);
        pst.setInt(1,budget_id);
        pst.executeQuery();
        System.out.println("Budget setting deleted successfully.");
    }

    public static void showUserBudget(int user_id) throws SQLException {
        Connection connection=DbConnection.getConnecton();
        String query = "SELECT b.budget_setting_id, e.category_name, b.budget_amount, b.start_date, b.end_date "  +
                "FROM budget_setting b " +
                "JOIN expense_category e ON b.expense_category_id = e.expense_category_id " +
                "WHERE b.user_id = ?";
        PreparedStatement pst=connection.prepareStatement(query);
        pst.setInt(1,user_id);
        ResultSet rs=pst.executeQuery();
        System.out.println("budget_id "+" category_name "+" budget_amount ");
        while(rs.next()){
            System.out.println(rs.getInt("budget_setting_id")+"       "+rs.getString("category_name")+"  "+rs.getDouble("budget_amount")+"   "+rs.getDate("start_date")+"  "+rs.getDate("end_date"));
        }
    }

    public static ResultSet getDatesofBudget(int budgetId) throws SQLException {
        String q="SELECT start_date,end_date from budget_setting where budget_id=? ";
        Connection connection=DbConnection.getConnecton();
        PreparedStatement pst=connection.prepareStatement(q);
        pst.setInt(1,budgetId);
        return pst.executeQuery();
    }

    public static void showUserBudgetFromandTo(int user_id,java.sql.Date st,java.sql.Date ed) throws SQLException {
        Connection connection=DbConnection.getConnecton();
        String query = "SELECT b.budget_setting_id, e.category_name, b.budget_amount, b.start_date, b.end_date "  +
                "FROM budget_setting b " +
                "JOIN expense_category e ON b.expense_category_id = e.expense_category_id " +
                "WHERE b.user_id = ? AND start_date=? and end_date=? ";
        PreparedStatement pst=connection.prepareStatement(query);
        pst.setInt(1,user_id);
        pst.setDate(2,st);
        pst.setDate(3,ed);
        ResultSet rs=pst.executeQuery();
        System.out.println("budget_id "+" category_name "+" budget_amount ");
        while(rs.next()){
            System.out.println(rs.getInt("budget_setting_id")+"       "+rs.getString("category_name")+"  "+rs.getDouble("budget_amount")+"   "+rs.getDate("start_date")+"  "+rs.getDate("end_date"));
        }
    }
}
