package BudgetManagementServices;

import BudgetManagementClasses.DbConnection;
import BudgetManagementClasses.Goals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GoalService {

    public void addGoal(Goals goal) throws SQLException {
        Connection connection = DbConnection.getConnecton();
        String sql = "INSERT INTO goals (user_id, goal_name, goal_amount, current_amount, target_date, description, status, created_on, updated_on) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, goal.user_id);
            stmt.setString(2, goal.goal_name);
            stmt.setDouble(3, goal.goal_amount);
            stmt.setDouble(4, goal.current_amount);
            stmt.setDate(5, new java.sql.Date(goal.target_date.getTime()));
            stmt.setString(6, goal.description);
            stmt.setString(7, goal.status);
            stmt.setTimestamp(8, new java.sql.Timestamp(goal.createdOn.getTime()));
            stmt.setTimestamp(9, new java.sql.Timestamp(goal.updatedOn.getTime()));
            stmt.executeUpdate();
            System.out.println("Goal added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateGoal(Goals goal) throws SQLException {
        Connection connection = DbConnection.getConnecton();
        String sql = "UPDATE goals SET goal_name = ?, goal_amount = ?, current_amount = ?, target_date = ?, description = ?, status = ?, updated_on = ? WHERE goal_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, goal.goal_name);
            stmt.setDouble(2, goal.goal_amount);
            stmt.setDouble(3, goal.current_amount);
            stmt.setDate(4, new java.sql.Date(goal.target_date.getTime()));
            stmt.setString(5, goal.description);
            stmt.setString(6, goal.status);
            stmt.setTimestamp(7, new java.sql.Timestamp(goal.updatedOn.getTime()));
            stmt.setInt(8, goal.goal_id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Goal updated successfully.");
            } else {
                System.out.println("No goal found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteGoal(int goalId) throws SQLException {
        Connection connection = DbConnection.getConnecton();
        String sql = "DELETE FROM goals WHERE goal_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, goalId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Goal and its contributions deleted successfully.");
            } else {
                System.out.println("No goal found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static  void showUserGoals(int user_id) throws SQLException {
        Connection connection=DbConnection.getConnecton();
        String query = "SELECT * FROM goals WHERE user_id = ?";
        PreparedStatement pst=connection.prepareStatement(query);
        pst.setInt(1,user_id);
        ResultSet rs=pst.executeQuery();
        // Print the header with proper formatting
        System.out.printf("%-10s %-20s %-15s %-15s %-15s %-10s%n",
                "goal_id", "goal_name", "goal_amount", "current_amount", "target_date", "status");

        // Print each row of the result set
        while (rs.next()) {
            System.out.printf("%-10d %-20s %-15.2f %-15.2f %-15s %-10s%n",
                    rs.getInt("goal_id"),
                    rs.getString("goal_name"),
                    rs.getDouble("goal_amount"),
                    rs.getDouble("current_amount"),
                    rs.getDate("target_date"),
                    rs.getString("status"));
        }
    }
    public static boolean checkGoals(int goal_id,java.sql.Date date) throws SQLException {
        Connection connection=DbConnection.getConnecton();
        String query = "SELECT status FROM goals WHERE goal_id = ? and target_date<=?";
        PreparedStatement pst=connection.prepareStatement(query);
        pst.setInt(1,goal_id);
        pst.setDate(2,date);
        ResultSet rs=pst.executeQuery();
        String goal_status="";
        while(rs.next()){
            goal_status=rs.getString("status");
        }
        return goal_status.equalsIgnoreCase("In Progress");
    }

    public static double getCurrAmount(int goal_id) throws SQLException {
        Connection connection=DbConnection.getConnecton();
        String query = "SELECT current_amount FROM goals WHERE goal_id = ? ";
        PreparedStatement pst=connection.prepareStatement(query);
        pst.setInt(1,goal_id);

        ResultSet rs=pst.executeQuery();
        double amount=0;
        while(rs.next()){
            amount=rs.getDouble("current_amount");
        }
        return amount;
    }

    public static void updateStatus(int goal_id) throws SQLException {
        Connection connection=DbConnection.getConnecton();
        String query = "SELECT current_amount,goal_amount FROM goals WHERE goal_id = ? ";
        PreparedStatement pst=connection.prepareStatement(query);
        pst.setInt(1,goal_id);

        ResultSet rs=pst.executeQuery();

        double goal_amount=0,cur_amount=0;
        while(rs.next()){
            cur_amount=rs.getDouble("current_amount");
            goal_amount=rs.getDouble("goal_amount");
        }
        if(cur_amount>=goal_amount){
            PreparedStatement pstt=connection.prepareStatement("UPDATE goals SET status=? where goal_id=?");
            pstt.setInt(2,goal_id);
            pstt.setString(1,"Completed");
            pstt.executeUpdate();
        }
    }
}
