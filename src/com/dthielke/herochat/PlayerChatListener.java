package com.dthielke.herochat;

import java.util.Iterator;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;

public class PlayerChatListener extends PlayerListener {

    @Override
    public void onPlayerChat(PlayerChatEvent event) {
        if (event.isCancelled())
            return;

        ChatterManager cm = HeroChat.getChatterManager();

        Chatter sender = cm.getChatter(event.getPlayer());
        Channel channel = sender.getActiveChannel();
        Set<Player> recipients = event.getRecipients();

        for (Iterator<Player> iter = recipients.iterator(); iter.hasNext();)
            if (!channel.isMember(cm.getChatter(iter.next())))
                iter.remove();
    }

}
