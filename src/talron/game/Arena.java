package talron.game;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Arena {

    public List<Player> playerList = new ArrayList<>();
    public int id = 0;
    public Location spawn = null;

    public Arena(Location spawn, int id) {
        this.id = id;
        this.spawn = spawn;
    }

    public int getArenaId() {
        return this.id;
    }

    public List<Player> getPlayers() {
        return this.playerList;
    }
}
