package cse.plugin;

import org.bukkit.entity.Player;

public class StaffRanks {

	private static StaffRanks sr;
	public StaffRanks() {
		sr = this;
	}

	public String getPrefix(Player p) {
		if (Plugin.getInstance().pexhook.isEnabled()) {
			return org.bukkit.ChatColor.translateAlternateColorCodes('&', Plugin.getInstance().pexhook.getPrefix(p));
		}
		return "";
	}
	public String getWithPrefix(Player p) {
		if (getPrefix(p).equals("")) {
			return p.getDisplayName();
		}
		return getPrefix(p) + p.getName();
	}
	public static StaffRanks getRanks() {
		return sr;
	}
}
