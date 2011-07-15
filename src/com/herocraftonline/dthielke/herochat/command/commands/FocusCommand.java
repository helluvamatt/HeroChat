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
import com.herocraftonline.dthielke.herochat.chatters.Chatter;
import com.herocraftonline.dthielke.herochat.command.BaseCommand;
import com.herocraftonline.dthielke.herochat.util.Messaging;

public class FocusCommand extends BaseCommand {

    public FocusCommand(HeroChat plugin) {
        super(plugin);
        setName("Focus");
        setDescription("Directs all future messages to a channel");
        setUsage("§e/ch §9<channel> §8[pass]");
        setMinArgs(1);
        setMaxArgs(2);
        getIdentifiers().add("ch");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            ChannelManager channelManager = plugin.getChannelManager();
            Channel channel = channelManager.getChannel(args[0]);
            Player player = (Player) sender;
            Chatter chatter = plugin.getChatterManager().getChatter(player);

            if (channel == null) {
                Messaging.send(sender, "Channel not found.");
                return;
            }

            if (!channel.hasChatter(chatter)) {
                if (!channel.canJoin(chatter)) {
                    Messaging.send(sender, "You can't focus this channel.");
                    return;
                }

                String password = channel.getPassword();
                if (!password.isEmpty()) {
                    if (args.length < 2 || !args[1].equals(password)) {
                        Messaging.send(sender, "Wrong password.");
                        return;
                    }
                }

                if (!channel.addChatter(chatter, true)) {
                    return;
                }
            }

            chatter.setFocus(channel, true);
        }
    }
}
