package de.raffi.lobbynpcs.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import de.raffi.pluginlib.npc.NPC;
import net.labymod.serverapi.bukkit.LabyModPlugin;

public class LabyModHook {

	public static void sendEmote(Player p, NPC npc, int id) {
		JsonArray array = new JsonArray();
		JsonObject forcedEmote = new JsonObject();
		forcedEmote.addProperty("uuid", npc.getProfile().getId().toString());
		forcedEmote.addProperty("emote_id", (Number) id);
		array.add((JsonElement) forcedEmote);
		LabyModPlugin.getInstance().sendServerMessage(p, "emote_api", (JsonElement) array);
	}
	public static boolean isLabyModInstalled() {
		return Bukkit.getPluginManager().getPlugin("LabyModAPI")!=null;
	}

}
