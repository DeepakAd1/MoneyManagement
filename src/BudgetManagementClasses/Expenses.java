package BudgetManagementClasses;

import java.sql.Timestamp;

public class Expenses {
    public int expense_id;
    public int user_id;
    public int account_id;
    public int expense_category_id;
    public double amount;
    public java.sql.Date date;
    public String note;
    public String description;
    public Timestamp createdOn;
    public Timestamp updatedOn;
}
