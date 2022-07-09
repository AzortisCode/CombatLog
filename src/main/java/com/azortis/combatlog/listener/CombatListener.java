package com.azortis.combatlog.listener;

import com.azortis.combatlog.managers.CombatManager;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class CombatListener implements Listener {
    private final CombatManager manager;

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
                // todo: track who spawns creepers
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

    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLeave(PlayerQuitEvent event) {
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {

    }


}
