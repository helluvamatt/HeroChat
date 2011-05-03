package com.herocraftonline.dthielke.herochat.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

import com.herocraftonline.dthielke.herochat.util.Message;

@SuppressWarnings("serial")
public class HeroChatMessagePreprocessEvent extends Event implements Cancellable {
    
    private final Message message;
    private boolean cancelled = false;
    
    public HeroChatMessagePreprocessEvent(Message message) {
        super("HeroChatMessagePreprocessEvent");
        this.message = message;
    }
    
    public final Message getMessage() {
        return message;
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
