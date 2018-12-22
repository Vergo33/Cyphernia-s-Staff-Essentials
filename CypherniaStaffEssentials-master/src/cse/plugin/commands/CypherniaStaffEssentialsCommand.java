package cse.plugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CypherniaStaffEssentialsCommand extends CommandBase {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (checkPerms(sender, "cse.staff", true)) {
			sender.sendMessage(ChatColor.GOLD + "Cyphernia's Staff Essentials Help Page:");
			sender.sendMessage(ChatColor.GOLD + "<> - required   [] - optional");
			sender.sendMessage(ChatColor.GREEN + "/alert <message> - alert all online staff members (permission to get alerted: cse.staff)");
			sender.sendMessage(ChatColor.GREEN + "/clearchat [player] - clear's a players chat. If no player is specified, then clears all players' chat. (permmission: cse.command.clearChat)");
			sender.sendMessage(ChatColor.GREEN + "/freeze/unfreeze [player] - freezes a player. If no player is specified, then freeze all players. (permission: cse.command.freeze  bypass: cse.bypass.freeze)");
			sender.sendMessage(ChatColor.GREEN + "/maintenance [reason] - enables maintenace mode. All players will be kicked, and non allowed to join (permission: cse.command.maintenance  bypass: cse.bypass.maintenance)");
			sender.sendMessage(ChatColor.GREEN + "/silentjoin - enables silent join mode. No one will see your join message (permission: cse.command.silentjoin  permission to see silent join message: cse.staff)");
			sender.sendMessage(ChatColor.GREEN + "/staffchat - enables staff chat mode. all messages you send will go to the staffchat channel (permission: cse.command.staffChat)");
			sender.sendMessage(ChatColor.GREEN + "/stafflist - lists all online staff members (permission to be a staff: cse.staff)");
			sender.sendMessage(ChatColor.GREEN + "/vanish - vanishes/unvanishes the player. No one will see you in tab either (permission: cse.command.vanish  permission to see vanished players: cse.seeVanished");
			sender.sendMessage(ChatColor.GREEN + "/warn <player> <message> - warns the player in chat (permission: cse.command.warn)");
			sender.sendMessage(ChatColor.AQUA + "You are running " + ChatColor.GOLD + "Cyphernia's Staff Essentials v1.3.4" + ChatColor.AQUA + " - By kai1029, Cyphernia Dev");
		}
		return false;
	}
	
}
