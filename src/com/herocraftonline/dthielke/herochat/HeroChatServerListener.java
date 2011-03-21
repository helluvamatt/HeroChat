package com.herocraftonline.dthielke.herochat;

import org.bukkit.event.server.PluginEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.Plugin;

public class HeroChatServerListener extends ServerListener {

    private HeroChat plugin;
    
    public HeroChatServerListener(HeroChat plugin) {
        this.plugin = plugin;
    }
    
    public void onPluginEnabled(PluginEvent event) {
        Plugin plugin = event.getPlugin();
        String name = plugin.getDescription().getName();
        
        if (name.equals("Permissions")) {
            this.plugin.loadPermissions();
        } else if (name.equals("CraftIRC")) {
            this.plugin.loadCraftIRC();
        }
    }

}
