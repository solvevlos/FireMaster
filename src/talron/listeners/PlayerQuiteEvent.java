package talron.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import talron.game.ArenaManager;

public class PlayerQuiteEvent implements Listener {


    public PlayerQuiteEvent(talron.FireMaster plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        if (AbilityListener.playersInExplosion.contains(p)) {
            AbilityListener.playersInExplosion.remove(p);
        } else if (AbilityListener.playersInHeal.contains(p)) {
            AbilityListener.playersInHeal.remove(p);
        } else if (AbilityListener.playersInFireBlast.contains(p)) {
            AbilityListener.playersInFireBlast.remove(p);
        } else if (AbilityListener.playersInFireBurst.contains(p)) {
            AbilityListener.playersInFireBurst.remove(p);
        }
        if (ArenaManager.getManager().isInGame(p)) {
            ArenaManager.getManager().removePlayer(p);
        }
    }


}
