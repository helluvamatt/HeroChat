package com.herocraftonline.dthielke.herochat.chatters;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;

public class ChatterManager {

    private Set<Chatter> chatters = new HashSet<Chatter>();
    
    public void addChatter(Chatter chatter) {
        chatters.add(chatter);
    }

    public void removeChatter(Chatter chatter) {
        chatters.remove(chatter);
    }

    public Chatter getChatter(String name) {
        for (Chatter chatter : chatters) {
            if (chatter.getPlayer().getName().equalsIgnoreCase(name)) {
                return chatter;
            }
        }
        return null;
    }
    
    public Chatter getChatter(Player player) {
        return getChatter(player.getName());
    }
    
    public final Chatter[] getChatters() {
        return chatters.toArray(new Chatter[0]);
    }

}
