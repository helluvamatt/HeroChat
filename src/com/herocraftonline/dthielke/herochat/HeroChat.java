/**
 * Copyright (C) 2011 DThielke <dave.thielke@gmail.com>
 * 
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send a letter to
 * Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 **/

package com.herocraftonline.dthielke.herochat;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.herocraftonline.dthielke.herochat.channels.ChannelManager;
import com.herocraftonline.dthielke.herochat.chatters.ChatterManager;
import com.herocraftonline.dthielke.herochat.command.CommandManager;
import com.herocraftonline.dthielke.herochat.command.commands.BanCommand;
import com.herocraftonline.dthielke.herochat.command.commands.CreateCommand;
import com.herocraftonline.dthielke.herochat.command.commands.FocusCommand;
import com.herocraftonline.dthielke.herochat.command.commands.GMuteCommand;
import com.herocraftonline.dthielke.herochat.command.commands.HelpCommand;
import com.herocraftonline.dthielke.herochat.command.commands.IgnoreCommand;
import com.herocraftonline.dthielke.herochat.command.commands.JoinCommand;
import com.herocraftonline.dthielke.herochat.command.commands.KickCommand;
import com.herocraftonline.dthielke.herochat.command.commands.LeaveCommand;
import com.herocraftonline.dthielke.herochat.command.commands.ListCommand;
import com.herocraftonline.dthielke.herochat.command.commands.ModCommand;
import com.herocraftonline.dthielke.herochat.command.commands.MuteCommand;
import com.herocraftonline.dthielke.herochat.command.commands.QuickMsgCommand;
import com.herocraftonline.dthielke.herochat.command.commands.ReloadCommand;
import com.herocraftonline.dthielke.herochat.command.commands.RemoveCommand;
import com.herocraftonline.dthielke.herochat.command.commands.ToggleCommand;
import com.herocraftonline.dthielke.herochat.command.commands.WhoCommand;
import com.herocraftonline.dthielke.herochat.util.ConfigManager;
import com.herocraftonline.dthielke.herochat.util.PermissionManager;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class HeroChat extends JavaPlugin {

    public PermissionHandler permissions;
    private ChannelManager channelManager;
    private ChatterManager chatterManager;
    private CommandManager commandManager;
    private ConfigManager configManager;
    private PermissionManager permissionManager;
    private HeroChatPlayerListener playerListener;
    private static Logger log = Logger.getLogger("Minecraft");

    @Override
    public void onDisable() {
        configManager.save();
        PluginDescriptionFile desc = getDescription();
        log(Level.INFO, desc.getName() + " version " + desc.getVersion() + " disabled.");
    }

    @Override
    public void onEnable() {
        channelManager = new ChannelManager(this);
        chatterManager = new ChatterManager();
        configManager = new ConfigManager(this);
        configManager.load();
        configManager.save();

        registerEvents();
        registerCommands();

        loadPermissions();
        checkConflict("iChat");
        checkConflict("EssentialsChat");
        
        PluginDescriptionFile desc = getDescription();
        log(Level.INFO, desc.getName() + " version " + desc.getVersion() + " enabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return commandManager.dispatch(sender, command, label, args);
    }

    private void registerEvents() {
        playerListener = new HeroChatPlayerListener(this);
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvent(Type.PLAYER_CHAT, playerListener, Priority.Highest, this);
        pluginManager.registerEvent(Type.PLAYER_JOIN, playerListener, Priority.Monitor, this);
        pluginManager.registerEvent(Type.PLAYER_QUIT, playerListener, Priority.Monitor, this);
        pluginManager.registerEvent(Type.PLAYER_COMMAND_PREPROCESS, playerListener, Priority.Highest, this);
    }

    private void registerCommands() {
        commandManager = new CommandManager();
        // page 1
        commandManager.addCommand(new ListCommand(this));
        commandManager.addCommand(new WhoCommand(this));
        commandManager.addCommand(new FocusCommand(this));
        commandManager.addCommand(new JoinCommand(this));
        commandManager.addCommand(new LeaveCommand(this));
        commandManager.addCommand(new QuickMsgCommand(this));
        commandManager.addCommand(new IgnoreCommand(this));
        // page 2
        commandManager.addCommand(new CreateCommand(this));
        commandManager.addCommand(new RemoveCommand(this));
        commandManager.addCommand(new ModCommand(this));
        commandManager.addCommand(new KickCommand(this));
        commandManager.addCommand(new BanCommand(this));
        commandManager.addCommand(new MuteCommand(this));
        commandManager.addCommand(new GMuteCommand(this));
        commandManager.addCommand(new ReloadCommand(this));
        // page 3
        commandManager.addCommand(new ToggleCommand(this));
        commandManager.addCommand(new HelpCommand(this));
    }

    public void loadPermissions() {
        Plugin plugin = this.getServer().getPluginManager().getPlugin("Permissions");
        if (plugin != null) {
            if (plugin.isEnabled()) {
                Permissions permissions = (Permissions) plugin;
                this.permissions = permissions.getHandler();
                permissionManager = new PermissionManager(permissions.getHandler());
                this.configManager.loadPlayers();
                log(Level.INFO, "Permissions " + permissions.getDescription().getVersion() + " detected.");
            }
        }
    }

    private void checkConflict(String pluginName) {
        Plugin plugin = this.getServer().getPluginManager().getPlugin(pluginName);
        if (plugin != null) {
            if (plugin.isEnabled()) {
                issueConflictWarning(plugin);
            }
        }
    }

    public void issueConflictWarning(Plugin conflict) {
        log(Level.WARNING, "Conflicting plugin detected: " + conflict.getDescription().getName() + ". If you experience issues, please try disabling this plugin.");
    }

    public void log(Level level, String msg) {
        log.log(level, "[HeroChat] " + msg.replaceAll("§[0-9a-f]", ""));
    }

    public ChannelManager getChannelManager() {
        return channelManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public PermissionManager getPermissionManager() {
        return permissionManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public ChatterManager getChatterManager() {
        return chatterManager;
    }

}
