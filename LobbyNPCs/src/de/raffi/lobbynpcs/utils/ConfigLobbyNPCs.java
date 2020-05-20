package de.raffi.lobbynpcs.utils;

import de.raffi.pluginlib.test.setup.Setup;

public class ConfigLobbyNPCs {
	
	@Setup(description ="Enter permission for creating a NPC", defaultValue = "lobbynpc.create")
	public static String PERMISSION_NPC_CREATE = "lobbynpc.create";
	@Setup(description = "Enter permission for removing a NPC", defaultValue = "lobbynpc.remove")
	public static String PERMISSION_NPC_REMOVE = "lobbynpc.remove";
	@Setup(description = "Enter permission for this setup", defaultValue = "lobbynpc.setup")
	public static String PERMISSION_SETUP = "lobbynpc.setup";
	@Setup(description = "Enter permission for reloading config", defaultValue = "lobbynpc.reload")
	public static String PERMISSION_RELOAD = "lobbynpc.reload";
	@Setup(description = "Enter message for no permission.", defaultValue = "§cInsufficient permission.")
	public static String NO_PERMISSION = "§cInsufficient permission.";
	@Setup(description = "Enter prefix for plugin messages", defaultValue = "§eLobbyNPCs §8| ")
	public static String PREFIX = "§eLobbyNPCs §8| ";
	@Setup(description = "Enter connecting message", defaultValue = "§aConnecting to server ...")
	public static String connectingMessage = "§aConnecting to server ...";
	@Setup(description = "NPC interaction cooldown. In milliseconds (1 sec = 1000 milliseconds)", defaultValue = "1000")
	public static int NPC_COOLDOWN = 1000;

}
