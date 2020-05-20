package de.raffi.lobbynpcs.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.raffi.lobbynpcs.main.LobbyNPCs;
import de.raffi.lobbynpcs.utils.ConfigLobbyNPCs;
import de.raffi.lobbynpcs.utils.InventoryManager;
import de.raffi.lobbynpcs.utils.LobbyNPCManager;
import de.raffi.pluginlib.npc.NPC;
import de.raffi.pluginlib.test.InputHandler;
import de.raffi.pluginlib.test.MessageHandler;
import de.raffi.pluginlib.test.YesNoCallback;
import de.raffi.pluginlib.test.setup.PluginSetup;

public class LobbyNPCCommand implements CommandExecutor {

	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;

				if(args.length == 1) {
					if(args[0].equalsIgnoreCase("create")) {
						if(p.hasPermission(ConfigLobbyNPCs.PERMISSION_NPC_CREATE)) {
							InputHandler.getYesNoFeedback(p, ConfigLobbyNPCs.PREFIX+"§6Please go the place where the NPC should be.", "§a[I found the place!]", "", new YesNoCallback() {
								Location loc=p.getLocation();
								String npcName="Notch";
								boolean fetch=false;
								String skinName="Notch";
								boolean showInTab = false;
								String server="Lobby";
								ItemStack item = null;
								boolean autoRotate = true;
								boolean forcefield = false;
			
								@Override
								public void onHandlerRemoved(boolean b) {
									InputHandler.getInputFrom(p, ConfigLobbyNPCs.PREFIX+"§6Enter the skin name of the npc (name is used to get cape etc.)", new MessageHandler() {
										
										@Override
										public void onMessageDenied(String message) {p.sendMessage("§cError: String length is bigger than maximum of 16!");}
										
										@Override
										public void onHandlerRemoved() {
											InputHandler.getYesNoFeedback(p, ConfigLobbyNPCs.PREFIX+"§6Fetch the UUID of " + npcName + " (That enables cape)?", "§a[Yes]", "§c[No]", new YesNoCallback() {
												
												@Override
												public void onHandlerRemoved(boolean b) {
													InputHandler.getInputFrom(p, ConfigLobbyNPCs.PREFIX+"§6From which player should the skin be loaded from (Enter player name)?", new MessageHandler() {
														
														@Override
														public void onMessageDenied(String message) {
															p.sendMessage("§cError: String length is bigger than maximum of 16!");
															
														}
														
														@Override
														public void onHandlerRemoved() {
															InputHandler.getYesNoFeedback(p, ConfigLobbyNPCs.PREFIX+"§6Show the NPC in the tablist?", "§a[Yes]", "§c[No]", new YesNoCallback() {
																
																@Override
																public void onHandlerRemoved(boolean b) {
																	InputHandler.getInputFrom(p, ConfigLobbyNPCs.PREFIX+"§6On what server should a player get send to (servername)?", new MessageHandler() {
																		
																		@Override
																		public void onMessageDenied(String message) {
																			// TODO Auto-generated method stub
																			
																		}
																		
																		@Override
																		public void onHandlerRemoved() {
																			InputHandler.getYesNoFeedback(p,  ConfigLobbyNPCs.PREFIX+"§6Equipp NPC hand item in your HAND!", "§a[Got it!]", "", new YesNoCallback() {
																				
																				@Override
																				public void onHandlerRemoved(boolean b) {
																					InputHandler.getYesNoFeedback(p,  ConfigLobbyNPCs.PREFIX+"§6Rotate the NPC to the player? (not recommended for large servers)", "§a[Yes]", "§c[No]", new YesNoCallback() {
																						
																						@Override
																						public void onHandlerRemoved(boolean b) {
																							InputHandler.getYesNoFeedback(p, ConfigLobbyNPCs.PREFIX+"§6Enable forcefield?", "§a[Yes]", "§c[No]", new YesNoCallback() {
																								
																								@Override
																								public void onHandlerRemoved(boolean state) {
																									forcefield = state;
																									InputHandler.getYesNoFeedback(p, ConfigLobbyNPCs.PREFIX+"§6Sneak when player sneak?", "§a[Yes]", "§c[No]", new YesNoCallback() {
																										
																										@Override
																										public void onHandlerRemoved(boolean state) {
																											NPC npc = new NPC(loc, npcName, fetch, skinName);
																											
																											if(item != null)
																												npc.setHandItem(item);
																											npc.setRemovedFromTablist(!showInTab);
																											npc.register();
																											npc.enableAutoSpawn();
																											LobbyNPCManager.setAutoRotate(npc, autoRotate);
																											LobbyNPCManager.setServer(npc, server);
																											LobbyNPCManager.setItem(npc, item);
																											LobbyNPCManager.setForcefield(npc, forcefield);	
																											LobbyNPCManager.setAutoSneak(npc, state);
																											
																											LobbyNPCManager.setProperty(npc, "server", server);
																											LobbyNPCManager.setProperty(npc, "rotate", autoRotate);
																											LobbyNPCManager.setProperty(npc, "forcefield", forcefield);
																											LobbyNPCManager.setProperty(npc, "sneak", state);

																											p.sendMessage(ConfigLobbyNPCs.PREFIX+"§aCreated NPC successfully. Rejoin to see a change.");
																											
																										}
																										
																										@Override
																										public void decline() {
																											// TODO Auto-generated method stub
																											
																										}
																										
																										@Override
																										public void accept() {
																											// TODO Auto-generated method stub
																											
																										}
																									});
																									
																								}
																								
																								@Override
																								public void decline() {
																									// TODO Auto-generated method stub
																									
																								}
																								
																								@Override
																								public void accept() {
																									// TODO Auto-generated method stub
																									
																								}
																							});
																						}
																						
																						@Override
																						public void decline() {
																							autoRotate = false;
																							
																						}
																						
																						@Override
																						public void accept() {
																							autoRotate = true;
																							
																						}
																					});
																					
																				}
																				
																				@Override
																				public void decline() {
																					p.sendMessage("§cError");
																					
																				}
																				
																				@Override
																				public void accept() {
																					item = p.getItemInHand();
																					
																				}
																			});
																		}
																		
																		@Override
																		public boolean handleMessage(String message) {
																			server = message;
																			return true;
																		}
																	});
																	
																}
																
																@Override
																public void decline() {
																	showInTab = false;
																	
																}
																
																@Override
																public void accept() {
																	showInTab = true;
																	
																}
															});
														}
														
														@Override
														public boolean handleMessage(String message) {
															skinName=message;
															return message.length()<=16;
														}
													});
													
												}
												
												@Override
												public void decline() {
													fetch = false;
													
												}
												
												@Override
												public void accept() {
													fetch = true;
													
												}
											});
										}
										
										@Override
										public boolean handleMessage(String message) {
											npcName=message.replace("&", "§");
											return message.length()<=16;
										}
									});
								}
								
								@Override
								public void decline() {		
									p.sendMessage(ConfigLobbyNPCs.PREFIX+"§cYou canceled NPC creation");
								}
								
								@Override
								public void accept() {
									loc = p.getLocation();
								}
							});
						} else p.sendMessage(ConfigLobbyNPCs.NO_PERMISSION);
					
					} else if(args[0].equalsIgnoreCase("remove")) {
						if(p.hasPermission(ConfigLobbyNPCs.PERMISSION_NPC_REMOVE)) {
							InventoryManager.openRemoveInventory(p);
						}else p.sendMessage(ConfigLobbyNPCs.NO_PERMISSION);
					} else if(args[0].equalsIgnoreCase("setup")) {
						if(p.hasPermission(ConfigLobbyNPCs.PERMISSION_SETUP)) {
							new PluginSetup(p, ConfigLobbyNPCs.class, new Runnable() {
								public void run() {
									PluginSetup.saveValues(ConfigLobbyNPCs.class, LobbyNPCs.config);
									LobbyNPCs.startSetup=false;
									PluginSetup.loadValues(ConfigLobbyNPCs.class, LobbyNPCs.config);
									p.sendMessage(ConfigLobbyNPCs.PREFIX+"§aLobbyNPCS Setup completed. ");
								}
							}).start();
						} else p.sendMessage(ConfigLobbyNPCs.NO_PERMISSION);
						
					} else if(args[0].equalsIgnoreCase("reload")) {
						if(p.hasPermission(ConfigLobbyNPCs.PERMISSION_RELOAD)) {
							p.sendMessage(ConfigLobbyNPCs.PREFIX+"§aReloading config ...");
							PluginSetup.loadValues(ConfigLobbyNPCs.class, LobbyNPCs.config);
							p.sendMessage(ConfigLobbyNPCs.PREFIX+"§aComplete!");
						} else p.sendMessage(ConfigLobbyNPCs.NO_PERMISSION);
					} else if(args[0].equalsIgnoreCase("help")) {
						p.sendMessage(ConfigLobbyNPCs.PREFIX);
						if(p.hasPermission(ConfigLobbyNPCs.PERMISSION_NPC_CREATE))
							p.sendMessage(ConfigLobbyNPCs.PREFIX + "§e/lobbynpc create §7Create a new NPC");
						if(p.hasPermission(ConfigLobbyNPCs.PERMISSION_NPC_REMOVE))
							p.sendMessage(ConfigLobbyNPCs.PREFIX + "§e/lobbynpc remove §7Remove existing NPC");
						if(p.hasPermission(ConfigLobbyNPCs.PERMISSION_RELOAD))
							p.sendMessage(ConfigLobbyNPCs.PREFIX + "§e/lobbynpc reload §7Reload the fileconfiguration");
						if(p.hasPermission(ConfigLobbyNPCs.PERMISSION_SETUP))
							p.sendMessage(ConfigLobbyNPCs.PREFIX + "§e/lobbynpc setup §7Configure plugin-settings ingame");
						
						p.sendMessage(ConfigLobbyNPCs.PREFIX + "§e/lobbynpc help §7Show this help");
						p.sendMessage(ConfigLobbyNPCs.PREFIX);
					}else 
						p.sendMessage(ConfigLobbyNPCs.PREFIX+"§cUnkown command. Please use /lobbynpc help");
				} else 
					p.sendMessage(ConfigLobbyNPCs.PREFIX+"§cUnkown command. Please use /lobbynpc help");
						
		}
		return false;
	}

}
