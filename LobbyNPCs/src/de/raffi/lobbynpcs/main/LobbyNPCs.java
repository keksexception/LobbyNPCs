package de.raffi.lobbynpcs.main;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.raffi.lobbynpcs.commands.LobbyNPCCommand;
import de.raffi.lobbynpcs.listener.NPCListener;
import de.raffi.lobbynpcs.utils.ConfigLobbyNPCs;
import de.raffi.lobbynpcs.utils.LobbyNPCManager;
import de.raffi.pluginlib.bungeecord.BungeeAPI;
import de.raffi.pluginlib.test.setup.PluginSetup;

public class LobbyNPCs extends JavaPlugin{
	
	private static LobbyNPCs instance;
	public static final File config = new File("plugins/LobbyNPCs", "config.yml");
	public static boolean startSetup=false;
	@Override
	public void onEnable() {
		instance = this;
		getCommand("lobbynpc").setExecutor(new LobbyNPCCommand());
		
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new NPCListener(), this);
		
		
		PluginSetup.loadValues(ConfigLobbyNPCs.class, config);
		startSetup = !PluginSetup.isSaved(ConfigLobbyNPCs.class, config);
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			public void run() {
				LobbyNPCManager.loadNPCS();
			}
		},30);
	
		
		try {
			BungeeAPI.registerChannel(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static LobbyNPCs getInstance() {
		return instance;
	}
	@Override
	public void onDisable() {
		PluginSetup.saveValues(ConfigLobbyNPCs.class, config);
		LobbyNPCManager.saveNPCS();
	}
}
