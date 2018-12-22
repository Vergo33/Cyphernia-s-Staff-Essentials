package cse.plugin.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import cse.plugin.Config;
import cse.plugin.StaffRanks;
import cse.plugin.commands.SilentJoinCommand;

public class SilentJoinListener implements Listener {

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent e) {
		if (SilentJoinCommand.silent.contains(e.getPlayer().getUniqueId())) {
			e.setJoinMessage("");
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (p.hasPermission("cse.staff")) {
					Player joined = e.getPlayer();
					p.sendMessage(Config.seeSilentJoinMessage.replaceAll("%player%", StaffRanks.getRanks().getPrefix(joined) + joined.getName()));
				}
			}
		}
	}

}
