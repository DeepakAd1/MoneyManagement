package BudgetManagementServices;

import BudgetManagementClasses.DbConnection;
import BudgetManagementClasses.Incomes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class IncomeService {
    public void addIncome(Incomes income) throws SQLException {
        Connection connection = DbConnection.getConnecton();
        String sql = "INSERT INTO incomes (user_id, account_id, income_category_id, amount, date, note, description, created_on, updated_on) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, income.user_id);
            stmt.setInt(2, income.account_id);
            stmt.setInt(3, income.income_category_id);
            stmt.setDouble(4, income.amount);
            stmt.setDate(5, new java.sql.Date(income.date.getTime()));
            stmt.setString(6, income.note);
            stmt.setString(7, income.description);
            stmt.setDate(8, new java.sql.Date(income.createdOn.getTime()));
            stmt.setDate(9, new java.sql.Date(income.updatedOn.getTime()));
            stmt.executeUpdate();
            System.out.println("Income added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateIncome(Incomes income) throws SQLException {
        Connection connection = DbConnection.getConnecton();
        String sql = "UPDATE incomes SET user_id = ?, account_id = ?, income_category_id = ?, amount = ?, date = ?, note = ?, description = ?, updated_on = ? WHERE income_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, income.user_id);
            stmt.setInt(2, income.account_id);
            stmt.setInt(3, income.income_category_id);
            stmt.setDouble(4, income.amount);
            stmt.setDate(5, new java.sql.Date(income.date.getTime()));
            stmt.setString(6, income.note);
            stmt.setString(7, income.description);
            stmt.setDate(8, new java.sql.Date(income.updatedOn.getTime()));
            stmt.setInt(9, income.income_id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Income details updated successfully.");
            } else {
                System.out.println("No income found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void showIncomeCategory(int user_id) throws SQLException {
        Connection con=DbConnection.getConnecton();
        PreparedStatement stmt = con.prepareStatement("select * from income_category where user_id=?");
        stmt.setInt(1,user_id);
        ResultSet rs=stmt.executeQuery();
        // Print the header with fixed-width columns
        System.out.printf("%-20s %-20s %-30s%n", "income_category_id", "category_name", "description");

        // Print each row of the result set with the same fixed-width columns
        while (rs.next()) {
            System.out.printf("%-20d %-20s %-30s%n",
                    rs.getInt("income_category_id"),
                    rs.getString("category_name"),
                    rs.getString("description"));
        }
    }

    public static void showIncomeofUser(int user_id) throws SQLException {
        Connection con = DbConnection.getConnecton();
        String sql = "SELECT i.income_id, ic.category_name, i.amount, i.date, i.note, i.description " +
                "FROM incomes i " +
                "JOIN income_category ic ON i.income_category_id = ic.income_category_id " +
                "WHERE i.user_id = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, user_id);
        ResultSet rs = stmt.executeQuery();

        System.out.println("Income ID | Category Name  |  Amount  |  Date  |  Note  |  Description");
        System.out.println("----------------------------------------------------------------------");

        while (rs.next()) {
            int incomeId = rs.getInt("income_id");
            String categoryName = rs.getString("category_name");
            double amount = rs.getDouble("amount");
            Date date = rs.getDate("date");
            String note = rs.getString("note");
            String description = rs.getString("description");

            System.out.println(incomeId + "        | " + categoryName + "  |  " + amount + "  |  " + date + "  |  " + note + "  |  " + description);
        }
    }

}

