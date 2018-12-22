package cse.plugin.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cse.plugin.Config;
import cse.plugin.listener.SilentJoinListener;

public class SilentJoinCommand extends CommandBase {

	public static List<UUID> silent = new ArrayList<UUID>();
	
	public SilentJoinListener sjl;
	public SilentJoinCommand() {
		sjl = new SilentJoinListener();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (checkPerms(sender, "cse.command.silentJoin", false)) {
			if (isPlayer(sender)) {
				if (!silent.contains(((Player) sender).getUniqueId())) {
					silent.add(((Player)sender).getUniqueId());
					sender.sendMessage(Config.prefix + "Hidden join messages!");
				} else {
					silent.remove(((Player)sender).getUniqueId());
					sender.sendMessage(Config.prefix + "Showing join messages!");
				}
			} else {
				sender.sendMessage("You can't run this from the console!");
			}
			return true;
		}
		return false;
	}

}
