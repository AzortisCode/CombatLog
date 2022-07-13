package com.azortis.combatlog.managers;

import com.azortis.combatlog.CombatLog;
import com.azortis.combatlog.listener.CombatListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CombatManager {
    private final CombatLog plugin;

    public Map<UUID, Integer> combatTimer = new HashMap<>();

    public CombatManager(CombatLog plugin) {
        this.plugin = plugin;


        Bukkit.getServer().getPluginManager().registerEvents(new CombatListener(this), plugin);
    }



    public void processAttack(Player damaged, Player damager) {
        // todo: do this
        combatTimer.put(damaged.getUniqueId(), 30);
        combatTimer.put(damager.getUniqueId(), 30);
    }
}
