package Controller;

import Service.messageService;
import Model.Message;

import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;

public class SocialMediaController {

    private messageService messageService;

    public SocialMediaController() {
        this.messageService = new messageService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();

        // Define your endpoints
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/accounts/{account_id}/messages",this::getMessagesByUserHandler);
        app.get("/messages/{message_id}",this::getMessagesByIdHandler);
        // You can add more like app.post("/messages", this::addMessageHandler)

        return app;
    }

    /**
     * GET /messages
     * Returns all messages from the database as JSON.
     */
    private void getAllMessagesHandler(Context context) {
        List<Message> messages = messageService.getAllMessages();
        context.json(messages);  
    }

    private void getMessagesByIdHandler(Context context) {
       
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);
        context.json(message);
        
        if (message != null) {
            context.json(message);
        } else {
            context.status(200).result(""); 
        }
    } 

    private void getMessagesByUserHandler(Context ctx) {
        try {
            int userId = Integer.parseInt(ctx.pathParam("account_id"));
            List<Message> messages = messageService.getAllMessagesFromUser(userId);

            ctx.json(messages); // respond with the list of messages as JSON
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid user ID format.");
        } catch (Exception e) {
            ctx.status(500).result("Internal server error.");
        }
    }

    /**
     * Example handler (optional to keep)
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }
}
