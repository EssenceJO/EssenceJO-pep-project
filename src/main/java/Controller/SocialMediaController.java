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
        app.post("/messages", this::createMessageHandler);
        app.patch("/messages/{message_id}", this::patchMessageHandler);



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
    try {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);

        if (message != null) {
            context.json(message);
        } else {
            context.status(200).result(""); // empty body, still 200
        }

    } catch (NumberFormatException e) {
        context.status(400).result("Invalid message ID.");
    } catch (Exception e) {
        e.printStackTrace();
        context.status(500).result("Internal server error.");
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

    private void createMessageHandler(Context ctx) {
    try {
        Message newMessage = ctx.bodyAsClass(Message.class);

        // Validate message_text: must not be blank and < 255 characters
        String text = newMessage.getMessage_text();
        if (text == null || text.trim().isEmpty() || text.length() >= 255) {
            ctx.status(400).result("");  // ✅ return 400 with empty body
            return;
        }

        Message createdMessage = messageService.createMessage(newMessage);
        if (createdMessage != null) {
            ctx.status(200).json(createdMessage);
        } else {
            ctx.status(500).result("Message could not be created.");
        }

    } catch (Exception e) {
        e.printStackTrace();
        ctx.status(400).result(""); // ✅ Invalid body = 400 with empty body
    }
}

private void patchMessageHandler(Context ctx) {
    try {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message existingMessage = messageService.getMessageById(messageId);

        if (existingMessage == null) {
            ctx.status(400).result("");
            return;
        }

        Message patch = ctx.bodyAsClass(Message.class);

        // Use original values unless updated
        String newText = patch.getMessage_text() != null ? patch.getMessage_text() : existingMessage.getMessage_text();
        long newEpoch = existingMessage.getTime_posted_epoch(); // unchanged
        int postedBy = existingMessage.getPosted_by();          // unchanged

        // Validate new text
        if (newText.trim().isEmpty() || newText.length() >= 255) {
            ctx.status(400).result("");
            return;
        }

        Message updated = new Message(messageId, postedBy, newText, newEpoch);
        Message result = messageService.updateMessage(messageId, updated);

        if (result != null) {
            ctx.status(200).json(result);
        } else {
            ctx.status(500).result("Failed to update.");
        }

    } catch (Exception e) {
        e.printStackTrace();
        ctx.status(400).result("");
    }
}




    /**
     * Example handler (optional to keep)
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }
}
