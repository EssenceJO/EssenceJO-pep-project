package Service;

import Model.Message;
import DAO.messageDAO;

import java.util.List;

public class messageService {
     private messageDAO messageDAO;
    /**
     * no-args constructor for creating a new AuthorService with a new AuthorDAO.
     * There is no need to change this constructor.
     */
    public messageService(){
        messageDAO = new messageDAO();
    }

     public messageService(messageDAO MessageDAO){
        this.messageDAO = MessageDAO;
    }

      /**
     * TODO: Use the AuthorDAO to retrieve all authors.
     *
     * @return all authors
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public List<Message> getAllMessagesFromUser(int id) {
        return messageDAO.getAllMessagesFromUser(id);
    }

    public Message getMessageById(int id) {
    return messageDAO.getMessageById(id);
}

public Message createMessage(Message message) {
    return messageDAO.createMessage(message);
}

public Message updateMessage(int messageId, Message updatedMessage) {
    return messageDAO.updateMessage(messageId, updatedMessage);
}

public Message deleteMessageById(int messageId) {
    return messageDAO.deleteMessageById(messageId);
}



     /**
     * TODO: Use the AuthorDAO to persist an author. The given Author will not have an id provided.
     *
     * @param author an author object.
     * @return The persisted author if the persistence is successful.
     
    public Message addMessage(Message message) {
        return messageDAO.insertMessage(message);
    } */ //uncomment after you add the addMessage to messageDAO
}

