package com.dthielke.herochat;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class HeroChat extends JavaPlugin {

    private static final Logger log = Logger.getLogger("Minecraft");
    
    @Override
    public void onDisable() {
        log.info(getDescription().getName() + " version " + getDescription().getVersion() + " is disabled.");
    }

    @Override
    public void onEnable() {
        log.info(getDescription().getName() + " version " + getDescription().getVersion() + " is enabled.");
    }

}
