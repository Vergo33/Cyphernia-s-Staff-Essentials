package cse.plugin.listener;

import java.io.*;
import java.util.logging.Level;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import cse.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import cse.plugin.commands.StaffChatCommand;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class StaffChatListener implements Listener, PluginMessageListener {

	private StaffChatCommand scc;
	public StaffChatListener(StaffChatCommand scc) {
		this.scc = scc;
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		String p = e.getPlayer().getName();
		if (this.scc.pchat.contains(p)) {
			e.setCancelled(true);

			// Sending staff chat message to all servers //
			ByteArrayDataOutput out = ByteStreams.newDataOutput();

			out.writeUTF("Forward");
			out.writeUTF("ONLINE");
			out.writeUTF("StaffChat");

			ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
			DataOutputStream msgout = new DataOutputStream(msgbytes);
			try {
				msgout.writeUTF(e.getMessage());
				msgout.writeUTF(e.getPlayer().getName());
				msgout.writeUTF(Plugin.serverName);
			} catch (IOException exception){
				exception.printStackTrace();
			}

			out.writeShort(msgbytes.toByteArray().length);
			out.write(msgbytes.toByteArray());

			e.getPlayer().sendPluginMessage(Plugin.getInstance(), "BungeeCord", out.toByteArray());

			for (Player ps : Bukkit.getOnlinePlayers()) {
				if (ps.hasPermission("cse.staffChat")) {
					ps.sendMessage(this.scc.getChatPrefix(e.getPlayer()).replaceAll("%message%", e.getMessage()).replaceAll("%server%",
							Plugin.serverName));
				}
			}
			Bukkit.getLogger().log(Level.INFO, this.scc.getChatPrefix(e.getPlayer()).replaceAll("%message%", e.getMessage()).replaceAll("%server%",
					Plugin.serverName));
		}
	}

	@Override
	public void onPluginMessageReceived(String channel, Player p, byte[] message) {
		if (!channel.equals("BungeeCord")) {
			return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
		if (subchannel.equals("StaffChat")) {
			short length = in.readShort();
			byte[] msgbytes = new byte[length];
			in.readFully(msgbytes);

			try {
				DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
				String staffMessage = msgin.readUTF();
				String player = msgin.readUTF();
				String server = msgin.readUTF();

				for (Player ps : Bukkit.getOnlinePlayers()) {
					if (ps.hasPermission("cse.staffChat")) {
						ps.sendMessage(this.scc.getChatPrefix(player).replaceAll("%message%", staffMessage).replaceAll("%server%", server));
					}
				}
				Bukkit.getLogger().log(Level.INFO, this.scc.getChatPrefix(player).replaceAll("%message%", staffMessage).replaceAll("%server%", server));

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
