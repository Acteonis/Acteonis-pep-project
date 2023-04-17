package Service;

import Model.Account;
import DAO.AccountDAO;

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
        public Account add_Account(Account account){
                if(account.getAccount_id()==0 && account.getUsername()!=" " && account.getPassword().length()>4){
                        return AccountDAO.insert_Account(account);
                }
                else {return null;}
        }

        public Account check_user_Login(String username, String password){
                Account account = null;//?! Will it work?!
                if(account.getUsername().equals(username) && account.getPassword().equals(password)){
                        return AccountDAO.user_Login(username, password);
                }
                else return null;
        }
}
