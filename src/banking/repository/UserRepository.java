package banking.repository;

import banking.entity.Transaction;
import banking.entity.User;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class UserRepository {

    private static Set<User> users = new HashSet<>();

//    It will store the transaction history
    private static List<Transaction> transactions =  new ArrayList<>();

    Map<String,Boolean> chequeBookRequest = new HashMap<>();

    static {
        User user1 = new User("admin","admin","8697588053","admin",0.0);
        User user2 = new User("user2","user2","8697588456","user",1000.0);
        User user3 = new User("user3","user3","8697345456","user",2000.0);
        User user4 = new User("user4","user4","8697345456","user",2000.0);

        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
    }

    public void approveChequeBookRequest(String userId){
        if (chequeBookRequest.containsKey(userId)){
            chequeBookRequest.put(userId,true);
        }
    }

    public List<String> getUserIdForCheckBookRequest(){
        List<String> userIds = new ArrayList<>();

        for (Map.Entry<String, Boolean> entry : chequeBookRequest.entrySet()){
            if (!entry.getValue()){
                userIds.add(entry.getKey());
            }
        }
        return userIds;
    }

    public void raisedCheckBookRequest(String userId){
            chequeBookRequest.put(userId,false);
    }

    public Map<String,Boolean> getAllChequeBookRequest(){
        return chequeBookRequest;
    }
    public boolean transferAmount(String userId, String payeeUserId, Double amount){

        boolean isDebit = debit(userId,amount,payeeUserId);
        boolean isCredit = credit(payeeUserId,amount,userId);

        return isDebit && isCredit;

    }

    private boolean debit(String userId, Double amount,String payeeUserId){
        User user = getUser(userId);

        Double accountBalance = user.getAccountBalance();

        users.remove(user);

        Double finalBalance = accountBalance - amount;

        user.setAccountBalance(finalBalance);

        Transaction transaction = new Transaction(LocalDate.now(),payeeUserId,amount,"Debit",accountBalance,finalBalance,userId);

        transactions.add(transaction);

        System.out.println(transaction);

        return users.add(user);
    }



    private boolean credit(String payeeUserId, Double amount,String userId){
        User user = getUser(payeeUserId);

        Double accountBalance = user.getAccountBalance();

        users.remove(user);

        Double finalBalance = accountBalance + amount;

        user.setAccountBalance(finalBalance);

        Transaction transaction = new Transaction(LocalDate.now(),userId,amount,"Credit",accountBalance,finalBalance,payeeUserId);

        transactions.add(transaction);

        System.out.println(transaction);

        return users.add(user);
    }

//    Printing
    public void printTransactions(String userId){
            List<Transaction> filteredTransactions = transactions.stream().filter(transaction -> transaction.getTransactionPerformedBy().equals(userId)).collect(Collectors.toList());

        System.out.println("Date\t UserId\t Transaction Amount\t Type\t Initial Balance \t Final Balance");
        System.out.println("-----------------------------------------------------------------------------");
            for (Transaction t : filteredTransactions){
                System.out.println(t.getTransactionDate()+"\t"+t.getTransactionUserId()+"\t"+t.getTransactionAmount()+"\t\t"+t.getTransactionType()+"\t\t"+t.getInitialBalance()+"\t\t"+t.getFinalBalance());
            }
        System.out.println("-----------------------------------------------------------------------------");
    }

    public User getUser(String userId){
        List<User> result  = users.stream().filter(user -> user.getUserName().equals(userId)).collect(Collectors.toList());
        if (!result.isEmpty()){
            return result.get(0);
        }else{
            return null;
        }
    }

    public Double checkBankBalance(String userId){
      List<User> result =  users.stream().filter(user -> user.getUserName().equals(userId)).collect(Collectors.toList());

      if (!result.isEmpty()){
           return result.get(0).getAccountBalance();
      }else {
          return null;
      }
    }

    public void printUser(){
        System.out.println(users);
    }

    public  User login(String UserName, String password){
       List<User> finalList =  users.stream().filter(user -> user.getUserName().equals(user.getUserName()) && user.getPassWord().equals(password)).collect(Collectors.toList());

       if(!finalList.isEmpty()){
           return finalList.get(0);
       }else {
           return null;
       }
    }

    public boolean addNewCustomer(String userName, String password , String contactNumber){
    User user = new User(userName,password,contactNumber,"user",500.0);
    return users.add(user);

    }
}
