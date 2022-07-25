package com.azortis.combatlog.managers;

import com.azortis.combatlog.CombatLog;
import com.azortis.combatlog.listener.WorldGuardListener;
import org.bukkit.Bukkit;

public class WorldGuardManager {

    public final CombatLog plugin;
    public CombatManager combatManager;

    public WorldGuardManager(CombatLog combatLog, CombatManager combatManager) {
        this.plugin = combatLog;
        this.combatManager = combatManager;
        Bukkit.getServer().getPluginManager().registerEvents(new WorldGuardListener(this, combatManager), plugin);
    }

}
