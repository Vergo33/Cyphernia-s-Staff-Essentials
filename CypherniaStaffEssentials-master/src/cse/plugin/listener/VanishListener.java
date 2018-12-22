package cse.plugin.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import cse.plugin.commands.VanishCommand;

public class VanishListener implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler(priority=EventPriority.HIGH)
	public void onPlayerJoin(PlayerJoinEvent e) {
		for (Player p : VanishCommand.vanished) {
			if (!e.getPlayer().hasPermission("cse.seeVanished")) {
				e.getPlayer().hidePlayer(p);
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority=EventPriority.HIGH)
	public void onPlayerQuit(PlayerQuitEvent e) {
		for (Player p : VanishCommand.vanished) {
			if (!e.getPlayer().hasPermission("cse.seeVanished")) {
				e.getPlayer().showPlayer(p);
			}
		}
	}


}
