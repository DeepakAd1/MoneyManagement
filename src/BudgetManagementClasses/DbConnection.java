package BudgetManagementClasses;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    public static Connection getConnecton() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/budgetmanagement","root","Deepakad");
    }
}
