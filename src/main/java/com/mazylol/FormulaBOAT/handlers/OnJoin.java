package com.mazylol.FormulaBOAT.handlers;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class OnJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Audience audience = Audience.audience(event.getPlayer());

        Component header = Component.text("Hello, " + event.getPlayer().getName() + "!");

        audience.sendPlayerListHeader(header);
    }
}
