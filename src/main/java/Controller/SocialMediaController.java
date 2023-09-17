package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Model.Message;
import Service.SocialMediaService;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.List;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    private static final Gson gson = new Gson();
    private static SocialMediaService service = new SocialMediaService();

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("register", this::registerUser);
        app.post("messages", this::createMessage);
        app.get("messages/:message_id", this::getMessageById);
        app.get("messages", this::getAllMessage);
        app.post("login", this::loginUser);
        app.delete("messages/:message_id", this::deleteMessage);
        app.patch("messages/:message_id", this::updateMessage);
        app.get("accounts/:account_id/messages", this::getMessagesByUser);
        return app;
    }
    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }
    private void getAllMessage(Context context) throws SQLException {
        List<Message> result = service.getAllMessage();
        context.json(result);
    }

    private void registerUser(Context context) throws Exception {
        String request = context.body();
        Account account = gson.fromJson(request, Account.class);
        if (account.getPassword().length() < 4) {
            context.status(400);
            return;
        }
        String username = account.getUsername();
        if (username == null || "".equals(username) || service.getAccByUsername(username) != null) {
            context.status(400);
            return;
        }
        Account result = service.regisAcc(account);
        if (result == null) {
            context.status(400);
            return;
        }
        context.json(result);
    }

    private void loginUser(Context context) throws SQLException {
        String request = context.body();
        Account account = gson.fromJson(request, Account.class);
        Account result = service.getAccByUsername(account.username);
        if (result == null || !result.getPassword().equals(account.getPassword())) {
            context.status(401);
            return;
        }
        context.json(result);
    }

    private void createMessage(Context context) throws SQLException {
        String request = context.body();
        Message message = gson.fromJson(request, Message.class);
        String messageText = message.getMessage_text();
        if (messageText == null || "".equals(messageText) || messageText.length() >= 255) {
            context.status(400);
            return;
        }
        Account account = service.selectAccountById(message.getPosted_by());
        if (account == null) {
            context.status(400);
        } else {
            Message resule = service.createMessage(message);
            context.json(resule);
        }
    }


    private void getMessageById(Context context) throws SQLException {
        String messageId = context.pathParam("message_id");
        Message message = service.getMessageById(messageId);
        context.status(200);
        if (message != null) {
            context.json(message);
        }
    }

    private void deleteMessage(Context context) throws Exception {
        String messageId = context.pathParam("message_id");
        Message message = service.getMessageById(messageId);
        context.status(200);
        if (message != null) {
            service.deleteMessage(messageId);
            context.json(message);
        }
    }

    private void updateMessage(Context context) throws Exception {
        String messageId = context.pathParam("message_id");
        String mes = context.body();
        Message message = gson.fromJson(mes, Message.class);
        Message message1 = service.getMessageById(messageId);
        String messageText = message.getMessage_text();
        if (message1 == null || messageText.length() >= 255 || messageText.length() == 0) {
            context.status(400);
            return;
        }
        Message result = service.updateMessage(messageId, messageText);
        context.json(result);
    }

    private void getMessagesByUser(Context context) throws SQLException {
        String userId = context.pathParam("account_id");
        List<Message> messages = service.getMessageByUser(Integer.parseInt(userId));
        context.status(200);
        context.json(messages);
    }

}