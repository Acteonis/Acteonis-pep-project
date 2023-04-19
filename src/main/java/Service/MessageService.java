package Service;

import DAO.AccountDAO;
import Model.Message;
import DAO.MessageDAO;
import Model.Account;
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

    public Message createMessage(Message message) throws SQLException {
        if (message.getMessage_text().isBlank() || message.getMessage_text().length() > 255) {
            throw new IllegalArgumentException("Message text must be between 1 and 255 characters");
        }
        if (accountDAO.Get_User_byID(message.getPosted_by()) == null) {
            throw new IllegalArgumentException("Posted_by user does not exist");
        }
        return messageDAO.createMessage(message);
    }


    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int messageId) throws SQLException {
        return messageDAO.retrieve_Message_by_ID(messageId);
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


    public static Message update_Message_by_ID(int id, Message message){
        message = MessageDAO.retrieve_Message_by_ID(id);
        if(message!=null && message.getMessage_text()!=" " && message.getMessage_text().length()<255){
             MessageDAO.update_Message_by_ID(id,message);
             return message;
        }
        else {return null;}
    }

    public List<Message> getAllMessagesForUser(int userId) throws SQLException {
        return messageDAO.getAllMessagesForUser(userId);
    }

}
