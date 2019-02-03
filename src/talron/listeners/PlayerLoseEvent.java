package talron.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import talron.game.ArenaManager;

public class PlayerLoseEvent implements Listener {

    public PlayerLoseEvent(talron.FireMaster plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void PlayerLoseEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();


        player.spigot().respawn();
        player.getInventory().clear();
        player.setAllowFlight(false);
        player.setFlying(false);

            ArenaManager.getManager().removePlayer(player);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have lost!"));

        if (AbilityListener.playersInExplosion.contains(player)) {
            AbilityListener.playersInExplosion.remove(player);
        } else if (AbilityListener.playersInHeal.contains(player)) {
            AbilityListener.playersInHeal.remove(player);
        } else if (AbilityListener.playersInFireBlast.contains(player)) {
            AbilityListener.playersInFireBlast.remove(player);
        } else if (AbilityListener.playersInFireBurst.contains(player)) {
            AbilityListener.playersInFireBurst.remove(player);
        }

    }
}
