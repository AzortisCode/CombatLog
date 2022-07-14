package com.azortis.combatlog.managers;

import com.azortis.combatlog.CombatLog;
import com.azortis.combatlog.listener.CombatListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class CombatManager {
    private final CombatLog plugin;
    public Map<UUID, Set<Creeper>> creeperSpawner = new HashMap<>();
    public Map<UUID, Integer> combatTimer = new HashMap<>();

    public CombatManager(CombatLog plugin) {
        this.plugin = plugin;


        Bukkit.getServer().getPluginManager().registerEvents(new CombatListener(this), plugin);
    }

    public void processAttack(Player damaged, Player damager) {
        combatTimer.put(damaged.getUniqueId(), 30);
        combatTimer.put(damager.getUniqueId(), 30);
        new BukkitRunnable() {
            @Override
            public void run() {
                if(combatTimer.get(damaged.getUniqueId()) == 0){
                    combatTimer.remove(damaged.getUniqueId());
                } else if (combatTimer.get(damaged.getUniqueId()) > 0) {
                    combatTimer.put(damaged.getUniqueId(), combatTimer.get(damaged.getUniqueId()) - 1);
                }
                if(combatTimer.get(damager.getUniqueId()) == 0){
                    combatTimer.remove(damager.getUniqueId());
                } else if (combatTimer.get(damager.getUniqueId()) > 0) {
                    combatTimer.put(damager.getUniqueId(), combatTimer.get(damager.getUniqueId()) - 1);
                }
                if(combatTimer.containsKey(damaged.getUniqueId()) && combatTimer.containsKey(damager.getUniqueId())) cancel();
            }
        }.runTaskTimer(plugin, 20, 20);
    }

    public void trackingCreeper(Player player) {
        Creeper creeper = (Creeper) player.getWorld().spawnEntity(player.getLocation().add(0,1,0), EntityType.CREEPER);
        if (!creeperSpawner.containsKey(player.getUniqueId())) creeperSpawner.put(player.getUniqueId(), new HashSet<>());
        creeperSpawner.get(player.getUniqueId()).add(creeper);
    }

    public void spawnVillager(Player player){

    }
}