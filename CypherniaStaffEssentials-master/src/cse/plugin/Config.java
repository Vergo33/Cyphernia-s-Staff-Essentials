package cse.plugin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {
	
	
	public static String prefix;
	public static String staffChat;
	
	public static String noPermsMessage;
	public static String toManyArgs;
	
	public static String clearedChatMessage;
	
	public static String freezeMessage;
	public static String unfreezeMessage;
	
	public static String seeSilentJoinMessage;
	
	public static List<String> staffListMessage = new ArrayList<String>();
	
	public static String leaveMessage;
	public static String joinMessage;
	
	public static String maintenanceMessage;
	
	public static String alertMessageFormat;
	
	public static String warnMessageFormat;
	
	public static boolean maintenance;
	public static List<String> vanishedPlayers;
	
	public static FileConfiguration loadConfig(Plugin plugin) {
		FileConfiguration config = plugin.getConfig();
		config.addDefault("prefix", "&7[&aCyphernia's Staff Essentials&7] &r");
		config.addDefault("staff-chat", "&7[&aSC&7] [&a%server%&7]&r %rank%%player%: %message%");
		
		config.addDefault("no-perms-message", "&cYou do not have permission to do that!");
		config.addDefault("to-many-args", "&bYou entered in too many arguments!");
		
		config.addDefault("cleared-chat-message", "&bChat has been cleared by: %player%");
		
		config.addDefault("freeze-message", "&bYou have been frozen!");
		config.addDefault("unfreeze-message", "&bYou have been unfrozen!");
		
		config.addDefault("see-silent-join-message", "&b%player% has joined silently!");
				
		List<String> staffListMessageList = new ArrayList<String>();
		staffListMessageList.add("&aStaff Online: %staffcount%");
		staffListMessageList.add("&bStaff with Name: %staff%");
		config.addDefault("staff-list-message", staffListMessageList);
		
		config.addDefault("leave-message", "&e%player% has left the game");
		config.addDefault("join-message", "&e%player% has joined the game");
		
		config.addDefault("maintenance-message", "The server is in maintenance mode.");
		
		config.addDefault("alert-message-format", "&7[&cStaffAlert&7]&r %player%: %message%");
		
		config.addDefault("warn-message-format", "&7[&cWarning&7]&r You have been warned for \"%reason%\"!");
		
		config.addDefault("maintenance", false);
		config.addDefault("vanished-players", new ArrayList<String>());
		
		config.options().copyDefaults(true);
		plugin.saveDefaultConfig();
		
		
		prefix = ChatColor.translateAlternateColorCodes('&', config.getString("prefix"));
		staffChat = ChatColor.translateAlternateColorCodes('&', config.getString("staff-chat"));
		
		noPermsMessage = ChatColor.translateAlternateColorCodes('&', config.getString("no-perms-message"));
		toManyArgs = ChatColor.translateAlternateColorCodes('&', config.getString("to-many-args"));
		
		clearedChatMessage = ChatColor.translateAlternateColorCodes('&', config.getString("cleared-chat-message"));
		
		freezeMessage = ChatColor.translateAlternateColorCodes('&', config.getString("freeze-message"));
		unfreezeMessage = ChatColor.translateAlternateColorCodes('&', config.getString("unfreeze-message"));
		
		seeSilentJoinMessage = ChatColor.translateAlternateColorCodes('&', config.getString("see-silent-join-message"));
		
		for (String str : config.getStringList("staff-list-message")) {
			staffListMessage.add(ChatColor.translateAlternateColorCodes('&', str));
		}
		
		leaveMessage = ChatColor.translateAlternateColorCodes('&', config.getString("leave-message"));
		joinMessage = ChatColor.translateAlternateColorCodes('&', config.getString("join-message"));
		
		maintenanceMessage = ChatColor.translateAlternateColorCodes('&', config.getString("maintenance-message"));
		
		alertMessageFormat = ChatColor.translateAlternateColorCodes('&', config.getString("alert-message-format"));
		
		warnMessageFormat = ChatColor.translateAlternateColorCodes('&', config.getString("warn-message-format"));
		
		maintenance = config.getBoolean("maintenance");
		vanishedPlayers = config.getStringList("vanished-players");
		
		return config;
	}
}
