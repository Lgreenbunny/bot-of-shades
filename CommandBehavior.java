package myfirstbot;

import java.util.ArrayList;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class CommandBehavior implements MessageCreateListener{
	ArrayList<Channel> listOfChannels;
	Channel general;
	User actualBotUser;
	DiscordApi api;
	String pre;
	ReactionList reactions;
	MessageBuilder builder;
	CommandBehavior(ArrayList<Channel> list, User me){
		listOfChannels = list;
		general = listOfChannels.get(0);
		actualBotUser = me;
		//get your mention tag to check if someone mentioned you
		pre = "!!";
		builder = new MessageBuilder();

		reactions = new ReactionList(
			list.get(0).asServerChannel()
			.orElse(null).getServer()
		);
		
	}
	
	//when a message is created, this function's used to handle the message event
	@Override
	public void onMessageCreate(MessageCreateEvent event) {
		System.out.println("saw message");

		//MessageBuilder speech = new MessageBuilder();
		//this will still make it print: System.out.println(event.getMessageContent());
		//has multiple examples https://github.com/Javacord/Javacord
		String content = event.getMessageContent(); 
		User commandUser = event.getMessageAuthor().asUser().get();
		TextChannel channel = event.getChannel();
		
		if(content.matches(pre+"copy .*")) { //copy command, repeats your message
			String cut = content.replace(pre+"copy ", "");
			builder.appendCode("", cut);
			builder.append("\t~" + commandUser.getName());
			builder.send(channel);
			//event.getChannel().sendMessage("```"+ content.substring(6)+
			builder.removeContent();
		}
		
		if(content.equalsIgnoreCase(pre+"ping")) { 
			channel.sendMessage("received ping at " +
			event.getMessage().getCreationTimestamp()+
			" probably");
		}
		
		/*
		get the message and see if it pings the bot, or make a handler for a ping instead
		also when it's pinged, respond with the prefix and a message
		*/
		if(content.matches("<@!(" + actualBotUser.getId() + ")>.+")) 
			channel.sendMessage("y ping tho? smh");
		
		if(content.matches(pre+"((command)(s){0,1})|help")) {
			channel.sendMessage("```Help menu```\n"
			+pre+"ping`- ping thing\n"
			+pre+"copy [message]`- repeats your message... maybe\n"
			+pre+"startMurders` (:\n"
			+pre+"help/command[s]`- this message");
		}

		if(content.matches(pre + "auction(\\s)*")){
			reactions.eventListReacts(event);
		}
		
	}
}
