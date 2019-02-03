package talron.timers;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import talron.listeners.AbilityListener;

public class FireBurstCooldown extends BukkitRunnable {

    private int count = 5;
    private Player player = AbilityListener.playerB;

    @Override
    public void run() {
        if (count == 0) {
            this.cancel();
            AbilityListener.playersInFireBurst.remove(player);
            count = 5;
        }
        count--;
    }
}
