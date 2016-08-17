package com.kan_model.bukkit.First;

import com.kan_model.bukkit.First.Listener.GuiListener;
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
 */
public class ReportWrong extends JavaPlugin {

//    private DatabaseMetaData sql;
    public static final String RW = "[ReportWrong]";
    private File configFile;
    private File langFile;
    private FileConfiguration config;
    private static FileConfiguration lang;
    private SaveSql sql;

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
        this.getServer().getPluginManager().registerEvents(new GuiListener(),this);
        sql = new SaveSql(this,config);
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

    public FileConfiguration reloadSetting(){
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
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "----------[ReportWrong]-----------");
        sender.sendMessage(ChatColor.GOLD + lang.getString("help.1"));
        sender.sendMessage(ChatColor.GREEN + lang.getString("help.2"));
        sender.sendMessage(ChatColor.GREEN + lang.getString("help.3"));
        sender.sendMessage(ChatColor.GREEN + lang.getString("help.4"));
        sender.sendMessage(ChatColor.GREEN + lang.getString("help.5"));
        sender.sendMessage(ChatColor.GREEN + lang.getString("help.6"));
        sender.sendMessage(ChatColor.GREEN + lang.getString("help.7"));
        sender.sendMessage(ChatColor.GREEN + lang.getString("help.8"));
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
