package com.kan_model.bukkit.First.Listener;

import com.kan_model.bukkit.First.ReportWrong;
import com.kan_model.bukkit.First.SQL.SaveSql;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

/**
 * Created by kgdwhsk on 2016/8/20.
 */
public class AdminJoinRemind implements Listener {
    @EventHandler
    public void onAdminJoin(PlayerJoinEvent event) throws SQLException {
        if (event.getPlayer().isOp()){
            int count = SaveSql.getUndoneCount();
            if (count != 0) {
                event.getPlayer().sendMessage(ChatColor.GREEN + ReportWrong.RW + "你有" + count + "条未解决举报!");
            }
        }
    }
}
