package myfirstbot;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import java.util.ArrayList;

public class ReactionList {
    /* In this class, it:
     * uses jsons to manage the saved messages and reaction meanings whenever the bot's shut down
     * Uses ReactObjects (info from json) to save which messages have reaction roles, and which reactions control which roles
     * makes event handlers, based on ReactObjects, that give or take a role of the person clicking the reaction
     * 
     * WHen a message's clicked:
     * event listener gets the user and message info and sends it to eventListenReacts()
     * ReactRole objects related to the message id AND reaction are found
     * based on the click/unclick, it adds/removes the role from the user    
     */
    private Role auctionRole;
    private Server serv;
    //constructor here...
    ReactionList(Server serv){
        this.serv = serv;
        //read json in that one folder
        //put target message and react info inside ReactObject classes
        //add event listeners for reaction clicks

        //for now, we can just use a command to turn them on as a test, so only the ReactObject will be setup:
        /* get auction role by a server object
         * then find the Role by searching by name (getRolesByNameIgnoreCase)
         * use the role object to check if the user has said role (hasUser)
         */
        auctionRole = (serv.getRolesByNameIgnoreCase("auction ping")).get(0);
    }
    

    //makes one or more event listeners for the messages
    //will be made private after the test command works
    public void eventListReacts(MessageCreateEvent event){
       MessageAuthor member = event.getMessageAuthor();
        //given an event, gets a member, message and reaction, 
        //get the ReactObject linked to the message 

        //if the role linked to the ReactObject matches the user's role, remove that role on unclick, 
        //otherwise add that role to them, 

        //make sure clicking doesnt remove the role, and unclicking doesnt add the role
        //send a message stating the change either in the chat or a DM, the chat could probably handle it...
        
        /*Role.addUser(user), 
        Role.hasUser(user)
        can use .getName() to get the role name if needed


        
        MessageCreateListener is used in CommandBehavior, which handles the test command for this
        so it'll send the user (getMessageAuthor()) from MessageCreateEvent for a user & role test:
        */
        
        //check role
        //if the Optional turns out to be null instead of an event, returns a null 
        User temp = null;
        if(serv != null){
            temp =  member.asUser().orElse(null);
        }
        else{
            //message here

        }
        if(temp != null){
            if(auctionRole.hasUser(temp)){
                auctionRole.removeUser(temp);
                //status message here
            }
            else{
                auctionRole.addUser(temp);
                //status message here
            }
        }
        else{
            //message here

        }
    }


    
    //uses the params to search for an exact match in the list of ReactObjects, null if no object found 
    private ReactObjects checkForRoleReact(){ return null;}

    //handles the clicks whenever an event listener picks up a click
    private void addRole(){
    }
    private void removeRole(){}



    //records the message, role and reaction info for a new react role message
    //also updates/makes a new event listener for this new message



    //delete the objects related to and the json information for a deleted message
    //or a message that was prompted to be un-reaction role'd
}

class ReactObjects{
    private String messageID;
    private String reactEmote;
    private String role;//possibly change based on how roles are implemented
    //ReactObjects(){}
    //setters
    //getters
} // only used with ReactionList