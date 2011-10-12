package com.dthielke.herochat;

import java.util.logging.Logger;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.dthielke.herochat.command.CommandHandler;
import com.dthielke.herochat.command.commands.HelpCommand;

public class HeroChat extends JavaPlugin {

    private static final Logger log = Logger.getLogger("Minecraft");
    private static final CommandHandler cmdHndlr = new CommandHandler();
    private static final ChannelManager chnnlMngr = new ChannelManager();
    private static final ChatterManager chttrMngr = new ChatterManager();

    @Override
    public void onDisable() {
        log.info(getDescription().getName() + " version " + getDescription().getVersion() + " is disabled.");
    }

    @Override
    public void onEnable() {
        log.info(getDescription().getName() + " version " + getDescription().getVersion() + " is enabled.");

        registerCommands();
        registerEvents();

        setupDummyEnvironment();
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        return cmdHndlr.dispatch(sender, label, args);
    }

    private void registerCommands() {
        cmdHndlr.addCommand(new HelpCommand());
    }

    private void registerEvents() {
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvent(Type.PLAYER_CHAT, new PlayerChatListener(), Priority.High, this);
    }

    private void setupDummyEnvironment() {
        Channel channel = new StandardChannel("Dummy", "D");
        chnnlMngr.addChannel(channel);

        for (Player player : getServer().getOnlinePlayers()) {
            Chatter chatter = new StandardChatter(player);
            chttrMngr.addChatter(chatter);
            chatter.addChannel(channel);
            chatter.setActiveChannel(channel);
        }
    }

    public static CommandHandler getCommandHandler() {
        return cmdHndlr;
    }
    
    public static ChannelManager getChannelManager() {
        return chnnlMngr;
    }
    
    public static ChatterManager getChatterManager() {
        return chttrMngr;
    }

}
