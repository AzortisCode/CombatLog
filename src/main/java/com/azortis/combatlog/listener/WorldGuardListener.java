package com.azortis.combatlog.listener;

import com.azortis.combatlog.managers.WorldGuardManager;
import org.bukkit.event.Listener;

public class WorldGuardListener implements Listener {

    private WorldGuardManager worldGuardManager;

    public WorldGuardListener(WorldGuardManager worldGuardManager) {
        this.worldGuardManager = worldGuardManager;
    }

}