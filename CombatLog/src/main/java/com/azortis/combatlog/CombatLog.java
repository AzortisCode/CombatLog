package com.azortis.combatlog;

import com.azortis.combatlog.hooks.WorldGuardHook;
import com.azortis.combatlog.mangers.EventManager;
import com.azortis.combatlog.mangers.WorldGuardManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class CombatLog extends JavaPlugin {

    EventManager eventManager;

    @Override
    public void onEnable() {
        // Plugin startup logic

        //storage

        //initialize
        eventManager = new EventManager(this);
        if(WorldGuardHook.init()) {
            new WorldGuardManager();
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
