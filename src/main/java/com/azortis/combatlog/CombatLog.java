package com.azortis.combatlog;

import com.azortis.combatlog.hook.WorldGuardHook;
import com.azortis.combatlog.managers.CombatManager;
import com.azortis.combatlog.managers.WorldGuardManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class CombatLog extends JavaPlugin {

    private CombatManager combatManager;
    private WorldGuardManager worldGuardManager;

    @Override
    public void onEnable() {
        // Plugin startup logic

        // Configurations


        // Storage

        // Feature enabling
        combatManager = new CombatManager(this);

        if(WorldGuardHook.init()) {
            worldGuardManager = new WorldGuardManager(this, combatManager);
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
