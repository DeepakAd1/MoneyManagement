package BudgetManagementServices;

import BudgetManagementClasses.DbConnection;
import BudgetManagementClasses.Expenses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExpenseService {
    public void addExpense(Expenses expense) throws SQLException {
        Connection connection = DbConnection.getConnecton();
        String sql = "INSERT INTO expenses (user_id, account_id, expense_category_id, amount, date, note, description, created_on, updated_on) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, expense.user_id);
            stmt.setInt(2, expense.account_id);
            stmt.setInt(3, expense.expense_category_id);
            stmt.setDouble(4, expense.amount);
            stmt.setDate(5, new java.sql.Date(expense.date.getTime()));
            stmt.setString(6, expense.note);
            stmt.setString(7, expense.description);
            stmt.setDate(8, new java.sql.Date(expense.createdOn.getTime()));
            stmt.setDate(9, new java.sql.Date(expense.updatedOn.getTime()));
            stmt.executeUpdate();
            System.out.println("Expense added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateExpense(Expenses expense) throws SQLException {
        Connection connection = DbConnection.getConnecton();
        String sql = "UPDATE expenses SET user_id = ?, account_id = ?, expense_category_id = ?, amount = ?, date = ?, note = ?, description = ?, updated_on = ? WHERE expense_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, expense.user_id);
            stmt.setInt(2, expense.account_id);
            stmt.setInt(3, expense.expense_category_id);
            stmt.setDouble(4, expense.amount);
            stmt.setDate(5, new java.sql.Date(expense.date.getTime()));
            stmt.setString(6, expense.note);
            stmt.setString(7, expense.description);
            stmt.setDate(8, new java.sql.Date(expense.updatedOn.getTime()));
            stmt.setInt(9, expense.expense_id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Expense details updated successfully.");
            } else {
                System.out.println("No expense found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void showExpenses(int user_id) throws SQLException {
        Connection con = DbConnection.getConnecton();
        PreparedStatement p = con.prepareStatement("SELECT expense_id, amount, date, note, description FROM expenses WHERE user_id = ?");
        p.setInt(1, user_id);
        ResultSet rs = p.executeQuery();

        // Print the header with proper formatting
        System.out.printf("%-10s %-10s %-15s %-30s %-30s%n",
                "expense_id", "amount", "date", "note", "description");

        // Print each row of the result set
        while (rs.next()) {
            System.out.printf("%-10d %-10.2f %-15s %-30s %-30s%n",
                    rs.getInt("expense_id"),
                    rs.getDouble("amount"),
                    rs.getDate("date"),
                    rs.getString("note"),
                    rs.getString("description"));
        }
    }



}

