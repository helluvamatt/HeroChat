package com.herocraftonline.dthielke.herochat.channels;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.ConfigurationNode;

import com.herocraftonline.dthielke.herochat.HeroChat;
import com.herocraftonline.dthielke.herochat.chatters.Chatter;
import com.herocraftonline.dthielke.herochat.event.ChannelMessageEvent;
import com.herocraftonline.dthielke.herochat.messages.Message;
import com.herocraftonline.dthielke.herochat.messages.PlayerMessage;
import com.herocraftonline.dthielke.herochat.messages.PluginMessage;
import com.herocraftonline.dthielke.herochat.util.Messaging;
import com.herocraftonline.dthielke.herochat.util.PermissionManager.ChannelPermission;

public class Channel {

    public static enum Mode {
        INCLUSIVE,
        EXCLUSIVE;
    }

    public static final String MSG_FORMAT = "{color}[{nick}] {prefix}{sender}{color}{suffix}: {message}";
    public static final String JOIN_FORMAT = "{color}[{nick}]: {message}";
    public static final String LEAVE_FORMAT = "{color}[{nick}]: {message}";
    public static final String BAN_FORMAT = "{color}[{nick}]: {message}";

    protected final HeroChat plugin;

    protected String name;
    protected String nick;
    protected String password;
    protected String format;

    protected ChatColor color;
    protected Mode mode;

    protected boolean enabled = true;
    protected boolean verbose = true;
    protected boolean quick = false;

    protected Set<Chatter> chatters = new HashSet<Chatter>();
    protected Set<String> moderators = new HashSet<String>();
    protected Set<String> bans = new HashSet<String>();
    protected Set<String> mutes = new HashSet<String>();

    public Channel(HeroChat plugin, String name, String nick) {
        this.plugin = plugin;
        this.name = name;
        this.nick = nick;
        this.password = "";
        this.format = MSG_FORMAT;
        this.color = ChatColor.WHITE;
        this.mode = Mode.INCLUSIVE;
    }

    public Channel(HeroChat plugin, String name, String nick, String password, String format, ChatColor color, Mode mode) {
        this(plugin, name, nick);
        this.password = password;
        this.format = format;
        this.color = color;
        this.mode = mode;
    }

    public boolean canJoin(Chatter chatter) {
        Player player = chatter.getPlayer();
        if (mode == Mode.INCLUSIVE) {
            return !plugin.getPermissionManager().hasPermission(player, this, ChannelPermission.DENY);
        } else if (mode == Mode.EXCLUSIVE) {
            return plugin.getPermissionManager().hasPermission(player, this, ChannelPermission.ALLOW);
        }
        return false;
    }

    public boolean canSpeak(Chatter chatter) {
        Player player = chatter.getPlayer();
        if (mode == Mode.INCLUSIVE) {
            return !plugin.getPermissionManager().hasPermission(player, this, ChannelPermission.MUTE);
        } else if (mode == Mode.EXCLUSIVE) {
            return plugin.getPermissionManager().hasPermission(player, this, ChannelPermission.SPEAK);
        }
        return false;
    }

    public boolean sendMessage(Message message) {
        if (!enabled) {
            return false;
        }
        
        // fire a message event
        ChannelMessageEvent event = new ChannelMessageEvent(message);
        plugin.getServer().getPluginManager().callEvent(event);

        // check if the event was cancelled
        if (event.isCancelled()) {
            return false;
        }

        // format the message
        String formatted = Messaging.format(event.getData());

        // send the result to the recipients
        for (Chatter chatter : message.getRecipients()) {
            chatter.getPlayer().sendMessage(formatted);
        }

        return true;
    }
    
    public boolean sendPlayerMessage(Chatter sender, String message) {
        return sendPlayerMessage(sender, message, format);
    }

    public boolean sendPlayerMessage(Chatter sender, String message, String format) {
        Player player = sender.getPlayer();

        // check if the player is muted
        if (mutes.contains(player.getName()) || sender.isMuted()) {
            Messaging.send(player, "You are muted.");
            return false;
        }

        // fetch necessary data
        String name = player.getName();
        String world = player.getWorld().getName();
        String prefix = plugin.permissions.getUserPermissionString(world, name, "prefix");
        String suffix = plugin.permissions.getUserPermissionString(world, name, "suffix");
        String group = plugin.permissions.getGroup(world, name);
        String groupPrefix = plugin.permissions.getGroupPrefix(world, group);
        String groupSuffix = plugin.permissions.getGroupSuffix(world, group);

        // create the message object
        PlayerMessage msgContainer = new PlayerMessage(message, format, this, chatters, sender, prefix, suffix, group, groupPrefix, groupSuffix);

        // send the message
        return sendMessage(msgContainer);
    }
    
    public boolean sendPluginMessage(JavaPlugin sender, String message) {
        return sendPluginMessage(sender, message, format);
    }

    public boolean sendPluginMessage(JavaPlugin sender, String message, String format) {
        // create the message object
        PluginMessage msgContainer = new PluginMessage(message, format, this, chatters, sender);

        // send the message
        return sendMessage(msgContainer);
    }

    public boolean addChatter(Chatter chatter, boolean notify) {
        Player player = chatter.getPlayer();
        // check if the player is banned
        if (bans.contains(player.getName())) {
            // notify if banned
            if (notify) {
                Messaging.send(player, "You are banned from $1.", name);
            }
            return false;
        }
        // add the player
        boolean joined = chatters.add(chatter);
        if (joined) {
            chatter.addToChannel(this);
        }
        // notify if added
        if (notify && joined) {
            if (verbose) {
                sendPluginMessage(plugin, "&f" + player.getDisplayName() + color + " joined the channel.", JOIN_FORMAT);
            }
        }
        return joined;
    }

    public boolean removeChatter(Chatter chatter, boolean notify) {
        Player player = chatter.getPlayer();
        // remove the player
        boolean removed = chatters.remove(player);
        if (removed) {
            chatter.removeFromChannel(this);
        }
        // notify if removed
        if (notify && removed) {
            if (verbose) {
                sendPluginMessage(plugin, "&f" + player.getDisplayName() + color + " left the channel.", LEAVE_FORMAT);
                Messaging.send(player, "Left $1.", name);
            }
        }
        return removed;
    }

    public boolean hasChatter(Chatter chatter) {
        return chatters.contains(chatter);
    }

    public boolean addModerator(Chatter chatter, boolean notify) {
        Player player = chatter.getPlayer();

        // add the player to the moderators list
        boolean added = addModerator(player.getName());
        // notify if added
        if (notify && added) {
            Messaging.send(player, "Now moderating $1.", name);
        }
        return added;
    }

    private boolean addModerator(String player) {
        return moderators.add(player);
    }

    public boolean removeModerator(Chatter chatter, boolean notify) {
        Player player = chatter.getPlayer();

        // remove the player from the moderators list
        boolean removed = removeModerator(player.getName());
        // notify if removed
        if (notify && removed) {
            Messaging.send(player, "No longer moderating $1.", name);
        }
        return removed;
    }

    private boolean removeModerator(String player) {
        return moderators.remove(player);
    }

    public boolean isModerator(Chatter chatter) {
        return moderators.contains(chatter.getPlayer().getName());
    }

    public final String[] getModerators() {
        return moderators.toArray(new String[0]);
    }

    public boolean banChatter(Chatter chatter, boolean notify) {
        Player player = chatter.getPlayer();

        // add the player to the bans list
        boolean banned = banPlayer(player.getName());
        // notify if banned
        if (notify && banned) {
            sendPluginMessage(plugin, "&f" + player.getDisplayName() + color + " was banned from the channel.", BAN_FORMAT);
        }
        // remove the player from the channel
        if (banned) {
            removeChatter(chatter, true);
        }
        return banned;
    }

    private boolean banPlayer(String player) {
        return bans.add(player);
    }

    public boolean isBanned(Chatter chatter) {
        return isBanned(chatter.getPlayer().getName());
    }
    
    public boolean isBanned(String player) {
        return bans.contains(player);
    }

    public boolean unbanChatter(Chatter chatter, boolean notify) {
        Player player = chatter.getPlayer();

        // remove the player from the moderators list
        boolean unbanned = unbanPlayer(player.getName());
        // notify if removed
        if (notify && unbanned) {
            Messaging.send(player, "No longer banned from $1.", name);
        }
        return unbanned;
    }

    private boolean unbanPlayer(String player) {
        return bans.remove(player);
    }

    public final String[] getBans() {
        return bans.toArray(new String[0]);
    }

    public boolean muteChatter(Chatter chatter, boolean notify) {
        Player player = chatter.getPlayer();

        // add the player to the bans list
        boolean muted = mutePlayer(player.getName());
        // notify if muted
        if (notify && muted) {
            Messaging.send(player, "Muted in $1.", name);
        }
        // remove the player from the channel
        if (muted) {
            removeChatter(chatter, false);
        }
        return muted;
    }

    private boolean mutePlayer(String player) {
        return mutes.add(player);
    }

    public boolean unmuteChatter(Chatter chatter, boolean notify) {
        Player player = chatter.getPlayer();

        // remove the player from the moderators list
        boolean unmuted = unmutePlayer(player.getName());
        // notify if unmuted
        if (notify && unmuted) {
            Messaging.send(player, "No longer muted in $1.", name);
        }
        return unmuted;
    }

    public boolean unmutePlayer(String player) {
        return mutes.remove(player);
    }
    
    public boolean isMuted(Chatter chatter) {
        return isMuted(chatter.getPlayer().getName());
    }
    
    public boolean isMuted(String player) {
        return mutes.contains(player);
    }

    public final String[] getMutes() {
        return mutes.toArray(new String[0]);
    }
    
    public final Chatter[] getChatters() {
        return chatters.toArray(new Chatter[0]);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public ChatColor getColor() {
        return color;
    }

    public void setColor(ChatColor color) {
        this.color = color;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public boolean isQuick() {
        return quick;
    }

    public void setQuick(boolean quick) {
        this.quick = quick;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (name == null ? 0 : name.toLowerCase().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Channel other = (Channel) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equalsIgnoreCase(other.name)) {
            return false;
        }
        return true;
    }

    public void save(ConfigurationNode config) {
        config.setProperty("nickname", nick);
        config.setProperty("password", password);
        config.setProperty("format", format);
        config.setProperty("color", color.toString());
        config.setProperty("distance", 0);
        config.setProperty("flags.join-messages", verbose);
        config.setProperty("flags.shortcut-allowed", quick);
        config.setProperty("lists.bans", bans);
        config.setProperty("lists.moderators", moderators);
    }

    public static Channel load(HeroChat plugin, ConfigurationNode config, String name) {
        // Collect necessary data from the config
        String nick = config.getString("nickname", "nick");
        String password = config.getString("password", "");
        String format = config.getString("message-format", "");
        ChatColor color = ChatColor.valueOf(config.getString("color", "WHITE"));

        boolean verbose = config.getBoolean("flags.join-messages", true);
        boolean quick = config.getBoolean("flags.shortcut-allowed", false);

        Set<String> bans = new HashSet<String>(config.getStringList("lists.bans", null));
        Set<String> mods = new HashSet<String>(config.getStringList("lists.moderators", null));

        int distance = config.getInt("distance", 0);

        // Create the channel
        Channel channel;
        if (distance > 0) {
            channel = new LocalChannel(plugin, name, nick);
            ((LocalChannel) channel).setDistance(distance);
        } else {
            channel = new Channel(plugin, name, nick);
        }

        // Apply the settings we collected earlier
        channel.setPassword(password);
        channel.setFormat(format.isEmpty() ? MSG_FORMAT : format);
        channel.setColor(color);
        channel.setVerbose(verbose);
        channel.setQuick(quick);
        channel.bans = bans;
        channel.moderators = mods;

        // Return the finished channel
        return channel;
    }

}
