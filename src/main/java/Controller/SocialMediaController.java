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
        // You can add more like app.post("/messages", this::addMessageHandler)

        return app;
    }

    /**
     * GET /messages
     * Returns all messages from the database as JSON.
     */
    private void getAllMessagesHandler(Context context) {
        List<Message> messages = messageService.getAllMessages();
        context.json(messages);  // Automatically serialized using Jackson
    }

    /**
     * Example handler (optional to keep)
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }
}
