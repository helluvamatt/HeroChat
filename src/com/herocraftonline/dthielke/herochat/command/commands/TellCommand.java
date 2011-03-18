package com.herocraftonline.dthielke.herochat.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.herocraftonline.dthielke.herochat.ConversationManager;
import com.herocraftonline.dthielke.herochat.HeroChat;
import com.herocraftonline.dthielke.herochat.command.BaseCommand;

public class TellCommand extends BaseCommand {

    public TellCommand(HeroChat plugin) {
        super(plugin);
        name = "Tell";
        description = "Starts or ends a private conversation";
        usage = "§e/ch tell §9<player> §eOR /tell §9<player>";
        minArgs = 0;
        maxArgs = 1;
        identifiers.add("tell");
        identifiers.add("ch tell");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player teller = (Player) sender;
            ConversationManager cm = plugin.getConversationManager();
            if (args.length == 1) {
                Player tellee = plugin.getServer().getPlayer(args[0]);
                if (tellee != null) {
                    if (tellee != teller) {
                        cm.start(teller, tellee);
                        teller.sendMessage(plugin.getTag() + "§cStarted conversation with " + tellee.getName());
                    }
                } else {
                    teller.sendMessage(plugin.getTag() + "§cPlayer not found");
                }
            } else {
                if (cm.hasActive(teller)) {
                    cm.end(teller);
                    teller.sendMessage(plugin.getTag() + "§cEnded your conversation");
                }
            }
        }
    }

}
