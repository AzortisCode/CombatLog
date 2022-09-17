package com.azortis.combatlog.listners;

import com.azortis.combatlog.mangers.EventManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class AffectListener implements Listener {

    private EventManager manager;

    public AffectListener(EventManager manager){
        this.manager = manager;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event){
        if(event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) return; //todo toggle enderpearl option
        if(manager.combatTimer.containsKey(event.getPlayer().getUniqueId())) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event){
        if(manager.combatTimer.containsKey(event.getPlayer().getUniqueId())) event.getPlayer().setHealth(0); //todo villager mode
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event){
        if(!(manager.combatTimer.containsKey(event.getPlayer().getUniqueId()))) return;
        if(!(manager.blockedCommands.contains(event.getMessage()))) return;
        event.setCancelled(true);
    }

}
