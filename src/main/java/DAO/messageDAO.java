package DAO;
import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class messageDAO {
      public List<Message> getAllAuthors(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> message = new ArrayList<>();
        try {
            //Write SQL logic here
            String sql = "select * from message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message1 = new Message(rs.getInt("id"), rs.getInt("postedby"), rs.getString("text"),rs.getInt("timeposted"));
                message.add(message1);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return message;
    }
}
