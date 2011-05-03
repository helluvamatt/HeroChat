/**
 * Copyright (C) 2011 DThielke <dave.thielke@gmail.com>
 * 
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send a letter to
 * Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 **/

package com.herocraftonline.dthielke.herochat.channels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

import com.herocraftonline.dthielke.herochat.HeroChat;

public class ChannelManager {

    private HeroChat plugin;
    private List<ChannelOld> channels;
    private ChannelOld defaultChannel;
    private String defaultMsgFormat;
    private HashMap<String, String> activeChannels;
    private HashMap<String, List<String>> ignoreLists;
    private List<String> mutelist;

    public ChannelManager(HeroChat plugin) {
        this.plugin = plugin;
        activeChannels = new HashMap<String, String>();
        ignoreLists = new HashMap<String, List<String>>();
        mutelist = new ArrayList<String>();
    }

    public String[] getPlayerList() {
        return activeChannels.keySet().toArray(new String[0]);
    }

    public void removeFromAll(String name) {
        for (ChannelOld c : channels) {
            c.removePlayer(name);
        }
    }

    public void joinAutoChannels(String name) {
        Player player = plugin.getServer().getPlayer(name);
        if (player != null) {
            String group = plugin.getPermissionManager().getGroup(player);
            for (ChannelOld c : channels) {
                if (c.isAutoJoined()) {
                    if (c.getWhitelist().isEmpty() || c.getWhitelist().contains(group)) {
                        c.addPlayer(name);
                    }
                }
            }
        }
    }

    public List<ChannelOld> getJoinedChannels(String name) {
        List<ChannelOld> list = new ArrayList<ChannelOld>();
        for (ChannelOld c : channels) {
            if (c.getPlayers().contains(name)) {
                list.add(c);
            }
        }
        if (list.isEmpty()) {
            defaultChannel.addPlayer(name);
            list.add(defaultChannel);
        }
        return list;
    }

    public ChannelOld getActiveChannel(String name) {
        String active = activeChannels.get(name);
        if (active == null) {
            activeChannels.put(name, defaultChannel.getName());
            return defaultChannel;
        }
        return getChannel(active);
    }

    public void setActiveChannel(String player, String channel) {
        if (channel != null) {
            activeChannels.put(player, channel);
        } else {
            activeChannels.remove(player);
        }
    }

    public boolean isIgnoring(String ignorer, String ignoree) {
        List<String> ignoreList = ignoreLists.get(ignorer);
        if (ignoreList != null) {
            return ignoreList.contains(ignoree);
        }
        return false;
    }

    public void addIgnore(String ignorer, String ignoree) {
        List<String> ignoreList = ignoreLists.get(ignorer);
        if (ignoreList == null) {
            ignoreList = new ArrayList<String>();
        }
        ignoreList.add(ignoree);
        ignoreLists.put(ignorer, ignoreList);
    }

    public void removeIgnore(String ignorer, String ignoree) {
        List<String> ignoreList = ignoreLists.get(ignorer);
        if (ignoreList != null) {
            ignoreList.remove(ignoree);
        }
    }

    public List<String> getIgnoreList(String name) {
        List<String> ignoreList = ignoreLists.get(name);
        if (ignoreList == null) {
            ignoreList = new ArrayList<String>();
        }
        return ignoreList;
    }

    public void setIgnoreList(String name, List<String> ignoreList) {
        ignoreLists.put(name, ignoreList);
    }

    public ChannelOld getChannel(String name) {
        for (ChannelOld c : channels) {
            if (c.getName().equalsIgnoreCase(name) || c.getNick().equalsIgnoreCase(name)) {
                return c;
            }
        }
        return null;
    }

    public void addChannel(ChannelOld c) {
        channels.add(c);
    }

    public void removeChannel(ChannelOld c) {
        channels.remove(c);
    }

    public List<ChannelOld> getChannels() {
        return channels;
    }

    public void setChannels(List<ChannelOld> channels) {
        this.channels = channels;
    }

    public void setDefaultChannel(ChannelOld defaultChannel) {
        this.defaultChannel = defaultChannel;
    }

    public ChannelOld getDefaultChannel() {
        return defaultChannel;
    }

    public void setDefaultMsgFormat(String defaultMsgFormat) {
        this.defaultMsgFormat = defaultMsgFormat;
    }

    public String getDefaultMsgFormat() {
        return defaultMsgFormat;
    }
    public void setMutelist(List<String> mutelist) {
        this.mutelist = mutelist;
    }

    public List<String> getMutelist() {
        return mutelist;
    }

}
