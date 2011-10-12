/**
 * Copyright (C) 2011 DThielke <dave.thielke@gmail.com>
 * 
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ or send a letter to
 * Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 **/

package com.dthielke.herochat.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.dthielke.herochat.util.Messaging;

public class CommandHandler {

    private LinkedHashMap<String, Command> commands = new LinkedHashMap<String, Command>();
    private HashMap<String, Command> identifiers = new HashMap<String, Command>();

    public void addCommand(Command command) {
        commands.put(command.getName().toLowerCase(), command);
        for (String ident : command.getIdentifiers()) {
            identifiers.put(ident.toLowerCase(), command);
        }
    }

    public boolean dispatch(CommandSender sender, String label, String[] args) {
        for (int argsIncluded = args.length; argsIncluded >= 0; argsIncluded--) {
            String identifier = label;
            for (int i = 0; i < argsIncluded; i++) {
                identifier += " " + args[i];
            }

            Command cmd = getCmdFromIdent(identifier, sender);
            if (cmd == null) {
                continue;
            }

            String[] realArgs = Arrays.copyOfRange(args, argsIncluded, args.length);

            if (!cmd.isInProgress(sender)) {
                if (realArgs.length < cmd.getMinArguments() || realArgs.length > cmd.getMaxArguments()) {
                    displayCommandHelp(cmd, sender);
                    return true;
                } else if (realArgs.length > 0 && realArgs[0].equals("?")) {
                    displayCommandHelp(cmd, sender);
                    return true;
                }
            }

            if (!hasPermission(sender, cmd.getPermission())) {
                Messaging.send(sender, "Insufficient permission.");
                return true;
            }

            cmd.execute(sender, identifier, realArgs);
            return true;
        }

        return true;
    }

    public Command getCmdFromIdent(String ident, CommandSender executor) {
        ident = ident.toLowerCase();
        if (identifiers.containsKey(ident))
            return identifiers.get(ident);
        
        for (Command cmd : commands.values()) {
            if (cmd.isIdentifier(executor, ident))
                return cmd;
        }
        
        return null;
    }

    public Command getCommand(String name) {
        return commands.get(name.toLowerCase());
    }

    public List<Command> getCommands() {
        return new ArrayList<Command>(commands.values());
    }

    public void removeCommand(Command command) {
        commands.remove(command);
        for (String ident : command.getIdentifiers()) {
            identifiers.remove(ident.toLowerCase());
        }
    }

    private void displayCommandHelp(Command cmd, CommandSender sender) {
        sender.sendMessage("§cCommand:§e " + cmd.getName());
        sender.sendMessage("§cDescription:§e " + cmd.getDescription());
        sender.sendMessage("§cUsage:§e " + cmd.getUsage());
        if (cmd.getNotes() != null) {
            for (String note : cmd.getNotes()) {
                sender.sendMessage("§e" + note);
            }
        }
    }

    public static boolean hasPermission(CommandSender sender, String permission) {
        if (sender.isOp())
            return true;
        
        if (!(sender instanceof Player) || permission == null || permission.isEmpty())
            return true;

        Player player = (Player) sender;
        return player.hasPermission(permission);
    }
}
