/**
 * Copyright (C) 2011 DThielke <dave.thielke@gmail.com>
 * 
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send a letter to
 * Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 **/

package com.herocraftonline.dthielke.herochat.channels;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;

import com.herocraftonline.dthielke.herochat.HeroChat;
import com.herocraftonline.dthielke.herochat.chatters.Chatter;
import com.herocraftonline.dthielke.herochat.util.PermissionManager;
import com.herocraftonline.dthielke.herochat.util.PermissionManager.ChannelPermission;

public class ChannelManager {

    private final HeroChat plugin;
    private Set<Channel> channels = new HashSet<Channel>();
    private Channel defaultChannel;

    public ChannelManager(HeroChat plugin) {
        this.plugin = plugin;
    }

    public boolean addChannel(Channel channel) {
        return channels.add(channel);
    }

    public boolean removeChannel(Channel channel) {
        if (channel.equals(defaultChannel)) {
            return false;
        }
        for (Chatter chatter : channel.getChatters()) {
            channel.removeChatter(chatter, true);
            if (chatter.getFocus().equals(channel)) {
                if (defaultChannel.hasChatter(chatter)) {
                    chatter.setFocus(defaultChannel, true);
                } else {
                    chatter.setFocus(chatter.getChannels()[0], true);
                }
            }
        }
        return channels.remove(channel);
    }

    public Channel getChannel(String name) {
        for (Channel channel : channels) {
            if (channel.getName().equalsIgnoreCase(name) || channel.getNick().equalsIgnoreCase(name)) {
                return channel;
            }
        }
        return null;
    }

    public void autoPopulateChannels(Chatter chatter, boolean firstJoin) {
        Player player = chatter.getPlayer();
        PermissionManager perm = plugin.getPermissionManager();
        for (Channel channel : channels) {
            if (perm.hasPermission(player, channel, ChannelPermission.AUTOJOIN_ALWAYS)) {
                channel.addChatter(chatter, false);
            } else if (firstJoin && perm.hasPermission(player, channel, ChannelPermission.AUTOFOCUS_ONCE)) {
                channel.addChatter(chatter, false);
            }
        }
    }
    
    public void disableChannels() {
        for (Channel channel : channels) {
            channel.setEnabled(false);
        }
    }
    
    public void enableChannels() {
        for (Channel channel : channels) {
            channel.setEnabled(true);
        }
    }

    public void setDefaultChannel(Channel defaultChannel) {
        this.defaultChannel = defaultChannel;
    }

    public Channel getDefaultChannel() {
        return defaultChannel;
    }
    
    public final Channel[] getChannels() {
        return channels.toArray(new Channel[0]);
    }

}
