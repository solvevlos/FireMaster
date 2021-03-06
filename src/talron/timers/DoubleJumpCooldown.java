package talron.timers;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import talron.listeners.AbilityListener;

public class DoubleJumpCooldown extends BukkitRunnable {


    private int count = 2;
    private Player player = AbilityListener.playerB;

    @Override
    public void run() {
        if (count == 0) {
            this.cancel();
            AbilityListener.playersInDouble.remove(player);
            count = 2;
        }
        count--;
    }

}
