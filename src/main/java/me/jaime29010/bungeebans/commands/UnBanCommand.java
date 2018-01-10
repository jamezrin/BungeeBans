package me.jaime29010.bungeebans.commands;

import me.jaime29010.bungeebans.Main;
import me.jaime29010.bungeebans.core.entries.BanEntry;
import me.jaime29010.bungeebans.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class UnBanCommand extends Command {
    private final Main main;
    public UnBanCommand(Main main) {
        super("gunban", "bungeebans.gunban", "");
        this.main = main;
    }

    @Override
    public void execute(final CommandSender sender, String[] args) {
        if (args.length == 1) {
            String name = args[0];
            BanEntry entry = main.getManager().getBanEntry(name);
            if(entry != null) {
                sender.sendMessage(new TextComponent(Utils.colorize(main.getConfig().getSection("messages").getString("successful_unban"))));
                main.getManager().unban(name);
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