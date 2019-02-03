package talron;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import talron.game.ArenaManager;
import talron.listeners.*;

import java.util.List;


/**
 * Created by Talron on 4/28/18
 * I recoded version of the game FireMaster
 * A Last Air Bender type game featuring only
 * Fire Bender. The objective is to shoot other players
 * off a map using abilities such as FireBlast and FireBurst.
 * The last player standing wins.
 */

public class FireMaster extends JavaPlugin {

    public static FireMaster plugin;

    @Override
    public void onEnable() {
        plugin = this;

        new AbilityListener(this);
        new PlayerQuiteEvent(this);
        new PlayerLoseEvent(this);
        new PlayerJoin(this);

        new ArenaManager(this);
        ArenaManager.getManager().loadGames();

        List<World> worlds = Bukkit.getServer().getWorlds();
        for (World world : worlds) {
            Bukkit.getServer().unloadWorld(world.getName(), false);
            Bukkit.getServer().createWorld(new WorldCreator(world.getName()));
            world.setAutoSave(true);
        }


        getCommand("fm").setExecutor(new talron.Commands());
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&e(!) &aFireMaster enabled"));
    }

    @Override
    public void onDisable() {
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&e(!) &cFireMaster disabled"));
        for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
            pl.teleport((Location) getConfig().get("MainLobbySpawn"));
            pl.kickPlayer(ChatColor.translateAlternateColorCodes('&', "&6&lFireMaster: &cServer restarting"));
        }
    }
}
