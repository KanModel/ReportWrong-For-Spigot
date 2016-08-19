package com.kan_model.bukkit.First.Listener;

import com.kan_model.bukkit.First.ReportWrong;
import com.kan_model.bukkit.First.SQL.SaveSql;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by kgdwhsk on 2016/8/19.
 */



public class JoinRewardListener implements Listener{

    private Connection connection;
    private static Statement statement = null;
    private FileConfiguration co;

    public JoinRewardListener(ReportWrong main){
        this.connection = SaveSql.getConnection();
        statement = SaveSql.getStatement();
        co = main.getMyConfig();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        String playerName = player.getName();
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM rewardList WHERE complete = 0;");

            while (resultSet.next()){
                String name = resultSet.getString("name");
                String item = co.getString("RewardItem").toUpperCase();
//                rewardPlayer.getInventory().addItem(
//                        new ItemStack(Material.getMaterial(item),co.getInt("RewardCount")));
                if (playerName.equalsIgnoreCase(name)){
//                    Inventory inventory = player.getInventory();
//                    inventory.addItem();
                    if (resultSet.getInt("rc") != 0) {
                        ItemStack rewardItem = new ItemStack(Material.getMaterial(item), co.getInt("RewardCount"));
                        player.getWorld().dropItemNaturally(player.getLocation(), rewardItem);
                        player.sendMessage(ChatColor.GREEN + ReportWrong.RW + "你被给予了回报！");
                    }else {
                        player.sendMessage(ChatColor.RED + ReportWrong.RW + "你的举报被解决不带回报");
                    }
                }
            }
//            while (resultSet.next()){
//                time = resultSet.getString("ctime");
//                resultSet.getInt("Rtype");
//                sender.sendMessage(ChatColor.GREEN + ReportWrong.RW + " " + resultSet.getInt("id") + " " + time + " 玩家[" +
//                        resultSet.getString("name") + "]举报世界[" + resultSet.getString("world") +
//                        "] x:" + resultSet.getInt("x") + " y:" + resultSet.getInt("y") + " z:" + resultSet.getInt("z"));
//            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
