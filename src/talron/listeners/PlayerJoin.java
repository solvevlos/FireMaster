package talron.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import talron.FireMaster;

public class PlayerJoin implements Listener {

    FileConfiguration config = FireMaster.plugin.getConfig();

    public PlayerJoin(talron.FireMaster plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void PlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1200, 1200);
        event.getPlayer().setAllowFlight(true);
        event.getPlayer().teleport((Location) config.get("MainLobbySpawn"));
    }

}
