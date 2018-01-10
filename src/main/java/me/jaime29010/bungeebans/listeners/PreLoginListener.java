package me.jaime29010.bungeebans.listeners;

import me.jaime29010.bungeebans.Main;
import me.jaime29010.bungeebans.core.entries.BanEntry;
import me.jaime29010.bungeebans.core.entries.IPBanEntry;
import me.jaime29010.bungeebans.utils.Utils;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PreLoginListener implements Listener {
    private final Main main;
    public PreLoginListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPreLoginEvent(PreLoginEvent event) {
        BanEntry entry = main.getManager().getBanEntry(event.getConnection().getName());
        if(entry != null) {
            event.setCancelled(true);
            event.setCancelReason("You are banned");
            event.getConnection().disconnect(new TextComponent(Utils.colorize(
                    main.getConfig().getSection("messages").getString("gban_reason")
                    .replace("{punisher}", entry.getPunisher())
                    .replace("{punished}", entry.getPunished())
            )));
        } else {
            entry = main.getManager().getIPBanEntry(event.getConnection().getAddress().getAddress());
            if (entry != null) {
                event.setCancelled(true);
                event.setCancelReason("You are banned");
                event.getConnection().disconnect(new TextComponent(Utils.colorize(
                        main.getConfig().getSection("messages").getString("gbanip_reason")
                        .replace("{punisher}", entry.getPunisher())
                        .replace("{punished}", entry.getPunished())
                        .replace("{address}", ((IPBanEntry)entry).getAddress())
                )));
            }
        }
    }
}
