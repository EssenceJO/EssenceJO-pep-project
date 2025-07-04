package Controller;

import Service.messageService;
import Model.Account;
import Model.Message;

import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;
import Service.userService;

public class SocialMediaController {

    private messageService messageService;
    private userService userService;

    public SocialMediaController() {
        this.messageService = new messageService();
        this.userService = new userService();
    }

    

    public Javalin startAPI() {
        Javalin app = Javalin.create();

        // Define your endpoints
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/accounts/{account_id}/messages",this::getMessagesByUserHandler);
        app.get("/messages/{message_id}",this::getMessagesByIdHandler);
        app.post("/messages", this::createMessageHandler);
        app.patch("/messages/{message_id}", this::patchMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.post("/login", this::loginHandler);
        app.post("/register", this::registerHandler);






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

        String messageText = newMessage.getMessage_text();
        int userId = newMessage.getPosted_by();

        // Validate message text
        if (messageText == null || messageText.trim().isEmpty() || messageText.length() > 255) {
            ctx.status(400).result("");
            return;
        }

        // Check if user exists
        Account user = userService.getUserById(userId);
        if (user == null) {
            ctx.status(400).result("");  // 👈 This makes the test pass
            return;
        }

        Message created = messageService.createMessage(newMessage);
        if (created != null) {
            ctx.status(200).json(created);
        } else {
            ctx.status(400).result("");
        }

    } catch (Exception e) {
        e.printStackTrace();
        ctx.status(400).result("");
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

private void deleteMessageHandler(Context ctx) {
    try {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessageById(messageId);

        if (deletedMessage != null) {
            ctx.status(200).json(deletedMessage);
        } else {
            ctx.status(200).result(""); // Message not found but still return 200 with empty body
        }
    } catch (Exception e) {
        e.printStackTrace();
        ctx.status(400).result(""); // Bad path param or error
    }
}

private void loginHandler(Context ctx) {
    try {
        Account credentials = ctx.bodyAsClass(Account.class);

        if (credentials.getUsername() == null || credentials.getPassword() == null) {
            ctx.status(400).result("");
            return;
        }

        Account user = userService.login(credentials.getUsername(), credentials.getPassword());

        if (user != null) {
            ctx.status(200).json(user);
        } else {
            ctx.status(401).result(""); // handle in a future test
        }

    } catch (Exception e) {
        e.printStackTrace();
        ctx.status(400).result("");
    }
}

private void registerHandler(Context ctx) {
    try {
        Account newUser = ctx.bodyAsClass(Account.class);

        String username = newUser.getUsername();
        String password = newUser.getPassword();

        // Check for null, blank, or short password
        if (username == null || password == null ||
            username.trim().isEmpty() || password.trim().isEmpty() ||
            password.length() < 4) {

            ctx.status(400).result(""); 
            return;
        }

        Account createdUser = userService.register(newUser);

        if (createdUser != null) {
            ctx.status(200).json(createdUser);
        } else {
            ctx.status(400).result("");
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
