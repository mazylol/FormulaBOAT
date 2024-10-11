package com.mazylol.FormulaBOAT.handlers;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.text.DecimalFormat;

public class OnMove implements Listener {
    private Location lastLocation = null;
    private long lastTime = System.currentTimeMillis();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Location currentLocation = event.getPlayer().getLocation();
        long currentTime = System.currentTimeMillis();

        // run every second
        if (currentTime - lastTime < 1000) {
            return;
        }

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        if (lastLocation != null) {
            double distance = currentLocation.distance(lastLocation);
            double timeElapsed = (currentTime - lastTime) / 1000.0;
            double velocity = distance / timeElapsed;

            Component velocityComponent = Component.text(String.format("%s (%s blocks/second)", event.getPlayer().getName(), df.format(velocity)));
            event.getPlayer().playerListName(velocityComponent);
        } else {
            Component velocityComponent = Component.text(event.getPlayer().getName());
            event.getPlayer().playerListName(velocityComponent);
        }

        lastLocation = currentLocation.clone();
        lastTime = currentTime;
    }
}