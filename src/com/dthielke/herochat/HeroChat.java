package com.dthielke.herochat;

import java.util.logging.Logger;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.dthielke.herochat.command.CommandHandler;
import com.dthielke.herochat.command.commands.HelpCommand;

public class HeroChat extends JavaPlugin {

    private static final Logger log = Logger.getLogger("Minecraft");
    private static final CommandHandler cmdHndlr = new CommandHandler();

    @Override
    public void onDisable() {
        log.info(getDescription().getName() + " version " + getDescription().getVersion() + " is disabled.");
    }

    @Override
    public void onEnable() {
        log.info(getDescription().getName() + " version " + getDescription().getVersion() + " is enabled.");
        
        registerCommands();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        return cmdHndlr.dispatch(sender, label, args);
    }
    
    private void registerCommands() {
        cmdHndlr.addCommand(new HelpCommand());
    }
    
    public static CommandHandler getCommandHandler() {
        return cmdHndlr;
    }

}
