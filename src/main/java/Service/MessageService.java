package Service;

import Model.Message;
import DAO.MessageDAO;

import java.util.List;

public class MessageService {

    public static MessageDAO messageDAO;

    //no-args constructor
    public MessageService(){
        messageDAO = new MessageDAO();
    }

    //constructor for MessageDAO when MessageDAO is provided
    public MessageService(MessageDAO messageDAO){
        this.messageDAO=messageDAO;
    }
    //create message
    public Message insert_Message(Message message){
        if(message.getMessage_text()!=null && message.getMessage_text().length()<255 && message.getPosted_by()!=0){
            return MessageDAO.insert_Message(message);
        }
        return null;
    }

    public static List<Message> retrieve_All_Messages(){
        return messageDAO.retrieve_All_Messages();
    }

    public static Message retrieve_Message_by_ID(int id){
            return messageDAO.retrieve_Message_by_ID(id);
    }

    public static Message delete_Message_by_ID(int id, Message message){
        message = MessageDAO.retrieve_Message_by_ID(id);
        if(message!=null){
            MessageDAO.delete_Message_by_ID(id, message);
            return message;
        }
        else {
            return null;
        }
    }

    public static Message update_Message_by_ID(int id, Message message){
        message = MessageDAO.retrieve_Message_by_ID(id);
        if(message!=null && message.getMessage_text()!=null && message.getMessage_text().length()<255){
             MessageDAO.update_Message_by_ID(id,message);
             return message;
        }
        else {return null;}
    }

    public static List<Message> get_Messages_by_UserID(int id){
        return MessageDAO.get_Messages_by_UserID(id);
    }

}
