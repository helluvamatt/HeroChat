/**
 * Copyright (C) 2011 DThielke <dave.thielke@gmail.com>
 * 
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send a letter to
 * Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 **/

package com.herocraftonline.dthielke.herochat.command.commands;

import java.util.Set;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.herocraftonline.dthielke.herochat.HeroChat;
import com.herocraftonline.dthielke.herochat.chatters.Chatter;
import com.herocraftonline.dthielke.herochat.command.BaseCommand;
import com.herocraftonline.dthielke.herochat.util.Messaging;

public class IgnoreCommand extends BaseCommand {

    public IgnoreCommand(HeroChat plugin) {
        super(plugin);
        setName("Ignore");
        setDescription("Ignores all messages from a player");
        setUsage("§e/ch ignore §8[player]");
        setMinArgs(0);
        setMaxArgs(1);
        getIdentifiers().add("ch ignore");
        getIdentifiers().add("ignore");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Chatter chatter = plugin.getChatterManager().getChatter(player);

            if (args.length == 0) {
                displayIgnoreList(chatter);
                return;
            }

            if (chatter.isIgnoring(args[1])) {
                chatter.setIgnore(args[1], false);
                Messaging.send(sender, "No longer ignoring $1.", args[1].toLowerCase());
            } else {
                chatter.setIgnore(args[1], true);
                Messaging.send(sender, "Now ignoring $1.", args[1].toLowerCase());
            }
        }
    }

    private void displayIgnoreList(Chatter chatter) {
        Set<String> ignoreList = chatter.getIgnores();
        if (ignoreList.isEmpty()) {
            Messaging.send(chatter.getPlayer(), "Not ignoring anyone.");
        } else {
            String ignoreListMsg = "Ignoring: ";
            for (String s : ignoreList) {
                ignoreListMsg += s + ",";
            }
            ignoreListMsg = ignoreListMsg.substring(0, ignoreListMsg.length() - 1);
            Messaging.send(chatter.getPlayer(), ignoreListMsg);
        }
    }

}
