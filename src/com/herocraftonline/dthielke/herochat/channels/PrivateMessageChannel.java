package com.herocraftonline.dthielke.herochat.channels;

import java.util.logging.Level;

import com.herocraftonline.dthielke.herochat.HeroChat;
import com.herocraftonline.dthielke.herochat.chatters.Chatter;
import com.herocraftonline.dthielke.herochat.event.ChannelMessageEvent;
import com.herocraftonline.dthielke.herochat.messages.Message;
import com.herocraftonline.dthielke.herochat.messages.PlayerMessage;
import com.herocraftonline.dthielke.herochat.util.Messaging;

public class PrivateMessageChannel extends Channel {

    private static final String ID_FORMAT = "TRANSIENT_%d";
    private static int ID = 0;

    public PrivateMessageChannel(HeroChat plugin) {
        super(plugin, String.format(ID_FORMAT, ID), String.format(ID_FORMAT, ID));
        setHidden(true);
        ID++;
    }

    @Override
    public boolean canJoin(Chatter chatter) {
        System.out.println("cant join");
        return false;
    }

    @Override
    public boolean canLeave(Chatter chatter) {
        System.out.println("cant leave");
        return false;
    }

    public boolean sendMessage(Message message) {
        if (!enabled) {
            return false;
        }

        // fire a message event
        ChannelMessageEvent event = new ChannelMessageEvent(message);
        plugin.getServer().getPluginManager().callEvent(event);

        // check if the event was cancelled
        if (event.isCancelled()) {
            return false;
        }

        // format the message
        message = event.getData();

        if (message instanceof PlayerMessage) {
            PlayerMessage playerMessage = (PlayerMessage) message;
            Chatter sender = playerMessage.getSender();

            for (Chatter chatter : message.getRecipients()) {
                if (sender.equals(chatter)) {
                    String receiver = "";
                    for (Chatter tmp : message.getRecipients()) {
                        if (!sender.equals(tmp)) {
                            receiver = tmp.getPlayer().getDisplayName();
                            break;
                        }
                    }
                    message.setFormat("To " + receiver + ": {message}");
                } else {
                    message.setFormat("From {sender}: {message}");
                }
                String formatted = Messaging.format(message);
                chatter.sendMessage(message, formatted);
            }
        } else {
            String formatted = Messaging.format(message);
            for (Chatter chatter : message.getRecipients()) {
                chatter.sendMessage(message, formatted);
            }
        }

        return true;
    }

    public void update() {
        for (Chatter chatter : getChatters()) {
            if (chatter.getFocus().equals(this)) {
                return;
            }
        }

        for (Chatter chatter : getChatters()) {
            removeChatter(chatter, false);
        }

        plugin.getChannelManager().removeChannel(this);
        plugin.log(Level.INFO, "Cleaned transient channel.");
    }

}
