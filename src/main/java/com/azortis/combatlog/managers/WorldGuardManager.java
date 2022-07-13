package com.azortis.combatlog.managers;

import com.azortis.combatlog.CombatLog;
import com.azortis.combatlog.listener.WorldGuardListener;
import org.bukkit.Bukkit;

public class WorldGuardManager {

    private final CombatLog plugin;

    public WorldGuardManager(CombatLog plugin) {
        this.plugin = plugin;


        Bukkit.getServer().getPluginManager().registerEvents(new WorldGuardListener(this), plugin);
    }

}