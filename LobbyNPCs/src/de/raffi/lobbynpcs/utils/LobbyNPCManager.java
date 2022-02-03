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

public class LobbyNPCManager {
	
	public static List<NPCData> data = new ArrayList<>();
	private static final File npcdat = new File("plugins/LobbyNPCs", "npcdata.yml");
	private static final File npcjson = new File("plugins/LobbyNPCs", "npcdata.json");
	private static final FileConfiguration cfg = YamlConfiguration.loadConfiguration(npcdat);
	
	private static HashMap<NPC, HashMap<String, Object>> properties = new HashMap<>();
	

	public static void setItem(NPC npc, ItemStack item) {
		cfg.set(npc.getIdentification()+".item", item);
		saveFileCfg();
	}
	
	public static ItemStack getItem(NPC npc) {
		return cfg.getItemStack(npc.getIdentification()+".item");
	}

	

	public static boolean isAutoSneak(NPC npc) {
		return (boolean) getProperty(npc, "sneak");
	}


	public static boolean isForcefield(NPC npc) {
		return (boolean) getProperty(npc, "forcefield");
	}
	public static boolean isAutoRotate(NPC npc) {
		return (boolean) getProperty(npc, "rotate");
	}

	public static boolean isEmote(NPC npc) {
		return (boolean) getProperty(npc, "emote");
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
					String server = (String)npc.get("server");
					boolean rotate = (boolean) npc.get("rotate");
					boolean forcefield = (boolean) npc.get("rotate");
					boolean sneak = (boolean) npc.get("rotate");
					boolean emote = (boolean) npc.get("rotate");
					NPC finalnpc = new NPC(loc, uuid, displayName, skinName);
					if(getItem(finalnpc)!=null)
						finalnpc.setHandItem(getItem(finalnpc));
					setProperty(finalnpc, "server", server);
					setProperty(finalnpc, "rotate", rotate);
					setProperty(finalnpc, "forcefield", forcefield);
					setProperty(finalnpc, "sneak", sneak);
					setProperty(finalnpc, "emote", emote&&LabyModHook.isLabyModInstalled());
					finalnpc.register();
					finalnpc.enableAutoSpawn();
				}
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	public static void saveNPCS() {		
		JSONArray array = new JSONArray();
		
		for(NPC npc : NPCManager.npcs) {
			JSONObject singleNPC = new JSONObject();
			singleNPC.put("location", Helper.stringify(npc.getLocation()));
			singleNPC.put("uuid", npc.getProfile().getId().toString());
			singleNPC.put("displayname", npc.getProfile().getName());
			singleNPC.put("skinname", npc.getSkinName());
			singleNPC.put("server", getProperty(npc, "server"));
			singleNPC.put("rotate", (boolean)getProperty(npc, "rotate"));
			singleNPC.put("forcefield", (boolean)getProperty(npc, "forcefield"));
			singleNPC.put("sneak", (boolean)getProperty(npc, "sneak"));
			singleNPC.put("emote", (boolean)getProperty(npc, "emote"));
			array.add(singleNPC);
		}
		try {
			/**
			 * write json to the json file
			 */
			if(!npcjson.getParentFile().exists()) npcjson.getParentFile().mkdir();
			Files.write(Paths.get(npcjson.toURI()), array.toJSONString().getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		
	}
}
