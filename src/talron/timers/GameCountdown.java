package talron.timers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import talron.FireMaster;
import talron.game.Arena;
import talron.game.ArenaManager;

import java.util.List;

public class GameCountdown extends BukkitRunnable {

    FileConfiguration config = FireMaster.plugin.getConfig();

    private int count = 2 * 60;
    public Arena arena;

    @Override
    public void run() {
        if (count == 0) {
            this.cancel();
            List<Player> players = arena.getPlayers();
            for (Player pl : players) {
                pl.teleport((Location) config.get("MainLobbySpawn"));
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo one wins!"));
            }
            config.set("Arenas." + arena.getArenaId() + ".gameState", "WAITING");
            Bukkit.getServer().unloadWorld(config.getString("Arenas." + arena.getArenaId() + ".world"), false);
            Bukkit.getServer().createWorld(new WorldCreator(config.getString("Arenas." + arena.getArenaId() + ".world")));
        }
        if (arena.getPlayers().size() == 1) {
                this.cancel();
                Player player = arena.getPlayers().get(0);
                player.getInventory().clear();
                ArenaManager.getManager().removePlayer(player);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lFireMaster: &aYou have won the game!"));
                player.teleport((Location) config.get("MainLobbySpawn"));
                config.set("Arenas." + arena.getArenaId() + ".gameState", "WAITING");
            Bukkit.getServer().unloadWorld(config.getString("Arenas." + arena.getArenaId() + ".world"), false);
            Bukkit.getServer().createWorld(new WorldCreator(config.getString("Arenas." + arena.getArenaId() + ".world")));
        }
        if (arena.getPlayers().size() < 1) {
            this.cancel();
            config.set("Arenas." + arena.getArenaId() + ".gameState", "WAITING");
        }
            count--;
    }
}
