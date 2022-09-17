package com.azortis.combatlog.commands;

import com.azortis.combatlog.mangers.EventManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BlockedCommands implements CommandExecutor {

    EventManager manager;

    public BlockedCommands(EventManager eventManager){
        this.manager = eventManager;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) return false;
        if(strings == null) return false;
        if(strings[0].equalsIgnoreCase("add") || strings[0].equalsIgnoreCase("include")) {
            if(strings[1] == null) return false;
            for (int i = 1; i < strings.length; i++){
                manager.blockedCommands.add(strings[i].toLowerCase());
            }
        } else if(strings[0].equalsIgnoreCase("remove") || strings[0].equalsIgnoreCase("exclude")) {
            if(strings[1] == null) return false;
            manager.blockedCommands.forEach((value) ->{
                for (int i = 1; i < strings.length; i++){
                    if(value.equalsIgnoreCase(strings[i])){
                        manager.blockedCommands.remove(value);
                    }
                }
            });
        } else if (strings[0].equalsIgnoreCase("list")) {
            commandSender.sendMessage("current blocked commands:" + manager.blockedCommands);
        }
        //todo add command feedback (chatmessage)
        return true;
    }
}
