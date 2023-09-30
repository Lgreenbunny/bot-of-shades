package myfirstbot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class CommandBehavior implements MessageCreateListener{
	ArrayList<Channel> listOfChannels;
	ArrayList<String> tatsuCommand;
	Channel general;
	User actualBotUser;
	DiscordApi api;
	String pre;
	ReactionList reactions;
	MessageBuilder builder;
	String[] commands;
	CommandBehavior(ArrayList<Channel> list, User me) throws FileNotFoundException{
		listOfChannels = list;
		general = listOfChannels.get(0);
		actualBotUser = me;
		//get your mention tag to check if someone mentioned you
		pre = "!!";
		builder = new MessageBuilder();

		commands = new String[]{"ping", "ping response for the packets and the thign",
		"copy [message]", "repeats your message... maybe",
		"giveaway", "lets you get notified for auctions of some type :thonk:",
		"incense", "lets you do incenses in that one channel without needing mod/admin",
		"startMurders", "(:",
		"wholesome", "very pure command, multiple responses",
		"help/command[s]", "shows this whole message again"};
		
		try {
			reactions = new ReactionList(
				list.get(0).asServerChannel()
				.orElse(null).getServer()
			);
		} catch (IOException e) {
			e.printStackTrace();
		}

		tatsuCommand = new ArrayList<>();
		//this will be loaded with a json at a later time
		Scanner temp = new Scanner(new File("./aResource/theTatsuCommand.txt"));
		while(temp.hasNextLine())
			tatsuCommand.add(temp.nextLine());
		temp.close();
		
	}
	
	//when a message is created, this function's used to handle the message event
	@Override
	public void onMessageCreate(MessageCreateEvent event) {
		System.out.println("saw message");
		
		//MessageBuilder speech = new MessageBuilder();
		//this will still make it print: System.out.println(event.getMessageContent());
		//has multiple examples https://github.com/Javacord/Javacord
		String content = event.getMessageContent(); 

		if(content.length() > 2 && 
			content.substring(0, pre.length()).matches(".*" + pre + ".*")){

			User commandUser = event.getMessageAuthor().asUser().get();
			TextChannel channel = event.getChannel();
			String comCheck = content.substring(
				Math.min(content.length(), pre.length()), 
				Math.min(20, content.length()));

			if(comCheck.matches("copy .*")) { //copy command, repeats your message
				String cut = content.replace(pre+"copy ", "");
				builder.appendCode("java", cut);
				builder.append("\t~" + commandUser.getName());
				builder.send(channel);
				//event.getChannel().sendMessage("```"+ content.substring(6)+
				//builder.removeContent();
				builder.setContent("");
			}
			
			if(comCheck.equalsIgnoreCase("ping")) { //potentially reword this
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
			
			if(comCheck.matches("(command|help)(s)*")) {
				builder.appendCode("","help menu");
				BotSpeak.listSpeak(channel, builder, pre, commands);
				builder.send(channel);
				builder.setContent("");
				//builder.removeContent();
			}

			if(comCheck.matches("wholesome.*")){
				BotSpeak.plainSpeak(event, 
					tatsuCommand.get((int)(Math.random()*(tatsuCommand.size()-1)))
					);
			}
			//check the other commands in ReactionList
			reactions.eventListReacts(event, comCheck);
		}
	}
}
