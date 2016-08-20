package com.kan_model.bukkit.First.SQL;

import com.kan_model.bukkit.First.ReportWrong;
import com.kan_model.bukkit.First.Listener.GuiListener;
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
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS rwcount('rwcount' INT DEFAULT 0,'complete' INT DEFAULT 0)");
//            isRWCountNull();
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
//        try {
//            ResultSet resultSet = statement.executeQuery("SELECT COUNT(id) FROM 'rwlist'");
//            int id = resultSet.getInt(1) + 1;
//            statement.executeUpdate("INSERT INTO rwlist (id, name, world, x, y, z,Rtype,ctime) VALUES (" + id + ",'"
//                    + playerName + "','" + world + "'," + x + "," + y + "," + z + "," + type + ",'" + cotime + "');");
//            s = true;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        s = addReport(playerName,world,x,y,z,type,cotime,"None");
        return s;
    }

    public static boolean addReport(String playerName, String world, int x, int y, int z,int type ,String cotime ,String more){
        boolean s =false;
        try {
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(id) FROM 'rwlist'");
            int id = resultSet.getInt(1) + 1;
            statement.executeUpdate("INSERT INTO rwlist (id, name, world, x, y, z,Rtype,ctime,playerword) VALUES (" + id + ",'"
                    + playerName + "','" + world + "'," + x + "," + y + "," + z + "," + type + ",'" + cotime + "','" + more + "');");
            s = true;
            ResultSet rwcount = statement.executeQuery("SELECT * FROM 'rwcount'");
//            ResultSet rwcount = statement.executeQuery("SELECT * FROM 'rwcount'");
//            int ricount = rwcount.getInt(1);
//            if (ricount == 0){
//                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + ReportWrong.RW + "数据库新建成功！");
//                statement.executeUpdate("INSERT INTO rewardList (complete) VALUES (1)");
//            }
            statement.executeUpdate("INSERT INTO rwcount (rwcount) VALUES (" + id + ")");
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
                int type = resultSet.getInt("Rtype");
                String Type = null;
                switch (type){
                    case GuiListener.THEFT:
                        Type = "偷窃";
                        break;
                    case GuiListener.DESTROY:
                        Type = "破坏";
                        break;
                    case GuiListener.SBUG:
                        Type = "Bug";
                        break;
                }
                sender.sendMessage(ChatColor.GREEN  + "" + resultSet.getInt("id") + " " + time + " " +
                        resultSet.getString("name") + " 举报世界[" + resultSet.getString("world") +
                "] x:" + resultSet.getInt("x") + " y:" + resultSet.getInt("y") + " z:" + resultSet.getInt("z") + "发生" + Type);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet checkReport(int id) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT * FROM rwlist WHERE id = " + id + ";");
        return resultSet;
    }

    public static boolean haveId(int id){
        try {
            if (checkReport(id).getInt(1) == 0){
                return false;
            }else {
                return true;
            }
        }catch (SQLException e){

        }
        return false;
    }

    public static boolean setCompleted(ResultSet resultSet,int id) throws SQLException {
        if (haveId(id)) {
            try {
                if (resultSet.getInt("complete") == 0) {
                    statement.executeUpdate("UPDATE rwlist SET complete = 1 WHERE id = " + id);
                    statement.executeUpdate("UPDATE rwcount SET complete = 1 WHERE rwcount = " + id);
                    return true;
                } else {
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean setRewardCompleted(ResultSet resultSet,int id){
        try {
            statement.executeUpdate("UPDATE rewardList SET complete = 1 WHERE id = " + id);
            return true;
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

    public static int getUndoneCount() throws SQLException {
        int count = 0;
//        select fk, count(value)
//                from table
//        group by fk
//        having count(value) > 1;
//        try {
//            ResultSet resultSet;
////            SELECT COUNT(Customer) AS CustomerNilsen FROM Orders
////            WHERE Customer='Carter'
////            resultSet = statement.executeQuery("SELECT COUNT(complete) FROM 'rewardList' GROUP BY rewardList HAVING complete = 0");
////            SELECT COUNT (Store_Name)
////                    FROM Store_Information
////            WHERE Store_Name IS NOT NULL;
////            select 类别, count(*) AS 记录数 from A group by 类别;
////            resultSet = statement.executeQuery("SELECT complete,COUNT(*),ctime FROM 'rewardList'GROUP BY ctime HAVING complete = 0");
//            resultSet = statement.executeQuery("SELECT COUNT(id) FROM (SELECT id FROM 'rewardList' WHERE complete = 0)");
//            count = resultSet.getInt(1);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            count = 0;
//        }
        ResultSet rwcount = statement.executeQuery("SELECT COUNT(*) FROM 'rwcount'WHERE complete = 0");
        count = rwcount.getInt(1);
        return count;
    }

//    public static boolean isRWCountNull() throws SQLException {
//        ResultSet rwcount = statement.executeQuery("SELECT * FROM 'rwcount'");
//        int ricount = rwcount.getInt(1);
//        if (ricount == 0){
//            statement.executeUpdate("INSERT INTO rwcount (complete) VALUES (1)");
//            return false;
//        }
//        return true;
//    }
}
