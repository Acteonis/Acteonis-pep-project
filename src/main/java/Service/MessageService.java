package Service;

import DAO.AccountDAO;
import Model.Message;
import DAO.MessageDAO;
import Model.Account;
import Util.ConnectionUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MessageService {

    private final MessageDAO messageDAO;
    private final AccountDAO accountDAO;

    //no-args constructor
    public MessageService(){
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }

    //constructor for MessageDAO when MessageDAO is provided
    public MessageService(MessageDAO messageDAO, AccountDAO accountDAO){
        this.messageDAO=messageDAO;
        this.accountDAO=accountDAO;
    }
    //create message
//    public Message insert_Message(Message message){
//       if(message.getMessage_text()!=null && message.getMessage_text().length()<255 && message.getPosted_by()!=0){
//            return MessageDAO.insert_Message(message);
//        }
//        return null;
//    }

    /*As a user, I should be able to submit a new post on the endpoint POST localhost:8080/messages.
    The request body will contain a JSON representation of a message, which should be persisted to the database,
     but will not contain a message_id.

The creation of the message will be successful if and only if the message_text is not blank,
 is under 255 characters, and posted_by refers to a real, existing user. If successful,
  the response body should contain a JSON of the message, including its message_id.
  The response status should be 200, which is the default. The new message should be persisted to the database.
If the creation of the message is not successful, the response status should be 400. (Client error)*/

    public Message addMessage(Message message){
        return messageDAO.addMessage(message);
    }


    /*public Message createMessage(Message message) throws SQLException {
        /*if (message.getMessage_text().isBlank() || message.getMessage_text().length() >= 254 || message.getMessage_text().length() == 0) {
            throw new IllegalArgumentException("Message text must be between 1 and 255 characters");
        }
        if (accountDAO.Get_User_byID(message.getPosted_by()) == null) {
            throw new IllegalArgumentException("Posted_by user does not exist");
        }
        return messageDAO.createMessage(message);
    }*/

       /* if (message.getMessage_text().length() >0 && message.getMessage_text().length() <200 && accountDAO.Get_User_byID(message.getPosted_by()) != null);
        {
            return messageDAO.createMessage(message);
        }
    }*/

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int messageId) throws SQLException, IOException {
        return MessageDAO.retrieve_Message_by_ID(messageId);
    }

    public Optional<Message> deleteMessage(int messageId) throws SQLException {
        return messageDAO.deleteMessage(messageId);
    }

    /*public static Message delete_Message_by_ID(int id, Message message){
        message = MessageDAO.retrieve_Message_by_ID(id);
        if(message!=null){
            MessageDAO.delete_Message_by_ID(id);
            return message;
        }
        else {
            return null;
        }
    }*/

    /*public Flight updateFlight(int flight_id, Flight flight){
        flight = flightDAO.getFlightById(flight_id);
        if(flight!=null)
            {flightDAO.updateFlight(flight_id, flight);
                return flight;}
       else{return null;}
    }*/
/*The update of a message should be successful if and only if
the message id already exists and the new message_text is not blank and is not over 255 characters.
If the update is successful, the response body should contain the full
updated message (including message_id, posted_by, message_text, and time_posted_epoch),
and the response status should be 200, which is the default.
 The message existing on the database should have the updated message_text.*/
    public Message updateMessage(int message_id, Message message) throws SQLException, IOException {
        Message updated_message= MessageDAO.retrieve_Message_by_ID(message_id);
        if(message.getMessage_text().isBlank() || message.getMessage_text().length()>=255 || MessageDAO.retrieve_Message_by_ID(message_id)==null){
            return null;}

        messageDAO.updateMessage(message_id, message);
        String message_text= message.getMessage_text();
        updated_message.setMessage_text(message_text);
        return updated_message;
    }



    /*public static Message update_Message_by_ID(int id, Message message) throws SQLException, IOException {
        //message = MessageDAO.retrieve_Message_by_ID(id);
        if (message != null && message.getMessage_text() != " " && message.getMessage_text().length() < 255) {
            MessageDAO.update_Message_by_ID(id, message);
            return message;
        } else return null;
    }*/
    public List<Message> getAllMessagesForUser(int userId) throws SQLException {
        return messageDAO.getAllMessagesForUser(userId);
    }

}

