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
import com.herocraftonline.dthielke.herochat.chatters.Chatter;
import com.herocraftonline.dthielke.herochat.command.BaseCommand;
import com.herocraftonline.dthielke.herochat.util.Messaging;

public class QuickMsgCommand extends BaseCommand {

    public QuickMsgCommand(HeroChat plugin) {
        super(plugin);
        name = "Quick Message";
        description = "Sends a message without changing focus";
        usage = "§e/qm §9<channel> §9<msg> §eOR /§9<channel> §9<msg>";
        minArgs = 2;
        maxArgs = 1000;
        identifiers.add("qm");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Chatter chatter = plugin.getChatterManager().getChatter(player);
            Channel channel = plugin.getChannelManager().getChannel(args[0]);

            if (channel == null) {
                Messaging.send(player, "Channel not found.");
                return;
            }

            if (!channel.hasChatter(chatter)) {
                Messaging.send(player, "You are not in $1.", channel.getName());
                return;
            }

            String msg = "";
            for (int i = 1; i < args.length; i++) {
                msg += args[i] + " ";
            }
            msg = msg.trim();

            channel.sendPlayerMessage(chatter, msg);
        }
    }

}
