package Service;

import Model.Account;
import DAO.userDAO;

import java.util.List;

public class userService {
    private userDAO userDAO;

    
public userService() {
    this.userDAO = new userDAO(); 
}

public Account login(String username, String password) {
    return userDAO.getUserByUsernameAndPassword(username, password);
}

public Account register(Account account) {
    return userDAO.insertUser(account);
}

public Account getUserById(int id) {
    return userDAO.getUserById(id);
}



}
