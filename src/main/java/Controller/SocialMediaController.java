package Controller;

import DAO.AccountDAO;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.Javalin;
import io.javalin.http.Context;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     *
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     * <p>
     * public Javalin startAPI() {
     * Javalin app = Javalin.create();
     * app.get("example-endpoint", this::exampleHandler);
     * <p>
     * return app;
     * }
     */


    AccountService accountService;
    MessageService messageService;


    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();

    }


    public Javalin startAPI() {
        Javalin app = Javalin.create();//that's all right. Relax, soldier!
        //app.get("/account",this::postAccountRegistrationHandler);//?!
        //app.post("localhost:8080/register", this::postAccountRegistrationHandler);
        //
        app.post("localhost:8080/register",this::handleCreateAccount);
        app.post("localhost:8080/login", this::handleLogin);
        app.post("localhost:8080/messages",this::createMessageHandle);
        app.get("localhost:8080/messages", this::getAllMessages);
        app.get("localhost:8080/messages/{message_id}", this::getMessageById);
        app.delete("localhost:8080/messages/{message_id}", this::deleteMessage);
        app.patch("localhost:8080/messages/{message_id}", this:: updateMessageByIDHandler);
        app.get("localhost:8080/accounts/{account_id}/messages",this::getAllMessagesForUser);
        //app.start(8080);
        return app;
    }

    /*public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("localhost:8080/messages", this::messagesHandler);
        app.get("localhost:8080/messages/{message_id}", this::messages_iHandler);
        app.get("localhost:8080/accounts/{account_id}/messages", this::account_idmessagesHandler);
        return app;*/



	/*
	public Javalin startAPI() {
		Javalin app=Javalin.create();

		//lets start with registration API
		app.post("/register", this::postSocialMediaHandler);//Check handler!!!


		return app;
	}*/

    /**
     * This is an example handler for an example endpoint.
     *
     * @param //context The Javalin Context object manages information about both the HTTP request and response.
     */


    /*As a user, I should be able to create a new Account on the endpoint POST localhost:8080/register.
     The body will contain a representation of a JSON Account, but will not contain an account_id.

    The registration will be successful if and only if the username is not blank, the password is at least 4 characters long,
    and an Account with that username does not already exist. If all these conditions are met,
    !!!!!!!!!!!the response body should contain a JSON of the Account, including its account_id.!!!!!!!!!!

    The response status should be 200 OK, which is the default. The new account should be persisted to the database.
    If the registration is not successful, the response status should be 400. (Client error)*/


    public void handleCreateAccount(Context ctx) throws IOException, SQLException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account createdAccount = accountService.create(account);
        String response = mapper.writeValueAsString(createdAccount);
        ctx.status (200).result(response);
    }

    //context.json(bookService.getAllAvailableBooks()
   /* private void postAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.add_Account(account);

        if (addedAccount != null) {//?! Is it correct?! Do I need to check it?!
           // ctx.status(200);
            ctx.json(addedAccount);
        } else {
            ctx.status(400);
        }
    }*/
/*As a user, I should be able to verify my login on the endpoint POST localhost:8080/login.
 The request body will contain a JSON representation of an Account, not containing an account_id.
 In the future, this action may generate a Session token to allow the user to securely use the site.
 We will not worry about this for now.

The login will be successful if and only if the username and password provided in the request body
JSON match a real account existing on the database. If successful,
the response body should contain a JSON of the account in the response body,
including its account_id. The response status should be 200 OK, which is the default.
If the login is not successful, the response status should be 401. (Unauthorized)*/

    public void handleLogin(Context ctx) throws Exception {
        Account account = ctx.bodyAsClass(Account.class);
        Account existingAccount = accountService.findByUsernameAndPassword(account.getUsername(), account.getPassword());
        if (existingAccount != null) {
            ctx.json(existingAccount);
        } else {
            ctx.status(401);
        }
    }

   /*private void postLoginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        //String username = String.parseString(ctx.pathParam("username"));//?!
        //String password = String.parseString(ctx.pathParam("password"));//?!
        //Account login_account = accountService.check_user_Login( username, password);

        ctx.json(accountService.check_user_Login(ctx.pathParam("username"), ctx.pathParam("password")));

        /*ctx.json(flightService.getAllFlightsFromCityToCity(ctx.pathParam("departure_city"),ctx.pathParam("arrival_city")));*/
        /*if (login_account == null) {
            ctx.status(401);
        } else {
            //ctx.status(200);
            ctx.json(account); //do I need insert account_id also?*/




/*As a user, I should be able to submit a new post on the endpoint POST localhost:8080/messages.
The request body will contain a JSON representation of a message, which should be persisted to the database,
but will not contain a message_id.

The creation of the message will be successful if and only if the message_text is not blank,
is under 255 characters, and posted_by refers to a real, existing user.
If successful, the response body should contain a JSON of the message, including its message_id.
 The response status should be 200, which is the default. The new message should be persisted to the database.
If the creation of the message is not successful, the response status should be 400. (Client error)*/

    public void createMessageHandle(Context ctx) throws SQLException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
         message = messageService.createMessage(message);
        ctx.status(200);
        ctx.result(mapper.writeValueAsString(message));
    }

   /* private void newPostHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message postedMessage = messageService.insert_Message(message);
        if (postedMessage != null) {//?! Is it correct?! Do I need to check it?!
            //ctx.status(200);
            ctx.json(postedMessage);
        } else {
            ctx.status(400);
        }
}*/
/*As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages.

The response body should contain a JSON representation of a list containing all messages retrieved from the database.
It is expected for the list to simply be empty if there are no messages.
The response status should always be 200, which is the default.*/
   public void getAllMessages(Context ctx) {
       List<Message> messages = messageService.getAllMessages();
       ctx.json(messages);
   }

/*As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages/{message_id}.

The response body should contain a JSON representation of the message identified by the message_id.
It is expected for the response body to simply be empty if there is no such message.
The response status should always be 200, which is the default.*/

    public void getMessageById(Context context) throws SQLException {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);

        if (message != null) {
            context.json(message);
        } else {
            context.json("");
        }
    }
   /* private void getMessageByIDHandler(Context ctx) throws JsonProcessingException, SQLException {
        ObjectMapper mapper = new ObjectMapper();
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.retrieve_Message_by_ID(id);
        if(message != null) {
            ctx.json(mapper.writeValueAsString(message));
        } else {
            ctx.result("");
        }
    }*/


/*As a User, I should be able to submit a DELETE request on the endpoint DELETE localhost:8080/messages/{message_id}.

The deletion of an existing message should remove an existing message from the database.
 If the message existed, the response body should contain the now-deleted message.
The response status should be 200, which is the default.
If the message did not exist, the response status should be 200, but the response body should be empty.
This is because the DELETE verb is intended to be idempotent, ie,
multiple calls to the DELETE endpoint should respond with the same type of response.*/

    public void deleteMessage(Context ctx) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Optional<Message> message = messageService.deleteMessage(messageId);
        if (message.isPresent()) {
            ctx.result(mapper.writeValueAsString(message.get()));
        } else {
            ctx.result("");
        }
    }

       /* private void deleteMessageByIDHandler (Context ctx) throws JsonProcessingException {
            ObjectMapper mapper = new ObjectMapper();//Do I really need message as the parameter?!
            Message message = mapper.readValue(ctx.body(), Message.class);
            int id = Integer.parseInt(ctx.pathParam("message_id"));
            Message deleted_message = MessageService.delete_Message_by_ID(id, message);
            System.out.println(message);
            if (deleted_message != null) {
                ctx.json(message);
                ctx.status(200);
            } else {
                //ctx.status(200);
            }
        }*/
/*As a user, I should be able to submit a PATCH request on the endpoint PATCH localhost:8080/messages/{message_id}.
 The request body should contain a new message_text values to replace the message identified by message_id.
 The request body can not be guaranteed to contain any other information.

The update of a message should be successful if and only if the message id already exists and
the new message_text is not blank and is not over 255 characters. If the update is successful,
the response body should contain the full updated message (including message_id, posted_by, message_text, and time_posted_epoch),
 and the response status should be 200, which is the default. The message existing on the database should have the updated message_text.
If the update of the message is not successful for any reason, the response status should be 400. (Client error)*/

    /*private void updateFlightHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Flight flight = mapper.readValue(ctx.body(), Flight.class);
        int flight_id = Integer.parseInt(ctx.pathParam("flight_id"));
        Flight updatedFlight = flightService.updateFlight(flight_id, flight);
        System.out.println(updatedFlight);
        if(updatedFlight == null){
            ctx.status(400);
        }else{
            ctx.json(mapper.writeValueAsString(updatedFlight));
        }

    }*/



    private void updateMessageByIDHandler (Context ctx) throws JsonProcessingException {
            ObjectMapper mapper = new ObjectMapper();
            Message message = mapper.readValue(ctx.body(), Message.class);
            int id = Integer.parseInt(ctx.pathParam("message_id"));
            Message updated_message = MessageService.update_Message_by_ID(id, message);
            if (updated_message == null) {
                ctx.status(400);
            } else {
                ctx.json(mapper.writeValueAsString(updated_message));
                ctx.status(200);
            }
        }
    /*As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/accounts/{account_id}/messages.

The response body should contain a JSON representation of a list containing all messages posted by a particular user,
 which is retrieved from the database. It is expected for the list to simply be empty if there are no messages.
 The response status should always be 200, which is the default.*/

    public void getAllMessagesForUser(Context context) throws SQLException {
        int userId = Integer.parseInt(context.pathParam("account_id"));
        List<Message> messages = messageService.getAllMessagesForUser(userId);
        context.json(messages);
    }

    /*private void getMessagesByParticularUser (Context ctx)throws JsonProcessingException {
            ObjectMapper mapper = new ObjectMapper();
            int id = Integer.parseInt(ctx.pathParam("posted_by"));
            List<Message> messages = MessageService.get_Messages_by_UserID(id);
            System.out.println(messages);
            ctx.json(messages);

        }*/




}