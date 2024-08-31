package BudgetManagementUserQueries;

import BudgetManagementClasses.DbConnection;

import java.sql.*;

public class Queries {
    static Connection con;

    static {
        try {
            con = DbConnection.getConnecton();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Queries() throws SQLException {
    }

    public static void ShowExpensesFromandTo(int userId, java.sql.Date st_date, java.sql.Date ed_date) throws SQLException {
        ResultSet rs=ExpensesFromandTo(userId,st_date,ed_date);

        System.out.printf("%-10s %-10s %-12s %-20s %-20s%n", "Expense ID", "Amount", "Date", "Account Type", "Category Name");
        System.out.println("--------------------------------------------------------------");

// Print each row
        while (rs.next()) {
            int expenseId = rs.getInt("expense_id");
            double amount = rs.getDouble("amount");
            Date date = rs.getDate("date");
            String accountType = rs.getString("account_type");
            String categoryName = rs.getString("category_name");

            // Print the formatted row
            System.out.printf("%-10d %-10.2f %-12s %-20s %-20s%n", expenseId, amount, date, accountType, categoryName);
        }
    }

    public static void SumDailyExpenseFromandTo(int userId, java.sql.Date st_date, java.sql.Date ed_date) throws SQLException {
        String query="Select date,sum(amount) as total_expenditure from expenses where user_id=? and date between ? and ? group by date ";

        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1, userId);
        pst.setDate(2, st_date);
        pst.setDate(3, ed_date);

        // Execute the query
        ResultSet rs = pst.executeQuery();

        // Print header
        System.out.printf("%-12s %-15s%n", "Date", "Total Amount");
        System.out.println("----------------------------");

        // Iterate through the result set and print each row
        while (rs.next()) {
            Date date = rs.getDate("date");
            double totalAmount = rs.getDouble("total_expenditure");

            // Print formatted output
            System.out.printf("%-12s %-15.2f%n", date, totalAmount);
        }
    }

    public static void ShowIncomesFromandTo(int userId,java.sql.Date st_date, java.sql.Date ed_date) throws SQLException {

        ResultSet rs=IncomesFromandTo(userId,st_date,ed_date);
        System.out.printf("%-10s %-20s %-10s %-12s %-20s %n", "Income ID","Category Name","Amount", "Date", "Account Type");
        System.out.println("--------------------------------------------------------------");

// Print each row
        while (rs.next()) {
            int expenseId = rs.getInt("income_id");
            String categoryName = rs.getString("category_name");
            double amount = rs.getDouble("amount");
            Date date = rs.getDate("date");
            String accountType = rs.getString("account_type");


            // Print the formatted row
            System.out.printf("%-10d %-20s %-10.2f %-12s %-20s %n", expenseId, categoryName, amount, date, accountType);
        }
    }

    public static void ShowIncomeCategoryFromandTo(int userId, java.sql.Date st_date, java.sql.Date ed_date) throws SQLException {
        ResultSet rs=IncomeCategoryFromandTo(userId,st_date,ed_date);
        System.out.printf("%-6s %-5s %-16s %-10s %n", " Year "," Month "," Category_Name "," Amount ");
        System.out.println("--------------------------------------------------------------");

// Print each row
        while (rs.next()) {
            Long year = rs.getLong("year");
            Long month = rs.getLong("month");
            double amount = rs.getDouble("amount");
            String category_name = rs.getString("category_name");


            // Print the formatted row
            System.out.printf("%-6s %-5s %-16s %-10s %n", year, month, category_name, amount);
        }

    }

    public static void ShowExpenseCategoryFromandTo(int userId, java.sql.Date st_date, java.sql.Date ed_date) throws SQLException {

        ResultSet rs=ExpenseCategoryFromandTo(userId, st_date, ed_date);
        System.out.printf("%-6s %-5s %-18s %-10s %n", " Year "," Month "," Category_Name "," Amount ");
        System.out.println("--------------------------------------------------------------");

// Print each row
        while (rs.next()) {
            Long year = rs.getLong("year");
            Long month = rs.getLong("month");
            double amount = rs.getDouble("amount");
            String category_name = rs.getString("category_name");


            // Print the formatted row
            System.out.printf(" %-6s %-6s %-18s %-10s %n ", year, month, category_name, amount);
        }

    }

    public static void ShowExpensesforCategory(int exp_cat_id,java.sql.Date st_date,java.sql.Date ed_date) throws SQLException {
        ResultSet rs=ExpensesforCategory(exp_cat_id,st_date, ed_date);

        System.out.printf("%-6s %-5s %-10s %n", " Year "," Month "," Expense ");
        System.out.println("--------------------------------------------------------------");

// Print each row
        while (rs.next()) {
            Long year = rs.getLong("year");
            Long month = rs.getLong("month");
            double amount = rs.getDouble("expense");



            // Print the formatted row
            System.out.printf(" %-6s %-6s %-10s %n ", year, month, amount);
        }
    }

    public static void ShowIncomesforCategory(int exp_cat_id,java.sql.Date st_date,java.sql.Date ed_date) throws SQLException {
        ResultSet rs=IncomesforCategory(exp_cat_id,st_date, ed_date);

        System.out.printf("%-6s %-5s %-10s %n", " Year "," Month "," Income ");
        System.out.println("--------------------------------------------------------------");

// Print each row
        while (rs.next()) {
            Long year = rs.getLong("year");
            Long month = rs.getLong("month");
            double amount = rs.getDouble("income");



            // Print the formatted row
            System.out.printf(" %-6s %-6s %-10s %n ", year, month, amount);
        }
    }


    public static void ShowBudgetResult(int userId,java.sql.Date st_date,java.sql.Date ed_date) throws SQLException {
        ResultSet resultSet=BudgetResult(userId,st_date,ed_date);

        System.out.printf("%-20s %-15s %-15s %-15s %-15s %-15s %-15s\n", "Category", "Budget Amount", "Start Date", "End Date", "Total Expenses", "Total Incomes", "Remaining Budget");
        System.out.println("----------------------------------------------------------------------------------------------------------");

        while (resultSet.next()) {
            String categoryName = resultSet.getString("category_name");
            double budgetAmount = resultSet.getDouble("total_budget_amount");
            Date budgetStartDate = resultSet.getDate("budget_start_date");
            Date budgetEndDate = resultSet.getDate("budget_end_date");
            double totalExpenses = resultSet.getDouble("total_expenses");
            double totalIncomes = resultSet.getDouble("total_incomes");
            double remainingBudget = resultSet.getDouble("remaining_budget");

            // Print data in table format
            System.out.printf("%-20s %-15.2f %-15s %-15s %-15.2f %-15.2f %-15.2f\n",
                    categoryName, budgetAmount, budgetStartDate, budgetEndDate, totalExpenses, totalIncomes, remainingBudget);
        }

    }

    public static void ShowExpensePercentageFromandTo(int user_id,java.sql.Date st_date,java.sql.Date ed_date) throws SQLException {
        ResultSet rs=ExpensePercentageFromandTo(user_id,st_date,ed_date);
        System.out.printf("%-20s %-10s %-7s %n "," category_name "," expense "," expense_percentage");
        while(rs.next()){
            String cat_name=rs.getString("category_name");
            double amount=rs.getDouble("expense");
            double tot_amount=rs.getDouble("total_expense");

            System.out.printf("%-20s %-10s %-3.3f %n",cat_name,amount,(amount/tot_amount)*100);
        }
    }

    public static void ShowIncomePercentageFromandTo(int user_id,java.sql.Date st_date,java.sql.Date ed_date) throws SQLException {
        ResultSet rs=IncomePercentageFromandTo(user_id,st_date,ed_date);
        System.out.printf(" %-20s %-10s %-7s %n "," category_name "," income "," income_percentage");
        while(rs.next()){
            String cat_name=rs.getString("category_name");
            double amount=rs.getDouble("income");
            double tot_amount=rs.getDouble("total_income");

            System.out.printf(" %-20s %-10s %-3.3f %n",cat_name,amount,(amount/tot_amount)*100);
        }
    }

    public static ResultSet BudgetResult(int user_id,java.sql.Date st_date,java.sql.Date ed_date) throws SQLException {

        //IFNULL(SUM(incomes.amount), 0) - IFNULL(SUM(expenses.amount), 0) AS net_income,
        String query= """
            SELECT
                ec.category_name,
                IFNULL(SUM(budgets.budget_amount), 0) AS total_budget_amount,
                MIN(budgets.start_date) AS budget_start_date,
                MAX(budgets.end_date) AS budget_end_date,
                IFNULL(SUM(expenses.amount), 0) AS total_expenses,
                IFNULL(SUM(incomes.amount), 0) AS total_incomes,
                IFNULL(SUM(budgets.budget_amount), 0) - IFNULL(SUM(expenses.amount), 0) AS remaining_budget
            FROM
                budget_setting AS budgets
            LEFT JOIN
                expenses ON budgets.user_id = expenses.user_id
                AND budgets.expense_category_id = expenses.expense_category_id
                AND expenses.date BETWEEN ? AND ?
            LEFT JOIN
                incomes ON budgets.user_id = incomes.user_id
                AND incomes.date BETWEEN ? AND ?
            LEFT JOIN
                expense_category AS ec ON budgets.expense_category_id = ec.expense_category_id
            WHERE
                budgets.user_id = ? AND budgets.start_date BETWEEN ? AND ? AND budgets.end_date BETWEEN ? AND ?
            GROUP BY
                ec.category_name
            ORDER BY
                ec.category_name;
        """;
        PreparedStatement pst=con.prepareStatement(query);
        pst.setDate(1,st_date);
        pst.setDate(2,ed_date);
        pst.setDate(3,st_date);
        pst.setDate(4,ed_date);
        pst.setInt(5,user_id);
        pst.setDate(6,st_date);
        pst.setDate(7,ed_date);
        pst.setDate(8,st_date);
        pst.setDate(9,ed_date);

        return pst.executeQuery();
    }

    public static ResultSet ExpenseCategoryFromandTo(int userId, java.sql.Date st_date, java.sql.Date ed_date) throws SQLException {
        String query="SELECT YEAR(e.date) AS year, MONTH(e.date) AS month, ec.category_name, SUM(e.amount) AS amount"+
                " FROM expense_category ec "+ " JOIN expenses e on e.expense_category_id=ec.expense_category_id "+
                " WHERE e.user_id=? AND e.date BETWEEN ? AND ? "+
                " GROUP BY YEAR(date), MONTH(date), ec.category_name"+
                " ORDER BY YEAR(date), MONTH(date), amount";
        PreparedStatement pst=con.prepareStatement(query);
        pst.setDate(2,st_date);
        pst.setDate(3,ed_date);
        pst.setInt(1,userId);

        return pst.executeQuery();

    }

    public static ResultSet IncomesFromandTo(int userId, java.sql.Date st_date, java.sql.Date ed_date) throws SQLException {
        String query="SELECT "+
                "    i.income_id,i.amount," +
                "    i.date," +
                "    a.account_type," +
                "    ic.category_name" +
                " FROM " +
                "    incomes i" +
                " JOIN " +
                "    accounts a ON i.account_id = a.account_id" +
                " JOIN " +
                "    income_category ic ON i.income_category_id = ic.income_category_id" +
                "  WHERE" +
                "    i.user_id = ?" +
                "    AND i.date BETWEEN ? AND ? ;";
        PreparedStatement pst=con.prepareStatement(query);
        pst.setInt(1,userId);
        pst.setDate(2,st_date);
        pst.setDate(3,ed_date);
        return pst.executeQuery();
    }

    public static ResultSet ExpensesFromandTo(int userId, java.sql.Date st_date, java.sql.Date ed_date) throws SQLException {
        String query="SELECT "+
                "    e.expense_id," +
                "    e.amount," +
                "    e.date," +
                "    a.account_type," +
                "    ec.category_name" +
                " FROM " +
                "    expenses e" +
                " JOIN " +
                "    accounts a ON e.account_id = a.account_id" +
                " JOIN " +
                "    expense_category ec ON e.expense_category_id = ec.expense_category_id" +
                "  WHERE" +
                "    e.user_id = ?" +
                "    AND e.date BETWEEN ? AND ?; ";
        PreparedStatement pst=con.prepareStatement(query);
        pst.setInt(1,userId);
        pst.setDate(2,st_date);
        pst.setDate(3,ed_date);
        return pst.executeQuery();
    }

    public  static ResultSet IncomeCategoryFromandTo(int userId, java.sql.Date st_date, java.sql.Date ed_date) throws SQLException {
        String query="SELECT YEAR(i.date) AS year, MONTH(i.date) AS month, ic.category_name, SUM(i.amount) AS amount"+
                " FROM income_category ic "+ " JOIN incomes i on i.income_category_id=ic.income_category_id "+
                " WHERE i.user_id=? AND date BETWEEN ? AND ? "+
                " GROUP BY YEAR(date), MONTH(date), ic.category_name"+
                " ORDER BY YEAR(date), MONTH(date), amount";
        PreparedStatement pst=con.prepareStatement(query);
        pst.setDate(2,st_date);
        pst.setDate(3,ed_date);
        pst.setInt(1,userId);

        return pst.executeQuery();
    }

    public static ResultSet ExpensesforCategory(int exp_cat_id,java.sql.Date st_date, java.sql.Date ed_date) throws SQLException {
        String query="SELECT ec.category_name,Year(e.date) as year ,Month(e.date) as month ,SUM(e.amount) as expense"+
                " FROM expense_category ec "+ " JOIN expenses e on e.expense_category_id=ec.expense_category_id "+
                " WHERE ec.expense_category_id=? AND e.date BETWEEN ? AND ? "+
                " GROUP BY Year(e.date),Month(e.date),ec.category_name ";
        PreparedStatement pst=con.prepareStatement(query);
        pst.setDate(2,st_date);
        pst.setDate(3,ed_date);
        pst.setInt(1,exp_cat_id);

        return pst.executeQuery();
    }

    public static ResultSet ExpensePercentageFromandTo(int userId, java.sql.Date st_date, java.sql.Date ed_date) throws SQLException {
        String query="SELECT "+
                "    ec.category_name ,"+
                "    SUM(e.amount) as expense ," +
                "    ec.category_name ," + " Sum(SUM(e.amount)) over() AS total_expense "+
                " FROM " +
                "    expenses e" +
                " JOIN " +
                "    expense_category ec ON e.expense_category_id = ec.expense_category_id" +
                "  WHERE" +
                "    e.user_id = ?" +
                "    AND e.date BETWEEN ? AND ? "+" GROUP BY ec.category_name ";
        PreparedStatement pst=con.prepareStatement(query);
        pst.setInt(1,userId);
        pst.setDate(2,st_date);
        pst.setDate(3,ed_date);
        return pst.executeQuery();
    }

    public  static ResultSet IncomePercentageFromandTo(int userId, java.sql.Date st_date, java.sql.Date ed_date) throws SQLException {
        String query="SELECT "+
                "    ic.category_name ,"+
                "    SUM(i.amount) as income ," +
                "    ic.category_name ," + " Sum(SUM(i.amount)) over() AS total_income "+
                " FROM " +
                "    incomes i" +
                " JOIN " +
                "    income_category ic ON i.income_category_id = ic.income_category_id" +
                "  WHERE" +
                "    i.user_id = ?" +
                "    AND i.date BETWEEN ? AND ? "+" GROUP BY ic.category_name ";
        PreparedStatement pst=con.prepareStatement(query);
        pst.setInt(1,userId);
        pst.setDate(2,st_date);
        pst.setDate(3,ed_date);
        return pst.executeQuery();
    }

    public static ResultSet IncomesforCategory(int income_cat_id,java.sql.Date st_date, java.sql.Date ed_date) throws SQLException {
        String query="SELECT ec.category_name,Year(e.date) as year ,Month(e.date) as month ,SUM(e.amount) as income"+
                " FROM income_category ec "+ " JOIN incomes e on e.income_category_id=ec.income_category_id "+
                " WHERE ec.income_category_id=? AND e.date BETWEEN ? AND ? "+
                " GROUP BY Year(e.date),Month(e.date),ec.category_name ";
        PreparedStatement pst=con.prepareStatement(query);
        pst.setDate(2,st_date);
        pst.setDate(3,ed_date);
        pst.setInt(1,income_cat_id);

        return pst.executeQuery();
    }
}
