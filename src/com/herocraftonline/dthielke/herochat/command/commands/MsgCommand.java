package com.herocraftonline.dthielke.herochat.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.herocraftonline.dthielke.herochat.HeroChat;
import com.herocraftonline.dthielke.herochat.channels.Channel;
import com.herocraftonline.dthielke.herochat.channels.PrivateMessageChannel;
import com.herocraftonline.dthielke.herochat.chatters.Chatter;
import com.herocraftonline.dthielke.herochat.chatters.ChatterManager;
import com.herocraftonline.dthielke.herochat.command.BaseCommand;
import com.herocraftonline.dthielke.herochat.util.Messaging;

public class MsgCommand extends BaseCommand {

    public MsgCommand(HeroChat plugin) {
        super(plugin);
        setName("Private Message");
        setDescription("Sends a private message to a player");
        setUsage("§e/msg §9<player> §8[message]");
        setMinArgs(1);
        setMaxArgs(1000);
        getIdentifiers().add("msg");
        getIdentifiers().add("tell");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            Messaging.send(sender, "Player not found.");
            return;
        }

        if (args.length > 1) {
            String senderName;
            if (sender instanceof Player) {
                senderName = ((Player) sender).getDisplayName();
            } else {
                senderName = "CONSOLE";
            }
            String targetName = target.getDisplayName();
            String msg = "";
            for (int i = 1; i < args.length; i++) {
                msg += " " + args[i];
            }
            sender.sendMessage("To " + targetName + ":" + msg);
            target.sendMessage("From " + senderName + ":" + msg);
            return;
        }

        if (!(sender instanceof Player)) {
            return;
        }

        ChatterManager chatterManager = plugin.getChatterManager();
        Chatter playerChatter = chatterManager.getChatter((Player) sender);
        Chatter targetChatter = chatterManager.getChatter(target);

        Channel convo;
        Channel playerFocus = playerChatter.getFocus();
        Channel targetFocus = targetChatter.getFocus();
        if (playerFocus instanceof PrivateMessageChannel && playerFocus.hasChatter(targetChatter)) {
            convo = playerFocus;
        } else if (targetFocus instanceof PrivateMessageChannel && targetFocus.hasChatter(playerChatter)) {
            convo = targetFocus;
        } else {
            convo = new PrivateMessageChannel(plugin);
            plugin.getChannelManager().addChannel(convo);
        }

        convo.addChatter(playerChatter, false);
        convo.addChatter(targetChatter, false);
        playerChatter.setFocus(convo, false);

        Messaging.send(sender, "Joined a conversation with $1.", target.getDisplayName());
    }

}
