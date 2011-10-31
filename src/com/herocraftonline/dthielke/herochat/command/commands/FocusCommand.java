/**
 * Copyright (C) 2011 DThielke <dave.thielke@gmail.com>
 * 
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send a letter to
 * Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 **/

package com.herocraftonline.dthielke.herochat.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.herocraftonline.dthielke.herochat.HeroChat;
import com.herocraftonline.dthielke.herochat.channels.Channel;
import com.herocraftonline.dthielke.herochat.channels.ChannelManager;
import com.herocraftonline.dthielke.herochat.channels.ConversationManager;
import com.herocraftonline.dthielke.herochat.command.BaseCommand;

public class FocusCommand extends BaseCommand {

    public FocusCommand(HeroChat plugin) {
        super(plugin);
        name = "Focus";
        description = "Directs all future messages to a channel";
        usage = "§e/ch §9<channel> §8[pass]";
        minArgs = 1;
        maxArgs = 2;
        identifiers.add("ch");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String name = player.getName();
            ChannelManager cm = plugin.getChannelManager();
            Channel c = cm.getChannel(args[0]);
            if (c != null) {
                if (!c.getBlacklist().contains(name)) {
                    if (!c.getWhitelist().isEmpty()) {
                        if (!plugin.getPermissionManager().hasAny(player, c.getWhitelist())) {
                            sender.sendMessage(plugin.getTag() + "§cYou cannot join this channel");
                            return;
                        }
                    }
                    if (!c.getPlayers().contains(name)) {
                        String password = c.getPassword();
                        if (password.isEmpty() || args.length == 2 && args[1].equals(password) || plugin.getPermissionManager().isAdmin(player)) {
                            c.addPlayer(name);
                            sender.sendMessage(plugin.getTag() + "§cJoined channel " + c.getCName());
                        } else {
                            sender.sendMessage(plugin.getTag() + "§cWrong password for " + c.getCName());
                            return;
                        }
                    }
                    ConversationManager convos = plugin.getConversationManager();
                    if (convos.hasActive(player)) {
                        convos.end(player);
                        sender.sendMessage(plugin.getTag() + "§cEnded your conversation");
                    }
                    cm.setActiveChannel(name, c.getName());
                    sender.sendMessage(plugin.getTag() + "§cSet focus on " + c.getCName());
                } else {
                    sender.sendMessage(plugin.getTag() + "§cYou are banned from " + c.getCName());
                }
            } else {
                sender.sendMessage(plugin.getTag() + "§cChannel not found");
            }
        } else {
            sender.sendMessage(plugin.getTag() + "§cYou must be a player to use this command");
        }
    }

}
