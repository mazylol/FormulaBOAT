package com.mazylol.FormulaBOAT.handlers;

import com.mazylol.FormulaBOAT.State;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class OnClick implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!State.settingFinishLine) {
            return;
        }

        if (event.getPlayer() != State.playerSettingFinishLine) {
            return;
        }

        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);

        State.finishLine.add(event.getClickedBlock());
    }
}
