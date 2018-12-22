package cse.plugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import cse.plugin.Config;
import cse.plugin.Plugin;
import cse.plugin.listener.MaintenanceListener;

public class MaintenanceCommand extends CommandBase {

	public boolean maintenance = false;
	
	public MaintenanceListener ml;
	public MaintenanceCommand() {
		ml = new MaintenanceListener(this);
		maintenance = Plugin.getInstance().config.getBoolean("maintenance");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (checkPerms(sender, "cse.command.maintenance", true)) {
			if (!maintenance) {
				if (args.length == 0) {
					enable();
					sender.sendMessage(Config.prefix + "Maintenance mode enabled for reason: " + Config.maintenanceMessage);
					Plugin plugin = Plugin.getInstance();
					FileConfiguration config = plugin.config;
					Config.maintenance = true;
					config.set("maintenance", true);
					plugin.saveConfig();
				} else {
					String reason = "";
					for (int i = 0; i < args.length; i++) {
						reason = reason + args[i];
						if (i != args.length - 1) {
							reason = reason + " ";
						}
					}	
					Config.maintenanceMessage = reason;
					Plugin.getInstance().config.set("maintenance-message", reason);
					Plugin.getInstance().saveConfig();
					enable();
					sender.sendMessage(Config.prefix + "Maintenance mode enabled for reason: " + Config.maintenanceMessage);
				}
			} else {
				disable();
				sender.sendMessage(Config.prefix + "Maintenance mode disabled!");
				Plugin plugin = Plugin.getInstance();
				FileConfiguration config = plugin.config;
				Config.maintenance = false;
				config.set("maintenance", false);
				plugin.saveConfig();
			}
			return true;
		}

		return false;
	}
	
	

	public void enable() {
		this.maintenance = true;
		String msg = ChatColor.translateAlternateColorCodes('&', Config.maintenanceMessage);
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (!p.hasPermission("cse.bypass.maintenace")) {
				p.kickPlayer(msg);
			}
		}
	}

	public void disable() {
		this.maintenance = false;
	}

}
