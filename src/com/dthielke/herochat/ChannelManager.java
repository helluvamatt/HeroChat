package com.dthielke.herochat;

import java.util.ArrayList;
import java.util.List;

public class ChannelManager {

    private List<Channel> channels = new ArrayList<Channel>();

    public boolean addChannel(Channel channel) {
        if (channels.contains(channel))
            return false;
        
        channels.add(channel);
        return true;
    }

    public boolean removeChannel(Channel channel) {
        if (!channels.contains(channel))
            return false;
        
        channels.remove(channel);
        return true;
    }

    public Channel getChannel(String identifier) {
        for (Channel channel : channels)
            if (identifier.equalsIgnoreCase(channel.getName()) || identifier.equalsIgnoreCase(channel.getNick()))
                return channel;
        
        return null;
    }
    
    public List<Channel> getChannels() {
        return channels;
    }

}
