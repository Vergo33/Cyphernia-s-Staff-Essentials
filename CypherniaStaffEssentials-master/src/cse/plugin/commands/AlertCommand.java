package cse.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cse.plugin.Config;

public class AlertCommand extends CommandBase {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length >= 1) {

			String msg = "";
			String[] arrayOfString;
			int j = (arrayOfString = args).length;
			for (int i = 0; i < j; i++) {
				String s = arrayOfString[i];
				msg = msg + s + " ";
			}
			msg = msg.substring(0, msg.length() - 1);

			for (Player p : Bukkit.getOnlinePlayers()) {
				if (p.hasPermission("cse.staff")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', Config.alertMessageFormat)
							.replaceAll("%message%", msg)
							.replaceAll("%player%", sender.getName()));
				}
			}
			
			sender.sendMessage(ChatColor.AQUA + "Alerted all online staff members.");
		} else {
			sender.sendMessage(ChatColor.RED + "Usage: /staffalert <Message>");
		}
		return true;
	}
}
