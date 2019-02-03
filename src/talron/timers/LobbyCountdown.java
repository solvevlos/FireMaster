package talron.timers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import talron.game.Arena;

import static talron.FireMaster.plugin;

public class LobbyCountdown extends BukkitRunnable {
    FileConfiguration config = plugin.getConfig();

    private ItemStack fire = new ItemStack(Material.BLAZE_POWDER);
    private ItemStack feather = new ItemStack(Material.FEATHER);

    private int count = 60;
    public Arena arena;


    @Override
    public void run() {
        if (arena.getPlayers().size() < config.getInt("Arenas." + arena.getArenaId() + ".minPlayers")) {
            this.cancel();
            config.set("Arenas." + arena.getArenaId() + ".gameState", "WAITING");
            for (Player p : arena.getPlayers()) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cGame canceled!"));
            }
            count = 60;
        }
        if (config.getString("Arenas." + arena.getArenaId() + ".gameState").equals("WAITING")) {
            this.cancel();
        }
        if (arena.getPlayers().size() == config.getInt("Arenas." + arena.getArenaId() + ".maxPlayers")) {
            config.set("Arenas." + arena.getArenaId() + ".gameState", "RUNNING");
            for (Player pl : arena.getPlayers()) {
                pl.teleport((Location) config.get("Arenas." + arena.getArenaId() + ".spawn"));
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lFireMaster: &a&lThe game has begun!"));
                pl.sendTitle(ChatColor.translateAlternateColorCodes('&', "&6&lFireMaster"), null, 20, 20, 20);

                pl.getInventory().clear();
                pl.getInventory().setItem(0, fire);
                pl.getInventory().setItem(1, feather);
                pl.getInventory().setItem(2, fire);
                pl.getInventory().setItem(3, fire);
                pl.getInventory().setItem(4, fire);
                pl.getInventory().setItem(5, fire);
                pl.getInventory().setItem(6, fire);
                pl.getInventory().setItem(7, fire);
                pl.getInventory().setItem(8, fire);
            }
            this.cancel();
            startGameCountdown(arena);
            Bukkit.getWorld(config.getString("Arenas." + arena.getArenaId() + ".world")).setAutoSave(false);
        }
        if (count == 30) {
            for (Player pl : arena.getPlayers()) {
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lFireMaster: &a&oThe game is starting in &c30"));
            }
        } else if (count == 15) {
            for (Player pl : arena.getPlayers()) {
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lFireMaster: &a&oThe game is starting in &c15"));
            }
        } else if (count == 10) {
            for (Player pl : arena.getPlayers()) {
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lFireMaster: &a&oThe game is starting in &c10"));
            }
        } else if (count == 5) {
            for (Player pl : arena.getPlayers()) {
                pl.sendTitle(ChatColor.translateAlternateColorCodes('&', "&6&lFireMaster"), ChatColor.translateAlternateColorCodes('&', "&7In &a5"), 0, 40, 0);
            }
        } else if (count == 4) {
            for (Player pl : arena.getPlayers()) {
                pl.sendTitle(ChatColor.translateAlternateColorCodes('&', "&6&lFireMaster"), ChatColor.translateAlternateColorCodes('&', "&7In &a4"), 0, 40, 0);
            }
        } else if (count == 3) {
            for (Player pl : arena.getPlayers()) {
                pl.sendTitle(ChatColor.translateAlternateColorCodes('&', "&6&lFireMaster"), ChatColor.translateAlternateColorCodes('&', "&7In &a3"), 0, 40, 0);
            }
        } else if (count == 2) {
            for (Player pl : arena.getPlayers()) {
                pl.sendTitle(ChatColor.translateAlternateColorCodes('&', "&6&lFireMaster"), ChatColor.translateAlternateColorCodes('&', "&7In &a2"), 0, 40, 0);
            }
        } else if (count == 1) {
            for (Player pl : arena.getPlayers()) {
                pl.sendTitle(ChatColor.translateAlternateColorCodes('&', "&6&lFireMaster"), ChatColor.translateAlternateColorCodes('&', "&7In &a1"), 0, 20, 20);
            }
        } else if (count == 0) {
            config.set("Arenas." + arena.getArenaId() + ".gameState", "RUNNING");
            for (Player pl : arena.getPlayers()) {
                pl.teleport((Location) config.get("Arenas." + arena.getArenaId() + ".spawn"));
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m-----&r &6&lFireMaster &7&m-----&r"));
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8LEFT_CLICK_AIR = FireBlast"));
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8SHIFT + LEFT_CLICK_AIR = FireBurst"));
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8SHIFT + RIGHT_CLICK_AIR = Heal"));
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8SHIFT + RIGHT_CLICK_BLOCK = Explosion"));
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8FEATHER + LEFT_CLICK_AIR = Big Jump"));
                pl.sendTitle(ChatColor.translateAlternateColorCodes('&', "&6&lFireMaster"), ChatColor.translateAlternateColorCodes('&', "&cFight!"), 10, 20, 10);
                pl.setHealth(20);

                pl.getInventory().clear();
                pl.getInventory().setItem(0, fire);
                pl.getInventory().setItem(1, feather);
                pl.getInventory().setItem(2, fire);
                pl.getInventory().setItem(3, fire);
                pl.getInventory().setItem(4, fire);
                pl.getInventory().setItem(5, fire);
                pl.getInventory().setItem(6, fire);
                pl.getInventory().setItem(7, fire);
                pl.getInventory().setItem(8, fire);
            }
            this.cancel();
            startGameCountdown(arena);
            Bukkit.getWorld(config.getString("Arenas." + arena.getArenaId() + ".world")).setAutoSave(false);
        }
        count--;
    }

    private void startGameCountdown(Arena arena) {
        GameCountdown countdown = new GameCountdown();
        countdown.arena = arena;
        countdown.runTaskTimer(plugin, 0, 20);
    }
}
