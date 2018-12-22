package cse.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cse.plugin.Config;
import cse.plugin.StaffRanks;

public class StaffListCommand extends CommandBase {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		for (String s : Config.staffListMessage) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s)
					.replaceAll("%staff%", getStaff())
					.replaceAll("%staffcount%", "" + getOnlineStaff()));
		}
		return false;
	}

	public int getOnlineStaff() {
		int i = 0;
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.hasPermission("cse.staff") && VanishCommand.vanished.contains(p) == false) {
				i++;
			}
		}
		return i;
	}

	public String getStaff() {
		String staff = "";
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.hasPermission("cse.staff") && VanishCommand.vanished.contains(p) == false) {
				staff = staff + StaffRanks.getRanks().getWithPrefix(p) + ", ";
			}
		}
		if (staff.equals("")) {
			return "None";
		}
		return staff.substring(0, staff.length() - 2);
	}

}
