package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class MessageDAO {




    /*As a user, I should be able to submit a new post on the endpoint POST localhost:8080/messages.
    The request body will contain a JSON representation of a message, which should be persisted to the database,
    but will not contain a message_id.

    •The creation of the message will be successful if and only if the message_text is not blank,
        is under 255 characters, and posted_by refers to a real, existing user. If successful,
        the response body should contain a JSON of the message, including its message_id.
        The response status should be 200, which is the default. The new message should be persisted to the database.
    •If the creation of the message is not successful, the response status should be 400. (Client error)
    */

    public Message createMessage(Message message) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());
            int rowAffected = ps.executeUpdate();
            if (rowAffected == 0) {
                throw new SQLException("Creating message failed, no rows affected.");
            }
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    message.setMessage_id(generatedKeys.getInt(1));
                }
                else {
                    throw new SQLException("Creating message failed, no ID obtained.");
                }
            }
        }
        return message;
    }



    /*public static Message insert_Message(Message message){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "insert into message (message_id, posted_by,message_text,time_posted_epoch) values(?,?,?,?);";
            PreparedStatement ps=connection.prepareStatement(sql);
            ps.setInt(1, message.getMessage_id());
            ps.setInt(2,message.getPosted_by());
            ps.setString(3,message.getMessage_text());
            ps.setLong(4,message.getTime_posted_epoch());
            ps.executeUpdate();
            ResultSet pkeyResultSet = ps.getGeneratedKeys();
            if(pkeyResultSet.next()){//If message doesn't exsists then we must generate new message_id and return new message
                int generated_message_id = (int) pkeyResultSet.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }*/
    /*As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages.
•The response body should contain a JSON representation of a list containing all messages retrieved from the database.
It is expected for the list to simply be empty if there are no messages. The response status should always be 200,
which is the default.
*/
    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        try {
            Connection conn = ConnectionUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement("select * from message");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Message message = new Message();
                message.setMessage_id(rs.getInt("message_id"));
                message.setPosted_by(rs.getInt("posted_by"));
                message.setMessage_text(rs.getString("message_text"));
                message.setTime_posted_epoch(rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }


    /*public List<Message> retrieve_All_Messages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "select * from message;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message message = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                messages.add(message);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }*/

/*As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages/{message_id}.
•The response body should contain a JSON representation of the message identified by the message_id.
It is expected for the response body to simply be empty if there is no such message. The response status should always be 200,
 which is the default.
*/
public static Message retrieve_Message_by_ID(int id) {
    Connection connection = ConnectionUtil.getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM messages WHERE message_id=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Message(rs.getInt("message_id"), rs.getInt("user_id"), rs.getString("content"), rs.getLong("timestamp"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }




    /*Connection connection = ConnectionUtil.getConnection();

    try {
        String sql = "select * from message where message_id=?;";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
            return message;
        }
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
    return null;*/

    /*As a User, I should be able to submit a DELETE request on the endpoint DELETE localhost:8080/messages/{message_id}.
•The deletion of an existing message should remove an existing message from the database. If the message existed,
the response body should contain the now-deleted message. The response status should be 200, which is the default.
•If the message did not exist, the response status should be 200, but the response body should be empty.
This is because the DELETE verb is intended to be idempotent, ie,
 multiple calls to the DELETE endpoint should respond with the same type of response.
*/
    public Optional<Message> deleteMessage(int messageId) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM message WHERE message_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, messageId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Message message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                String deleteQuery = "DELETE FROM message WHERE message_id = ?";
                try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
                    deleteStmt.setInt(1, messageId);
                    deleteStmt.executeUpdate();
                }
                return Optional.of(message);
            } else {
                return Optional.empty();
            }
        }
    }

    /*public static Message delete_Message_by_ID(int id) {
        Connection connection = ConnectionUtil.getConnection();


        try {
            String sql = "delete * from message where message_id=? ";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Message message = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                return message;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }*/

    /*As a user, I should be able to submit a PATCH request on the endpoint PATCH localhost:8080/messages/{message_id}.
    The request body should contain a new message_text values to replace the message identified by message_id.
    The request body can not be guaranteed to contain any other information.
•The update of a message should be successful if and only if the message id already exists and the new message_text is not
blank and is not over 255 characters. If the update is successful, the response body should contain the full updated message
(including message_id, posted_by, message_text, and time_posted_epoch), and the response status should be 200, which is the default. The message existing on the database should have the updated message_text.
•If the update of the message is not successful for any reason, the response status should be 400. (Client error)
*/


    public static void update_Message_by_ID(int id, Message message){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "update message set posted_by=?, message_text=?, time_posted_epoch=? where message_id=?; ";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1,message.getPosted_by());
            ps.setString(2,message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());
            ps.setInt(4,id);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        //return message;
    }
/*As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/accounts/{account_id}/messages.
•The response body should contain a JSON representation of a list containing all messages posted by a particular user,
which is retrieved from the database. It is expected for the list to simply be empty if there are no messages.
The response status should always be 200, which is the default.
*/

    public List<Message> getAllMessagesForUser(int userId) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        String query = "SELECT * FROM message WHERE posted_by = ? ORDER BY time_posted_epoch DESC;";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int messageId = rs.getInt("message_id");
            String messageText = rs.getString("message_text");
            long timePostedEpoch = rs.getLong("time_posted_epoch");
            messages.add(new Message(messageId, userId, messageText, timePostedEpoch));
        }
        return messages;
    }

    /*public static List<Message>  get_Messages_by_UserID(int id){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "select * from message where posted_by=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message message = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                messages.add(message);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }
*/
}
