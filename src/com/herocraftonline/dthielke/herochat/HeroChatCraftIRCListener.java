package com.herocraftonline.dthielke.herochat;

import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

import com.ensifera.animosity.craftirc.IRCEvent;
import com.herocraftonline.dthielke.herochat.channels.Channel;

public class HeroChatCraftIRCListener extends CustomEventListener implements Listener {

    private HeroChat plugin;

    public HeroChatCraftIRCListener(HeroChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onCustomEvent(Event event) {
        if (event instanceof IRCEvent) {
            IRCEvent ircEvent = (IRCEvent) event;
            if (!ircEvent.isHandled()) {
                switch (ircEvent.eventMode) {
                case MSG:
                    String msg = ircEvent.msgData.message;
                    String sender = ircEvent.msgData.sender;
                    String channelTag = ircEvent.msgData.srcChannelTag;
                    String ircTag = plugin.getIrcTag();
                    Channel[] channels = plugin.getChannelManager().getChannels().toArray(new Channel[0]);
                    for (Channel c : channels) {
                        if (c.getIrcTags().contains(channelTag)) {
                            c.sendMessage(ircTag + sender, msg, c.getMsgFormat(), false);
                        }
                    }
                    ircEvent.setHandled(true);
                    break;
                case JOIN:
                    msg = ircEvent.msgData.message;
                    sender = ircEvent.msgData.sender;
                    channelTag = ircEvent.msgData.srcChannelTag;
                    ircTag = plugin.getIrcTag();
                    channels = plugin.getChannelManager().getChannels().toArray(new Channel[0]);
                    for (Channel c : channels) {
                        if (c.getIrcTags().contains(channelTag)) {
                            String joinMsg = "§f" + ircTag + sender + c.getColor().str + " has joined the channel";
                            c.sendMessage(ircTag + sender, joinMsg, Channel.joinFormat, false, false);
                        }
                    }
                    ircEvent.setHandled(true);
                    break;
                case BAN:
                case KICK:
                case PART:
                case QUIT:
                    msg = ircEvent.msgData.message;
                    sender = ircEvent.msgData.sender;
                    channelTag = ircEvent.msgData.srcChannelTag;
                    ircTag = plugin.getIrcTag();
                    channels = plugin.getChannelManager().getChannels().toArray(new Channel[0]);
                    for (Channel c : channels) {
                        if (c.getIrcTags().contains(channelTag)) {
                            String leaveMsg = "§f" + ircTag + sender + c.getColor().str + " has left the channel";
                            c.sendMessage(ircTag + sender, leaveMsg, Channel.joinFormat, false, false);
                        }
                    }
                    ircEvent.setHandled(true);
                    break;
                }
            }
        }
    }

}
