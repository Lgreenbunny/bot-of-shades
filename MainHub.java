//how to get a bot token https://discord.com/developers/docs/topics/oauth2#bots

/*notes:
 * can't make instances of many of the classes on here, have to fetch/get them.
 *
 *get iterators for collections later instead of converting all of them to arrayList
 *********optional<> class has a value that may/not be null, check .ifPresent() to see if there's a value, and
 *do .get() (just (), not an index) to get the actual value in there.
*/

/*TASKS
 * Make a ~help command that returns all of what the bot can do, done
   - update the ~help command whenever a command is added...
 *
 * Respond to a ping command, done
   - possibly with the time taken to ping, undone
 * 
 * Respond to a mention, done
 * 
 * DM a person who does a certain command
 * 
 * respond to something through DM
 * 
 * collect "votes" through reactions, or just count reactions after a certain time
   - maybe collect names of the reactors as well, so suspicion can be raised~ 
 *
 * Finally, try to do a game loop with all of these actions
 * *also remember to not start a game loop unless there's enough votes to join, at LEAST 3, but more is better
 * */

//https://github.com/Javacord/Example-Bot/blob/master/src/main/java/org/javacord/examplebot/

package myfirstbot;
import java.util.ArrayList;
//import org.json.simple.JSONObject;
import javax.json.Json;
import javax.json.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader; 
import java.util.Collection;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.intent.Intent;
//make messages
import org.javacord.api.util.logging.FallbackLoggerConfiguration;


//import org.javacord.api.util.logging.FallbackLoggerConfiguration;
public class MainHub {
	public static void main(String[] args) throws FileNotFoundException {
		
		FallbackLoggerConfiguration.setDebug(true); //does logging things

		//old: DiscordApi api = new DiscordApiBuilder().setToken("no").login().join();
		
		//get file to read from
		FileReader fly = new FileReader("./aResource/config.json");
		//change reader type to json
		JsonReader jread = Json.createReader(fly);
		//grab information from reader
		DiscordApi api = new DiscordApiBuilder()
				.setToken(jread.readObject().getString("token"))
				.setIntents(Intent.MESSAGE_CONTENT, Intent.GUILDS, Intent.GUILD_MEMBERS, Intent.GUILD_MESSAGES)
				.login().join();
		
		//close readerS
		
		
		System.out.println("Logged in!");
		
		try {
			/*starts the api, may be outdated, try .login().join() with discordapi? the bot still appears, tho
			 * https://javacord.org/wiki/getting-started/writing-your-first-bot.html#log-the-bot-in
			*/
			ArrayList<Channel> listOfChannels = start(api);

			//listens for messages and responds a certain way if needed
			CommandBehavior messageReading = new CommandBehavior(listOfChannels, api.getYourself());
			api.addMessageCreateListener(event ->{
				messageReading.onMessageCreate(event);});
			} catch(Exception e){
				System.out.printf("List of channels/command behavior broke in main:\n%s", e);
			}
	}
	
	//messageBuilder can make messages, Messageable can receive messages...
private static ArrayList<Channel> start(DiscordApi api){
	//talk at first
	/*MessageBuilder speech = new MessageBuilder();
	speech.append("hi");*/
	
	//get a channel by name, general atm
	//do you have to convert from collection to arraylist each time?
	//NOT this: ServerTextChannel channel = "657395214096400417";
	
	//Collection<Channel> list = api.getChannelsByName("general");
	Collection<Channel> list = api.getChannelsByName("bot-commands");
	ArrayList<Channel> listA = new ArrayList<Channel>(list);
	
	//send message(arraylist of channels index 0, convert to textChannel, if there's a textChannel return value)
	//speech.send(listA.get(0).asTextChannel().get());
	
	//normal execution in notes/discord, comment later
	return listA;
	}
}

