package cse.plugin.commands;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cse.plugin.Config;

public class WarnCommand extends CommandBase {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (checkPerms(sender, "cse.command.warn", true)) {
			Player plr = (Player) sender;
			if (args.length >= 2) {
				String target = args[0];
				if (Bukkit.getPlayer(target).isOnline()) {
					String reason = "";
					for (int i = 1; i < args.length; i++) {
						reason = reason + args[i] + " ";
					}
					reason = reason.substring(0, reason.length() - 1);
					String msg = Config.warnMessageFormat.replaceAll("%reason%", reason);
					
					try {
						ByteArrayOutputStream b = new ByteArrayOutputStream();
						DataOutputStream out = new DataOutputStream(b);
						
						out.writeUTF("Message");
						out.writeUTF(target);
						out.writeUTF(msg);

					} catch (Exception e) {
						e.printStackTrace();
					}
					
					sender.sendMessage(Config.prefix + "Warned player!");
					return true;
					} else { 
						plr.sendMessage(ChatColor.RED + target + " is not online!");
					} 
					
				
			} else {
				plr.sendMessage(ChatColor.RED + "Usage: /warn [Player] [Reason]");
			}
		
	}
		return false;
	}

}
