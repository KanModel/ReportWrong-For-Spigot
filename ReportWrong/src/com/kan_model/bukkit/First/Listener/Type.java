package com.kan_model.bukkit.First.Listener;

import org.bukkit.entity.Player;

/**
 * Created by kgdwhsk on 2016/8/18.
 */
public class Type{

    private int type = 0;
    private Player player = null;
    private boolean more = false;
    private String moreInformation = null;

    public Type(){
    }

    public Type(Player player){
        this.player = player;
    }

    public Type(int type, Player player){
        this.player = player;
        this.type = type;
    }

    public Type(int type, Player player,boolean more){
        this.player = player;
        this.type = type;
        this.more = more;
    }

    public int getType() {
        return type;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean getMore(){
        return more;
    }

    public void setMore() {
        this.more = false;
    }

    public String getMoreInformation() {
        return moreInformation;
    }

    public void setMoreInformation(String moreInformation) {
        this.moreInformation = moreInformation;
    }
}
//    private static final int  THEFT = 1;
//    private static final int DESTROY = 2;
//    private static final int SBUG = 3;
