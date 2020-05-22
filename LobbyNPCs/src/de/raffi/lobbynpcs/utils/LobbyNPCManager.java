package de.raffi.lobbynpcs.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import de.raffi.pluginlib.npc.NPC;
import de.raffi.pluginlib.npc.NPCData;
import de.raffi.pluginlib.npc.NPCManager;
import de.raffi.pluginlib.serialization.ObjectHelper;

public class LobbyNPCManager {
	
	public static List<NPCData> data = new ArrayList<>();
	private static final File dataFile = new File("plugins/LobbyNPCs", "npcdata.dat");
	private static final File npcdat = new File("plugins/LobbyNPCs", "npcdata.yml");
	private static final FileConfiguration cfg = YamlConfiguration.loadConfiguration(npcdat);
	
	private static HashMap<NPC, HashMap<String, Object>> properties = new HashMap<>();
	
	public static void setServer(NPC npc, String server) {
		cfg.set(npc.getIdentification()+".server", server);
		saveFileCfg();
	}
	public static String getServer(NPC npc) {
		return cfg.getString(npc.getIdentification()+".server");
	}
	public static void setItem(NPC npc, ItemStack item) {
		cfg.set(npc.getIdentification()+".item", item);
		saveFileCfg();
	}
	
	public static ItemStack getItem(NPC npc) {
		return cfg.getItemStack(npc.getIdentification()+".item");
	}
	public static void setAutoRotate(NPC npc, boolean b) {
		cfg.set(npc.getIdentification()+".autorotate", b);
		saveFileCfg();
	}
	public static boolean getAutoRotate(NPC npc) {
		return cfg.getBoolean(npc.getIdentification()+".autorotate");
	}
	
	public static void setAutoSneak(NPC npc, boolean b) {
		cfg.set(npc.getIdentification()+".sneak", b);
		saveFileCfg();
	}
	public static boolean getAutoSneak(NPC npc) {
		return cfg.getBoolean(npc.getIdentification()+".sneak");
	}
	public static boolean isAutoSneak(NPC npc) {
		return (boolean) getProperty(npc, "sneak");
	}
	
	public static void setForcefield(NPC npc, boolean b) {
		cfg.set(npc.getIdentification()+".forcefield", b);
		saveFileCfg();
	}
	public static boolean getForcefield(NPC npc) {
		return cfg.getBoolean(npc.getIdentification()+".forcefield");
	}
	public static boolean isForcefield(NPC npc) {
		return (boolean) getProperty(npc, "forcefield");
	}
	public static boolean isAutoRotate(NPC npc) {
		return (boolean) getProperty(npc, "rotate");
	}
	
	public static boolean getEmote(NPC npc) {
		return cfg.getBoolean(npc.getIdentification()+".emote");
	}
	public static boolean isEmote(NPC npc) {
		return (boolean) getProperty(npc, "emote");
	}
	public static void setEmote(NPC npc, boolean b) {
		cfg.set(npc.getIdentification()+".emote", b);
		saveFileCfg();
	}
	
	
	public static Object getProperty(NPC npc, String prop) {
		if(properties.get(npc)==null) properties.put(npc, new HashMap<>());
		return properties.get(npc).get(prop);
	}
	public static void setProperty(NPC npc, String prop, Object value) {
		if(properties.get(npc)==null) properties.put(npc, new HashMap<>());
		properties.get(npc).put(prop, value);
	}
	
	  
	 
	private static void saveFileCfg() {
		try {
			cfg.save(npcdat);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	public static void loadNPCS() {
		try {
			if(!dataFile.getParentFile().exists()) dataFile.getParentFile().mkdir();
			if(!dataFile.exists()) dataFile.createNewFile();
			ObjectHelper reader = new ObjectHelper(dataFile).reader();
			data = (List<NPCData>) reader.readObject();
			if(data == null) data = new ArrayList<>();
			reader.closeReader();
			for(NPCData dat : data) {
				NPC npc = dat.toNPC(true);
				if(getItem(npc)!=null)
					npc.setHandItem(getItem(npc));
				setProperty(npc, "server", getServer(npc));
				setProperty(npc, "rotate", getAutoRotate(npc));
				setProperty(npc, "forcefield", getForcefield(npc));
				setProperty(npc, "sneak", getAutoSneak(npc));
				setProperty(npc, "emote", getEmote(npc)&&LabyModHook.isLabyModInstalled());
			}
		} catch (Exception e) {}
	}
	public static void saveNPCS() {
		try {
			if(!dataFile.getParentFile().exists()) dataFile.getParentFile().mkdir();
			if(!dataFile.exists()) dataFile.createNewFile();
			ObjectHelper writer = new ObjectHelper(dataFile).writer();
			data.clear();
			for(NPC npc : NPCManager.npcs) {
				data.add(npc.toNPCData());
			}
			writer.writeObject(data);
			writer.closeWriter();
		} catch (Exception e) {e.printStackTrace();}
	}
}
