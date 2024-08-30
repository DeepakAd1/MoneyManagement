package BudgetManagementServices;

import BudgetManagementClasses.DbConnection;
import BudgetManagementClasses.GoalContribution;

import java.sql.*;

import static BudgetManagementServices.GoalService.*;

public class GoalContributionService {

    public void addGoalContribution(GoalContribution contribution) throws SQLException {
        Connection connection = DbConnection.getConnecton();
        String sql = "INSERT INTO goal_contribution (goal_id, amount_contribution, date, account_id, note, created_on, updated_on) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, contribution.goal_id);
            stmt.setDouble(2, contribution.amount_contribution);
            stmt.setDate(3, new java.sql.Date(contribution.date.getTime()));
            stmt.setInt(4, contribution.account_id);
            stmt.setString(5, contribution.note);
            stmt.setTimestamp(6, new java.sql.Timestamp(contribution.createdOn.getTime()));
            stmt.setTimestamp(7, new java.sql.Timestamp(contribution.updatedOn.getTime()));


            if(checkGoals(contribution.goal_id,contribution.date)){
                PreparedStatement stm=connection.prepareStatement("UPDATE goals SET current_amount=? where goal_id=? ");
                stm.setInt(2,contribution.goal_id);
                stm.setDouble(1,getCurrAmount(contribution.goal_id)+contribution.amount_contribution);
                stm.executeUpdate();
                updateStatus(contribution.goal_id);
                stmt.executeUpdate();
                System.out.println("Goal contribution added successfully.");
            }else{
                System.out.println("The goal is already accomplished champ!!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateGoalContribution(GoalContribution contribution) throws SQLException {
        Connection connection = DbConnection.getConnecton();
        PreparedStatement p=connection.prepareStatement("Select amount_contribution from goal_contribution where goal_contribution_id=(SELECT max(goal_contribution_id) from goal_contribution )");
        ResultSet r=p.executeQuery();
        double amount_contribution=0;
        while(r.next()){
            amount_contribution=r.getDouble("amount_contribution");
        }
        PreparedStatement stm=connection.prepareStatement("UPDATE goals SET current_amount=? where goal_id=? ");
        stm.setInt(2,contribution.goal_id);
        stm.setDouble(1,getCurrAmount(contribution.goal_id)-amount_contribution);
        stm.executeUpdate();
        String sql = "UPDATE goal_contribution SET amount_contribution = ?, date = ?, account_id = ?, note = ?, updated_on = ? WHERE goal_contribution_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, contribution.amount_contribution);
            stmt.setDate(2, new java.sql.Date(contribution.date.getTime()));
            stmt.setInt(3, contribution.account_id);
            stmt.setString(4, contribution.note);
            stmt.setTimestamp(5, new java.sql.Timestamp(contribution.updatedOn.getTime()));
            stmt.setInt(6, contribution.goal_contribution_id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Goal contribution updated successfully.");
            } else {
                System.out.println("No goal contribution found with the given ID.");
            }
            if(checkGoals(contribution.goal_id, (Date) contribution.updatedOn)){
                PreparedStatement stmm =connection.prepareStatement("UPDATE goals SET current_amount=? where goal_id=? ");
                stmm.setInt(2,contribution.goal_id);
                stmm.setDouble(1,getCurrAmount(contribution.goal_id)+contribution.amount_contribution);
                stmm.executeUpdate();
                updateStatus(contribution.goal_id);
            }else{
                System.out.println("The goal is already accomplished champ!!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteGoalContribution(int goalContributionId) throws SQLException {
        Connection connection = DbConnection.getConnecton();
        String sql = "DELETE FROM goal_contribution WHERE goal_contribution_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, goalContributionId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Goal contribution deleted successfully.");
            } else {
                System.out.println("No goal contribution found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void showGoalContribution(int user_id) throws SQLException {
        Connection connection=DbConnection.getConnecton();
        String query = "SELECT gc.goal_contribution_id,g.goal_name,g.goal_amount,gc.amount_contribution,gc.date"+
                " FROM goal_contribution gc join goals g on gc.goal_contribution_id=g.goal_id WHERE g.user_id = ?";
        PreparedStatement pst=connection.prepareStatement(query);
        pst.setInt(1,user_id);
        ResultSet rs=pst.executeQuery();
        // Print the header with proper formatting
        System.out.printf("%-20s %-20s %-15s %-20s %-15s%n",
                "goal_contribution_id", "goal_name", "goal_amount", "amount_contribution", "date");

        // Print each row of the result set
        while (rs.next()) {
            System.out.printf("%-20d %-20s %-15.2f %-20.2f %-15s%n",
                    rs.getInt("goal_contribution_id"),
                    rs.getString("goal_name"),
                    rs.getDouble("goal_amount"),
                    rs.getDouble("amount_contribution"),
                    rs.getDate("date"));
        }
    }
}

