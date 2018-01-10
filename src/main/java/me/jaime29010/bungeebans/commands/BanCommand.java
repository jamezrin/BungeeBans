package me.jaime29010.bungeebans.commands;

import me.jaime29010.bungeebans.Main;
import me.jaime29010.bungeebans.core.entries.BanEntry;
import me.jaime29010.bungeebans.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BanCommand extends Command {
    private final Main main;
    public BanCommand(Main main) {
        super("gban", "bungeebans.gban", "");
        this.main = main;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 1) {
            String name = args[0];
            BanEntry entry = main.getManager().getBanEntry(name);
            if (entry == null) {
                main.getManager().ban(name, sender.getName(), "None");
                main.getBridge().disconnectPlayer(name, sender.getName());
                main.getBridge().broadcastMessage(Utils.colorize(
                        main.getConfig().getSection("messages").getString("gban_broadcast")
                        .replace("{punisher}", sender.getName())
                        .replace("{punished}", name))
                );
            } else sender.sendMessage(new TextComponent(Utils.colorize(main.getConfig().getSection("messages").getString("already_banned"))));
        } else {
            for(String string : main.getConfig().getSection("messages").getStringList("command_usage")) {
                sender.sendMessage(new TextComponent(Utils.colorize(
                        string.replace("{command}", getName())
                )));
            }
        }
    }
}