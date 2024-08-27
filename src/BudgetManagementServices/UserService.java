package BudgetManagementServices;

import BudgetManagementClasses.DbConnection;
import BudgetManagementClasses.Users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {
    public void addUser(Users user) throws SQLException {
        Connection connection= DbConnection.getConnecton();
        String sql = "INSERT INTO users (user_name, email_id, password, created_on, updated_on) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.user_name);
            stmt.setString(2, user.email_id);
            stmt.setString(3, user.password);
            stmt.setDate(4, new java.sql.Date(user.createdOn.getTime()));
            stmt.setDate(5, new java.sql.Date(user.updatedOn.getTime()));
            stmt.executeUpdate();
            System.out.println("User added successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void removeUser(int userId) throws SQLException {
        Connection connection= DbConnection.getConnecton();
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User removed successfully.");
            } else {
                System.out.println("No user found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(Users user) throws SQLException {
        Connection connection= DbConnection.getConnecton();
        String sql = "UPDATE users SET user_name = ?, email_id = ?, password = ?, updated_on = ? WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.user_name);
            stmt.setString(2, user.email_id);
            stmt.setString(3, user.password);
            stmt.setDate(4, new java.sql.Date(user.updatedOn.getTime()));
            stmt.setInt(5, user.user_id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User details updated successfully.");
            } else {
                System.out.println("No user found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean doesUserExist(int userId) throws SQLException {
        Connection connection = DbConnection.getConnecton();
        String sql = "SELECT COUNT(*) FROM users WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Returns true if the count is greater than 0
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Returns false if an exception occurs or no rows are found
    }

    public static int getUserIdbyMail(String email_id) throws SQLException {
        Connection con=DbConnection.getConnecton();
        PreparedStatement p=con.prepareStatement("select user_id from users where email_id=?");
        p.setString(1,email_id);
        ResultSet rs=p.executeQuery();
        while(rs.next()){
            return rs.getInt("user_id");
        }
        return -1;
    }
}

