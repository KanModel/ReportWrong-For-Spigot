package com.kan_model.bukkit.First.Listener;

import com.kan_model.bukkit.First.ReportWrong;
import com.kan_model.bukkit.First.SQL.SaveSql;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kgdwhsk on 2016/8/19.
 */
public class ChatListener implements Listener{
    public static final int  THEFT = 1;
    public static final int DESTROY = 2;
    public static final int SBUG = 3;
    private Type untiType = null;
    private FileConfiguration lang = ReportWrong.getLang();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        untiType = GuiListener.getUntiType();
        if (untiType.getPlayer().getName().equalsIgnoreCase(event.getPlayer().getName())){
            untiType.setMoreInformation(event.getMessage());
            untiType.setMore();
            Player player = event.getPlayer();
            player.closeInventory();
            String world = player.getWorld().getName();
            int x = player.getLocation().getBlockX();
            int y = player.getLocation().getBlockY();
            int z = player.getLocation().getBlockZ();
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            switch (untiType.getType()) {
                case THEFT:
                    player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.LIGHT_PURPLE + time
                            + lang.getString("gui.c.confirm.theft")
                            + world + " [X]:" + x + " [Y]:" + y + " [Z]:" + z);
                    if (!(SaveSql.addReport(player.getName(), world, x, y, z, untiType.getType(), time,untiType.getMoreInformation()))){
                        player.sendMessage(ChatColor.RED + ReportWrong.RW + lang.getString("gui.c.confirm.failure"));
                    }else {
                        player.sendMessage(ChatColor.GREEN + ReportWrong.RW + "举报详情如下:" + ChatColor.WHITE + untiType.getMoreInformation());
                        event.setCancelled(true);
                    }
                    break;
                case DESTROY:
                    player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.LIGHT_PURPLE + time
                            + lang.getString("gui.c.confirm.destroy")
                            + world + " [X]:" + x + " [Y]:" + y + " [Z]:" + z);
                    if (!(SaveSql.addReport(player.getName(), world, x, y, z, untiType.getType(), time,untiType.getMoreInformation()))){
                        player.sendMessage(ChatColor.RED + ReportWrong.RW + lang.getString("gui.c.confirm.failure"));
                    }else {
                        player.sendMessage(ChatColor.GREEN + ReportWrong.RW + "举报详情如下:" + ChatColor.WHITE + untiType.getMoreInformation());
                        event.setCancelled(true);
                    }
                    break;
                case SBUG:
                    player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.LIGHT_PURPLE + time
                            + lang.getString("gui.c.confirm.sbug"));
                    if (!(SaveSql.addReport(player.getName(), world, x, y, z, untiType.getType(), time,untiType.getMoreInformation()))){
                        player.sendMessage(ChatColor.RED + ReportWrong.RW + lang.getString("gui.c.confirm.failure"));
                    }else {
                        player.sendMessage(ChatColor.GREEN + ReportWrong.RW + "举报详情如下:" + ChatColor.WHITE + untiType.getMoreInformation());
                        event.setCancelled(true);
                    }
                    break;
            }
        }
    }
}
