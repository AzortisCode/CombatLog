package com.azortis.combatlog.hook;

import org.bukkit.Bukkit;

import java.util.logging.Level;

public class WorldGuardHook {
    private static WorldGuardHook instance;
    private final boolean hasWorldGuard;

    public WorldGuardHook(boolean hasWorldGuard) {
        this.hasWorldGuard = hasWorldGuard;
    }

    public boolean isHasWorldGuard() {
        return hasWorldGuard;
    }

    public static WorldGuardHook getInstance() {
        if (instance == null) {
            // Throw an error here, not initialized
            Bukkit.getServer().getLogger().log(Level.WARNING, "Worldguard is not initialized!");
        }
        return instance;
    }

    // Only be called by our main method
    public static boolean init() {
        boolean isEnabled = Bukkit.getServer().getPluginManager().isPluginEnabled("WorldGuard");
        instance = new WorldGuardHook(isEnabled);

        Bukkit.getServer().getLogger().log(Level.WARNING, "Worldguard is required for some features of this plugin, worldguard dependent features are disabled.");

        // Returns that whether worldguard exists or not.
        return isEnabled;
    }
}
