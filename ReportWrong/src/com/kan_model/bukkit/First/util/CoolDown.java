package com.kan_model.bukkit.First.util;

import com.kan_model.bukkit.First.ReportWrong;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created by kgdwhsk on 2016/8/19.
 */
public class CoolDown {
    //    public static void main(String args[]){
////        Calendar date = new GregorianCalendar(2000,);
//        System.out.println(date.getTime());
//        Calendar date2 = new GregorianCalendar();
//        System.out.println(date2.getTime());
//        System.out.println(date.compareTo(date2));
//    }
    private boolean coolDownNow = true;
    private String playerName;
//    private int time;

    public CoolDown(){
        this.coolDownNow = true;
    }

    public CoolDown(Player player, ReportWrong main){
        playerName = player.getName();
        new BukkitRunnable() {

            int time = main.getMyConfig().getInt("ReportCD");  // 六十秒

            @Override
            public void run() {
                if(time == 0) {
                    coolDownNow = false;
                    cancel();  // 终止线程
                    return;
                }
                // your code ...
            }
        }.runTaskTimer(main, 0L, 20L);
    }

    public boolean isCoolDownNow() {
        return coolDownNow;
    }

    public String getPlayerName() {
        return playerName;
    }

//    public int getTime() {
//        return time;
//    }
}

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

//package me.GlitchMaster_YT.ChatCoolDown;

//        import java.util.HashMap;
//        import java.util.logging.Logger;
//        import org.bukkit.ChatColor;
//        import org.bukkit.entity.Player;
//        import org.bukkit.event.EventHandler;
//        import org.bukkit.event.Listener;
//        import org.bukkit.event.player.AsyncPlayerChatEvent;
//        import org.bukkit.plugin.PluginDescriptionFile;
//        import org.bukkit.plugin.java.JavaPlugin;

class ChatCoolDown extends JavaPlugin implements Listener {
    public final Logger logger = this.getLogger();
    public static ChatCoolDown plugin;
    private final HashMap<Player, Long> cooldown = new HashMap();

    public ChatCoolDown() {
    }

    public void onEnable() {
        PluginDescriptionFile pdfFile = this.getDescription();
        this.logger.info(pdfFile.getName() + " Version " + pdfFile.getVersion() + " Has Been Enabled!");
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    public void onDisable() {
        PluginDescriptionFile pdfFile = this.getDescription();
        this.logger.info(pdfFile.getName() + " Has Been Disabled!");
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if(this.cooldown.containsKey(p)) {
            if(System.currentTimeMillis() - ((Long)this.cooldown.get(p)).longValue() > 5000L) {
                this.cooldown.remove(p);
                return;
            }

            if(!p.isOp() || !p.hasPermission("ChatCoolDown.bypass")) {
                p.sendMessage(ChatColor.RED + "You must wait 5 seconds before being able to speak again!");
                e.setCancelled(true);
            }
        } else if(!p.isOp() || !p.hasPermission("ChatCoolDown.bypass")) {
            this.cooldown.put(p, Long.valueOf(System.currentTimeMillis()));
        }

    }
}

