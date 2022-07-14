package com.azortis.combatlog.managers;

import com.azortis.combatlog.CombatLog;
import com.azortis.combatlog.hook.WorldGuardHook;
import com.azortis.combatlog.listener.WorldGuardListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class WorldGuardManager {

    private final CombatLog plugin;
    private static WorldGuardHook instance;

    public WorldGuardManager(CombatLog plugin) {
        this.plugin = plugin;
        instance = WorldGuardHook.getInstance();
        Bukkit.getServer().getPluginManager().registerEvents(new WorldGuardListener(this), plugin);
    }

    public void fakeBlockSpawn(List<Location> locations, Player player) {
        for(Location location: locations) {
            player.sendBlockChange(location, Material.RED_STAINED_GLASS.createBlockData());
        }
    }
}