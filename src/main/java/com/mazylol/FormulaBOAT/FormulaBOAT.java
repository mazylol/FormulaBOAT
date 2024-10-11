package com.mazylol.FormulaBOAT;

import com.mazylol.FormulaBOAT.handlers.OnClick;
import com.mazylol.FormulaBOAT.handlers.OnJoin;
import com.mazylol.FormulaBOAT.handlers.OnMove;
import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.time.Duration;

public final class FormulaBOAT extends JavaPlugin {
    State state;

    @Override
    public void onEnable() {
        getLogger().info("Starting FormulaBOAT");

        BukkitScheduler scheduler = getServer().getScheduler();

        getServer().getPluginManager().registerEvents(new OnMove(), this);
        getServer().getPluginManager().registerEvents(new OnJoin(), this);
        getServer().getPluginManager().registerEvents(new OnClick(), this);

        LifecycleEventManager<Plugin> manager = this.getLifecycleManager();

        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();

            commands.register(
                    Commands.literal("setFinishLine").executes(ctx -> {
                        if (!ctx.getSource().getExecutor().isOp()) {
                            ctx.getSource().getSender().sendPlainMessage("You do not have permission to use this command");
                            return Command.SINGLE_SUCCESS;
                        }

                        ctx.getSource().getSender().sendPlainMessage("Setting the finish line, click the desired blocks");

                        State.playerSettingFinishLine = (org.bukkit.entity.Player) ctx.getSource().getExecutor();
                        State.settingFinishLine = true;

                        return Command.SINGLE_SUCCESS;
                    }).build(),
                    "Set the finish line"
            );

            commands.register(
                    Commands.literal("endFinishLine").executes(ctx -> {
                        if (!ctx.getSource().getExecutor().isOp()) {
                            ctx.getSource().getSender().sendPlainMessage("You do not have permission to use this command");
                            return Command.SINGLE_SUCCESS;
                        }

                        ctx.getSource().getSender().sendPlainMessage("Ending the finish line");
                        for (org.bukkit.block.Block block : State.finishLine) {
                            ctx.getSource().getSender().sendPlainMessage(String.format("Block at %d, %d, %d", block.getX(), block.getY(), block.getZ()));
                        }

                        State.playerSettingFinishLine = null;
                        State.settingFinishLine = false;

                        return Command.SINGLE_SUCCESS;
                    }).build(),
                    "End the finish line"
            );

            commands.register(
                    Commands.literal("resetFinishLine").executes(ctx -> {
                        if (!ctx.getSource().getExecutor().isOp()) {
                            ctx.getSource().getSender().sendPlainMessage("You do not have permission to use this command");
                            return Command.SINGLE_SUCCESS;
                        }

                        ctx.getSource().getSender().sendPlainMessage("Resetting the finish line");

                        State.finishLine.clear();

                        return Command.SINGLE_SUCCESS;
                    }).build(), "Reset the finish line"
            );

            commands.register(
                    Commands.literal("startRace").executes(ctx -> {
                        if (!ctx.getSource().getExecutor().isOp()) {
                            ctx.getSource().getSender().sendPlainMessage("You do not have permission to use this command");
                            return Command.SINGLE_SUCCESS;
                        }

                        ctx.getSource().getSender().sendPlainMessage("Starting the race");

                        for (Block block : State.finishLine) {
                            block.getWorld().getBlockAt(block.getX(), block.getY() + 1, block.getZ()).setType(org.bukkit.Material.BEDROCK);
                        }

                        Audience audience = Audience.audience(getServer().getOnlinePlayers());

                        scheduler.runTask(this, () -> {
                            Component titleText = Component.text("Ready").color(NamedTextColor.RED);
                            Title title = Title.title(titleText, Component.empty(), Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(1), Duration.ofSeconds(1)));

                            audience.showTitle(title);
                        });

                        scheduler.runTaskLater(this, () -> {
                            audience.clearTitle();

                            Component titleText = Component.text("Set").color(NamedTextColor.YELLOW);
                            Title title = Title.title(titleText, Component.empty(), Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(1), Duration.ofSeconds(1)));

                            audience.showTitle(title);
                        }, 60L);

                        scheduler.runTaskLater(this, () -> {
                            audience.clearTitle();

                            Component titleText = Component.text("Go!").color(NamedTextColor.GREEN);
                            Title title = Title.title(titleText, Component.empty(), Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(1), Duration.ofSeconds(1)));

                            audience.showTitle(title);

                            State.racing = true;
                            State.ableToMove = true;

                            for (Block block : State.finishLine) {
                                block.getWorld().getBlockAt(block.getX(), block.getY() + 1, block.getZ()).setType(org.bukkit.Material.AIR);
                            }
                        }, 120L);

                        return Command.SINGLE_SUCCESS;
                    }).build(), "Start the race");

            commands.register(
                    Commands.literal("endRace").executes(ctx -> {
                        if (!ctx.getSource().getExecutor().isOp()) {
                            ctx.getSource().getSender().sendPlainMessage("You do not have permission to use this command");
                            return Command.SINGLE_SUCCESS;
                        }

                        ctx.getSource().getSender().sendPlainMessage("Ending the race");

                        for (Block block : State.finishLine) {
                            block.getWorld().getBlockAt(block.getX(), block.getY() + 1, block.getZ()).setType(org.bukkit.Material.AIR);
                        }

                        State.racing = false;

                        return Command.SINGLE_SUCCESS;
                    }).build(), "End the race");
        });
    }

    @Override
    public void onDisable() {
        getLogger().info("Stopping FormulaBOAT");
    }
}
