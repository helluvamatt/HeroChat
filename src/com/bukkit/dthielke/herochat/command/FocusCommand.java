package com.bukkit.dthielke.herochat.command;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;

import com.bukkit.dthielke.herochat.Channel;
import com.bukkit.dthielke.herochat.HeroChatPlugin;
import com.bukkit.dthielke.herochat.HeroChatPlugin.ChatColor;

public class FocusCommand extends Command {

    public FocusCommand(HeroChatPlugin plugin) {
        super(plugin);

        this.name = "focus";
        this.identifier = "/ch";
    }

    @Override
    public void execute(PlayerChatEvent event, Player sender, String[] args) {
        
        event.setCancelled(true);
        
        if (args[0].isEmpty()) {
            sender.sendMessage(ChatColor.ROSE.format() + "Usage: /ch <channel>");
            return;
        }

        Channel c = plugin.getChannel(args[0]);

        if (c != null) {
            boolean joined = c.addPlayer(sender);
            if (joined)
                sender.sendMessage("HeroChat: Joined channel " + c.getColoredName());

            plugin.setActiveChannel(sender, c);
            sender.sendMessage("HeroChat: Set active channel to " + c.getColoredName());

        } else {
            sender.sendMessage("HeroChat: Channel not found");
        }
    }

}