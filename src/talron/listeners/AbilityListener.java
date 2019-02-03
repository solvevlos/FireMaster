package talron.listeners;


import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.MaterialData;
import talron.timers.*;

import java.util.*;

import static talron.FireMaster.plugin;


public class AbilityListener implements Listener {

    public static List<Player> playersInFireBlast = new ArrayList<>();
    public static List<Player> playersInFireBurst = new ArrayList<>();
    public static List<Player> playersInHeal = new ArrayList<>();
    public static List<Player> playersInExplosion = new ArrayList<>();
    public static List<Player> playersInDouble = new ArrayList<>();
    public static Player playerB;

    public AbilityListener(talron.FireMaster plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void AbilityListener(PlayerInteractEvent event) {
        Action a = Action.LEFT_CLICK_AIR;
        Action b = Action.RIGHT_CLICK_AIR;
        Action c = Action.RIGHT_CLICK_BLOCK;
        Player player = event.getPlayer();
        playerB = player;

        FireBlastCooldown fireBlastCooldown = new FireBlastCooldown();
        FireBurstCooldown fireBurstCooldown = new FireBurstCooldown();
        HealCooldown healCooldown = new HealCooldown();
        ExplosionCooldown explosionCooldown = new ExplosionCooldown();
        DoubleJumpCooldown doubleJumpCooldown = new DoubleJumpCooldown();

        if (player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
            return;
        }

        //FIRE BLAST
        if (player.getInventory().getItemInMainHand().getType().equals(Material.BLAZE_POWDER)) {
                if (event.getAction() == a) {
                    if (!(player.isSneaking())) {
                        if (!(playersInFireBlast.contains(player))) {
                            for (Block loc : player.getLineOfSight(null, 50)) { //Thanks to some bukkit thread
                                playFireBlast(loc.getLocation());
                                Collection<Entity> entity = loc.getLocation().getWorld().getNearbyEntities(loc.getLocation(), 1, 1, 1);
                                for (Entity entity1 : entity) {
                                    if (!(entity1.equals(player))) {
                                        if (entity1 instanceof Player) {
                                            Player player1 = (Player) entity1;
                                            player1.setFireTicks(5 * 20);
                                            player1.damage(4);
                                            player1.setVelocity(player.getLocation().getDirection().setY(0).normalize().multiply(8)); //Thanks to some bukkit thread
                                        }
                                    }
                                }
                            }
                            playersInFireBlast.add(player);
                            fireBlastCooldown.runTaskTimer(plugin, 0, 20);
                        } else {
                            player.sendMessage(ChatColor.RED + "2 second cool down");
                        }
                    }
                    //FIRE BURST
                    else if (player.isSneaking()) {
                        if (!(playersInFireBurst.contains(player))) {
                            for (Block loc : player.getLineOfSight(null, 40)) {
                                playFireBurst(loc.getLocation());

                                if (!(loc.getType().equals(Material.AIR))) {
                                    loc.getRelative(BlockFace.EAST, 1).breakNaturally();
                                    loc.getRelative(BlockFace.WEST, 1).breakNaturally();
                                    loc.getRelative(BlockFace.NORTH, 1).breakNaturally();
                                    loc.getRelative(BlockFace.SOUTH, 1).breakNaturally();
                                    loc.getRelative(BlockFace.NORTH_EAST, 1).breakNaturally();
                                    loc.getRelative(BlockFace.NORTH_WEST, 1).breakNaturally();
                                    loc.getRelative(BlockFace.SOUTH_EAST, 1).breakNaturally();
                                    loc.getRelative(BlockFace.SOUTH_WEST, 1).breakNaturally();
                                    loc.breakNaturally();
                                }

                            }
                            playersInFireBurst.add(player);
                            fireBurstCooldown.runTaskTimer(plugin, 0, 20);
                        } else {
                            player.sendMessage(ChatColor.RED + "5 second cool down!");
                        }
                    }
                } else if (event.getAction() == b) {
                    //HEAL
                    if (player.isSneaking()) {
                        if (!(playersInHeal.contains(player))) {
                            if (player.getHealth() > 10) {
                                player.sendMessage(ChatColor.RED + "No need to be healed!");
                                return;
                            }
                            player.setHealth(player.getHealth() + 10);
                            Location l1 = player.getEyeLocation().getBlock().getRelative(BlockFace.EAST).getLocation();
                            Location l2 = player.getEyeLocation().getBlock().getRelative(BlockFace.WEST).getLocation();
                            Location l3 = player.getEyeLocation().getBlock().getRelative(BlockFace.NORTH).getLocation();
                            Location l4 = player.getEyeLocation().getBlock().getRelative(BlockFace.SOUTH).getLocation();
                            playHeal(l1, l2, l3, l4);

                            playersInHeal.add(player);
                            healCooldown.runTaskTimer(plugin, 0, 20);
                        } else {
                            player.sendMessage(ChatColor.RED + "20 second cool down");
                        }
                    }
                } else if (event.getAction() == c) {
                    //EXPLOSION
                    if (player.isSneaking()) {
                        if (!(playersInExplosion.contains(player))) {
                            for (Block loc : player.getLineOfSight(null, 50)) {
                                playExplosion(loc.getLocation());
                                if (!(loc.getType().equals(Material.AIR))) {
                                    player.getWorld().createExplosion(loc.getLocation(), 6);
                                }
                            }
                                playersInExplosion.add(player);
                                explosionCooldown.runTaskTimer(plugin, 0, 20);

                        } else {
                            player.sendMessage(ChatColor.RED + "30 second cool down");
                        }
                    }
                }


        } else if (player.getInventory().getItemInMainHand().getType().equals(Material.FEATHER)) {
            if (event.getAction() == a) {
            if (!(playersInDouble.contains(player))) {
                    player.setVelocity(player.getLocation().getDirection().setY(1.0).multiply(2.0));
                    player.setFallDistance(0);
                    playersInDouble.add(player);
                    doubleJumpCooldown.runTaskTimer(plugin, 0, 20);
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c2 second cool down"));
                }
            }
        }
    }

    //FIRE BLAST Methods
    private void playFireBlast(Location loc) {
        Particle fire = Particle.BLOCK_CRACK;
        loc.getWorld().spawnParticle(fire, loc, 10, new MaterialData(Material.ORANGE_SHULKER_BOX));
    }


    //FIRE BURST Methods
    private void playFireBurst(Location loc) {
        Particle fireBurst = Particle.LAVA;
        loc.getWorld().spawnParticle(fireBurst, loc, 10);
    }

    //HEAL Methods
    private void playHeal(Location loc, Location loc1, Location loc2, Location loc3) {
        Particle heal = Particle.VILLAGER_HAPPY;
        loc.getWorld().spawnParticle(heal, loc, 10);
        loc.getWorld().spawnParticle(heal, loc1, 10);
        loc.getWorld().spawnParticle(heal, loc2, 10);
        loc.getWorld().spawnParticle(heal, loc3, 10);
    }

    //EXPLOSION Methods
    private void playExplosion(Location loc) {
        Particle trail = Particle.FLAME;
        loc.getWorld().spawnParticle(trail, loc, 5);
    }


}
