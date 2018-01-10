package me.jaime29010.bungeebans.commands;

import me.jaime29010.bungeebans.Main;
import me.jaime29010.bungeebans.core.entries.IPBanEntry;
import me.jaime29010.bungeebans.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BanIPCommand extends Command {
    private final Main main;
    public BanIPCommand(Main main) {
        super("gbanip", "bungeebans.gbanip", "");
        this.main = main;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 1) {
            String name = args[0];
            if (main.getBridge().isOnline(name)) {
                main.getBridge().banPlayerIPGlobally(name, sender.getName());
            } else sender.sendMessage(new TextComponent(Utils.colorize(main.getConfig().getSection("messages").getString("player_offline"))));
        } else {
            for(String string : main.getConfig().getSection("messages").getStringList("command_usage")) {
                sender.sendMessage(new TextComponent(Utils.colorize(
                        string.replace("{command}", getName())
                )));
            }
        }
    }
}