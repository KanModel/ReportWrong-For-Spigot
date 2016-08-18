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
//        Class.forName("org.sqlite.JDBC");
//        Connection conn =
//                DriverManager.getConnection("jdbc:sqlite:test.db");
//        String url = "jdbc:saveSql://127.0.0.1:3306/scutcs";
//        try{
//            Class.forName("com.saveSql.jdbc.Driver").newInstance();
//            connection = (Connection) DriverManagerConnectionSource.createConnectionSource("jdbc:saveSql://127.0.0.1:3306/database", "root", "root").getConnection();
//            rw.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + rw.RW + "数据库启动成功！");
//        }catch (Exception e){
//            rw.getServer().getConsoleSender().sendMessage(ChatColor.RED + rw.RW + "数据库启动失败！");
//        }
        main = rw;
        sender = Bukkit.getConsoleSender();
        try {

//                connection = DriverManager.getConnection("jdbc:saveSql://" + rw.getMyConfig().getString("saveSql.ip") + "/" +
//                        rw.getMyConfig().getString("saveSql.dbname"), rw.getMyConfig().getString("saveSql.dbuser"), rw.getConfig().getString("saveSql.dbpass"));
//                connection = DriverManager.getConnection("jdbc:saveSql://127.0.0.1:3306/mc","root","root");main.getDataFolder()
            Class.forName("org.sqlite.JDBC");
//            sender.sendMessage(main.getDataFolder().toString() +"/rwdatabase.db");
            connection = DriverManager.getConnection("jdbc:sqlite:plugins/ReportWrong/rwdatabase.db");
            statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS 'rwlist'('id' INT NOT NULL,'name' VARCHAR(32) NOT NULL," +
                    "'world' VARCHAR(32) NOT NULL,'x' INT NOT NULL ,'y' INT NOT NULL ,'z' INT NOT NULL ,'Rtype' INT NOT NULL ," +
//                    "'playerword' VARCHAR DEFAULT 'None','complete' INT DEFAULT 0,'adminword' VARCHAR DEFAULT 'None')");
                    "'playerword' VARCHAR DEFAULT 'None','complete' INT DEFAULT 0,'adminword' VARCHAR DEFAULT 'None'," +
                    "'ctime' VARCHAR NOT NULL ,'UUID' VARCHAR DEFAULT 'None')");
//                    "'time' TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
//                    "'playerword' VARCHAR DEFAULT 'None','complete' INT DEFAULT 0,'adminword' VARCHAR DEFAULT 'None','time' DATETIME_INTERVAL_CODE )");
//                statement.executeUpdate("CREATE TABLE IF NOT EXISTS rwlist(" +
//                        "id INT NOT NULL," +
//                        "name VARCHAR(32) NOT NULL," +
//                        "wrold VARCHAR(32) NOT NULL," +
//                        "x INT NOT NULL ," +
//                        "y INT NOT NULL ," +
//                        "z INT NOT NULL ," +
//                        "more VARCHAR DEFAULT '无'," +
//                        ")");
//            statement.executeUpdate("INSERT INTO rwlist VALUES (1,'rwtest','world',1,1,1,'无详细')");
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
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//        String time = df.format(new Date());// new Date()为获取当前系统时间
        try {
//            select count(*) from [table_name]
//            String sql = (new StringBuilder("select * from qiandao where player='")).append(p.getName()).append("' ").toString();
//            ResultSet rs = statement.executeQuery(sql);
//            statement = connection.createStatement();
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

    public static void listReport(CommandSender sender){
//        Statement statement = null;
//        String sql = "SELECT * FROM 'rwlist'";
        try {
//            statement = connection.createStatement();
//            ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM 'rwlist'");
//            rs.next();
            ResultSet rs = statement.executeQuery("SELECT * FROM rwlist WHERE complete = 0;");
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//            String time = df.format(rs.getTimestamp("time"));
            String time;
            while (rs.next()){
//                time = new java.util.Date(rs.getTimestamp("time").getTime()).toLocaleString();
//                time = rs.getDate("time").toLocaleString();
                time = rs.getString("ctime");
                rs.getInt("Rtype");
                sender.sendMessage(ChatColor.GREEN + ReportWrong.RW + " " + rs.getInt("id") + " " + time + " 玩家[" +
                        rs.getString("name") + "]举报世界[" + rs.getString("world") +
                "] x:" + rs.getInt("x") + " y:" + rs.getInt("y") + " z:" + rs.getInt("z"));
            }
//            int maxId = rs.getInt("id") + 1;
//            while (statement.executeQuery("SELECT TOP 1 id FROM 'rwlist'").getInt("id") < maxId){
//
//            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
//        String sql="select * from book";
//        try{
//            Connection con = MyConnection.getConnection();
//            Statement statement = con.createStatement();
//            ResultSet resultSet = statement.executeQuery(sql);
//// ResultSetMetaData rsmd = resultSet.getMetaData();
//// int count = rsmd.getColumnCount();
//            while(resultSet.next()){
//                Book book = new Book();
//                book.setBookName(resultSet.getString("bookName"));
//                book.setPrice(resultSet.getString("price"));
//                book.setPicUrl(resultSet.getString("picUrl"));
//                list.add(book);
//            }
//        }catch(SQLException e){
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
    }

    public static ResultSet checkReport(int id) throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT * FROM rwlist WHERE id = " + id + ";");
        return rs;
    }

    public static void setCompleted(ResultSet resultSet,int id){
        try {
//            ResultSet rs = statement.executeQuery("SELECT * FROM rwlist WHERE id = " + id + ";");
//            UPDATE Person SET Address = 'Zhongshan 23', City = 'Nanjing'
//            WHERE LastName = 'Wilson'
            statement.executeUpdate("UPDATE rwlist SET complete = 1 WHERE id = " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static SaveSql getSaveSql() {
        return saveSql;
    }

    public static Connection getConnection() {
        return connection;
    }
}
