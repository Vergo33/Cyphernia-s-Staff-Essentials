package cse.plugin.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import cse.plugin.Config;
import cse.plugin.Plugin;
import cse.plugin.StaffRanks;
import cse.plugin.listener.FreezeListener;

public class FreezeCommand extends CommandBase {

	public List<UUID> freeze = new ArrayList<UUID>();
	public HashMap<Player, Location> loc = new HashMap<Player, Location>();
	public Integer cronId = null;
	public FreezeListener fl;

	public FreezeCommand() {
		this.fl = new FreezeListener(this);
		Plugin plugin = Plugin.getInstance();
		FileConfiguration config = plugin.config;
		try {
			List<String> frozenPlayers = config.getStringList("frozen-players");
			for (String str : frozenPlayers) {
				String[] items = str.split(",");

				for (Player p : Bukkit.getServer().getOnlinePlayers()) {
					if (p.getUniqueId().toString().equalsIgnoreCase(items[0].replaceAll(",", ""))) {
						freeze.add(p.getUniqueId());
						World world = Bukkit.getWorld(items[4].replaceAll(",", ""));
						double x = Double.parseDouble(items[1].replaceAll(",", ""));
						double y = Double.parseDouble(items[2].replaceAll(",", ""));
						double z = Double.parseDouble(items[3].replaceAll(",", ""));
						Location newLoc = new Location(world, x, y, z);
						loc.put(p, newLoc);
					}
				}
			}
			if (freeze.size() > 0) {
				startRepeatingTask(Plugin.getInstance());
			}
		} catch (Exception ex) {}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (checkPerms(sender, "cse.command.freeze", true)) {
			if (label.equalsIgnoreCase("freeze") || label.equalsIgnoreCase("fr")) {
				if (args.length == 1) {
					freeze(args[0], sender);
				} else if (args.length == 0) {
					freezeAll(sender);
				} else {
					sender.sendMessage(Config.toManyArgs);
				}
			} else if (label.equalsIgnoreCase("unfreeze") || label.equalsIgnoreCase("uf")) {
				if (args.length == 1) {
					unfreeze(args[0], sender);
				} else if (args.length == 0) {
					unfreezeAll(sender);
				} else {
					sender.sendMessage(Config.toManyArgs);
				}
			}
			return true;
		}
		return false;
	}

	public void freeze(String str, CommandSender sender) {
		Player p = null;
		try {
			p = Bukkit.getPlayer(str);
			p.getLocation();
		} catch (Exception ex) {
			sender.sendMessage(Config.prefix + "That player is not online!");
		} finally {
			if (!this.freeze.contains(p.getUniqueId())) {
				if (!p.hasPermission("cse.bypass.freeze")) {
					this.freeze.add(p.getUniqueId());
					this.updateFreezeConfig();
					p.sendMessage(Config.freezeMessage);
					this.loc.put(p, p.getLocation());
					sender.sendMessage(ChatColor.GREEN + "Successfully frozen player " + StaffRanks.getRanks().getWithPrefix(p));
					if (this.cronId == null) {
						startRepeatingTask(Plugin.getInstance());
					}
				} else {
					sender.sendMessage(Config.prefix + "That player has the freeze bypass.");
				}
			} else {
				sender.sendMessage(Config.prefix + "That player is already frozen.");
			}
		}
	}

	@SuppressWarnings({ "deprecation", "unlikely-arg-type" })
	public void unfreeze(String str, CommandSender sender) {
		Player p = null;
		try {
			p = Bukkit.getPlayer(str);
			p.getLocation();
		} catch (Exception ex) {
			Plugin plugin = Plugin.getInstance();
			FileConfiguration config = plugin.config;
			List<String> frozen = config.getStringList("frozen-players");
			for (String frozenItem : frozen) {
				String[] items = frozenItem.split(",");
				if (Bukkit.getOfflinePlayer(str).hasPlayedBefore()) {
					if (items[0].replaceAll(",", "").equalsIgnoreCase(Bukkit.getOfflinePlayer(str).getUniqueId().toString())) {
						frozen.remove(frozenItem);
						updateFreezeConfig();
						sender.sendMessage(Config.prefix + "Removed that offline player from the configuration file. They will no longer be frozen when they login.");
						return;
					}
				} else {
					sender.sendMessage(Config.prefix + "Error code 11: could not remove that offline player from the configuration file, since they either have not played on this server, or have not logged in in the last 30 days. It is advised to alert Jared or Kai.");
					return;
				}
			}
		} finally {
			if ((this.freeze.isEmpty()) || (!this.freeze.contains(p.getUniqueId()))) {
				sender.sendMessage(Config.prefix + "That player is not frozen.");
			} else {
				int i = 0;
				int i2 = 0;
				for (UUID uuid : this.freeze) {
					if (uuid.toString().equalsIgnoreCase(p.getUniqueId().toString())) {
						i2 = i;
						this.loc.remove(Integer.valueOf(i));
					}
					i++;
				}
				this.freeze.remove(i2);
				this.updateFreezeConfig();
				p.sendMessage(Config.unfreezeMessage);
				sender.sendMessage(ChatColor.GREEN + "Successfully unfrozen player " + StaffRanks.getRanks().getWithPrefix(p));
				if (this.freeze.size() == 0) {
					cancelRepeatingTask(Plugin.getInstance());
				}
			}
		}
	}

	public void freezeAll(CommandSender sender) {
		int playersFrozen = 0;
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (!p.hasPermission("cse.bypass.freeze")) {
				playersFrozen++;
				this.freeze.add(p.getUniqueId());
				this.updateFreezeConfig();
				p.sendMessage(Config.freezeMessage);
				this.loc.put(p, p.getLocation());
			}
		}
		sender.sendMessage(Config.prefix + "Successfully frozen all players!");
		if (this.cronId == null && playersFrozen > 0) {
			startRepeatingTask(Plugin.getInstance());
		}
	}
	public void unfreezeAll(CommandSender sender) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (this.freeze.contains(p.getUniqueId())) {
				p.sendMessage(Config.unfreezeMessage);
			}
		}
		this.freeze.clear();
		this.updateFreezeConfig();
		this.loc.clear();
		sender.sendMessage(Config.prefix + "Successfully unfrozen all players!");
		if (this.cronId != null) {
			cancelRepeatingTask(Plugin.getInstance());
		}
	}

	public void cancelRepeatingTask(Plugin plugin) {
		BukkitScheduler scheduler = plugin.getServer().getScheduler();
		scheduler.cancelTask(this.cronId);
		this.cronId = null;
	}
	public void startRepeatingTask(Plugin plugin) {
		BukkitScheduler scheduler = plugin.getServer().getScheduler();
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				for (UUID uuid : freeze) {
					try {
						Player player = Bukkit.getPlayer(uuid);
						if (loc.get(player).distance(player.getLocation()) >= 0.2D) {
							Location l = loc.get(player);
							Location l2 = new Location(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ());
							l2.setPitch(player.getLocation().getPitch());
							l2.setYaw(player.getLocation().getYaw());
							player.teleport(l2);
						}
					} catch (Exception ex) {}
				}
			}
		};
		int cronId = scheduler.scheduleSyncRepeatingTask(plugin, runnable, 0L, 5);
		this.cronId = cronId;
	}

	public void updateFreezeConfig() {
		Plugin plugin = Plugin.getInstance();
		FileConfiguration config = plugin.config;
		List<String> frozenPlayers = new ArrayList<String>();
		for (UUID uuid : freeze) {
			Player p = Bukkit.getPlayer(uuid);
			String uuid2 = p.getUniqueId().toString();
			String x = p.getLocation().getX() + "";
			String y = p.getLocation().getY() + "";
			String z = p.getLocation().getZ() + "";
			String world = p.getWorld().getName();
			frozenPlayers.add(uuid2 + "," + x + "," + y + "," + z + "," + world);
		}
		config.set("frozen-players", frozenPlayers);
		plugin.saveConfig();
	}

	public boolean isOnline(UUID uuid) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.getUniqueId().toString().equalsIgnoreCase(uuid.toString())) {
				return true;
			}
		}
		return false;
	}

}
