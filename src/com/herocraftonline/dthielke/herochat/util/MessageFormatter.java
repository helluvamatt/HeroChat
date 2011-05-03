package com.herocraftonline.dthielke.herochat.util;

public final class MessageFormatter {
    private static final String[] HEALTH_COLORS = { "§0", "§4", "§6", "§e", "§2" };
    
    public static String stripColors(String input) {
        return input.replaceAll("[§&][0-9a-f]", "");
    }
    
    public static String format(Message msg) {
        String formattedMsg = msg.format;
        
        // replace tags with data
        formattedMsg = formattedMsg.replace("{prefix}", msg.prefix);
        formattedMsg = formattedMsg.replace("{suffix}", msg.suffix);
        formattedMsg = formattedMsg.replace("{group-prefix}", msg.groupPrefix);
        formattedMsg = formattedMsg.replace("{group-suffix}", msg.groupSuffix);
        formattedMsg = formattedMsg.replace("{world}", msg.prefix);
        formattedMsg = formattedMsg.replace("{channel-name}", msg.channelName);
        formattedMsg = formattedMsg.replace("{channel-nick}", msg.channelNick);
        formattedMsg = formattedMsg.replace("{channel-color}", msg.channelColor);
        formattedMsg = formattedMsg.replace("{sender}", msg.sender);
        formattedMsg = formattedMsg.replace("{receiver}", msg.receiver);
        formattedMsg = formattedMsg.replace("{health}", msg.health);
        formattedMsg = formattedMsg.replace("{message}", msg.message);
        
        // convert ampersand color codes
        formattedMsg = formattedMsg.replaceAll("&([0-9a-f])", "§$1");
        
        return formattedMsg;
    }
    
    public static String createHealthBar(int health) {
        // make sure 0 <= health <= 20
        if (health < 0) {
            health = 0;
        }
        if (health > 20) {
            health = 20;
        }
        int fullBars = health / 4;
        int remainder = health % 4;
        String healthBar = "";
        
        // append full health bars
        for (int i = 0; i < fullBars; i++) {
            healthBar += HEALTH_COLORS[4] + "|";
        }
        
        // add remainder health bar with appropriate color
        int barsLeft = 5 - fullBars;
        if (barsLeft > 0) {
            healthBar += HEALTH_COLORS[remainder] + "|";
            barsLeft--;
            for (int i = 0; i < barsLeft; i++) {
                healthBar += HEALTH_COLORS[0] + "|";
            }
        }
        return healthBar;
    }
    
}
