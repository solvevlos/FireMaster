package talron;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import talron.game.ArenaManager;

public class Commands implements CommandExecutor {

    FileConfiguration config = FireMaster.plugin.getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (cmd.getName().equalsIgnoreCase("fm")) {
                Player player = (Player) sender;
                if (args.length == 0) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&m-----&r &6&lFireMaster &8&m-----&r"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7- &a/fm join (ID)"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7- &a/fm leave (ID)"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7- &a/fm admin"));
                } else {
                    if (args[0].equalsIgnoreCase("admin")) {

                        if (player.hasPermission("firemaster.admin")) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7- &a/fm newArena (ID)"));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7- &a/fm setSpawn (ID)"));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7- &a/fm setMaxPlayers (ID) (Number)"));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7- &a/fm setMinPlayers (ID) (Number)"));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7- &a/fm setMainLobbySpawn"));
                        }

                    } else if (args[0].equalsIgnoreCase("newArena")) {
                        if (player.hasPermission("firemaster.admin")) {
                            ArenaManager.getManager().createArena(player.getLocation(), player.getWorld());
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aCreated new Arena section"));
                        }
                    } else if (args[0].equalsIgnoreCase("setSpawn")) {
                        if (player.hasPermission("firemaster.admin")) {
                            if (args.length != 2) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPlease put in all arguments"));
                            } else {
                                config.set("Arenas." + args[1] + ".spawn", player.getLocation());
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aset spawn for arena"));
                                FireMaster.plugin.saveConfig();
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("setMaxPlayers")) {
                        if (player.hasPermission("firemaster.admin")) {
                            if (args.length != 3) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPlease put in all arguments, the arena ID and player amount"));
                            } else {
                                config.set("Arenas." + args[1] + ".maxPlayers", args[2]);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aset max players for arena"));
                                FireMaster.plugin.saveConfig();
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("setMinPlayers")) {
                        if (player.hasPermission("firemaster.admin")) {
                            if (args.length != 3) {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPlease put in all arguments, the arena ID and player amount"));
                            } else {
                                config.set("Arenas." + args[1] + ".minPlayers", args[2]);
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aset min players for arena"));
                                FireMaster.plugin.saveConfig();
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("setMainLobbySpawn")) {
                        if (player.hasPermission("firemaster.admin")) {
                            config.createSection("MainLobbySpawn");
                            config.set("MainLobbySpawn", player.getLocation());
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aset main lobby"));
                            FireMaster.plugin.saveConfig();
                        }
                    } else if (args[0].equalsIgnoreCase("join")) {
                        if (args.length != 2) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPlease put in all arguments"));
                        } else {
                            ArenaManager.getManager().addPlayer(player, Integer.parseInt(args[1]));
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have &ajoined &7a game of &6&lFireMaster"));
                        }
                    } else if (args[0].equalsIgnoreCase("leave")) {

                            ArenaManager.getManager().removePlayer(player);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7You have &cleft &7a game of &6&lFireMaster"));
                            player.teleport((Location) config.get("MainLobbySpawn"));
                    }
                }
            }
        }
        return true;
    }
}