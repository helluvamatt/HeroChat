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
import com.herocraftonline.dthielke.herochat.util.PermissionManager;
import com.herocraftonline.dthielke.herochat.util.PermissionManager.Permission;

public class ModCommand extends BaseCommand {

    public ModCommand(HeroChat plugin) {
        super(plugin);
        name = "Mod";
        description = "Grants moderator privileges to a player";
        usage = "§e/ch mod §9<channel> <player>";
        minArgs = 2;
        maxArgs = 2;
        identifiers.add("ch mod");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        PermissionManager permissions = plugin.getPermissionManager();
        ChannelManager channelManager = plugin.getChannelManager();
        Channel channel = channelManager.getChannel(args[0]);

        if (channel == null) {
            Messaging.send(sender, "Channel not found.");
            return;
        }

        Player target = plugin.getServer().getPlayer(args[1]);
        if (target == null) {
            Messaging.send(sender, "Player not found.");
            return;
        }

        Chatter targetChatter = plugin.getChatterManager().getChatter(target);
        boolean targetMod = channel.isModerator(targetChatter);

        if (sender instanceof Player) {
            Player player = (Player) sender;
            Chatter playerChatter = plugin.getChatterManager().getChatter(player);

            boolean adminModPerm = permissions.hasPermission(player, Permission.ADMIN_MOD);
            boolean playerMod = channel.isModerator(playerChatter);

            if (!playerMod && !adminModPerm) {
                Messaging.send(player, "Insufficient permission.");
                return;
            }
            
            if (targetMod) {
                if (!adminModPerm) {
                    Messaging.send(player, "$1 is already moderating $2.", target.getName(), channel.getName());
                    return;
                }
            }
        }
        
        if (targetMod) {
            channel.removeModerator(targetChatter, true);
            Messaging.send(sender, "$1 is no longer moderating $2.", target.getName(), channel.getName());
        } else {
            channel.addModerator(targetChatter, true);
            Messaging.send(sender, "$1 is now moderating $2.", target.getName(), channel.getName());
        }
    }

}
