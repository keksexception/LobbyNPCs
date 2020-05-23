package de.raffi.lobbynpcs.utils;

import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import de.raffi.lobbynpcs.main.LobbyNPCs;

public class Bungee {
	


	public static final void sendToServer(String server, Player player) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Connect");
		out.writeUTF(server);
		player.sendPluginMessage(LobbyNPCs.getInstance(), "BungeeCord", out.toByteArray());
	}

	

}
