/**
 * Copyright (C) 2011 DThielke <dave.thielke@gmail.com>
 * 
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send a letter to
 * Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 **/

package com.herocraftonline.dthielke.herochat;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.herocraftonline.dthielke.herochat.channels.Channel;
import com.herocraftonline.dthielke.herochat.chatters.Chatter;

public class HeroChatPlayerListener extends PlayerListener {

    private final HeroChat plugin;

    public HeroChatPlayerListener(HeroChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) {
            return;
        }

        String input = event.getMessage().substring(1);
        String[] args = input.split(" ");
        Channel channel = plugin.getChannelManager().getChannel(args[0]);
        if (channel != null && channel.isQuick()) {
            event.setCancelled(true);
            plugin.getCommandManager().dispatch(event.getPlayer(), null, "qm", args);
        }
    }

    @Override
    public void onPlayerChat(PlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }
        event.setCancelled(true);
        Player player = event.getPlayer();
        Chatter chatter = plugin.getChatterManager().getChatter(player);
        Channel channel = chatter.getFocus();
        if (channel == null) {
            return;
        }
        channel.sendPlayerMessage(chatter, event.getMessage(), channel.getFormat());
    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getConfigManager().loadPlayer(player);
    }

    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.getConfigManager().savePlayer(player);
    }

}
