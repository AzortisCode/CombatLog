package com.azortis.combatlog.commands;

import com.azortis.combatlog.mangers.EventManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CombatTimeCommand implements CommandExecutor {

    EventManager manager;

    public CombatTimeCommand(EventManager manager){
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) return false;
        Integer CT = manager.combatTimer.get(((Player) commandSender).getUniqueId());
        commandSender.sendMessage("CombatTimer: " + CT + "s");
        return true;
    }
}