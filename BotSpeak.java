package myfirstbot;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

final class BotSpeak { 
    // classes that help the bot talk in servers
    //some of these methods may be overloaded
    private BotSpeak(){}

    //may be overloaded with different sources, like Channel, DM, etc. 
    //given a MessageCreate event, get the channel and send the given String
    static public void plainSpeak(MessageCreateEvent event, String text){
        MessageBuilder temp = new MessageBuilder();
        temp.append(text);
        temp.send(event.getChannel());
    }
    
    //puts a whole list of text in a message, sometimes with formatting
    static public void listSpeak(TextChannel channel, MessageBuilder build, String type, String text){
    }
}
