package banking.service;

import banking.entity.User;
import banking.repository.UserRepository;

import java.util.List;
import java.util.Map;

public class UserService {

    private UserRepository userRepository = new UserRepository();
    public void printUser(){
        userRepository.printUser();
    }

    public User login(String userName, String password){
        return userRepository.login(userName,password);
    }

    public boolean addNewCustomer(String userName, String password , String contactNumber){
       return userRepository.addNewCustomer(userName,password,contactNumber);
    }

    public Double checkBankBalance(String userId){
        return userRepository.checkBankBalance(userId);
    }

    public User getUser(String userId){
        return userRepository.getUser(userId);
    }

    public boolean transferAmount(String userId, String payeeUserId, Double amount){
        return userRepository.transferAmount(userId,payeeUserId,amount);
    }

    public void printTransactions(String userId){
        userRepository.printTransactions(userId);
    }

    public void raisedCheckBookRequest(String userId){
        userRepository.raisedCheckBookRequest(userId);
    }
    public Map<String, Boolean> getAllChequeBookRequest(){
        return userRepository.getAllChequeBookRequest();
    }
    public List<String> getUserIdForCheckBookRequest(){
            return userRepository.getUserIdForCheckBookRequest();
    }
    public void approveChequeBookRequest(String userId){
        userRepository.approveChequeBookRequest(userId);
    }
}

