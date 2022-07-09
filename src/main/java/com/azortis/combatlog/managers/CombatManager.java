package com.azortis.combatlog.managers;

import com.azortis.combatlog.CombatLog;
import com.azortis.combatlog.listener.CombatListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CombatManager {
    private final CombatLog plugin;

    public CombatManager(CombatLog plugin) {
        this.plugin = plugin;


        Bukkit.getServer().getPluginManager().registerEvents(new CombatListener(this), plugin);
    }



    public void processAttack(Player damaged, Player damager) {
        // todo: do this
    }
}
