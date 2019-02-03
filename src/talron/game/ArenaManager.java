package talron.game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import talron.FireMaster;
import talron.timers.LobbyCountdown;

import java.util.ArrayList;
import java.util.List;

import static talron.FireMaster.plugin;

public class ArenaManager {

    private FileConfiguration config = FireMaster.plugin.getConfig();
    private static ArenaManager am = new ArenaManager();
    List<Arena> arenaList = new ArrayList<>();
    int arenaSize = 0;


    public ArenaManager(FireMaster fireMaster) {
        plugin = fireMaster;
    }

    public ArenaManager() {

    }

    public static ArenaManager getManager() {
        return am;
    }

    public Arena getArena(int i) {
        for (Arena a : arenaList) {
            if (a.getArenaId() == i) {
                return a;
            }
        }
        return null;
    }

    public Arena getPlayerArena(Player player) {
        for(Arena a : arenaList){
            if(a.getPlayers().contains(player))
                return a;
        }
        return null;
    }

    public void addPlayer(Player p, int i) {

        Arena a = getArena(i);
        if (a == null)  {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInvalid Arena ID"));
            return;
        }
        if (getGameState(i).equals("RUNNING")) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThat game is currently running!"));
            return;
        }
        a.getPlayers().add(p);
        p.getInventory().setArmorContents(null);
        p.getInventory().clear();
        p.setPlayerListName(ChatColor.GOLD + p.getName());
        p.teleport((Location) config.get("Arenas." + a.getArenaId() + ".lobbySpawn"));
        if (getGameState(i).equalsIgnoreCase("WAITING")) {
            if (a.getPlayers().size() >= config.getInt("Arenas." + a.getArenaId() + ".minPlayers")) {
                Bukkit.getWorld(config.getString("Arenas." + i + ".world")).setAutoSave(false);
                setGameState(i, "STARTING");
                startLobbyCountdown(a);
            }
        }
    }

    public void removePlayer(Player p) {
        Arena a = null;
        for (Arena arena : arenaList) {
            if (arena.getPlayers().contains(p)) {
                a = arena;
            }
        }
        if(a == null || !a.getPlayers().contains(p)){//make sure it is not null
            p.sendMessage("Invalid operation!");
            return;
        }

        a.getPlayers().remove(p);
        p.setPlayerListName(ChatColor.WHITE + p.getName());
        p.teleport((Location) config.get("MainLobbySpawn"));
    }

    public Arena createArena(Location loc, World world) {
        int num = arenaSize + 1;
        arenaSize++;

        Arena a = new Arena(loc, num);
        arenaList.add(a);
        config.createSection("Arenas." + num + ".spawn");
        config.createSection("Arenas." + num + ".world");
        config.createSection("Arenas." + num + ".lobbySpawn");
        config.createSection("Arenas." + num + ".minPlayers");
        config.createSection("Arenas." + num + ".maxPlayers");
        config.createSection("Arenas." + num + ".gameState");
        config.set("Arenas." + num + ".lobbySpawn", loc);
        config.set("Arenas." + num + ".world", world.getName());
        config.set("Arenas." + num + ".gameState", "WAITING");
        List<Integer> list = config.getIntegerList("Arenas.Arenas");
        list.add(num);
        config.set("Arenas.Arenas", list);
        FireMaster.plugin.saveConfig();

        return a;
    }

    public Arena reloadArena(Location l) {
        int num = arenaSize + 1;
        arenaSize++;

        Arena a = new Arena(l, num);
        arenaList.add(a);

        return a;
    }

    public void removeArena(int i) {
        Arena a = getArena(i);
        if(a == null) {
            return;
        }
        arenaList.remove(a);

        config.set("Arenas." + i, null);
        List<Integer> list = config.getIntegerList("Arenas.Arenas");
        list.remove(i);
        config.set("Arenas.Arenas", list);
        FireMaster.plugin.saveConfig();
    }

    public boolean isInGame(Player p){
        for(Arena a : arenaList){
            if(a.getPlayers().contains(p))
                return true;
        }
        return false;
    }

    public void loadGames(){
        arenaSize = 0;

        if(plugin.getConfig().getIntegerList("Arenas.Arenas").isEmpty()){
            return;
        }

        for(int i : plugin.getConfig().getIntegerList("Arenas.Arenas")){
            Arena a = reloadArena((Location) plugin.getConfig().get("Arenas." + i + ".lobbySpawn"));
            a.id = i;
        }
    }

    public void setGameState(int id, String string) {
        config.set("Arenas." + id + ".gameState", string);
    }

    public String getGameState(int id) {
        return (String) config.get("Arenas." + id + ".gameState");
    }

    private void startLobbyCountdown(Arena arena) {
        LobbyCountdown countdown = new LobbyCountdown();
        countdown.arena = arena;
        countdown.runTaskTimer(plugin, 0, 20);
    }

}
