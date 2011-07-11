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
import com.herocraftonline.dthielke.herochat.command.BaseCommand;
import com.herocraftonline.dthielke.herochat.util.Messaging;
import com.herocraftonline.dthielke.herochat.util.PermissionManager.Permission;

public class ReloadCommand extends BaseCommand {

    public ReloadCommand(HeroChat plugin) {
        super(plugin);
        setName("Reload");
        setDescription("Reloads the plugin");
        setUsage("§e/ch reload");
        setMinArgs(0);
        setMaxArgs(0);
        getIdentifiers().add("ch reload");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (plugin.getPermissionManager().hasPermission(player, Permission.RELOAD)) {
                plugin.getConfigManager().savePlayers();
                plugin.onEnable();
                Messaging.send(player, "Plugin reloaded.");
            } else {
                Messaging.send(player, "Insufficient permission.");
            }
        } else {
            plugin.getConfigManager().savePlayers();
            plugin.onEnable();
        }
    }

}
