package com.kan_model.bukkit.First.SQL;

import com.kan_model.bukkit.First.ReportWrong;
//import com.saveSql.jdbc.Connection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by kgdwhsk on 2016/8/16.
 */
public class SaveSql {
    private static Connection connection;
    private ReportWrong main;
    private CommandSender sender;
    private static SaveSql saveSql;
    public static Statement statement;

    public SaveSql(ReportWrong rw, FileConfiguration config){
        main = rw;
        sender = Bukkit.getConsoleSender();
        try {

            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:plugins/ReportWrong/rwdatabase.db");
            statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS 'rwlist'('id' INT NOT NULL,'name' VARCHAR(32) NOT NULL," +
                    "'world' VARCHAR(32) NOT NULL,'x' INT NOT NULL ,'y' INT NOT NULL ,'z' INT NOT NULL ,'Rtype' INT NOT NULL ," +
                    "'playerword' VARCHAR DEFAULT 'None','complete' INT DEFAULT 0,'adminword' VARCHAR DEFAULT 'None'," +
                    "'ctime' VARCHAR NOT NULL ,'UUID' VARCHAR DEFAULT 'None')");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS rewardList('id' INT NOT NULL,'name' VARCHAR (32) NOT NULL ," +
                    "'ri' VARCHAR NOT NULL ,'rc' INT NOT NULL ,'complete' INT DEFAULT 0,'UUID' VARCHAR DEFAULT 'None')");
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + rw.RW + "数据库启动成功！");
        } catch (SQLException e1) {
            e1.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + rw.RW + "数据库启动失败！");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void addArgs(CommandSender sender, FileConfiguration config, String name, Object value){
        config.set(name,value);
//        sender.sendMessage(ReportWrong.RW + ChatColor.RED + "缺少参数" + name);
        try {
            config.save(new File(main.getDataFolder(),"config.yml"));
//            sender.sendMessage(ReportWrong.RW + ChatColor.GREEN + "成功添加参数" + name);
        } catch (IOException e) {
            e.printStackTrace();
//            sender.sendMessage(ReportWrong.RW + ChatColor.RED + "添加参数" + name + "失败");
        }
    }

    public static boolean addReport(String playerName, String world, int x, int y, int z,int type ,String cotime){
        boolean s = false;
        try {
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(id) FROM 'rwlist'");
            int id = resultSet.getInt(1) + 1;
            statement.executeUpdate("INSERT INTO rwlist (id, name, world, x, y, z,Rtype,ctime) VALUES (" + id + ",'"
                    + playerName + "','" + world + "'," + x + "," + y + "," + z + "," + type + ",'" + cotime + "');");
            s = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static boolean recordReward(String playerName,String rewardItem,int rewardCount){
        boolean s =false;
        try {
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(id) FROM 'rewardList'");
            int id = resultSet.getInt(1) + 1;
            statement.executeUpdate("INSERT INTO rewardList (id,name,ri,rc) VALUES ("+ id + ",'" + playerName +"','" + rewardItem
                    + "'," + rewardCount + ")");
            s = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static void listReport(CommandSender sender){
//        Statement statement = null;
//        String sql = "SELECT * FROM 'rwlist'";
        try {
//            statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM 'rwlist'");
//            resultSet.next();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM rwlist WHERE complete = 0;");
            String time;
            while (resultSet.next()){
                time = resultSet.getString("ctime");
                resultSet.getInt("Rtype");
                sender.sendMessage(ChatColor.GREEN + ReportWrong.RW + " " + resultSet.getInt("id") + " " + time + " 玩家[" +
                        resultSet.getString("name") + "]举报世界[" + resultSet.getString("world") +
                "] x:" + resultSet.getInt("x") + " y:" + resultSet.getInt("y") + " z:" + resultSet.getInt("z"));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet checkReport(int id) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT * FROM rwlist WHERE id = " + id + ";");
        return resultSet;
    }

    public static boolean setCompleted(ResultSet resultSet,int id){
        try {
            if (resultSet.getInt("complete") == 0) {
                statement.executeUpdate("UPDATE rwlist SET complete = 1 WHERE id = " + id);
                return true;
            }else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static SaveSql getSaveSql() {
        return saveSql;
    }

    public static Statement getStatement() {
        return statement;
    }

    public static Connection getConnection() {
        return connection;
    }
}
