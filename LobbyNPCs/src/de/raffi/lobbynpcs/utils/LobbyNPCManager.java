package de.raffi.lobbynpcs.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


import de.raffi.pluginlib.npc.NPC;
import de.raffi.pluginlib.npc.NPCData;
import de.raffi.pluginlib.npc.NPCManager;
import de.raffi.pluginlib.serialization.ObjectHelper;

public class LobbyNPCManager {
	
	public static List<NPCData> data = new ArrayList<>();
	private static final File dataFile = new File("plugins/LobbyNPCs", "npcdata.dat");
	private static final File npcdat = new File("plugins/LobbyNPCs", "npcdata.yml");
	private static final File npcjson = new File("plugins/LobbyNPCs", "npcdata.json");
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
			if(!npcjson.exists()) {
				System.out.println("[   INFO   ] [LobbyNPCs/NPC loading progress] npcdata.json does not exist. Skipping initialisation");
			} else {
				System.out.println("[   INFO   ] [LobbyNPCs/NPC loading progress] Loading npcdata.json");
				FileReader reader = new FileReader(npcjson);
				JSONParser jsonParser = new JSONParser();
				
				JSONArray npcs = (JSONArray) jsonParser.parse(reader);
				for(Object singleNPC : npcs.toArray()) {
					JSONObject npc = (JSONObject) singleNPC;
					Location loc = Helper.createLocation((String) npc.get("location"));
					UUID uuid = UUID.fromString((String) npc.get("uuid"));
					String displayName = (String) npc.get("displayname");
					String skinName = (String)npc.get("skinname");
					NPC finalnpc = new NPC(loc, uuid, displayName, skinName);
					if(getItem(finalnpc)!=null)
						finalnpc.setHandItem(getItem(finalnpc));
					setProperty(finalnpc, "server", getServer(finalnpc));
					setProperty(finalnpc, "rotate", getAutoRotate(finalnpc));
					setProperty(finalnpc, "forcefield", getForcefield(finalnpc));
					setProperty(finalnpc, "sneak", getAutoSneak(finalnpc));
					setProperty(finalnpc, "emote", getEmote(finalnpc)&&LabyModHook.isLabyModInstalled());
					finalnpc.register();
					finalnpc.enableAutoSpawn();
					System.out.println("[   INFO   ] [LobbyNPCs/NPC loading progress] " + finalnpc.getSkinName() + " have been registered and loaded");
				}
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(!dataFile.exists()) return;
		/*
		 * load from old config
		 */
		System.out.println("[   INFO   ] [LobbyNPCs/NPC loading progress] Found old NPC data! Restoring old data ....");
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
	@SuppressWarnings("unchecked")
	public static void saveNPCS() {
		/*try {
			if(!dataFile.getParentFile().exists()) dataFile.getParentFile().mkdir();
			if(!dataFile.exists()) dataFile.createNewFile();
			ObjectHelper writer = new ObjectHelper(dataFile).writer();
			data.clear();
			for(NPC npc : NPCManager.npcs) {
				data.add(npc.toNPCData());
			}
			writer.writeObject(data);
			writer.closeWriter();
		} catch (Exception e) {e.printStackTrace();}*/
		
		JSONArray array = new JSONArray();
		
		for(NPC npc : NPCManager.npcs) {
			JSONObject singleNPC = new JSONObject();
			singleNPC.put("location", Helper.stringify(npc.getLocation()));
			singleNPC.put("uuid", npc.getProfile().getId().toString());
			singleNPC.put("displayname", npc.getProfile().getName());
			singleNPC.put("skinname", npc.getSkinName());
			array.add(singleNPC);
		}
		try {
			if(!npcjson.getParentFile().exists()) npcjson.getParentFile().mkdir();
			Files.write(Paths.get(npcjson.toURI()), array.toJSONString().getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		
	}
}
