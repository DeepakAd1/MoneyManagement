import BudgetManagementClasses.*;
import BudgetManagementServices.*;

import java.sql.Timestamp;
import java.util.Scanner;
import java.sql.SQLException;
import java.util.Date;

import static BudgetManagementServices.AccountService.*;
import static BudgetManagementServices.BudgetSettingService.*;
import static BudgetManagementServices.ExpenseBookmarkService.removeExpenseBookmark;
import static BudgetManagementServices.ExpenseBookmarkService.showExpenseBookmark;
import static BudgetManagementServices.ExpenseCategoryService.getExpenseCategoryId;
import static BudgetManagementServices.ExpenseCategoryService.showUserExpenseCategories;
import static BudgetManagementServices.ExpenseService.showExpenses;
import static BudgetManagementServices.GoalContributionService.deleteGoalContribution;
import static BudgetManagementServices.GoalContributionService.showGoalContribution;
import static BudgetManagementServices.GoalService.deleteGoal;
import static BudgetManagementServices.GoalService.showUserGoals;
import static BudgetManagementServices.IncomeService.showIncomeCategory;
import static BudgetManagementServices.IncomeService.showIncomeofUser;
import static BudgetManagementServices.UserService.getUserIdbyMail;
import static BudgetManagementUserQueries.Queries.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        UserService userService = new UserService();

        System.out.println("Welcome! Are you a new user or an existing user? (new/existing)");
        String userType = scanner.nextLine();

        if ("new".equalsIgnoreCase(userType)) {
            // New User Operations
            Users user = new Users();
            System.out.println("Enter your name: ");
            user.user_name = scanner.nextLine();
            System.out.println("Enter your email: ");
            user.email_id = scanner.nextLine();
            scanner.nextLine();
            System.out.println("Enter your password: ");
            user.password = scanner.nextLine();
            scanner.nextLine();
            System.out.println("Enter your currencyId: ");
            user.currency=scanner.nextInt();
            scanner.nextLine();
            user.createdOn = new Date();
            user.updatedOn = new Date();
            userService.addUser(user);

            int user_id = getUserIdbyMail(user.email_id);
            if (user_id == -1) {
                System.out.print("Invalid user mail id");
                throw new Exception("Invalid");
            }
            System.out.println("Your user ID is: " + user_id);

            // After adding user, proceed with other operations
            performOperations(scanner, user_id);

        } else if ("existing".equalsIgnoreCase(userType)) {
            // Existing User Operations
            System.out.println("Enter your Mail ID: ");
            String email = scanner.next();
            int user_id = getUserIdbyMail(email);

            // Check if user exists
            if (userService.doesUserExist(user_id)) {
                System.out.println("Welcome back! What would you like to do?");
                performOperations(scanner, user_id);
            } else {
                System.out.println("User not found. Please ensure your user ID is correct.");
            }
        } else {
            System.out.println("Invalid user type entered.");
        }
    }

    // Helper method to perform operations
    public static void performOperations(Scanner scanner, int user_id) throws SQLException {
        System.out.println("Would you like to Add or Update or Delete or View? (add/update/delete/view)");
        String action = scanner.next();

        if ("add".equalsIgnoreCase(action)) {
            System.out.println("What would you like to add?");
            System.out.println("1. Expenses");
            System.out.println("2. Incomes");
            System.out.println("3. Expense Category");
            System.out.println("4. Income Category");
            System.out.println("5. Accounts");
            System.out.println("6. Budget");
            System.out.println("7. Expense Bookmark");
            System.out.println("8. Goals");
            System.out.println("9. Goal Contribution");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    showUserExpenseCategories(user_id);
                    addExpenses(scanner, user_id);
                    break;
                case 2:
                    showIncomeCategory(user_id);
                    addIncomes(scanner, user_id);
                    break;
                case 3:
                    addExpenseCategory(scanner, user_id);
                    break;
                case 4:
                    addIncomeCategory(scanner, user_id);
                    break;
                case 5:
                    showUserAccounts(user_id);
                    addAccounts(scanner, user_id);
                    break;
                case 6:
                    showUserBudget(user_id);
                    addBudget(scanner, user_id);
                    break;
                case 7:
                    addExpenseBookmark(scanner, user_id);
                    break;
                case 8:
                    showUserGoals(user_id);
                    addGoals(scanner, user_id);
                    break;
                case 9:
                    addGoalContribution(scanner, user_id);
                    break;
                default:
                    System.out.println("Invalid option selected.");
            }
        } else if ("update".equalsIgnoreCase(action)) {
            System.out.println("What would you like to update?");
            System.out.println("1. My details");
            System.out.println("2. Expenses");
            System.out.println("3. Incomes");
            System.out.println("4. Expense Category");
            System.out.println("5. Income Category");
            System.out.println("6. Accounts");
            System.out.println("7. Budget");
            System.out.println("8. Goals");
            System.out.println("9. Goal Contribution");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    updateUserDetails(scanner,user_id);
                    break;
                case 2:
                    updateExpenses(scanner, user_id);
                    break;
                case 3:
                    updateIncomes(scanner, user_id);
                    break;
                case 4:
                    updateExpenseCategory(scanner, user_id);
                    break;
                case 5:
                    updateIncomeCategory(scanner, user_id);
                    break;
                case 6:
                    updateAccounts(scanner, user_id);
                    break;
                case 7:
                    updateBudget(scanner, user_id);
                    break;
                case 8:
                    updateGoals(scanner, user_id);
                    break;
                case 9:
                    updateGoalContribution(scanner, user_id);
                    break;
                default:
                    System.out.println("Invalid option selected.");
            }
        } else if("delete".equalsIgnoreCase(action)){

            System.out.println("What would you like to delete?");
            System.out.println("1. Budget");
            System.out.println("2. Goals");
            System.out.println("3. Goal Contribution");
            System.out.println("4. Expense Bookmark");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch(choice){
                case 1:
                    showUserBudget(user_id);

                    System.out.println("Enter the budget id to delete : ");
                    int budgetId=scanner.nextInt();
                    scanner.nextLine();

                    DeleteBudget(budgetId);

                    break;
                case 2:
                    showUserGoals(user_id);

                    System.out.println("Enter goal id to delete : ");
                    int goalId=scanner.nextInt();
                    scanner.nextLine();

                    deleteGoal(goalId);
                    break;
                case 3:
                    showGoalContribution(user_id);

                    System.out.println("Enter the goal contribution id to delete : ");
                    int goal_contribution_id=scanner.nextInt();
                    scanner.nextLine();

                    deleteGoalContribution(goal_contribution_id);
                    break;
                case 4:
                    showExpenseBookmark(user_id);
                    System.out.println("Enter the goal contribution id to delete : ");
                    int expenseBookmarkID=scanner.nextInt();
                    scanner.nextLine();
                    removeExpenseBookmark(expenseBookmarkID);

                    break;

            }
        }else if("view".equalsIgnoreCase(action)){
            boolean ch=true;

            while(ch){
            System.out.println("--------------------------------------------------------");
            System.out.println(" What would you like to view? ");
            System.out.println("1. View expenses between two dates");
            System.out.println("2. View daily expenses between two dates");
            System.out.println("3. View monthly expenses by category between two dates");
            System.out.println("4. View expenses for a specific category between two dates");

            System.out.println("5. View incomes between two dates");
            System.out.println("6. View monthly incomes by category between two dates");
            System.out.println("7. View incomes for specific category between two dates ");

            System.out.println("8. View budget status or result ");

            System.out.println("9. View expense percentages by category between two dates");

            System.out.println("10. View income percentages by category between two dates");

            System.out.println("11. View Expense Bookmark ");
            System.out.println("12. View account details ");
            System.out.println("13. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            switch (choice){
                case 1:
                    System.out.println("Enter a range of date(yyyy-MM-dd) from :");
                    java.sql.Date start_date = java.sql.Date.valueOf(scanner.next());

                    System.out.println("to date(yyyy-MM-dd) of :");
                    java.sql.Date end_date = java.sql.Date.valueOf(scanner.next());

                    ShowExpensesFromandTo(user_id, start_date, end_date);
                    break;
                case 2:
                    System.out.println("Enter a range of date(yyyy-MM-dd) from :");
                    start_date = java.sql.Date.valueOf(scanner.next());

                    System.out.println("to date(yyyy-MM-dd) of :");
                    end_date = java.sql.Date.valueOf(scanner.next());

                    SumDailyExpenseFromandTo(user_id, start_date, end_date);
                    break;
                case 3:
                    System.out.println("Enter a range of date(yyyy-MM-dd) from :");
                    start_date = java.sql.Date.valueOf(scanner.next());

                    System.out.println("to date(yyyy-MM-dd) of :");
                    end_date = java.sql.Date.valueOf(scanner.next());

                    ShowExpenseCategoryFromandTo(user_id, start_date, end_date);
                    break;
                case 4:
                    showUserExpenseCategories(user_id);
                    System.out.println("Enter the expense category Id: ");
                    int expense_cat_id=scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Enter a range of date(yyyy-MM-dd) from :");
                    start_date = java.sql.Date.valueOf(scanner.next());

                    System.out.println("to date(yyyy-MM-dd) of :");
                    end_date = java.sql.Date.valueOf(scanner.next());
                    ShowExpensesforCategory(expense_cat_id,start_date,end_date);
                    break;
                case 5:
                    System.out.println("Enter a range of date(yyyy-MM-dd) from :");
                    start_date = java.sql.Date.valueOf(scanner.next());

                    System.out.println("to date(yyyy-MM-dd) of :");
                    end_date = java.sql.Date.valueOf(scanner.next());

                    ShowIncomesFromandTo(user_id, start_date, end_date);
                    break;
                case 6:
                    System.out.println("Enter a range of date(yyyy-MM-dd) from :");
                    start_date = java.sql.Date.valueOf(scanner.next());

                    System.out.println("to date(yyyy-MM-dd) of :");
                    end_date = java.sql.Date.valueOf(scanner.next());

                    ShowIncomeCategoryFromandTo(user_id, start_date, end_date);
                    break;
                case 7:
                    showIncomeCategory(user_id);
                    System.out.println("Enter the expense category Id: ");
                    int income_cat_id=scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("Enter a range of date(yyyy-MM-dd) from :");
                    start_date = java.sql.Date.valueOf(scanner.next());

                    System.out.println("to date(yyyy-MM-dd) of :");
                    end_date = java.sql.Date.valueOf(scanner.next());

                    ShowIncomesforCategory(income_cat_id, start_date, end_date);
                    break;
                case 8:

                    System.out.println("Enter a range of date(yyyy-MM-dd) from :");
                    start_date = java.sql.Date.valueOf(scanner.next());

                    System.out.println("to date(yyyy-MM-dd) of :");
                    end_date = java.sql.Date.valueOf(scanner.next());
//                    showUserBudgetFromandTo(user_id,start_date,end_date);
                    ShowBudgetResult(user_id,start_date,end_date);

                    break;
                case 9:
                    System.out.println("Enter a range of date(yyyy-MM-dd) from :");
                    start_date = java.sql.Date.valueOf(scanner.next());

                    System.out.println("to date(yyyy-MM-dd) of :");
                    end_date = java.sql.Date.valueOf(scanner.next());

                    ShowExpensePercentageFromandTo(user_id,start_date,end_date);
                    break;
                case 10:
                    System.out.println("Enter a range of date(yyyy-MM-dd) from :");
                    start_date = java.sql.Date.valueOf(scanner.next());

                    System.out.println("to date(yyyy-MM-dd) of :");
                    end_date = java.sql.Date.valueOf(scanner.next());

                    ShowIncomePercentageFromandTo(user_id,start_date,end_date);
                    break;
                case 11:
                    showExpenseBookmark(user_id);
                    break;
                case 12:
                    showUserAccounts(user_id);
                    break;
                case 13:
                    ch=false;
                    System.out.println("Exited..");
                    break;
            }
        }
        }

        else {
            System.out.println("Invalid action entered.");
        }
    }


    // Implementations of add and update methods for various functionalities
    public static void updateUserDetails(Scanner scanner,int user_id) throws SQLException {
        UserService userService=new UserService();
        Users user = new Users();
        System.out.println("Enter your name: ");
        user.user_name = scanner.nextLine();
        scanner.nextLine();
        System.out.println("Enter your email: ");
        user.email_id = scanner.nextLine();
        scanner.nextLine();
        System.out.println("Enter your password: ");
        user.password = scanner.nextLine();
        scanner.nextLine();

        user.updatedOn = new Date();
        System.out.println("Enter your currencyId: ");
        user.currency=scanner.nextInt();
        scanner.nextLine();
        user.user_id=user_id;
        userService.updateUser(user);
    }

    public static void addExpenses(Scanner scanner,int user_id) {
        try {
            ExpenseService expenseService = new ExpenseService();

            System.out.println("Enter expense category name: ");
            String categoryName = scanner.next();
            scanner.nextLine();

            System.out.println("Enter amount: ");
            double amount = scanner.nextDouble();

            System.out.println("Enter date (yyyy-MM-dd): ");
            Date date = java.sql.Date.valueOf(scanner.next());

            int categoryId = getExpenseCategoryId(categoryName, user_id);

            showUserAccounts(user_id);
            System.out.println("enter account ID:");
            int accountid=scanner.nextInt();
            System.out.println("Enter note: ");
            String note = scanner.nextLine();
            scanner.nextLine();

            System.out.println("Enter description: ");
            String desp = scanner.nextLine();
            scanner.nextLine();

            Expenses expense = new Expenses();
            expense.user_id = user_id;
            expense.amount = amount;
            expense.expense_category_id = categoryId;
            expense.date = (java.sql.Date) date;
            expense.account_id = accountid;
            expense.note = note;
            expense.description = desp;
            expense.createdOn = new Timestamp(System.currentTimeMillis());
            expense.updatedOn = new Timestamp(System.currentTimeMillis());

            expenseService.addExpense(expense);
            RemoveAmount(expense.account_id,expense.user_id,expense.amount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateExpenses(Scanner scanner,int user_id) {
        try {
            ExpenseService expenseService = new ExpenseService();


            showExpenses(user_id);

            System.out.println("Enter expense ID to update: ");
            int expenseId = scanner.nextInt();
            scanner.nextLine(); // consume newline

            System.out.println("Enter expense category name: ");
            String categoryName = scanner.next();
            scanner.nextLine();

            System.out.println("Enter  new amount: ");
            double amount = scanner.nextDouble();

            System.out.println("Enter new date (yyyy-MM-dd): ");
            Date date = java.sql.Date.valueOf(scanner.next());

            int categoryId = getExpenseCategoryId(categoryName, user_id);

//            System.out.println("Enter account type: ");
//            String account_type = scanner.nextLine();
//
//            scanner.nextLine();
//            account_type.trim();
//            int accountid = getAccountId(account_type, user_id);
            showUserAccounts(user_id);
            System.out.println("enter new account ID:");
            int accountid=scanner.nextInt();
            scanner.nextLine();
            System.out.println("Enter new note: ");
            String note = scanner.nextLine();
            scanner.nextLine();

            System.out.println("Enter new description: ");
            String desp = scanner.nextLine();
            scanner.nextLine();

            Expenses expense = new Expenses();
            expense.expense_id=expenseId;
            expense.user_id = user_id;
            expense.amount = amount;
            expense.expense_category_id = categoryId;
            expense.date = (java.sql.Date) date;
            expense.account_id = accountid;
            expense.note = note;
            expense.description = desp;
            expense.updatedOn = new Timestamp(System.currentTimeMillis());

            expenseService.updateExpense(expense);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addIncomes(Scanner scanner,int user_id) {
        try {
            IncomeService incomeService = new IncomeService();
//            System.out.println("Enter user id: ");
//            int user_id = scanner.nextInt();
            showIncomeCategory(user_id);
            System.out.println("Enter income category id: ");
            int id = scanner.nextInt();

            System.out.println("Enter amount: ");
            double amount = scanner.nextDouble();
            scanner.nextLine();

            System.out.println("Enter date (yyyy-MM-dd): ");
            Date date = java.sql.Date.valueOf(scanner.nextLine());

            System.out.println("Enter account type: ");
            String account_type = scanner.nextLine();
            scanner.nextLine();

            int accountid = getAccountId(account_type, user_id);
            System.out.println("Enter description: ");
            String desp = scanner.nextLine();
            scanner.nextLine();

            System.out.println("Enter note: ");
            String note = scanner.nextLine();
            scanner.nextLine();

            Incomes income = new Incomes();
            income.user_id=user_id;
            income.amount = amount;
            income.date = (java.sql.Date) date;
            income.income_category_id = id;
            income.description = desp;
            income.note = note;
            income.account_id = accountid;
            income.createdOn=new Timestamp(System.currentTimeMillis());
            income.updatedOn = new Timestamp(System.currentTimeMillis());
            incomeService.addIncome(income);
            addAmount(income.account_id,income.user_id,income.amount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateIncomes(Scanner scanner,int user_id) {
        try {
            IncomeService incomeService = new IncomeService();
//            System.out.println("Enter user ID to update: ");
//            int user_id = scanner.nextInt();
            showIncomeofUser(user_id);
            System.out.println("Enter income ID to update: ");
            int incomeId = scanner.nextInt();
            scanner.nextLine(); // consume newline

            showIncomeCategory(user_id);
            System.out.println("Enter new income category id: ");
            int id = scanner.nextInt();

            System.out.println("Enter new amount: ");
            double amount = scanner.nextDouble();
            scanner.nextLine();

            System.out.println("Enter date (yyyy-MM-dd): ");
            Date date = java.sql.Date.valueOf(scanner.nextLine());

            System.out.println("Enter account type: ");
            String account_type = scanner.nextLine();
            scanner.nextLine();

            int accountid = getAccountId(account_type, user_id);
            System.out.println("Enter description: ");
            String desp = scanner.nextLine();
            scanner.nextLine();

            System.out.println("Enter note: ");
            String note = scanner.nextLine();
            scanner.nextLine();

            Incomes income = new Incomes();
            income.income_id=incomeId;
            income.user_id=user_id;
            income.amount = amount;
            income.date = (java.sql.Date) date;
            income.income_category_id = id;
            income.description = desp;
            income.note = note;
            income.account_id = accountid;
            income.updatedOn = new Timestamp(System.currentTimeMillis());

            incomeService.updateIncome(income);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

       public static void addExpenseCategory(Scanner scanner,int user_id) {
           try {
               ExpenseCategoryService categoryService = new ExpenseCategoryService();
//               System.out.println("Enter user ID: ");
//               int user_id = scanner.nextInt();
//               scanner.nextLine();
               System.out.println("Enter expense category name: ");
               String name = scanner.nextLine();
               scanner.nextLine();

               System.out.println("Enter description: ");
               String description = scanner.nextLine();
               scanner.nextLine();

               ExpenseCategory category = new ExpenseCategory();
               category.category_name = name;
               category.user_id = user_id;
               category.description = description;
               category.createdOn = new Timestamp(System.currentTimeMillis());
               category.updatedOn = new Timestamp(System.currentTimeMillis());
               categoryService.addExpenseCategory(category);
           } catch (SQLException e) {
               e.printStackTrace();
           }
       }

    public static void updateExpenseCategory(Scanner scanner,int user_id) {
        try {
            ExpenseCategoryService categoryService = new ExpenseCategoryService();
//            System.out.println("Enter user ID: ");
//            int user_id = scanner.nextInt();
//            scanner.nextLine();
            showUserExpenseCategories(user_id);
            System.out.println("Enter category ID to update: ");
            int categoryId = scanner.nextInt();
            scanner.nextLine(); // consume newline

            System.out.println("Enter new category name: ");
            String name = scanner.nextLine();
            scanner.nextLine();

            System.out.println("Enter new description: ");
            String description = scanner.nextLine();
            scanner.nextLine();

            ExpenseCategory category = new ExpenseCategory();
            category.user_id=user_id;
            category.expense_category_id = categoryId;
            category.category_name = name;
            category.description = description;
            category.updatedOn = new Timestamp(System.currentTimeMillis());

            categoryService.updateExpenseCategory(category);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addIncomeCategory(Scanner scanner,int user_id) {
        try {
            IncomeCategoryService categoryService = new IncomeCategoryService();
//            System.out.println("Enter user ID: ");
//            int user_id = scanner.nextInt();
//            scanner.nextLine();

            System.out.println("Enter category name: ");
            String name = scanner.nextLine();
            scanner.nextLine();
            System.out.println("Enter description: ");
            String description = scanner.nextLine();

            IncomeCategory category = new IncomeCategory();
            category.user_id=user_id;
            category.category_name = name;
            category.description = description;
            category.createdOn = new Timestamp(System.currentTimeMillis());
            category.updatedOn = new Timestamp(System.currentTimeMillis());

            categoryService.addIncomeCategory(category);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateIncomeCategory(Scanner scanner,int user_id) {
        try {
            IncomeCategoryService categoryService = new IncomeCategoryService();
//            System.out.println("Enter user ID: ");
//            int user_id = scanner.nextInt();
//            scanner.nextLine();

            showIncomeCategory(user_id);
            System.out.println("Enter category ID to update: ");
            int categoryId = scanner.nextInt();
            scanner.nextLine(); // consume newline
            System.out.println("Enter new category name: ");
            String name = scanner.nextLine();
            System.out.println("Enter new description: ");
            String description = scanner.nextLine();

            IncomeCategory category = new IncomeCategory();
            category.income_category_id = categoryId;
            category.category_name = name;
            category.description = description;
            category.user_id=user_id;
            category.updatedOn = new Timestamp(System.currentTimeMillis());
            categoryService.updateIncomeCategory(category);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addAccounts(Scanner scanner,int user_id) {
        try {
            AccountService accountService = new AccountService();
//            System.out.println("Enter user ID: ");
//            int user_id = scanner.nextInt();
//            scanner.nextLine();

            System.out.println("Enter account type: ");
            String type = scanner.nextLine();
            System.out.println("Enter new balance: ");
            double balance = scanner.nextDouble();
            scanner.nextLine(); // consume newline


            Accounts account = new Accounts();
            account.account_type = type;
            account.balance=balance;
            account.user_id = user_id;
            account.createdOn = new Timestamp(System.currentTimeMillis());
            account.updatedOn = new Timestamp(System.currentTimeMillis());

            accountService.addAccount(account);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateAccounts(Scanner scanner,int user_id) {
        try {
            AccountService accountService = new AccountService();
//            System.out.println("Enter user ID: ");
//            int user_id = scanner.nextInt();
//            scanner.nextLine();
            showUserAccounts(user_id);
            System.out.println("Enter account ID to update: ");
            int accountId = scanner.nextInt();
            scanner.nextLine(); // consume newline

            System.out.println("Enter new account type: ");
            String type = scanner.nextLine();
            System.out.println("Enter new balance: ");
            double balance = scanner.nextDouble();
            scanner.nextLine(); // consume newline

            Accounts account = new Accounts();
            account.account_id = accountId;
            account.account_type = type;
            account.user_id=user_id;
            account.balance=balance;
            account.updatedOn = new Timestamp(System.currentTimeMillis());

            accountService.updateAccount(account);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addBudget(Scanner scanner,int user_id) {
        try {
            BudgetSettingService budgetService = new BudgetSettingService();
//            System.out.println("Enter user ID: ");
//            int user_id = scanner.nextInt();
//            scanner.nextLine();

            showUserExpenseCategories(user_id);
            char str='y';
            while(str!='n'){
            System.out.println("Enter expense category id: ");
            int e_category_id = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Enter amount to set budget for the category : ");
            double amount = scanner.nextDouble();
            scanner.nextLine();
            System.out.println("Enter start date of your budget (yyyy-MM-dd): ");
            Date startDate = java.sql.Date.valueOf(scanner.next());
            System.out.println("Enter end date of your budget (yyyy-MM-dd): ");
            Date endDate = java.sql.Date.valueOf(scanner.next());

            BudgetSetting budget = new BudgetSetting();
            budget.expense_category_id = e_category_id;
            budget.budget_amount = amount;
            budget.user_id=user_id;
            budget.start_date = (java.sql.Date) startDate;
            budget.end_date = (java.sql.Date) endDate;
            budget.createdOn = new Timestamp(System.currentTimeMillis());
            budget.updatedOn = new Timestamp(System.currentTimeMillis());


            budgetService.addBudgetSetting(budget);
            System.out.println("Do you like to add another(y/n) ");
            str=scanner.next().charAt(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateBudget(Scanner scanner,int user_id) {
        try {
            BudgetSettingService budgetService = new BudgetSettingService();
//            System.out.println("Enter user ID: ");
//            int user_id = scanner.nextInt();
//            scanner.nextLine();

            showUserBudget(user_id);
            System.out.println("Enter budget ID to update: ");
            int budgetId = scanner.nextInt();
            scanner.nextLine(); // consume newline

            showUserExpenseCategories(user_id);

            System.out.println("Enter expense category id: ");
            int e_category_id = scanner.nextInt();

            System.out.println("Enter new amount: ");
            double amount = scanner.nextDouble();
            System.out.println("Enter new start date (yyyy-MM-dd): ");
            Date startDate = java.sql.Date.valueOf(scanner.next());
            System.out.println("Enter new end date (yyyy-MM-dd): ");
            Date endDate = java.sql.Date.valueOf(scanner.next());

            BudgetSetting budget = new BudgetSetting();
            budget.budget_setting_id=budgetId;
            budget.expense_category_id = e_category_id;
            budget.budget_amount = amount;
            budget.user_id=user_id;
            budget.start_date = (java.sql.Date) startDate;
            budget.end_date = (java.sql.Date) endDate;
            budget.updatedOn = new Timestamp(System.currentTimeMillis());

            budgetService.updateBudgetSetting(budget);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addExpenseBookmark(Scanner scanner,int user_id) {
        try {
            ExpenseBookmarkService bookmarkService = new ExpenseBookmarkService();
//            System.out.println("Enter user ID: ");
//            int user_id = scanner.nextInt();
//            scanner.nextLine();

            showUserExpenseCategories(user_id);
            System.out.println("Enter expense category id: ");
            int expense_cat_id = scanner.nextInt();
            scanner.nextLine();

            ExpenseBookmark bookmark = new ExpenseBookmark();
            bookmark.user_id = user_id;
            bookmark.expense_category_id =expense_cat_id;
            bookmark.createdOn= new Timestamp(System.currentTimeMillis());

            bookmarkService.addExpenseBookmark(bookmark);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addGoals(Scanner scanner,int user_id) {
        try {
            GoalService goalsService = new GoalService();
//            System.out.println("Enter user ID: ");
//            int user_id = scanner.nextInt();
//            scanner.nextLine();

            System.out.println("Enter goal name: ");
            String name = scanner.nextLine();
            scanner.nextLine();
            System.out.println("Enter goal description: ");
            String description = scanner.nextLine();
            scanner.nextLine();
            System.out.println("Enter target amount: ");
            double targetAmount = scanner.nextDouble();
            scanner.nextLine();
            System.out.println("Enter current amount: ");
            double currentAmount = scanner.nextDouble();
            scanner.nextLine();
            System.out.println("Enter target date (yyyy-MM-dd): ");
            Date targetDate = java.sql.Date.valueOf(scanner.next());
            scanner.nextLine(); // consume newline
            System.out.println("Enter goal status(In Progress/Completed): ");
            String status = scanner.nextLine();

            Goals goal = new Goals();
            goal.user_id=user_id;
            goal.description = description;
            goal.goal_amount = targetAmount;
            goal.current_amount = currentAmount;
            goal.goal_name=name;
            goal.target_date= (java.sql.Date) targetDate;
            goal.status=status;
            goal.createdOn = new Timestamp(System.currentTimeMillis());
            goal.updatedOn = new Timestamp(System.currentTimeMillis());

            goalsService.addGoal(goal);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateGoals(Scanner scanner,int user_id) {
        try {
            GoalService goalsService = new GoalService();
//            System.out.println("Enter user ID: ");
//            int user_id = scanner.nextInt();
//            scanner.nextLine();

            showUserGoals(user_id);
            System.out.println("Enter goal ID to update: ");
            int goalId = scanner.nextInt();
            scanner.nextLine(); // consume newline
            System.out.println("Enter goal name: ");
            String name = scanner.nextLine();
            scanner.nextLine();
            System.out.println("Enter goal description: ");
            String description = scanner.nextLine();
            scanner.nextLine();
            System.out.println("Enter target amount: ");
            double targetAmount = scanner.nextDouble();
            scanner.nextLine();
            System.out.println("Enter current amount: ");
            double currentAmount = scanner.nextDouble();
            scanner.nextLine();
            System.out.println("Enter target date (yyyy-MM-dd): ");
            Date targetDate = java.sql.Date.valueOf(scanner.next());
            scanner.nextLine(); // consume newline
            System.out.println("Enter goal status(In Progress/Completed): ");
            String status = scanner.nextLine();

            Goals goal = new Goals();
            goal.description = description;
            goal.goal_amount = targetAmount;
            goal.current_amount = currentAmount;
            goal.goal_name=name;
            goal.target_date= (java.sql.Date) targetDate;
            goal.status=status;
            goal.updatedOn = new Timestamp(System.currentTimeMillis());
            goal.goal_id = goalId;
            goal.user_id=user_id;


            goalsService.updateGoal(goal);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addGoalContribution(Scanner scanner,int user_id) {
        try {
            GoalContributionService contributionService = new GoalContributionService();
//            System.out.println("Enter user ID: ");
//            int user_id = scanner.nextInt();
//            scanner.nextLine();

            showUserGoals(user_id);
            System.out.println("Enter goal ID: ");
            int goalId = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Enter contribution amount: ");
            double amount = scanner.nextDouble();
            scanner.nextLine();
            System.out.println("Enter contribution date (yyyy-MM-dd): ");
            Date date = java.sql.Date.valueOf(scanner.next());
//            scanner.nextLine();
            showUserAccounts(user_id);

            System.out.println("Enter account ID: ");
            int accountId = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Enter notes for the goal:");
            String note=scanner.nextLine();


            GoalContribution contribution = new GoalContribution();
            contribution.goal_id = goalId;
            contribution.amount_contribution = amount;
            contribution.date = (java.sql.Date) date;
            contribution.account_id=accountId;
            contribution.note=note;
            contribution.createdOn = new Timestamp(System.currentTimeMillis());
            contribution.updatedOn = new Timestamp(System.currentTimeMillis());

            contributionService.addGoalContribution(contribution);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateGoalContribution(Scanner scanner,int user_id) {
        try {
            GoalContributionService contributionService = new GoalContributionService();
//            System.out.println("Enter user ID: ");
//            int user_id = scanner.nextInt();
//            scanner.nextLine();

            showGoalContribution(user_id);

            System.out.println("Enter contribution ID to update: ");
            int contributionId = scanner.nextInt();
            scanner.nextLine(); // consume newline

            System.out.println("Enter new contribution amount: ");
            double amount = scanner.nextDouble();
            System.out.println("Enter new contribution date (yyyy-MM-dd): ");
            Date date = java.sql.Date.valueOf(scanner.next());
            scanner.nextLine(); // consume newline
            showUserAccounts(user_id);
            System.out.println("Enter account id :");
            int account_id=scanner.nextInt();
            scanner.nextLine();
            System.out.println("Enter note :");
            String note=scanner.nextLine();
            scanner.nextLine();

            GoalContribution contribution = new GoalContribution();
            contribution.account_id=account_id;
            contribution.note=note;
            contribution.goal_contribution_id = contributionId;
            contribution.amount_contribution = amount;
            contribution.date = (java.sql.Date) date;
            contribution.updatedOn=new Timestamp(System.currentTimeMillis());

            contributionService.updateGoalContribution(contribution);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}