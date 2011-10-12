package com.dthielke.herochat;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class ChatterManager {

    private List<Chatter> chatters = new ArrayList<Chatter>();

    public boolean addChatter(Chatter chatter) {
        if (chatters.contains(chatter))
            return false;
        
        chatters.add(chatter);
        return true;
    }

    public boolean removeChatter(Chatter chatter) {
        if (!chatters.contains(chatter))
            return false;
        
        chatters.remove(chatter);
        return true;
    }
    
    public Chatter getChatter(Player player) {
        for (Chatter chatter : chatters)
            if (player.equals(chatter.getPlayer()))
                return chatter;
        return null;
    }

    public Chatter getChatter(String identifier) {
        for (Chatter chatter : chatters)
            if (identifier.equalsIgnoreCase(chatter.getName()))
                return chatter;
        
        return null;
    }
    
    public List<Chatter> getChatters() {
        return chatters;
    }
    
}
