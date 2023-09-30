package myfirstbot;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ReactionList{
    /* In this class, it:
     * uses jsons to manage the saved messages and reaction meanings whenever the bot's shut down
     * Uses ReactObjects (info from json) to save which messages have reaction roles, and which reactions control which roles
     * makes event handlers, based on ReactObjects, that give or take a role of the person clicking the reaction
     * 
     * When a message's clicked:
     * event listener gets the user and message info and sends it to eventListenReacts()
     * ReactRole objects related to the message id AND reaction are found
     * based on the click/unclick, it adds/removes the role from the user    
     */
    protected CommandRoleObject giveawayRole, incenseRole;
    protected Server serv;
    //curent role's this bot can manage: Giveaway pkmn (pingable), Incense
    ReactionList(Server serv) throws StreamReadException, DatabindException, IOException{
        this.serv = serv;
        /*add event listeners for reaction messages with a json and RoleObjects later */

        //for now, we can just use a command to turn them on as a test, so only the ReactObject will be setup:
        /* get auction role by a server object
         * then find the Role by searching by name (getRolesByNameIgnoreCase)
         * use the role object to check if the user has said role (hasUser)
         */
        HashMap<String, String> commands = jsonSetupCommandRole();
        giveawayRole = new CommandRoleObject(commands.get("giveaway"), "giveaway");
        incenseRole = new CommandRoleObject(commands.get("incense"),"incense");
    }
    

    //makes event listeners, or responds to commands based on the message and commandCheck
    public void eventListReacts(MessageCreateEvent event, String commandCheck){
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
            BotSpeak.plainSpeak(event, "Server wasn't found??");

        }

        //command check and method calls here
        if(temp != null){
            /*later on, this should use an array of RoleObjects and use the one that matches 
            CommandCheck*/
            if(commandCheck.matches(String.format(".*%s.*", giveawayRole.command)))
                plainRoleCommand(giveawayRole, commandCheck, temp, event);
                
            else if(commandCheck.matches(String.format(".*%s.*", incenseRole.command)))
                plainRoleCommand(incenseRole, commandCheck, temp, event);
        }
        else{
            BotSpeak.plainSpeak(event, "Member wasn't found??");

        }
    }
     

    //handle plain text commands involving role adding/removing
    private void plainRoleCommand(CommandRoleObject roleObj, String commandCheck, User temp, MessageCreateEvent event){
        //add or remove that role
        Role tempRole = roleObj.getRoleObject(serv);
        if(tempRole.hasUser(temp)){
            tempRole.removeUser(temp);
            BotSpeak.plainSpeak(event, 
                String.format("Removed %s role from %s.", 
                    roleObj.command,
                    temp.getNickname(serv).orElse("[someone]")));
        }
        else{
            tempRole.addUser(temp);
            BotSpeak.plainSpeak(event, 
                String.format("Added %s role to %s.", 
                    roleObj.command,
                    temp.getNickname(serv).orElse("[someone]")));
        }
    }
    
    //
    private HashMap<String, String> jsonSetupCommandRole() throws StreamReadException, DatabindException, IOException{
        //used for putting the entire string in the class later on
        ObjectMapper objMap = new ObjectMapper();
        //used to get all the data from the file all at once
        byte[] temp = Files.readAllBytes(Paths.get("./aResource/commandRole.json"));

        return objMap.readValue(temp, HashMap.class); // there's no HashMap<String, String> class
    }
    //edit json values and apply to file
    //remove json values and apply to file


    //handle anti-bot roles/new user roles
    /* one way: userRoleAddListener OR listen for any message button clicks (if it's able to see it)
    * whenever the chosen event happens, remove their newUser role if they had it.
    */


    /* whenever this command's used by an ADMIN, find all the users without roles 
     * and give them the newUser role 
     * this isn't a gigantic server but when you get the 
     * set of users, potentially convert it to a treeset with addAll, and implement the compareTo 
     * also, try to ignore any role additions while this process is going on*/


    /* whenever a new person joins the guild, immediately add the newUser role to them */
    

    //handle reactions given an emoji and message
    //to-do later







/* ******************************
    //uses the params to search for an exact match in the list of ReactObjects, null if no object found 
    private ReactRoleObject checkForRoleReact(){ return null;}

    //handles the clicks whenever an event listener picks up a click
    private void addRoleObject(){
    }
    private void removeRoleObject(){}
**********************************/





    /*whenever a reaction's added or removed, check if the messageID matches any ReactObject ids
     * if it does, check if the reactions match any
    */



    
    //records the message, role and reaction info for a new react role message here
    //makes another RoleObject for this role
    //also updates/makes a new event listener for this new message (soon)



    //delete the RoleObject related to the role/reaction deleted
    //deletes the json information related to the role
}

class ReactRoleObject{
    //if this is a reaction role, command will be null, otherwise messageID and reactEmote will be null.
    protected int messageID; //which message this reaction is attached to
    protected String reactEmote; //which emote this role represents
    protected String theRole; //Role needs to be found from the list of roles in the given server
    protected String command; //which command triggers this role change
    ReactRoleObject(String theRole, int message, String reactEmote){
        this.theRole = theRole;
        command = null;
        this.reactEmote = reactEmote;
        messageID = message;
    }
    //set & get methods here
    public int getMessageID() {
        return messageID;
    }
    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }
    public String getReactEmote() {
        return reactEmote;
    }
    public void setReactEmote(String reactEmote) {
        this.reactEmote = reactEmote;
    }
    public String getTheRole() {
        return theRole;
    }
    public void setTheRole(String theRole) {
        this.theRole = theRole;
    }
    public String getCommand() {
        return command;
    }
    public void setCommand(String command) {
        this.command = command;
    }

    /* TBA
    public Role getRoleObject(Server serv) {
        return (serv.getRolesByNameIgnoreCase(theRole)).get(0);
    }
    public Message getMessageObject(Server serv) {
        return ;
    }
    public Emoji getEmojiObject(Server serv) {
        return ;
    }*/

} // only used with ReactionList

class CommandRoleObject{
    //if this is a reaction role, command will be null, otherwise messageID and reactEmote will be null.
    protected String theRole; //Role needs to be found from the list of roles in the given server
    protected String command; //which command triggers this role change
    CommandRoleObject(String theRole, String command){
        this.theRole = theRole;
        this.command = command;
    }
    //set & get methods here
    public String getRole() {
        return theRole;
    }
    public Role getRoleObject(Server serv) {
        return (serv.getRolesByNameIgnoreCase(theRole)).get(0);
    }
    public String getCommand() {
        return command;
    }
    public void setRole(String theRole) {
        this.theRole = theRole;
    }
    public void setCommand(String command) {
        this.command = command;
    }
} // only used with ReactionList