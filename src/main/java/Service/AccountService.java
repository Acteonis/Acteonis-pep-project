package Service;

import Model.Account;
import DAO.AccountDAO;

import java.sql.SQLException;
import java.util.Optional;

public class AccountService{
        public AccountDAO accountDAO;

        //no-args constructor;
        public AccountService(){
                accountDAO = new AccountDAO();
        }
        //Constructor for a AccountService when a AccountDAO is provided.
        public AccountService(AccountDAO accountDAO){
                this.accountDAO=accountDAO;
        }
        //get user
        public static Account get_User(int id){
                return AccountDAO.Get_User_byID(id);
        }



        //Review the method of checking existing account
        public Optional<Account> findByUsername(String username) throws SQLException {
                return accountDAO.findByUsername(username);
        }
        public Account findByUsernameAndPassword(String username, String password) throws SQLException {
                return accountDAO.findByUsernameAndPassword(username, password);
        }

        public Account create(Account account) throws SQLException {
                Optional<Account> existingAccount = findByUsername(account.getUsername());
                if (existingAccount.isPresent() && account.getUsername().isEmpty() && account.getPassword().length() < 4) {
                        return null;
                }

                return accountDAO.create(account);
        }
        /*public Account add_Account(Account account){
                if(account.getAccount_id()==0 && account.getUsername()!=" " && account.getPassword().length()>4){
                        return AccountDAO.insert_Account(account);
                }
                else {return null;}
        }*/

        public Account login(String username, String password) throws SQLException {
                Account account = accountDAO.findByUsernameAndPassword(username, password);
                if (account == null) {
                        return null;
                }
                return account;
        }
       /* public Account check_user_Login(String username, String password){
                Account account = new Account();//?! Will it work?!
                if(account.getUsername().equals(username) && account.getPassword().equals(password)){
                        return AccountDAO.user_Login(username, password);
                }
                else return null;
        }*/
}
