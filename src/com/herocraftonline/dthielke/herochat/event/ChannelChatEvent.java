package com.herocraftonline.dthielke.herochat.event;

import com.herocraftonline.dthielke.herochat.channels.ChannelOld;

@SuppressWarnings("serial")
public class ChannelChatEvent extends ChannelEvent {

    protected String source;
    protected String message;
    protected String format;
    protected boolean sentByPlayer;

    public ChannelChatEvent(Type type, ChannelOld channel, String source, String message, String format, boolean sentByPlayer) {
        super(ChannelChatEvent.class.getSimpleName(), channel);
        this.source = source;
        this.message = message;
        this.format = format;
        this.sentByPlayer = sentByPlayer;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public boolean isSentByPlayer() {
        return sentByPlayer;
    }

    public void setSentByPlayer(boolean sentByPlayer) {
        this.sentByPlayer = sentByPlayer;
    }

}
