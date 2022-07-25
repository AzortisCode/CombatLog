package com.azortis.combatlog.managers;

import com.azortis.combatlog.CombatLog;
import com.azortis.combatlog.listener.CombatListener;
import com.azortis.combatlog.listener.WorldGuardListener;
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
        new BukkitRunnable() {
            @Override
            public void run() {
                processAttack();
            }
        }.runTaskTimer(plugin, 20, 20);
        Bukkit.getServer().getPluginManager().registerEvents(new CombatListener(this), plugin);
    }

    public void processAttack() {
        combatTimer.forEach((key, value) -> {
            if(value == 0){
                combatTimer.remove(key);
            } else if (value > 0) {
                combatTimer.put(key, value-1);
            }
        });
    }

    public void trackingCreeper(Player player) {
        Creeper creeper = (Creeper) player.getWorld().spawnEntity(player.getLocation().add(0,1,0), EntityType.CREEPER);
        if (!creeperSpawner.containsKey(player.getUniqueId())) creeperSpawner.put(player.getUniqueId(), new HashSet<>());
        creeperSpawner.get(player.getUniqueId()).add(creeper);
    }

}