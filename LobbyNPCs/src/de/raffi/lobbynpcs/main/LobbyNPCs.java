package de.raffi.lobbynpcs.main;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.raffi.lobbynpcs.commands.LobbyNPCCommand;
import de.raffi.lobbynpcs.listener.NPCListener;
import de.raffi.lobbynpcs.utils.ConfigLobbyNPCs;
import de.raffi.lobbynpcs.utils.LobbyNPCManager;
import de.raffi.pluginlib.compability.npchandler.NPCHandlerManager;
import de.raffi.pluginlib.main.PluginLib;
import de.raffi.pluginlib.test.setup.PluginSetup;
import de.raffi.pluginlib.utils.SpigotUpdater;
import de.raffi.pluginlib.utils.UpdateCallback;

public class LobbyNPCs extends JavaPlugin{
	
	private static LobbyNPCs instance;
	public static final File config = new File("plugins/LobbyNPCs", "config.yml");
	public static boolean startSetup=false;
	/**
	 * @since 1.0-b2
	 */
	public static final int requiredVersion = 4;
	public static boolean updateRequired = false;
	@SuppressWarnings("unused")
	@Override
	public void onEnable() {
		instance = this;
		if(PluginLib.API_VERSION < requiredVersion) {
			System.out.println("[LobbyNPCs] [ERROR] [CRITICAL] LOBBYNPCS REQUIRES A NEWER VERSION OF PluginLib. Current: " + PluginLib.API_VERSION + " Required: " + requiredVersion);
			System.out.println("[LobbyNPCs] You have to update PluginLib in order to use this plugin.");
			Bukkit.getPluginManager().disablePlugin(instance);
			System.out.println("------------PluginInfo--------------");
			printInfo();
			System.out.println("------------PluginInfo--------------");
			return;
		} 
		getCommand("lobbynpc").setExecutor(new LobbyNPCCommand());
		
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new NPCListener(), this);
		
		
		PluginSetup.loadValues(ConfigLobbyNPCs.class, config);
		startSetup = !PluginSetup.isSaved(ConfigLobbyNPCs.class, config);
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, LobbyNPCManager::loadNPCS,30);
	
		
		try {
			getServer().getMessenger().registerOutgoingPluginChannel(getInstance(), "BungeeCord");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("[LobbyNPCs] Checking for updates ...");
			SpigotUpdater.checkForUpdates("79022", new UpdateCallback() {
				
				@Override
				public void onVersionReceived(String s) {
					updateRequired = !s.equals(getInstance().getDescription().getVersion());			
					if(updateRequired)
						System.out.println("[LobbyNPCs] Please update: https://www.spigotmc.org/resources/79022/");
					else System.out.println("[LobbyNPCs] No new version found.");
					
				}
				
				@Override
				public void onFail() {
					System.out.println("[LobbyNPCs] Update check failed.");
				}
			});

	
	}
	/**
	 * @since 1.0-b2
	 */
	public void printInfo() {
		System.out.println("LobbyNPC version: versionid:" + requiredVersion + " versionname:" + getDescription().getVersion());
		System.out.println("PluginLib version: versionid:" + PluginLib.API_VERSION + " versionname:" + PluginLib.getInstance().getDescription().getVersion());
		System.out.println("NMS version: " + PluginLib.getServerVersion());
		System.out.println("Bukkit version: "+ getServer().getBukkitVersion());
		System.out.println("Server version: "+getServer().getVersion());
		System.out.println("NPC Handler: " + NPCHandlerManager.npcHandler.getClass().getName());
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
