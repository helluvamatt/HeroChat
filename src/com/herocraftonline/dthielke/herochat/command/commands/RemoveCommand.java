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

public class RemoveCommand extends BaseCommand {

    public RemoveCommand(HeroChat plugin) {
        super(plugin);
        setName("Remove");
        setDescription("Removes a channel");
        setUsage("§e/ch remove §9<channel>");
        setMinArgs(1);
        setMaxArgs(1);
        getIdentifiers().add("ch remove");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ChannelManager channelManager = plugin.getChannelManager();
        Channel channel = channelManager.getChannel(args[0]);

        if (channel == null) {
            Messaging.send(sender, "Channel not found.");
            return;
        }

        if (channel.equals(channelManager.getDefaultChannel())) {
            Messaging.send(sender, "You cannot delete the default channel.");
            return;
        }

        if (sender instanceof Player) {
            Player player = (Player) sender;
            PermissionManager permissions = plugin.getPermissionManager();
            Chatter chatter = plugin.getChatterManager().getChatter(player);

            boolean removePerm = permissions.hasPermission(player, Permission.REMOVE);
            boolean adminRemovePerm = permissions.hasPermission(player, Permission.ADMIN_REMOVE);
            boolean mod = channel.isModerator(chatter);

            if ((!removePerm && !adminRemovePerm) || (removePerm && !adminRemovePerm && !mod)) {
                Messaging.send(player, "Insufficient permission.");
                return;
            }
        }

        channelManager.removeChannel(channel);
        plugin.getConfigManager().save();

        Messaging.send(sender, "Removed $1.", channel.getName());
    }

}
