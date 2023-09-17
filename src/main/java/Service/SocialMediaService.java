package Service;

import DAO.SocialMediaDAO;
import Model.Account;
import Model.Message;

import java.sql.SQLException;
import java.util.List;

public class SocialMediaService {
    private static SocialMediaDAO socialMediaDAO = new SocialMediaDAO();

    public List<Message> getAllMessage() throws SQLException {
        List<Message> result = socialMediaDAO.getAllMessage();
        return result;
    }

    public Account regisAcc(Account account) throws Exception {
        return socialMediaDAO.regisAcc(account);
    }

    public Account selectAccountById(int id) throws SQLException {
        return socialMediaDAO.selectAccountById(id);
    }

    public Message createMessage(Message message) throws SQLException {
        return socialMediaDAO.createMessage(message);
    }

    public Message getMessageById(String messageId) throws SQLException {
        return socialMediaDAO.getMessageById(messageId);
    }

    public void deleteMessage(String id) throws Exception {
        socialMediaDAO.deleteMessageById(id);
    }

    public Message updateMessage(String id, String message) throws Exception {
        return socialMediaDAO.updateMessage(id, message);
    }

    public List<Message> getMessageByUser(int id) throws SQLException {
        return socialMediaDAO.getMessageByUser(id);
    }

    public Account getAccByUsername(String username) throws SQLException {
        return socialMediaDAO.getAccByUsername(username);
    }
}
