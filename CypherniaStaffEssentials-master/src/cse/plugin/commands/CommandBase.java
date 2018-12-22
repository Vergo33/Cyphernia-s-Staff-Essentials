package cse.plugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cse.plugin.Config;

public abstract class CommandBase implements CommandExecutor {
	public boolean checkPerms(CommandSender sender, String perm, boolean console) {
		if (sender.hasPermission(perm) || sender.isOp()) {
			if (sender instanceof Player) {
				return true;
			} else {
				if (console) {
					return true;
				}
			}
		}
		sender.sendMessage(Config.noPermsMessage);
		return false;
	}
	public boolean isOnline(CommandSender sender, Player p) {
		if (p != null) {
			return true;
		}
		sender.sendMessage(Config.prefix + ChatColor.RED + "That player isn't online!");
		return false;
	}
	public boolean isPlayer(CommandSender sender) {
		if ((sender instanceof Player)) {
			return true;
		}
		sender.sendMessage(Config.prefix + "You have to be a player to use this command!");
		return false;
	}
}
