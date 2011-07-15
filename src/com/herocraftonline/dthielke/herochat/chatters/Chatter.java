package com.herocraftonline.dthielke.herochat.chatters;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.util.config.ConfigurationNode;

import com.herocraftonline.dthielke.herochat.HeroChat;
import com.herocraftonline.dthielke.herochat.channels.Channel;
import com.herocraftonline.dthielke.herochat.messages.Message;
import com.herocraftonline.dthielke.herochat.messages.PlayerMessage;
import com.herocraftonline.dthielke.herochat.util.Messaging;
import com.herocraftonline.dthielke.herochat.util.PermissionManager;
import com.herocraftonline.dthielke.herochat.util.PermissionManager.ChannelPermission;

public class Chatter {

    private final HeroChat plugin;
    private final String playerName;
    private Channel focus;
    private Set<Channel> channels = new HashSet<Channel>();
    private Set<String> ignores = new HashSet<String>();
    private boolean muted = false;

    public Chatter(HeroChat plugin, Player player) {
        this.plugin = plugin;
        this.playerName = player.getName();
    }

    public void initialize(boolean firstRun) {
        Player player = getPlayer();
        PermissionManager perm = plugin.getPermissionManager();
        if (perm == null) {
            return;
        }
        for (Channel channel : plugin.getChannelManager().getChannels()) {
            if (perm.hasPermission(player, channel, ChannelPermission.AUTOJOIN_ALWAYS)) {
                addToChannel(channel);
            } else if (firstRun && perm.hasPermission(player, channel, ChannelPermission.AUTOJOIN_ONCE)) {
                addToChannel(channel);
            }
        }
        filterChannels();
        for (Channel channel : this.channels) {
            if (perm.hasPermission(player, channel, ChannelPermission.AUTOFOCUS_ALWAYS)) {
                focus = channel;
                break;
            } else if (firstRun && perm.hasPermission(player, channel, ChannelPermission.AUTOFOCUS_ONCE)) {
                focus = channel;
                break;
            }
        }
        Channel defaultChannel = plugin.getChannelManager().getDefaultChannel();
        if (this.channels.isEmpty()) {
            addToChannel(defaultChannel);
        }
        if (focus == null || !focus.hasChatter(this)) {
            focus = defaultChannel;
        }
    }

    public void filterChannels() {
        for (Channel channel : getChannels()) {
            if (!channel.canJoin(this)) {
                removeFromChannel(channel);
            }
        }
    }

    public boolean sendMessage(Message msg, String formattedMsg) {
        if (msg instanceof PlayerMessage) {
            Chatter sender = ((PlayerMessage) msg).getSender();
            if (isIgnoring(sender)) {
                return false;
            }
        }
        getPlayer().sendMessage(formattedMsg);
        return true;
    }

    public Player getPlayer() {
        return plugin.getServer().getPlayer(playerName);
    }

    public boolean isIgnoring(Chatter other) {
        return ignores.contains(other.playerName.toLowerCase());
    }

    public boolean isIgnoring(String player) {
        return ignores.contains(player.toLowerCase());
    }

    public void setIgnore(Chatter other, boolean ignore) {
        setIgnore(other.getPlayer().getName(), ignore);
    }

    public void setIgnore(String player, boolean ignore) {
        if (ignore) {
            ignores.add(player.toLowerCase());
        } else {
            ignores.remove(player.toLowerCase());
        }
    }

    public Set<String> getIgnores() {
        return new HashSet<String>(ignores);
    }

    public void addToChannel(Channel channel) {
        boolean added = channels.add(channel);
        if (added) {
            channel.addChatter(this, false);
        }
    }

    public void removeFromChannel(Channel channel) {
        boolean removed = channels.remove(channel);
        if (removed) {
            channel.removeChatter(this, false);
            if (focus.equals(channel)) {
                Channel defaultChannel = plugin.getChannelManager().getDefaultChannel();
                if (defaultChannel.hasChatter(this)) {
                    focus = defaultChannel;
                } else if (!channels.isEmpty()) {
                    focus = channels.iterator().next();
                } else {
                    focus = null;
                }
            }
        }
    }

    public Set<Channel> getChannels() {
        return new HashSet<Channel>(channels);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((playerName == null) ? 0 : playerName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Chatter other = (Chatter) obj;
        if (playerName == null) {
            if (other.playerName != null)
                return false;
        } else if (!playerName.equals(other.playerName))
            return false;
        return true;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setFocus(Channel focus, boolean notify) {
        this.focus = focus;
        if (notify) {
            Messaging.send(getPlayer(), "Focused $1.", focus.getName());
        }
    }

    public Channel getFocus() {
        return focus;
    }

    public void save(ConfigurationNode config) {
        config.setProperty("player-name", playerName);
        List<String> channelNames = new ArrayList<String>();
        for (Channel channel : channels) {
            channelNames.add(channel.getName());
        }
        config.setProperty("focus", focus.getName());
        config.setProperty("channels", channelNames);
        config.setProperty("ignores", ignores);
        config.setProperty("muted", muted);
    }

    public static Chatter load(HeroChat plugin, ConfigurationNode config, Player player) {
        String focusName = config.getString("focus");
        Set<String> channels = new HashSet<String>(config.getStringList("channels", null));
        Set<String> ignores = new HashSet<String>(config.getStringList("ignores", null));
        boolean muted = config.getBoolean("muted", false);

        Chatter chatter = new Chatter(plugin, player);

        if (focusName == null) {
            chatter.setFocus(plugin.getChannelManager().getDefaultChannel(), false);
        } else {
            Channel focus = plugin.getChannelManager().getChannel(focusName);
            if (focus == null) {
                focus = plugin.getChannelManager().getDefaultChannel();
            }
            chatter.setFocus(focus, false);
        }

        for (String channelName : channels) {
            Channel channel = plugin.getChannelManager().getChannel(channelName);
            if (channel == null) {
                continue;
            }
            chatter.addToChannel(channel);
        }

        chatter.ignores = ignores;
        chatter.setMuted(muted);
        return chatter;
    }

    public String toString() {
        return getPlayer().getDisplayName();
    }

}
