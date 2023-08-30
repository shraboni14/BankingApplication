package banking.main;

import banking.entity.User;
import banking.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    static Main main = new Main();
    static UserService userService = new UserService();
    public static void main(String[] args) {
        while (true) {
            System.out.println("Enter UserName : ");
            String userName = scanner.nextLine();

            System.out.println("Enter Password : ");
            String password = scanner.nextLine();

            User user = userService.login(userName, password);

            if (user != null && user.getRole().equals("admin")) {
                main.initAdmin();
            } else if (user != null && user.getRole().equals("user")) {
                main.initCustomer(user);
            } else
                System.out.println("Login Failed");
        }
    }

    private void initAdmin(){

        boolean flag = true;
        String userId = "";
        while (flag) {
            System.out.println("You are admin");
            System.out.println("1 . Exit/Logout");
            System.out.println("2 . Create a customer Account");
            System.out.println("3 . See all transaction");
            System.out.println("4 . Check Bank Balance");
            System.out.println("5 . Approve CheckBook Request");
            int selectedOption = scanner.nextInt();
            scanner.nextLine();

            switch (selectedOption) {
                case 1:
                    flag=false;
                    System.out.println("Successfully logged out");
                    break;

                case 2:
                    main.addNewCustomer();
                    System.out.println("Add new customer");
                    break;
                case 3:
                    System.out.println("Enter User Id: ");
                     userId = scanner.nextLine();
                    printTransactions(userId);
                    break;

                case 4:
                    System.out.println("Enter user Id : ");
                     userId = scanner.nextLine();
                     Double accountBalance = checkBankBalance(userId);
                    System.out.println("Your Account balance is : " + accountBalance);
                    break;

                case 5:
                    List<String> userIds = getUserIdForCheckBookRequest();
                    System.out.println("Please select user Id from Below : ");
                    System.out.println(userIds);

                     userId = scanner.next();
                     approveChequeBookRequest(userId);

                    System.out.println("Checkbook Request is approved");

                    break;
                default:
                    System.out.println("wrong choice");
            }
        }
    }

    private void approveChequeBookRequest(String userId){
        userService.approveChequeBookRequest(userId);
    }

    private List<String> getUserIdForCheckBookRequest(){
        return userService.getUserIdForCheckBookRequest();
    }

    public void addNewCustomer(){
        System.out.println("Enter Username");
        String userName = scanner.nextLine();

        System.out.println("Enter Password");
        String password = scanner.nextLine();

        System.out.println("Enter contact number : ");
        String contactNumber = scanner.nextLine();

        boolean result = userService.addNewCustomer(userName,password,contactNumber);

        if (result){
            System.out.println("Customer account is created");
        }else {
            System.out.println("Customer acc creation faild");
        }
    }

    private void initCustomer(User user) {
        boolean flag = true;

        while (flag) {
            System.out.println("1. Exit/Logout");
            System.out.println("2. Check Account Balance");
            System.out.println("3. Fund Transfer");
            System.out.println("4. See all Transaction ");
            System.out.println("5. Raise CheckBook Request ");

            int selectedOption = scanner.nextInt();
            scanner.nextLine();
            switch (selectedOption) {
                case 1:
                    flag=false;
                    System.out.println("Successfully logged out");
                    break;

                case 2:
                    Double balance = main.checkBankBalance(user.getUserName());
                    if (balance!=null){
                        System.out.println("Your bank balance is "+ balance);
                    }else
                    {
                        System.out.println("Check your UserName and Password");
                    }
                    break;
                case 3:
                    main.fundTransfer(user);
                    break;

                case 4:
                    main.printTransactions(user.getUserName());
                    break;
                case 5:
                    String userId = user.getUserName();
                    Map<String,Boolean> map = getAllChequeBookRequest();

                    if (map.containsKey(userId) && map.get(user.getUserName())){
                        System.out.println("Your have already raised a request and it is already approved");
                    } else if (map.containsKey(userId) && !map.get(userId)) {
                        System.out.println("You have already Raised a request and pending for approval");
                    }else {
                        raisedCheckBookRequest(userId);
                        System.out.println("Request Raised Successfully");
                    }

                    break;
                default:
                    System.out.println("wrong choice");
            }

        }
    }

    private Map<String, Boolean> getAllChequeBookRequest(){
        return userService.getAllChequeBookRequest();
    }

    private void raisedCheckBookRequest(String userId){
        userService.raisedCheckBookRequest(userId);
    }

    private void printTransactions(String userId){
        userService.printTransactions(userId);
    }

    private void fundTransfer(User userDetails){
        System.out.println("Enter Payee account user id: ");
        String payeeAccountId = scanner.nextLine();


        User user = getUser(payeeAccountId);

        if (user != null){
            System.out.println("Enter Amount to Transfer");
            Double amount = scanner.nextDouble();

            Double userAccountBalance = checkBankBalance(userDetails.getUserName());

            if (userAccountBalance >= amount){
                boolean result = userService.transferAmount(userDetails.getUserName(),payeeAccountId,amount);

                if (result){
                    System.out.println("Amount transferred successfully");
                }else {
                    System.out.println("Transfer failed");
                }
            }else {
                System.out.println("Your balance is insufficient" + userAccountBalance);
            }
        }else {
            System.out.println("Please enter valid UserName");
        }
    }

    private User getUser(String userId){
        return userService.getUser(userId);
    }

    private Double checkBankBalance(String userId){
        return userService.checkBankBalance(userId);
    }
}
