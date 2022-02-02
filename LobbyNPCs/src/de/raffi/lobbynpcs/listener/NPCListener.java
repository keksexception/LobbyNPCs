package de.raffi.lobbynpcs.listener;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;

import de.raffi.lobbynpcs.main.LobbyNPCs;
import de.raffi.lobbynpcs.utils.ConfigLobbyNPCs;
import de.raffi.lobbynpcs.utils.InventoryManager;
import de.raffi.lobbynpcs.utils.LabyModHook;
import de.raffi.lobbynpcs.utils.LobbyNPCManager;
import de.raffi.pluginlib.event.PlayerInteractAtNPCEvent;
import de.raffi.pluginlib.npc.NPC;
import de.raffi.pluginlib.npc.NPCManager;
import de.raffi.pluginlib.test.InputHandler;
import de.raffi.pluginlib.test.YesNoCallback;
import de.raffi.pluginlib.test.setup.PluginSetup;

public class NPCListener implements Listener{
	
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if(LobbyNPCs.startSetup) {
			if(e.getPlayer().hasPermission("lobbynpc.admin")) {
				InputHandler.getYesNoFeedback(e.getPlayer(), "§c§lLobbyNPCs is not configured yet.", "§a[Configure now]", "§c[Skip manual configuration]", new YesNoCallback() {
					
					@Override
					public void onHandlerRemoved(boolean b) {}
					
					@Override
					public void decline() {
						e.getPlayer().sendMessage("§6Skipped configuration! You can change the settings later in the config.yml.");
						PluginSetup.saveValues(ConfigLobbyNPCs.class, LobbyNPCs.config);
						LobbyNPCs.startSetup=false;
					}
					
					@Override
					public void accept() {
						new PluginSetup(e.getPlayer(), ConfigLobbyNPCs.class, new Runnable() {
							public void run() {
								PluginSetup.saveValues(ConfigLobbyNPCs.class, LobbyNPCs.config);
								e.getPlayer().sendMessage(ConfigLobbyNPCs.PREFIX+"§aLobbyNPCS Setup completed. ");
								LobbyNPCs.startSetup=false;
								PluginSetup.loadValues(ConfigLobbyNPCs.class, LobbyNPCs.config);
							}
						}).start();
					}
				});
				
			}
		}
		if(e.getPlayer().hasPermission("lobbynpc.admin")&&LobbyNPCs.updateRequired) {
			e.getPlayer().sendMessage(ConfigLobbyNPCs.PREFIX+"§b§lA new version of LobbyNPCs is available: §9§o§nhttps://www.spigotmc.org/resources/79022/");
		}
	}
	
	
	private HashMap<Player, Long> allow = new HashMap<>();
	@EventHandler
	public void onInteract(PlayerInteractAtNPCEvent e) {
		if(allow.get(e.getPlayer())==null) allow.put(e.getPlayer(), 1L);
		if(System.currentTimeMillis()-allow.get(e.getPlayer())>ConfigLobbyNPCs.NPC_COOLDOWN) {
			allow.put(e.getPlayer(), System.currentTimeMillis());
			e.getPlayer().sendMessage(ConfigLobbyNPCs.connectingMessage);
			de.raffi.lobbynpcs.utils.Bungee.sendToServer((String) LobbyNPCManager.getProperty(e.getNPC(), "server"), e.getPlayer());
		}
		
	}
	@EventHandler
	public void onSneak(PlayerToggleSneakEvent e) {
		NPCManager.npcs.forEach(npc->{
			if(LobbyNPCManager.isAutoSneak(npc))
				npc.setSneaking(e.getPlayer(), !e.getPlayer().isSneaking());
		});
	}
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		for(NPC npc : NPCManager.npcs) {
			if(LobbyNPCManager.isAutoRotate(npc)) {
				npc.rotateTo(p, p.getLocation());
			}
			if(LobbyNPCManager.isForcefield(npc)) {
				if(npc.getLocation().getWorld().getName().equals(p.getWorld().getName())) {
					if(npc.getLocation().distance(p.getLocation())<=1.2) {
						double Ax = p.getLocation().getX();
						double Ay = p.getLocation().getY();
						double Az = p.getLocation().getZ();
						double Bx = npc.getLocation().getX();
						double By = npc.getLocation().getY();
						double Bz = npc.getLocation().getZ();
						double x = Ax - Bx;
						double y = Ay - By;
						double z = Az - Bz;
						Vector v = new Vector(x, y, z).normalize().multiply(0.6D).setY(0.3D);
						p.setVelocity(v);
						if(LobbyNPCManager.isEmote(npc))
							LabyModHook.sendEmote(p, npc, new Random().nextInt(174));
					}
				}
			}
		}
	}
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getInventory()!=null&&e.getInventory().getTitle()!=null&&e.getInventory().getName()!=null&&e.getCurrentItem()!=null&&e.getCurrentItem().getType()!=Material.AIR) {
			if(e.getInventory().getTitle().equals(InventoryManager.TITLE)) {
				Player p = (Player) e.getWhoClicked();
				if(p.hasPermission(ConfigLobbyNPCs.PERMISSION_NPC_REMOVE)) {
					NPCManager.npcs.get(e.getSlot()).destroyForAll();
					NPCManager.npcs.remove(e.getSlot());
					InventoryManager.openRemoveInventory(p);
				}
				e.setCancelled(true);
			}
		}
	}

}
