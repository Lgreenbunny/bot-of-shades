package myfirstbot;

import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

final class BotSpeak { 
    // classes that help the bot talk in servers
    //some of these methods may be overloaded
    private BotSpeak(){}

    //may be overloaded with different sources, like Channel, DM, etc. 
    static public void plainSpeak(MessageCreateEvent event, String text){
        //given a MessageCreate event, get the channel and send the given String
        MessageBuilder temp = new MessageBuilder();
        temp.append(text);
        temp.send(event.getChannel());
    }
    
}
