package com.azortis.combatlog.listener;

import com.azortis.combatlog.managers.CombatManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CombatListener implements Listener {
    private final CombatManager manager;
    private Map<UUID, Set<Creeper>> creeperSpawner = new HashMap<>();
    public CombatListener(CombatManager manager) {
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) return;
        Player player = (Player) event.getEntity();
        Player damager = null;
        switch (event.getCause()) {
            case MAGIC:
            case PROJECTILE:
                if (!(event.getDamager() instanceof Projectile)) return;
                if (!(((Projectile) event.getDamager()).getShooter() instanceof Player)) return;
                damager = (Player) ((Projectile) event.getDamager()).getShooter();
                break;
            case THORNS:
            case ENTITY_SWEEP_ATTACK:
            case ENTITY_ATTACK:
                if (event.getDamager().getType() != EntityType.PLAYER) return;
                damager = (Player) event.getDamager();
                break;
            case ENTITY_EXPLOSION:
                if (event.getDamager().getType() != EntityType.CREEPER) return;
                for(Player spawner : Bukkit.getOnlinePlayers()) {
                    if(creeperSpawner.get(spawner.getUniqueId()).contains(event.getDamager())) damager = spawner;
                }
                break;
            default:
                break;
        }

        if (damager != null) {
            manager.processAttack(player, damager);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamageEvent(EntityDamageEvent event) {
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityBlockDamageEvent(EntityDamageByBlockEvent event) {
        if(event.getEntity().getType() != EntityType.PLAYER) return;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {

    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLeave(PlayerQuitEvent event) {
        creeperSpawner.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {

    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getItem().getType() == Material.CREEPER_SPAWN_EGG) return;
        if(event.getClickedBlock().getType() == Material.AIR) return;
        Creeper creeper = (Creeper) event.getPlayer().getWorld().spawnEntity(event.getClickedBlock().getLocation().add(0, 1, 0), EntityType.CREEPER, CreatureSpawnEvent.SpawnReason.EGG);
        Player player = event.getPlayer();
        if (!creeperSpawner.containsKey(player.getUniqueId())) creeperSpawner.put(player.getUniqueId(), new HashSet<>());
        creeperSpawner.get(player.getUniqueId()).add(creeper);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntitySpawn(CreatureSpawnEvent event) {
        if(event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.EGG) return;
        if(event.getEntity().getType() != EntityType.CREEPER) return;
        if(!(creeperSpawner.containsKey(event.getLocation()))) return;
    }
}