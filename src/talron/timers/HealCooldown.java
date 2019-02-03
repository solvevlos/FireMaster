package talron.timers;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import talron.listeners.AbilityListener;


public class HealCooldown extends BukkitRunnable {

    private int count = 20;
    private Player player = AbilityListener.playerB;

    @Override
    public void run() {
        if (count == 0) {
            this.cancel();
            AbilityListener.playersInHeal.remove(player);
            count = 20;
        }
        count--;
    }
}
