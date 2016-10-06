package com.kan_model.bukkit.First.Listener;

import com.kan_model.bukkit.First.ReportWrong;
import com.kan_model.bukkit.First.SQL.SaveSql;
import com.kan_model.bukkit.First.util.Type;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by kgdwhsk on 2016/8/19.
 */
public class ChatListener implements Listener{
    public static final int  THEFT = 1;
    public static final int DESTROY = 2;
    public static final int SBUG = 3;
//    private Type untiType = new Type();
    private FileConfiguration lang = ReportWrong.getLang();
    private FileConfiguration config;
    private static HashMap<Player, Long> cooldown = GuiListener.getCooldown();
    private static HashMap<Player, Type> Link = GuiListener.getLink();

    public ChatListener(ReportWrong main){
        config = main.getMyConfig();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        try {
//            untiType = GuiListener.getUntiType();
//            if (untiType.getPlayer().getName().equalsIgnoreCase(event.getPlayer().getName()) && untiType.getMore()) {
            if (true) {
                Player player = event.getPlayer();
                if (this.cooldown.containsKey(player)) {
                    if (System.currentTimeMillis() - ((Long) this.cooldown.get(player)).longValue() > 1000L * config.getInt("ReportCD")) {
                        addReport(player, event);
                    } else if (!player.isOp()) {
                        player.sendMessage(ChatColor.RED + ReportWrong.RW + "你刚刚举报过，等等再举报吧!");
                        event.setCancelled(true);
                    }else {
                        player.sendMessage(ChatColor.RED + ReportWrong.RW + "管理员不能举报!");
                    }
                } else if (!player.isOp()){
                    this.cooldown.put(player, Long.valueOf(System.currentTimeMillis()));
//                    if (untiType.getPlayer().getName().equalsIgnoreCase(player.getName())) {
                    if (Link.containsKey(player)) {
                        addReport(player, event);
                    } else {
                        player.sendMessage(ChatColor.RED + ReportWrong.RW + lang.getString("gui.c.confirm.failure"));
                    }
                    player.closeInventory();
                }else {
                    player.sendMessage(ChatColor.RED + ReportWrong.RW + "管理员不能举报!");
                }
//                untiType.setMore();
                Link.get(player).setMore();
            }
        }catch (NullPointerException e){

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void addReport(Player player,AsyncPlayerChatEvent event){
        Type t = Link.get(player);
        t.setMoreInformation(event.getMessage());
        t.setMore();
        player.closeInventory();
        String world = player.getWorld().getName();
        int x = player.getLocation().getBlockX();
        int y = player.getLocation().getBlockY();
        int z = player.getLocation().getBlockZ();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        int type = t.getType();
        switch (type) {
            case THEFT:
                player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.LIGHT_PURPLE + time
                        + lang.getString("gui.c.confirm.theft")
                        + world + " [X]:" + x + " [Y]:" + y + " [Z]:" + z);
                if (!(SaveSql.addReport(player.getName(), world, x, y, z, type, time, t.getMoreInformation()))) {
                    player.sendMessage(ChatColor.RED + ReportWrong.RW + lang.getString("gui.c.confirm.failure"));
                } else {
                    player.sendMessage(ChatColor.GREEN + ReportWrong.RW + "举报详情如下:" + ChatColor.WHITE + t.getMoreInformation());
                    event.setCancelled(true);
                }
                break;
            case DESTROY:
                player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.LIGHT_PURPLE + time
                        + lang.getString("gui.c.confirm.destroy")
                        + world + " [X]:" + x + " [Y]:" + y + " [Z]:" + z);
                if (!(SaveSql.addReport(player.getName(), world, x, y, z, type, time, t.getMoreInformation()))) {
                    player.sendMessage(ChatColor.RED + ReportWrong.RW + lang.getString("gui.c.confirm.failure"));
                } else {
                    player.sendMessage(ChatColor.GREEN + ReportWrong.RW + "举报详情如下:" + ChatColor.WHITE + t.getMoreInformation());
                    event.setCancelled(true);
                }
                break;
            case SBUG:
                player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.LIGHT_PURPLE + time
                        + lang.getString("gui.c.confirm.sbug"));
                if (!(SaveSql.addReport(player.getName(), world, x, y, z, type, time, t.getMoreInformation()))) {
                    player.sendMessage(ChatColor.RED + ReportWrong.RW + lang.getString("gui.c.confirm.failure"));
                } else {
                    player.sendMessage(ChatColor.GREEN + ReportWrong.RW + "举报详情如下:" + ChatColor.WHITE + t.getMoreInformation());
                    event.setCancelled(true);
                }
                break;
        }
    }
}
