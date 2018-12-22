package cse.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cse.plugin.Config;
import cse.plugin.StaffRanks;

public class ClearChatCommand extends CommandBase {

	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (checkPerms(sender, "cse.command.clearchat", true)) {
			if (args.length == 1) {
				Player target = Bukkit.getPlayer(args[0]);
				if (!isOnline(sender, target)) {
					return true;
				} else {
					if (sender instanceof Player) {
						clearChat(target, StaffRanks.getRanks().getPrefix((Player) sender) + sender.getName());
					} else {
						clearChat(target, sender.getName());
					}
					sender.sendMessage(Config.prefix + "Cleared " + args[0] + "'s Chat!");
				}
			} else if (args.length == 0) {
				for(Player p : Bukkit.getOnlinePlayers()){
					if (sender instanceof Player) {
						clearChat(p, StaffRanks.getRanks().getPrefix((Player) sender) + sender.getName());
					} else {
						clearChat(p, sender.getName());
					}
				}
				sender.sendMessage(Config.prefix + "Cleared everyone's Chat!");
			} else {
				sender.sendMessage(Config.toManyArgs);
			}
			return true;
		} else {
			sender.sendMessage(Config.noPermsMessage);
		}
		return false;
	}

	public void clearChat(Player p, String name) {
		String msg = ChatColor.translateAlternateColorCodes('&', Config.clearedChatMessage);
		msg = msg.replaceAll("%player%", name);
		for (int i = 0; i < 100; i++) {
			p.sendMessage(" ");
		}
		p.sendMessage(msg);
	}

}
