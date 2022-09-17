package com.azortis.combatlog.listners;

import com.azortis.combatlog.mangers.EventManager;
import org.bukkit.Material;
import org.bukkit.block.data.type.TNT;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class CombatListener implements Listener {

    EventManager manager;

    public CombatListener(EventManager manager){
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
        if(event.getEntityType() != EntityType.PLAYER) return;
        Player player = (Player) event.getEntity();
        Player damager = null;
        switch (event.getCause()) {
            case MAGIC:
            case PROJECTILE:
                if(!(event.getDamager() instanceof Projectile)) return;
                if(!(((Projectile) event.getDamager()).getShooter() instanceof Player)) return;
                damager = (Player)((Projectile) event.getDamager()).getShooter();
                break;
            case THORNS:
            case ENTITY_SWEEP_ATTACK:
            case ENTITY_ATTACK:
                if (event.getDamager().getType() != EntityType.PLAYER) return;
                damager = (Player) event.getDamager();
                break;
            case ENTITY_EXPLOSION:
                if (!(event.getDamager() instanceof Creeper)) return;
                Creeper creeper = (Creeper) event.getDamager();
                for(Player key: manager.creeperTracker.keySet()){
                    while(manager.creeperTracker.get(key).iterator().hasNext()){
                        if(creeper == manager.creeperTracker.get(key).iterator().next()){
                            damager = key;
                        }
                    }
                }
                // todo toggle, may not work with certain plugins
            default:
        }
        if(damager != null) {
            manager.combatTimer.put(player.getUniqueId(), 30); //todo add variable timer
            manager.combatTimer.put(damager.getUniqueId(), 30);// <--
            if(manager.invulnerabilityTimer.containsKey(damager.getUniqueId())) manager.invulnerabilityTimer.remove(damager.getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event){
        Player player = (Player) event.getEntity();
        //todo FireTick, poison tick
        switch (event.getCause()) {
            case FIRE_TICK:

            case POISON:
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityBlockDamage(EntityDamageByBlockEvent event){
        if (!(event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)) return;
        if (!(event.getDamager().getType() == Material.TNT)) return;
        TNT tnt = (TNT) event.getDamager();
    }

}
