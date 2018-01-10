package me.jaime29010.bungeebans.utils;

import com.imaginarycode.minecraft.redisbungee.RedisBungee;
import com.imaginarycode.minecraft.redisbungee.events.PubSubMessageEvent;
import me.jaime29010.bungeebans.Main;
import me.jaime29010.bungeebans.core.entries.IPBanEntry;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class RedisBridge implements Listener {
    private final Main main;
    public RedisBridge(Main main) {
        this.main = main;
        RedisBungee.getApi().registerPubSubChannels("BungeeBansMessageBroadcast", "BungeeBansPlayerDisconnect", "BungeeBansBanIP", "BungeeBansAlreadyBannedMessage");
        main.getProxy().getPluginManager().registerListener(main, this);
    }

    public void broadcastMessage(String string) {
        RedisBungee.getApi().sendChannelMessage("BungeeBansMessageBroadcast", string);
    }

    public void disconnectPlayer(String punished, String punisher) {
        RedisBungee.getApi().sendChannelMessage("BungeeBansPlayerDisconnect", String.format("%s, %s", punished, punisher));
    }

    public void banPlayerIPGlobally(String punished, String punisher) {
        RedisBungee.getApi().sendChannelMessage("BungeeBansBanIP", String.format("%s, %s", punished, punisher));
    }

    public boolean isOnline(String name) {
        return RedisBungee.getApi().getHumanPlayersOnline().contains(name);
    }

    @EventHandler
    public void onPubSubMessage(PubSubMessageEvent event) {
        switch (event.getChannel()) {
            case "BungeeBansMessageBroadcast": {
                main.getProxy().broadcast(new TextComponent(event.getMessage()));
                break;
            }
            case "BungeeBansPlayerDisconnect": {
                String[] data = event.getMessage().split("\\s*,\\s*");
                ProxiedPlayer player = main.getProxy().getPlayer(data [0]);
                if (player != null) {
                    player.disconnect(new TextComponent(Utils.colorize(
                            main.getConfig().getSection("messages").getString("gban_reason")
                            .replace("{punisher}", data [1])
                            .replace("{punished}", player.getName()))
                    ));
                }
                break;
            }
            case "BungeeBansBanIP": {
                String[] data = event.getMessage().split("\\s*,\\s*");
                ProxiedPlayer player = main.getProxy().getPlayer(data [0]);
                if (player != null) {
                    String punisher = data [1];
                    IPBanEntry entry = main.getManager().getIPBanEntry(player.getAddress().getAddress());
                    if(entry == null) {
                        main.getManager().banip(player, punisher, "None");
                        player.disconnect(new TextComponent(Utils.colorize(
                                main.getConfig().getSection("messages").getString("gbanip_reason")
                                        .replace("{punisher}", punisher)
                                        .replace("{punished}", player.getName())
                                        .replace("{address}", player.getAddress().getAddress().toString()))));
                        main.getBridge().broadcastMessage(Utils.colorize(
                                main.getConfig().getSection("messages").getString("gbanip_broadcast")
                                        .replace("{punisher}", punisher))
                                        .replace("{punished}", player.getName())
                                        .replace("{address}", player.getAddress().getAddress().toString()));
                    } else RedisBungee.getApi().sendChannelMessage("BungeeBansAlreadyBannedMessage", punisher);
                }
                break;
            }
            case "BungeeBansAlreadyBannedMessage": {
                ProxiedPlayer player = main.getProxy().getPlayer(event.getMessage());
                if (player != null) {
                    player.sendMessage(new TextComponent(Utils.colorize(main.getConfig().getSection("messages").getString("already_banned"))));
                }
                break;
            }
        }
    }
}
