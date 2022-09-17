package com.azortis.combatlog.listners;

import com.azortis.combatlog.mangers.EventManager;
import org.bukkit.Material;
import org.bukkit.block.data.type.TNT;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashSet;

public class TriggerListener implements Listener {

    private EventManager manager;

    public TriggerListener(EventManager manager){
        this.manager = manager;
    }
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItem().getType() != Material.CREEPER_SPAWN_EGG) return;
        if (event.getClickedBlock().getType() == Material.AIR) return;
        Player player = event.getPlayer();
        Creeper creeper = (Creeper) player.getWorld().spawnEntity(event.getClickedBlock().getLocation().add(0,1,0), EntityType.CREEPER);
        if(manager.creeperTracker.containsKey(player)) manager.creeperTracker.put(player, new HashSet<>());
        manager.creeperTracker.get(player).add(creeper);
    }
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerBlockPlacement(BlockPlaceEvent event) {
        if (!(event.getBlockPlaced().getType() == Material.TNT)) return;
        TNT tnt = (TNT) event.getBlockPlaced();
        tnt.setUnstable(true);
        if(manager.tntTracker.containsKey(event.getPlayer())) manager.tntTracker.put(event.getPlayer(), new HashSet<>());
        manager.tntTracker.get(event.getPlayer()).add(tnt);
        manager.tntTimer.put(tnt, 5);
        //todo add toggle
        //might cause bugs were players can keep someone in combat indefinitely. lava. fire
    }
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        manager.invulnerabilityTimer.put(event.getPlayer().getUniqueId(), 5); //todo variable time + toggle
    }

}
