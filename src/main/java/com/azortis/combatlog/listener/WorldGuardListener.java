package com.azortis.combatlog.listener;

import com.azortis.combatlog.managers.CombatManager;
import com.azortis.combatlog.managers.WorldGuardManager;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.*;

public class WorldGuardListener implements Listener {

    private WorldGuardManager worldGuardManager;
    private CombatManager combatManager;
    private List<Location> locations = new ArrayList<>();
    private Map<String, ProtectedRegion> regions = new HashMap<>();
    private List<ProtectedRegion> noPvpRegions = new ArrayList<>();
    private RegionContainer container;

    public WorldGuardListener(WorldGuardManager worldGuardManager, CombatManager combatManager) {
        this.worldGuardManager = worldGuardManager;
        this.combatManager = combatManager;
        container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                worldGuardTimer();
            }
        };
       new Timer().schedule(task, 0, 2);
    }

    public void worldGuardTimer(){
        combatManager.combatTimer.forEach((key,value) -> {
            //todo find a way to implement combatManager
            //key == UUID
            //value == combat time
            Player player = Bukkit.getPlayer(key);
            RegionManager regionManager = container.get(BukkitAdapter.adapt(player.getWorld()));
            if(regionManager != null) return;
            worldGuardProcessing(player, regionManager);
        });
    }

    public void worldGuardProcessing(Player player, RegionManager regionManager) {
        regions = regionManager.getRegions();
        regions.forEach((key,value) -> {
            if(value.getFlag(Flags.PVP) == StateFlag.State.ALLOW) {
                noPvpRegions.add(value);
            }
        });
        BlockVector3 min = BlockVector3.at(player.getLocation().getX()-3, player.getLocation().getY()-3, player.getLocation().getZ()-3);
        BlockVector3 max = BlockVector3.at(player.getLocation().getX()+3, player.getLocation().getY()+3, player.getLocation().getZ()+3);
        ProtectedRegion playerRegion = new ProtectedCuboidRegion("player", min, max);
        List<BlockVector2> points = playerRegion.getPoints();
        for(Location location: locations) {
            if(!playerRegion.contains((int) location.getX(),(int) location.getY(),(int) location.getZ())){
                player.sendBlockChange(location, Material.AIR.createBlockData());
                locations.remove(location);
            }
        }
        for(ProtectedRegion region: noPvpRegions) {
            for (BlockVector2 point: points) {
                for (int i = -3; i <= 6; i++) {
                    if (region.contains(point.getX(), (int) player.getLocation().getY() + i, point.getZ())) {
                        locations.add(new Location(player.getWorld(), point.getX(), (int) player.getLocation().getY() + i, point.getZ()));
                    }
                }
            }
        }
        for(Location location: locations) {
            if(location.getBlock().getType() != Material.AIR) player.sendBlockChange(location, Material.RED_STAINED_GLASS.createBlockData());
        }
    }
}