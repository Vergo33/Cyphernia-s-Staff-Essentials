package cse.plugin.listener;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import cse.plugin.Config;
import cse.plugin.commands.MaintenanceCommand;

public class MaintenanceListener implements Listener {
	
	private MaintenanceCommand mc;
	public MaintenanceListener(MaintenanceCommand mc) {
		this.mc = mc;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		String msg = ChatColor.translateAlternateColorCodes('&', Config.maintenanceMessage);
		if ((mc.maintenance) && (!e.getPlayer().hasPermission("cse.bypass.maintenance"))) {
			e.setJoinMessage("");
			e.getPlayer().kickPlayer(msg);
		} else if (mc.maintenance) {
			e.getPlayer().sendMessage(ChatColor.AQUA + "You have been allowed to enter the server while in maintenance mode.");
		}
	}
}
