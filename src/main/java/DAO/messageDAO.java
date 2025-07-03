package DAO;
import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class messageDAO {
      public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> message = new ArrayList<>();
        try {
            //Write SQL logic here
            String sql = "select * from message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message1 = new Message(rs.getInt("message_id"), 
                rs.getInt("posted_by"), 
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch"));
                message.add(message1);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return message;
    }

     public List<Message> getAllMessagesFromUser(int id){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> message = new ArrayList<>();
        try {
            //Write SQL logic here
            String sql = "select * from message where posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message1 = new Message(rs.getInt("message_id"), 
                rs.getInt("posted_by"), 
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch"));
                message.add(message1);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return message;
    }

    
    public Message getMessageById(int id) {
    Connection connection = ConnectionUtil.getConnection();
    try {
        String sql = "SELECT * FROM message WHERE message_id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return new Message(
                rs.getInt("message_id"),
                rs.getInt("posted_by"),
                rs.getString("message_text"),
                rs.getLong("time_posted_epoch")
            );
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null; // return null if no result found
}

public Message createMessage(Message message) {
    Connection connection = ConnectionUtil.getConnection();
    try {
        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ps.setInt(1, message.getPosted_by());
        ps.setString(2, message.getMessage_text());
        ps.setLong(3, message.getTime_posted_epoch());

        int rowsInserted = ps.executeUpdate();

        if (rowsInserted > 0) {
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                return new Message(generatedId, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return null; 
}

public Message updateMessage(int messageId, Message updatedMessage) {
    Connection connection = ConnectionUtil.getConnection();
    try {
        String sql = "UPDATE message SET message_text = ?, time_posted_epoch = ? WHERE message_id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, updatedMessage.getMessage_text());
        ps.setLong(2, updatedMessage.getTime_posted_epoch());
        ps.setInt(3, messageId);

        int rowsUpdated = ps.executeUpdate();
        if (rowsUpdated > 0) {
            return new Message(
                messageId,
                updatedMessage.getPosted_by(), // assumes posted_by is unchanged
                updatedMessage.getMessage_text(),
                updatedMessage.getTime_posted_epoch()
            );
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}

public Message deleteMessageById(int messageId) {
    Connection connection = ConnectionUtil.getConnection();
    try {
        // First retrieve the message to return it later
        Message messageToDelete = getMessageById(messageId);
        if (messageToDelete == null) {
            return null;
        }

        String sql = "DELETE FROM message WHERE message_id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, messageId);
        int rowsDeleted = ps.executeUpdate();

        if (rowsDeleted > 0) {
            return messageToDelete;
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}


}


