package com.herocraftonline.dthielke.herochat.channels;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.herocraftonline.dthielke.herochat.HeroChat;
import com.herocraftonline.dthielke.herochat.event.HeroChatMessagePreprocessEvent;
import com.herocraftonline.dthielke.herochat.util.Message;
import com.herocraftonline.dthielke.herochat.util.MessageFormatter;

public class Channel {

    public static enum Mode {
        INCLUSIVE,
        EXCLUSIVE;
    }
    
    protected final HeroChat plugin;
    
    protected String name;
    protected String nick;
    protected String pass;
    protected String format;
    
    protected ChatColor color;
    protected Mode mode;
    
    protected boolean enabled = true;
    protected boolean verbose = true;
    protected boolean quick = false;
    
    protected Set<Player> players = new HashSet<Player>();
    protected Set<String> moderators = new HashSet<String>();
    protected Set<String> bans = new HashSet<String>();
    protected Set<String> mutes = new HashSet<String>();
    
    public Channel(HeroChat plugin, String name, String nick, String pass, String format, ChatColor color, Mode mode) {
        this.plugin = plugin;
        this.name = name;
        this.nick = nick;
        this.pass = pass;
        this.format = format;
        this.color = color;
        this.mode = mode;
    }
    
    public boolean sendPlayerMessage(Player sender, String msg) {
        // aggregate necessary data in a Message object
        Message msgContainer = new Message();
        msgContainer.sender = sender.getName();
        msgContainer.channelColor = color.toString();
        msgContainer.channelName = name;
        msgContainer.channelNick = nick;
        msgContainer.format = format;
        msgContainer.health = MessageFormatter.createHealthBar(sender.getHealth());
        msgContainer.world = sender.getWorld().getName();
        msgContainer.group = plugin.permissions.getGroup(msgContainer.world, msgContainer.sender);
        msgContainer.groupPrefix = plugin.permissions.getGroupPrefix(msgContainer.world, msgContainer.sender);
        msgContainer.groupSuffix = plugin.permissions.getGroupSuffix(msgContainer.world, msgContainer.sender);
        msgContainer.prefix = plugin.permissions.getUserPermissionString(msgContainer.world, msgContainer.sender, "prefix");
        // use the group prefix is the user has no prefix of his/her own
        msgContainer.prefix = (msgContainer.prefix == null) ? msgContainer.groupPrefix : msgContainer.prefix;
        msgContainer.suffix = plugin.permissions.getUserPermissionString(msgContainer.world, msgContainer.sender, "suffix");
        // use the group suffix is the user has no suffix of his/her own
        msgContainer.suffix = (msgContainer.suffix == null) ? msgContainer.groupSuffix : msgContainer.suffix;
        // strip colors if sender doesn't have herochat.allowColors
        if (!plugin.permissions.has(sender, "herochat.allowColors")) {
            msg = MessageFormatter.stripColors(msg);
        }
        msgContainer.message = msg;
        
        // call preprocess event
        HeroChatMessagePreprocessEvent preprocessEvent = new HeroChatMessagePreprocessEvent(msgContainer);
        plugin.getServer().getPluginManager().callEvent(preprocessEvent);
        if (preprocessEvent.isCancelled()) {
            return false;
        }
        
        String formattedMsg = MessageFormatter.format(msgContainer);
        
        return true;
    }
    
    public boolean sendPluginMessage(JavaPlugin sender, String msg) {
        return true;
    }
    
}
