package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;

public class AccountDAO{




    public static Account Get_User_byID(int id) {
        Connection connection = ConnectionUtil.getConnection();
        //String user = ""; //what should be placed here?
        try {
            String sql = "select * from account where account_id=?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                return account;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

        public static Account insert_Account(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "insert into account (account_id,username,password) values(?,?,?);";
            PreparedStatement ps=connection.prepareStatement(sql); //Do I need here generated keys?
            ps.setInt(1,account.getAccount_id());//Here was used code from Account Model;
            ps.setString(2,account.getUsername());//Here was used code from Account Model;
            ps.setString(3,account.getPassword());//Here was used code from Account Model;
            ps.executeUpdate();//???
            ResultSet pkeyResultSet = ps.getGeneratedKeys();
            //Do we need generate account_id?
            if(pkeyResultSet.next()){//If account doesn't exsists then we must generate new account_id and return new account
                int generated_account_id = (int) pkeyResultSet.getLong(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
/*As a user, I should be able to verify my login on the endpoint POST localhost:8080/login.
The request body will contain a JSON representation of an Account, not containing an account_id.
In the future, this action may generate a Session token to allow the user to securely use the site. We will not worry about this for now.

The login will be successful if and only if the username and password provided
 in the request body JSON match a real account existing on the database.
 If successful, the response body should contain a JSON of the account in the response body,
  including its account_id. The response status should be 200 OK, which is the default.
If the login is not successful, the response status should be 401. (Unauthorized)*/
    public static Account user_Login(String username, String password){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql ="Select * from account where username=? and password =?;";
            PreparedStatement ps=connection.prepareStatement(sql);
            ps.setString(1,username);
            ps.setString(2,password);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            }

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return account;
    }



}



