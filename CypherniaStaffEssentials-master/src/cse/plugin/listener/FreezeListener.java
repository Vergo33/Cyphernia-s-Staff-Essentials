package cse.plugin.listener;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import cse.plugin.Plugin;
import cse.plugin.commands.FreezeCommand;

public class FreezeListener implements Listener {

	private FreezeCommand fc;
	public FreezeListener(FreezeCommand fc) {
		this.fc = fc;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerJoin(PlayerJoinEvent e) {
		List<String> frozenPlayers = Plugin.getInstance().config.getStringList("frozen-players");
		fc.freeze.clear();
		for (String str : frozenPlayers) {
			String[] items = str.split(",");
			for (Player p : Bukkit.getServer().getOnlinePlayers()) {
				if (p.getUniqueId().toString().equalsIgnoreCase(items[0].replaceAll(",", ""))) {
					fc.freeze.add(p.getUniqueId());
					World world = Bukkit.getWorld(items[4].replaceAll(",", ""));
					double x = Double.parseDouble(items[1].replaceAll(",", ""));
					double y = Double.parseDouble(items[2].replaceAll(",", ""));
					double z = Double.parseDouble(items[3].replaceAll(",", ""));
					Location newLoc = new Location(world, x, y, z);
					fc.loc.put(p, newLoc);
				}
			}
		}
		if (fc.freeze.size() > 0 && fc.cronId == null) {
			fc.startRepeatingTask(Plugin.getInstance());
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerQuit(PlayerQuitEvent e) {
		List<String> uuids = Plugin.getInstance().config.getStringList("frozen-players");
		boolean online = false;
		for (String str : uuids) {
			String[] items = str.split(",");
			for (Player p : Bukkit.getServer().getOnlinePlayers()) {
				if (!p.getName().equalsIgnoreCase(e.getPlayer().getName())) {
					try {
						if (p.getUniqueId().toString().equalsIgnoreCase(items[0].replaceAll(",", ""))) {
							online = true;
						}
					} catch (Exception ex) {}
				}
			}
		}
		if (online == false && fc.cronId != null) {
			fc.cancelRepeatingTask(Plugin.getInstance());
		}
	}

}
