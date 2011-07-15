package com.herocraftonline.dthielke.herochat.messages;

import java.util.Set;

import com.herocraftonline.dthielke.herochat.channels.Channel;
import com.herocraftonline.dthielke.herochat.chatters.Chatter;

public class Message {
    private final Channel channel;
    private Set<Chatter> recipients;
    private String message;
    private String format;

    public Message(String message, String format, Channel channel, Set<Chatter> recipients) {
        this.message = message;
        this.format = format;
        this.channel = channel;
        this.recipients = recipients;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public void setFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    public Channel getChannel() {
        return channel;
    }

    public Set<Chatter> getRecipients() {
        return recipients;
    }

    public void setRecipients(Set<Chatter> recipients) {
        this.recipients = recipients;
    }
}
