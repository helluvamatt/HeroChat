package com.herocraftonline.dthielke.herochat;

import java.util.logging.Level;

import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

import com.herocraftonline.dthielke.herochat.event.ChannelChatEvent;

public class ExampleChatChannelListener extends CustomEventListener implements Listener {

    protected HeroChat plugin;

    public ExampleChatChannelListener(HeroChat plugin) {
        this.plugin = plugin;
    }

    public void onCustomEvent(Event e) {
        if (e instanceof ChannelChatEvent) {
            ChannelChatEvent event = (ChannelChatEvent) e;
            plugin.log(Level.WARNING, event.getSource());
            String msg = event.getMessage();
            if (!((msg.contains("has joined the channel") || msg.contains("has left the channel")) && !event.isSentByPlayer())) {
                event.setMessage("HAXIN UR MSGS!");
                event.setSource("HAXOR");
                event.setSentByPlayer(false);
            }
        }
    }

}
