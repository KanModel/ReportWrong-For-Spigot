package com.kan_model.bukkit.First;

import com.kan_model.bukkit.First.Listener.*;
import com.kan_model.bukkit.First.SQL.SaveSql;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by kgdwhsk on 2016/8/15.
 * My First Plugin
 */
public class ReportWrong extends JavaPlugin {

    public static final String RW = "[ReportWrong]";
    private File configFile;
    private File langFile;
    private FileConfiguration config;
    private static FileConfiguration lang;

    @Override
    public void onEnable() {
        configFile = new File(getDataFolder(),"config.yml");
        this.saveDefaultConfig();
        langFile = new File(getDataFolder(),"lang.yml");
        this.saveMoreConfig(langFile.getName());
//        this.reloadConfig();
        lang = this.reloadSetting(langFile);
        try {
            this.reloadSetting();
//            this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + RW + "加载成功！");
            this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + RW + lang.getString("LoadSuccess"));
        }catch (Exception e){
//            this.getLogger().info("加载失败!");
            this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + RW + ChatColor.RED +lang.getString("LoadFailure"));
        }
        this.getCommand("reportwrong").setExecutor(new Command(this));
        this.getServer().getPluginManager().registerEvents(new GuiListener(this),this);
        this.getServer().getPluginManager().registerEvents(new OpenGUIListener(),this);
        this.getServer().getPluginManager().registerEvents(new ChatListener(this),this);
//        SaveSql sql = new SaveSql(this, config);
        new SaveSql(this, config);
        this.getServer().getPluginManager().registerEvents(new JoinRewardListener(this),this);
        this.getServer().getPluginManager().registerEvents(new AdminJoinRemind(),this);
//        if(!getDataFolder().exists()) {
//            getDataFolder().mkdir();
//        }
//        configFile = new File(getDataFolder(), "config.yml");
//        if (!(configFile.exists())){
//            saveDefaultConfig();
//        }
//        reloadConfig();
//        config = load(configFile);
//        reloadConfig();
//        config = getMyConfig();
//        File OFile = new File(getDataFolder(),"otherconfig.yml");
//        FileConfiguration other = load(OFile);
//        saveResource("config.yml",false);
    }

    @Override
    public void onDisable() {
//        saveConfig();
        try {
            config.save(configFile);
            lang.save(langFile);
            this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + RW + lang.getString("SaveSuccess"));
        } catch (IOException e1) {
            e1.printStackTrace();
            this.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + RW + ChatColor.RED +lang.getString("SaveFailure"));
        }
        try {
            SaveSql.getConnection().close();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    FileConfiguration reloadSetting(){
        return config = YamlConfiguration.loadConfiguration(configFile);
    }

    public FileConfiguration reloadSetting(File file){
        return YamlConfiguration.loadConfiguration(file);
    }
//    public FileConfiguration reloadSetting()
//    {
//        return config=Yaml.loadConfiguration(new File(getDataFolder(),"config.yml"));
//    }

//    public FileConfiguration load(File configFile){
//        if (!(configFile.exists())){
//            try {
//                configFile.createNewFile();
//                reloadConfig();
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//        }
//        return YamlConfiguration.loadConfiguration(configFile);
//    }

    public static void ShowHelp(CommandSender sender){
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "][----------[ReportWrong]-----------][");
        /*
        * Op's commands:list,check,deal,reload
        * */
        String index = "使用/reportwrong ? [n]查看第n页ReportWrong帮助！";
//        int count = 6 + 1;
        sender.sendMessage(ChatColor.GOLD + lang.getString("help.1"));
        sender.sendMessage(ChatColor.GREEN + lang.getString("help.2"));
        sender.sendMessage(ChatColor.GREEN + lang.getString("help.3"));
        sender.sendMessage(ChatColor.GREEN + lang.getString("help.4"));
        sender.sendMessage(ChatColor.GREEN + lang.getString("help.7"));
        sender.sendMessage(ChatColor.GREEN + lang.getString("help.8"));
//        for (String x : lang.getStringList("help")) {
//            sender.sendMessage(ChatColor.GREEN + x);
//        }
        if (sender.hasPermission("reportwrong.list")){
//            count ++;
            sender.sendMessage(ChatColor.GREEN + lang.getString("help.5"));
        }
        if (sender.hasPermission("reportwrong.check")){
//            count ++;
            sender.sendMessage(ChatColor.GREEN + lang.getString("help.6"));
        }
        if (sender.hasPermission("reportwrong.deal")){
//            count ++;
            sender.sendMessage(ChatColor.GREEN + lang.getString("help.7"));
        }
        if (sender.hasPermission("reportwrong.reload")){
//            count ++;
            sender.sendMessage(ChatColor.GREEN + lang.getString("help.9"));
        }
    }

    public FileConfiguration getMyConfig() {
        return config;
    }

//    public void saveDefaultConfig() {
//        if(!this.configFile.exists()) {
//            this.saveResource("config.yml", false);
//        }
//
//    }

    public void saveMoreConfig(String fileName){
        if (!this.langFile.exists()){
            this.saveResource(fileName,false);
        }
    }

    public static FileConfiguration getLang() {
        return lang;
    }

    //    public FileConfiguration getRWConfig() {
//        return config;
//    }
}
