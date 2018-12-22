package cse.plugin;

import java.util.logging.Level;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import cse.plugin.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

public class Plugin extends JavaPlugin implements Listener, PluginMessageListener {
	public FileConfiguration config;
	private static Plugin plugin;
	public PermissionsEXHook pexhook;
	public StaffRanks staffRanks;
	public static String serverName;
	
	@Override
	public void onEnable() {
		plugin = this;
		this.config = Config.loadConfig(this);
		this.register();
		pexhook = new PermissionsEXHook();
		staffRanks = new StaffRanks();
		Bukkit.getLogger().log(Level.INFO, Config.prefix + "This plugin has been enabled!");
	}
	@Override
	public void onDisable() {
		Bukkit.getLogger().log(Level.INFO, Config.prefix + "This plugin has been disabled!");
	}
	public void setExecutor(CommandExecutor ce, String... cmd) { 
		String[] arrayOfString;
		int j = (arrayOfString = cmd).length; 
		for (int i = 0; i < j; i++) { 
			String s = arrayOfString[i];
			PluginCommand pc = this.getCommand(s);
			pc.setExecutor(ce);
		}
	}
	public void register() {
		ClearChatCommand ccc = new ClearChatCommand();
		FreezeCommand fc = new FreezeCommand();
		StaffChatCommand scc = new StaffChatCommand();
		SilentJoinCommand sjc = new SilentJoinCommand();
		StaffListCommand slc = new StaffListCommand();
		VanishCommand vc = new VanishCommand();
		MaintenanceCommand mc = new MaintenanceCommand();
		AlertCommand al = new AlertCommand();
		WarnCommand wc = new WarnCommand();
		CypherniaStaffEssentialsCommand csec = new CypherniaStaffEssentialsCommand();
		
		setExecutor(ccc, new String[] {"clearChat"});
		setExecutor(fc, new String[] {"freeze", "unfreeze", "fr", "uf"});
		setExecutor(scc, new String[] {"staffchat", "sc"});
		setExecutor(sjc, new String[] {"silentjoin", "sj"});
		setExecutor(slc, new String[] {"stafflist", "slist", "sl", "staffonline", "sonline", "so"});
		setExecutor(vc, new String[] {"vanish", "v"});
		setExecutor(mc, new String[] {"maintenance"});
		setExecutor(al, new String[] {"alert", "al"});
		setExecutor(wc, new String[] {"warn"});
		setExecutor(csec, new String[] {"CypherniaStaffEssentials", "cse"});

		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", scc.scl);
		this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(scc.scl, this);
		pm.registerEvents(sjc.sjl, this);
		pm.registerEvents(mc.ml, this);
		pm.registerEvents(vc.vl, this);
		pm.registerEvents(fc.fl, this);
		pm.registerEvents(this, this);
	}
	public static Plugin getInstance() {
		return plugin;
	}
	public static String getServerName() { return serverName; }

	public static void sendPluginMessage(String... arguments) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();

		for (String s : arguments) {
			out.writeUTF(s);
		}

		Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);

		player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
	}

	public static void sendPluginMessage(Player p, String... arguments) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();

		for (String s : arguments) {
			out.writeUTF(s);
		}

		p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord")) {
			return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
		if (subchannel.equals("GetServer")) {
			String name = in.readUTF();
			serverName = name;
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		getServer().getScheduler().scheduleAsyncDelayedTask(this, new BukkitRunnable() {
			@Override
			public void run() {
				sendPluginMessage(e.getPlayer(), "GetServer");
			}
		}, 20L);
	}
}
