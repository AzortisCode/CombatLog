package com.azortis.combatlog.listener;

import com.azortis.combatlog.managers.WorldGuardManager;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldGuardListener implements Listener {

    private WorldGuardManager worldGuardManager;
    private List<Location> locations = new ArrayList<>();
    private Map<String, ProtectedRegion> regions = new HashMap<>();
    private List<ProtectedRegion> noPvpRegions = new ArrayList<>();

    public WorldGuardListener(WorldGuardManager worldGuardManager) {
        this.worldGuardManager = worldGuardManager;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMovement(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = container.get(BukkitAdapter.adapt(player.getWorld()));
        if(regionManager == null) return;
        regions = regionManager.getRegions();
        regions.forEach((key,value) -> {
            if(value.getFlag(Flags.PVP) == StateFlag.State.ALLOW) {
                noPvpRegions.add(value);
            }
        });
        BlockVector3 min = BlockVector3.at(player.getLocation().getX()-5, player.getLocation().getY()-5, player.getLocation().getZ()-5);
        BlockVector3 max = BlockVector3.at(player.getLocation().getX()+5, player.getLocation().getY()+5, player.getLocation().getZ()+5);
        ProtectedRegion playerRegion = new ProtectedCuboidRegion("player", min, max);
        List<BlockVector2> points = playerRegion.getPoints();
        for(ProtectedRegion region: noPvpRegions) {
            for (BlockVector2 point: points) {
                for (int i = -5; i <= 10; i++) {
                    if (region.contains(point.getX(), (int) player.getLocation().getY() + i, point.getZ())) {
                        locations.add(new Location(player.getWorld(), point.getX(), (int) player.getLocation().getY() + i, point.getZ()));
                    }
                }
            }
        }
        worldGuardManager.fakeBlockSpawn(locations, player);
        //todo update fakeblocks each tick
    }
}