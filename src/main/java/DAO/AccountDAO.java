package DAO;

import Model.Account;
import Util.ConnectionUtil;
import java.util.Optional;
import java.sql.*;

public class AccountDAO{

/*As a user, I should be able to create a new Account on the endpoint POST localhost:8080/register.
     The body will contain a representation of a JSON Account, but will not contain an account_id.

    The registration will be successful if and only if the username is not blank, the password is at least 4 characters long,
    and an Account with that username does not already exist. If all these conditions are met,
    !!!!!!!!!!!the response body should contain a JSON of the Account, including its account_id.!!!!!!!!!!

    The response status should be 200 OK, which is the default. The new account should be persisted to the database.
    If the registration is not successful, the response status should be 400. (Client error)*/

    //Optional is a container class which can holds only ine object and wors with null values correctly.
    // It helps to handle with NullPointerException

    /*public Account addAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO account (username, password) VALUES (?,?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());

            ps.executeUpdate();

            ResultSet pkeyResultSet = ps.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_account_id = (int) pkeyResultSet.getInt(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }*/

    public Optional<Account> findByUsername(String username) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM account WHERE username = ?")) {
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(new Account(
                        // .of() method is used to get an instance of the Optional class with the specified not-null value.
                        rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password")
                ));
            }
            return Optional.empty();
            //this method is used to get instance of Optional class
            //The returned object doesn’t have any value.
        }
    }

    /*public Account addAccount(Account account) throws SQLException {
        String query = "INSERT INTO account (username, password) VALUES (?, ?)";
        Connection conn = ConnectionUtil.getConnection();
        PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, account.getUsername());
        ps.setString(2, account.getPassword());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            account.setAccount_id(rs.getInt(1));
        }
        return account;
    }*/


    public Account addAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {

            String sql = "INSERT INTO account (username, password) VALUES (?,?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            preparedStatement.executeUpdate();

            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_account_id = (int) pkeyResultSet.getInt(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }


    /*public Account create(Account account) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO account (username, password) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, account.getUsername());
            statement.setString(2, account.getPassword());
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                account.setAccount_id(rs.getInt(1));
                return account;
            }
            throw new SQLException("Could not retrieve generated ID for Account");
        }
    }*/


    public static Account Get_User_byID(int id) {
        Connection connection = ConnectionUtil.getConnection();

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

       /* public static Account insert_Account(Account account) {
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
    }*/
/*As a user, I should be able to verify my login on the endpoint POST localhost:8080/login.
The request body will contain a JSON representation of an Account, not containing an account_id.
In the future, this action may generate a Session token to allow the user to securely use the site. We will not worry about this for now.

The login will be successful if and only if the username and password provided
 in the request body JSON match a real account existing on the database.
 If successful, the response body should contain a JSON of the account in the response body,
  including its account_id. The response status should be 200 OK, which is the default.
If the login is not successful, the response status should be 401. (Unauthorized)*/

    public Account findByUsernameAndPassword(String username, String password) throws SQLException {
        String sql = "SELECT * FROM account WHERE username=? AND password=?";
        try (Connection connection = ConnectionUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int accountId = rs.getInt("account_id");
                String GetUsername = rs.getString("username");
                String GetPassword = rs.getString("password");
                return new Account(accountId, GetUsername, GetPassword);
            }
        }
        return null;
    }


    public Account accountLogin(String username, String password)  {
        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "SELECT * FROM account WHERE username=? AND password=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()) {
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"),rs.getString("password"));
                return account;
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }



    /*public static Account user_Login(String username, String password){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql ="Select * from account where username=? and password =?;";
            PreparedStatement ps=connection.prepareStatement(sql);
            ps.setString(1,username);
            ps.setString(2,password);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                return account;
            }

        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }*/



}



