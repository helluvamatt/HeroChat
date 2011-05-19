package com.herocraftonline.dthielke.herochat.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

import com.herocraftonline.dthielke.herochat.messages.Message;

@SuppressWarnings("serial")
public class ChannelMessageEvent extends Event implements Cancellable {

    private final Message data;
    private boolean cancelled = false;

    public ChannelMessageEvent(Message data) {
        super("ChannelMessageEvent");
        this.data = data;
    }

    public final Message getData() {
        return data;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

}
