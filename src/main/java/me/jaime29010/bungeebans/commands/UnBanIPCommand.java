package me.jaime29010.bungeebans.commands;

import me.jaime29010.bungeebans.Main;
import me.jaime29010.bungeebans.core.entries.IPBanEntry;
import me.jaime29010.bungeebans.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class UnBanIPCommand extends Command {
    private final Main main;
    public UnBanIPCommand(Main main) {
        super("gunbanip", "bungeebans.gunbanip", "");
        this.main = main;
    }

    @Override
    public void execute(final CommandSender sender, String[] args) {
        if (args.length == 1) {
            String name = args[0];
            IPBanEntry entry = main.getManager().getIPBanEntry(name);
            if(entry != null) {
                sender.sendMessage(new TextComponent(Utils.colorize(main.getConfig().getSection("messages").getString("successful_unbanip"))));
                main.getManager().unbanip(name);
            } else sender.sendMessage(new TextComponent(Utils.colorize(main.getConfig().getSection("messages").getString("player_not_banned"))));
        } else {
            for(String string : main.getConfig().getSection("messages").getStringList("command_usage")) {
                sender.sendMessage(new TextComponent(Utils.colorize(
                        string.replace("{command}", getName())
                )));
            }
        }
    }
}