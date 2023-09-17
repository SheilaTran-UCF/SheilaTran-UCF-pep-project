package DAO;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SocialMediaDAO {
    public Account regisAcc(Account account) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        String query = "INSERT INTO account (username, password) VALUES (?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        // Set values for the placeholders in the SQL statement
        preparedStatement.setString(1, account.getUsername());
        preparedStatement.setString(2, account.getPassword());
        int rowsInserted = preparedStatement.executeUpdate();
        if (rowsInserted > 0) {
            return getAccByUsername(account.username);
        }
        return null;
    }

    public Account getAccByUsername(String name) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String select = "SELECT * FROM account where username = '?username'";
        select = select.replace("?username", name);
        PreparedStatement statement = connection.prepareStatement(select);
        ResultSet resultSet = statement.executeQuery();
        Account account = new Account();

        while (resultSet.next()) {
            int account_id = resultSet.getInt("account_id");
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");
            account.setAccount_id(account_id);
            account.setUsername(username);
            account.setPassword(password);
        }
        if (account.getUsername() == null) return null;
        return account;
    }

    public Message createMessage(Message message) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String query = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        // Set values for the placeholders in the SQL statement
        preparedStatement.setInt(1, message.getPosted_by());
        preparedStatement.setString(2, message.getMessage_text());
        preparedStatement.setLong(3, message.getTime_posted_epoch());
        int rowsInserted = preparedStatement.executeUpdate();

        if (rowsInserted > 0) {
            return getMessage(message.getPosted_by());
        }

        return new Message();
    }

    public Account selectAccountById(int id) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String select = "SELECT * FROM account where account_id = ?id";
        select = select.replace("?id", String.valueOf(id));
        PreparedStatement statement = connection.prepareStatement(select);
        ResultSet resultSet = statement.executeQuery();
        Account account = new Account();

        while (resultSet.next()) {
            int account_id = resultSet.getInt("account_id");
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");
            account.setAccount_id(account_id);
            account.setUsername(username);
            account.setPassword(password);
        }
        if (account.getAccount_id() == 0) return null;
        return account;
    }

    private Message getMessage(int postedBy) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String select = "SELECT * FROM message where posted_by = ?";
        select = select.replace("?", String.valueOf(postedBy));
        PreparedStatement statement = connection.prepareStatement(select);
        ResultSet resultSet = statement.executeQuery();
        Message result = new Message();

        while (resultSet.next()) {
            int message_id = resultSet.getInt("message_id");
            int posted_by = resultSet.getInt("posted_by");
            String message_text = resultSet.getString("message_text");
            long time_posted_epoch = resultSet.getLong("time_posted_epoch");
            result.setMessage_id(message_id);
            result.setPosted_by(posted_by);
            result.setMessage_text(message_text);
            result.setTime_posted_epoch(time_posted_epoch);
        }
        return result;
    }

    public List<Message> getMessageByUser(int postedBy) throws SQLException {
        List<Message> result = new ArrayList<>();
        Connection connection = ConnectionUtil.getConnection();
        String select = "SELECT * FROM message where posted_by = ?";
        select = select.replace("?", String.valueOf(postedBy));
        PreparedStatement statement = connection.prepareStatement(select);
        ResultSet resultSet = statement.executeQuery();
        Message message = new Message();

        while (resultSet.next()) {
            int message_id = resultSet.getInt("message_id");
            int posted_by = resultSet.getInt("posted_by");
            String message_text = resultSet.getString("message_text");
            long time_posted_epoch = resultSet.getLong("time_posted_epoch");
            message.setMessage_id(message_id);
            message.setPosted_by(posted_by);
            message.setMessage_text(message_text);
            message.setTime_posted_epoch(time_posted_epoch);
            result.add(message);
        }
        return result;
    }

    public Message getMessageById(String message_id) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String select = "SELECT * FROM message where message_id = ?";
        select = select.replace("?", message_id);
        PreparedStatement statement = connection.prepareStatement(select);
        ResultSet resultSet = statement.executeQuery();
        Message result = new Message();

        while (resultSet.next()) {
            int messageId = resultSet.getInt("message_id");
            int posted_by = resultSet.getInt("posted_by");
            String message_text = resultSet.getString("message_text");
            long time_posted_epoch = resultSet.getLong("time_posted_epoch");
            result.setMessage_id(messageId);
            result.setPosted_by(posted_by);
            result.setMessage_text(message_text);
            result.setTime_posted_epoch(time_posted_epoch);
        }
        if (result.getMessage_id()==0) return null;
        return result;
    }

    public List<Message> getAllMessage() throws SQLException {
        List<Message> result = new ArrayList<>();
        Connection connection = ConnectionUtil.getConnection();
        String select = "SELECT * FROM message";
        PreparedStatement statement = connection.prepareStatement(select);
        ResultSet resultSet = statement.executeQuery();
        Message message = new Message();

        while (resultSet.next()) {
            int messageId = resultSet.getInt("message_id");
            int posted_by = resultSet.getInt("posted_by");
            String message_text = resultSet.getString("message_text");
            long time_posted_epoch = resultSet.getLong("time_posted_epoch");
            message.setMessage_id(messageId);
            message.setPosted_by(posted_by);
            message.setMessage_text(message_text);
            message.setTime_posted_epoch(time_posted_epoch);
            result.add(message);
        }
        return result;
    }

    public void deleteMessageById(String id) throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        String deleteSQL = "DELETE FROM message WHERE message_id = ?id";
        deleteSQL = deleteSQL.replace("?id", id);

        connection.prepareStatement(deleteSQL);
    }

    public Message updateMessage(String id, String messageText) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
        String update = "UPDATE message SET message_text = ? WHERE message_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(update);

        preparedStatement.setString(1, messageText);
        preparedStatement.setInt(2, Integer.parseInt(id));
        preparedStatement.executeUpdate();

        return getMessageById(id);
    }
}
