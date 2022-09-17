package com.azortis.combatlog.mangers;

import com.azortis.combatlog.CombatLog;
import com.azortis.combatlog.commands.BlockedCommands;
import com.azortis.combatlog.commands.CombatTimeCommand;
import com.azortis.combatlog.listners.AffectListener;
import com.azortis.combatlog.listners.CombatListener;
import com.azortis.combatlog.listners.TriggerListener;
import org.bukkit.Bukkit;
import org.bukkit.block.data.type.TNT;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;


public class EventManager {

    public Map<UUID, Integer> combatTimer = new HashMap<>();
    public Map<UUID, Integer> invulnerabilityTimer = new HashMap<>();
    public Map<Player, Set<Creeper>> creeperTracker = new HashMap<>();
    public Map<Player, Set<TNT>> tntTracker = new HashMap<>();
    public Map<TNT, Integer> tntTimer = new HashMap<>();
    public List<String> blockedCommands = new ArrayList<>();

    public EventManager(CombatLog plugin){
        Bukkit.getServer().getPluginManager().registerEvents(new TriggerListener(this), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new CombatListener(this), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new AffectListener(this), plugin);
        Objects.requireNonNull(plugin.getCommand("combatTimer")).setExecutor(new CombatTimeCommand(this));
        Objects.requireNonNull(plugin.getCommand("blockedCommand")).setExecutor(new BlockedCommands(this));
        new BukkitRunnable() {
            @Override
            public void run() {
                processAttack();
            }
        }.runTaskTimer(plugin, 20, 20);
    }

    public void processAttack(){
        if(combatTimer != null) {
            combatTimer.forEach((key, value) -> {
                if(value == 0){
                    combatTimer.remove(key);
                } else {
                    combatTimer.put(key, value-1);
                    Bukkit.getPlayer(key).sendMessage("combat Time: " + value + "s");
                }
            });
        }
        if (invulnerabilityTimer != null) {
            invulnerabilityTimer.forEach((key, value) -> {
                if(value == 0){
                    invulnerabilityTimer.remove(key);
                } else {
                    invulnerabilityTimer.put(key, value-1);
                }
            });
        }
        if (tntTimer != null) {
            tntTimer.forEach((key, value) ->{
                if(value == 0){
                    tntTimer.remove(key);
                    tntTracker.forEach((key1, value1) -> {
                        if(value1.contains(key)){
                            value1.remove(key);
                        }
                    });
                } else {
                    tntTimer.put(key, value-1);
                }
            });
        }


    }
}
