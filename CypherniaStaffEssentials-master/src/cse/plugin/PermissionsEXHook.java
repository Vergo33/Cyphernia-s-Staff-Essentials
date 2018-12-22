package cse.plugin;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.PermissionUser;

public class PermissionsEXHook {
	public org.bukkit.plugin.Plugin pex = null;

	public PermissionsEXHook() {
		pex = Plugin.getInstance().getServer().getPluginManager().getPlugin("PermissionsEx");
		if (pex != null) {
			Bukkit.getLogger().log(Level.INFO, Config.prefix + "PermissionEX found! You can setup staff ranks in PEX's config!");
		} else {
			Bukkit.getLogger().log(Level.INFO, Config.prefix + "PermissionEX not found! You will not be able to use staff ranks!");
		}
	}

	public boolean isEnabled() {
		return pex != null;
	}

	public String getPrefix(Player p) {
		PermissionUser pu = ru.tehkode.permissions.bukkit.PermissionsEx.getUser(p);
		return pu.getPrefix();
	}
}
