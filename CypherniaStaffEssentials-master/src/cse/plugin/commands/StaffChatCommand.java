package cse.plugin.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cse.plugin.Config;
import cse.plugin.StaffRanks;
import cse.plugin.listener.StaffChatListener;

public class StaffChatCommand extends CommandBase {

	public List<String> pchat = new ArrayList<String>();

	public StaffChatListener scl;
	public StaffChatCommand() {
		scl = new StaffChatListener(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (checkPerms(sender, "cse.staffChat", true)) {
			String user;
			if (sender instanceof Player) {
				user = ((Player) sender).getName();
			} else {
				user = "CONSOLE";
			}
			if (!this.pchat.contains(user)) {
				this.pchat.add(user);
				sender.sendMessage(Config.prefix + "Joined staff chat!");
			} else {
				this.pchat.remove(user);
				sender.sendMessage(Config.prefix + "Left staff chat!");
			}

			return true;
		}
		return false;
	}

	public String getChatPrefix(Player p) {
		return Config.staffChat.replaceAll("%player%", p.getName()).replaceAll("%rank%", StaffRanks.getRanks().getPrefix(p));
	}

	public String getChatPrefix(String s) {
		return Config.staffChat.replaceAll("%player%", s).replaceAll("%rank%", StaffRanks.getRanks().getPrefix(Bukkit.getPlayer(s)));
	}

}
