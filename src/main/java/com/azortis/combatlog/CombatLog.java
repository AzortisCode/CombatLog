package com.azortis.combatlog;

import com.azortis.combatlog.hook.WorldGuardHook;
import com.azortis.combatlog.managers.CombatManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class CombatLog extends JavaPlugin {

    private CombatManager combatManager;

    @Override
    public void onEnable() {
        // Plugin startup logic

        // Configurations


        // Storage

        // Feature enabling
        combatManager = new CombatManager(this);

        if(WorldGuardHook.init()) {
            // todo: do our worldguard features here
        }




    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
