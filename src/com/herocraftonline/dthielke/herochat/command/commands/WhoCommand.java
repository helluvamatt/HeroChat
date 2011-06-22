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

public class WhoCommand extends BaseCommand {

    public WhoCommand(HeroChat plugin) {
        super(plugin);
        name = "Who";
        description = "Lists all users in your active channel";
        usage = "§e/ch who";
        minArgs = 0;
        maxArgs = 0;
        identifiers.add("ch who");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Chatter chatter = plugin.getChatterManager().getChatter(player);
            
            Channel focus = chatter.getFocus();
            Chatter[] chatters = focus.getChatters();
            String playerList = focus.getName() + ": ";
            
            for (Chatter member : chatters) {
                String name = member.getPlayer().getName();
                if (focus.isModerator(member)) {
                    name += "*";
                }
                playerList += name + ", ";
            }
            playerList = playerList.substring(0, playerList.length() - 2);
            Messaging.send(player, playerList);
        }
    }

}
