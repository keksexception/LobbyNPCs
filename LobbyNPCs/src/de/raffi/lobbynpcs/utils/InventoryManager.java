package de.raffi.lobbynpcs.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import de.raffi.pluginlib.builder.SkullBuilder;
import de.raffi.pluginlib.npc.NPC;
import de.raffi.pluginlib.npc.NPCManager;

public class InventoryManager {
	
	public static final String TITLE = "§cRemove NPC";
	public static void openRemoveInventory(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9*4, TITLE);
		for(NPC npc:NPCManager.npcs) {
			inv.addItem(new SkullBuilder(npc.getSkinName()).setName("§6"+npc.getProfile().getName()).setLore("§6UUID: " + npc.getProfile().getId(),"§6Target: "+ LobbyNPCManager.getProperty(npc, "server")).build());
		}
		p.openInventory(inv);
	}

}
