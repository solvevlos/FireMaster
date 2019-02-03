package talron.timers;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import talron.listeners.AbilityListener;

public class ExplosionCooldown extends BukkitRunnable {

    private int count = 30;
    private Player player = AbilityListener.playerB;

    @Override
    public void run() {
        if (count == 0) {
            this.cancel();
            AbilityListener.playersInExplosion.remove(player);
            count = 30;
        }
        count--;
    }

}
