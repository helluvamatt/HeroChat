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

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.config.ConfigurationNode;

import com.herocraftonline.dthielke.herochat.HeroChat;
import com.herocraftonline.dthielke.herochat.chatters.Chatter;
import com.herocraftonline.dthielke.herochat.event.ChannelMessageEvent;
import com.herocraftonline.dthielke.herochat.messages.Message;
import com.herocraftonline.dthielke.herochat.messages.PlayerMessage;
import com.herocraftonline.dthielke.herochat.util.Messaging;

public class LocalChannel extends Channel {

    public static final int DEFAULT_DISTANCE = 100;
    protected int distance = DEFAULT_DISTANCE;

    public LocalChannel(HeroChat plugin, String name, String nick) {
        super(plugin, name, nick);
    }

    public LocalChannel(HeroChat plugin, String name, String nick, String password, String format, ChatColor color, Mode mode, int distance) {
        super(plugin, name, nick, password, format, color, mode);
        this.distance = distance;
    }

    @Override
    public boolean sendMessage(Message message) {
        if (!enabled) {
            return false;
        }

        if (message instanceof PlayerMessage) {
            PlayerMessage pMessage = (PlayerMessage) message;
            Chatter speaker = pMessage.getSender();
            pMessage.setRecipients(getNearbyChatters(speaker));
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

    private Set<Chatter> getNearbyChatters(Chatter speaker) {
        Set<Chatter> nearbyChatters = new HashSet<Chatter>();
        Location sLoc = speaker.getPlayer().getLocation();
        String sWorld = sLoc.getWorld().getName();
        for (Chatter chatter : chatters) {
            if (chatter.equals(speaker)) {
                continue;
            }
            Player player = chatter.getPlayer();
            if (!chatter.isIgnoring(speaker)) {
                Location pLoc = player.getLocation();
                if (sWorld.equals(pLoc.getWorld().getName())) {
                    int dx = sLoc.getBlockX() - pLoc.getBlockX();
                    int dz = sLoc.getBlockZ() - pLoc.getBlockZ();
                    dx = dx * dx;
                    dz = dz * dz;
                    int d = (int) Math.sqrt(dx + dz);

                    if (d <= distance) {
                        nearbyChatters.add(chatter);
                    }
                }
            }
        }
        return nearbyChatters;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public void save(ConfigurationNode config, String path) {
        super.save(config, path);
        path += "." + name;
        config.setProperty(path + ".distance", 0);
    }

}
