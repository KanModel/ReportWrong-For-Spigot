package com.kan_model.bukkit.First.Listener;

import org.bukkit.entity.Player;

/**
 * Created by kgdwhsk on 2016/8/18.
 */
public class Type{

    private int type;
    private Player player;

    public Type(int type, Player player){
        this.player = player;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public Player getPlayer() {
        return player;
    }
}
//    private static final int  THEFT = 1;
//    private static final int DESTROY = 2;
//    private static final int SBUG = 3;
