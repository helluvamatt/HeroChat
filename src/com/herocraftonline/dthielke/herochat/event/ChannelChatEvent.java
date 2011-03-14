package com.herocraftonline.dthielke.herochat.event;

import com.herocraftonline.dthielke.herochat.channels.Channel;

@SuppressWarnings("serial")
public class ChannelChatEvent extends ChannelEvent {

    protected String source;
    protected String message;
    protected String format;
    protected boolean sentByPlayer;
    
    public ChannelChatEvent(Type type, Channel channel) {
        super(type, channel);
        source = "";
        message = "";
        format = "";
        sentByPlayer = false;
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
