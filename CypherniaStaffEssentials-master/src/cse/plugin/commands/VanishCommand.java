package cse.plugin.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import cse.plugin.Config;
import cse.plugin.Plugin;
import cse.plugin.StaffRanks;
import cse.plugin.listener.VanishListener;

public class VanishCommand extends CommandBase {

	public static List<Player> vanished = new ArrayList<Player>();

	public VanishListener vl;
	public VanishCommand() {
		vl = new VanishListener();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (checkPerms(sender, "cse.command.vanish", false)) {
			if (isPlayer(sender)) {
				Player p = (Player)sender;
				if (!this.isVanished(p)) {
					p.sendMessage(Config.prefix + "You have vanished!");
					this.vanish(p);
					List<String> vanishedPlayers = Config.vanishedPlayers;
					vanishedPlayers.add(p.getUniqueId().toString());
					Plugin plugin = Plugin.getInstance();
					FileConfiguration config = plugin.config;
					config.set("vanished-players", vanishedPlayers);
					plugin.saveConfig();
				} else {
					p.sendMessage(Config.prefix + "You have been revealed!");
					this.unvanish(p);
					try {
						List<String> vanishedPlayers = Config.vanishedPlayers;
						vanishedPlayers.remove(p.getUniqueId().toString());
						Plugin plugin = Plugin.getInstance();
						FileConfiguration config = plugin.config;
						config.set("vanished-players", vanishedPlayers);
						plugin.saveConfig();
					} catch (Exception ex) {}
				}
			} else {
				sender.sendMessage("You can't run this from the console!");
			}
			return true;
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	public void vanish(Player p) {
		vanished.add(p);
		String msg = Config.leaveMessage.replaceAll("%player%", StaffRanks.getRanks().getWithPrefix(p));
		boolean msgb = true;
		if (msg.equalsIgnoreCase("")) {
			msgb = false;
		}
		p.setPlayerListName(null);
		for (Player p2 : Bukkit.getOnlinePlayers()) {
			if (!p2.hasPermission("cse.seeVanished"))
				p2.hidePlayer(p);
			if (msgb && SilentJoinCommand.silent.contains(p.getUniqueId()) == false)
				p2.sendMessage(msg);
		}
	}

	@SuppressWarnings("deprecation")
	public void unvanish(Player p) {
		vanished.remove(p);
		String msg = Config.joinMessage.replaceAll("%player%", StaffRanks.getRanks().getWithPrefix(p));
		boolean msgb = true;
		if (msg.equalsIgnoreCase("")) {
			msgb = false;
		}
		p.setPlayerListName(StaffRanks.getRanks().getWithPrefix(p));
		for (Player p2 : Bukkit.getOnlinePlayers()) {
			if (!p2.hasPermission("cse.seeVanished"))
				p2.showPlayer(p);
			if (msgb && SilentJoinCommand.silent.contains(p.getUniqueId()) == false)
				p2.sendMessage(msg);
		}
	}
	public boolean isVanished(Player p) {
		return vanished.contains(p);
	}
}
