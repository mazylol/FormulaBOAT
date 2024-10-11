package com.mazylol.FormulaBOAT;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class State {
    public static boolean settingFinishLine = false;

    public static Player playerSettingFinishLine;

    public static List<Block> finishLine = new ArrayList<>();

    public static boolean racing = false;
    public static boolean ableToMove = false;
}
