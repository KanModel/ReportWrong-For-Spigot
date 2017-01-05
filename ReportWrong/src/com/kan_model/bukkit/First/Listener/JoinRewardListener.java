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
                if (resultSet.getInt("complete") == 0) {
                    String name = resultSet.getString("name");
                    String item = co.getString("RewardItem").toUpperCase();
                    int id = resultSet.getInt("id");
                    if (playerName.equalsIgnoreCase(name)) {
                        if (resultSet.getInt("rc") != 0) {
                            ItemStack rewardItem = new ItemStack(Material.getMaterial(item), co.getInt("RewardCount"));
                            player.getWorld().dropItemNaturally(player.getLocation(), rewardItem);
                            player.sendMessage(ChatColor.GREEN + ReportWrong.RW + "你被给予了回报！");
                            resultSet = statement.executeQuery("SELECT * FROM rewardList WHERE complete = 0;");
                            SaveSql.setRewardCompleted(resultSet, resultSet.getInt("complete"));
                        } else {
                            player.sendMessage(ChatColor.RED + ReportWrong.RW + "你的举报被解决不带回报");
                            resultSet = statement.executeQuery("SELECT * FROM rewardList WHERE complete = 0;");
                            SaveSql.setRewardCompleted(resultSet, resultSet.getInt("complete"));
                        }
                        statement.executeUpdate("UPDATE rewardList SET complete = 1 WHERE id =" + id );
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
