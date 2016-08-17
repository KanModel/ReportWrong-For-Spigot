package com.kan_model.bukkit.First;

import com.kan_model.bukkit.First.SQL.SaveSql;
import org.bukkit.*;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kgdwhsk on 2016/8/15.
 */
public class Command implements CommandExecutor{

    private ReportWrong main;
    private FileConfiguration co;
    private Inventory mainInv;
    private Inventory confirmInv;
    private Player player;

    private static ItemStack[] mainChest = new ItemStack[]{
            new ItemStack(Material.STONE, 1),
            new ItemStack(Material.GRASS, 1),
            new ItemStack(Material.DIAMOND, 1),
    };
    private static ItemStack[] confirmChest = new ItemStack[]{
            new ItemStack(Material.STICK, 1),
            new ItemStack(Material.COMPASS, 1),
            new ItemStack(Material.BOOK, 1),
    };

    Command(ReportWrong main){
        this.main = main;
        ItemMeta itemMeta = mainChest[0].getItemMeta();
        itemMeta.setDisplayName(ChatColor.DARK_GREEN + "举报偷窃");
        mainChest[0].setItemMeta(itemMeta);
        ItemMeta itemMeta2 = mainChest[1].getItemMeta();
        itemMeta2.setDisplayName(ChatColor.AQUA + "举报破坏");
        mainChest[1].setItemMeta(itemMeta2);
        itemMeta.setDisplayName(ChatColor.GOLD + "举报服务器bug");
        mainChest[2].setItemMeta(itemMeta);
        this.co = main.getMyConfig();
        itemMeta.setDisplayName(ChatColor.YELLOW + "确认");
        confirmChest[0].setItemMeta(itemMeta);
        itemMeta.setDisplayName(ChatColor.RED + "取消");
        confirmChest[1].setItemMeta(itemMeta);
        itemMeta.setDisplayName(ChatColor.RED + "详情汇报");
        List<String> lores = new ArrayList<String>();
        lores.add("打开对话框");
        lores.add("输入你举报的具体内容");
        itemMeta.setLore(lores);
        confirmChest[2].setItemMeta(itemMeta);
        itemMeta.setLore(null);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
        if (sender instanceof Player){
            player = (Player) sender;
            if (s.equalsIgnoreCase("rp") || s.equalsIgnoreCase("rw") || s.equalsIgnoreCase("reportwrong")){
                if (!(player.hasPermission("reportwrong.reportwrong"))) {
                    player.sendMessage(ChatColor.RED + ReportWrong.RW + "你没权限reportwrong.*哦=。=");
                    return true;
                } else {
                    mainInv = Bukkit.createInventory(player, 9,ChatColor.RED + "ReportWrong主界面");
                    confirmInv = Bukkit.createInventory(player, 9,ChatColor.ITALIC + "ReportWrong确认界面");

                    if (args.length == 0 ||(args.length > 0 && args[0].equalsIgnoreCase("?"))) {
                        ReportWrong.ShowHelp(sender);
                        return true;
                    } else if (args.length > 0 && args[0].equalsIgnoreCase("gui")) {
                        for (ItemStack x : mainChest) {
                            mainInv.addItem(x);
                        }
                        player.closeInventory();
                        player.openInventory(mainInv);
                        return true;
                    }else if (args.length > 0 && args[0].equalsIgnoreCase("list")){
                        if (player.hasPermission("reportwrong.reportwrong.list")) {
//                            Location
//                            World
                            try {
                                SaveSql.listReport(player);
                            }catch (Exception e){
                                player.sendMessage(ChatColor.RED + ReportWrong.RW + "获取失败!");
                            }
                        }else {
                            player.sendMessage(ChatColor.RED + ReportWrong.RW + "你没权限reportwrong.reportwrong.list哦=。=");
                        }
                        return true;
                    }else if (args.length > 0 && args[0].equalsIgnoreCase("version")){
                        player.sendMessage(ReportWrong.RW+ "版本信息：" + main.getDescription().getVersion());
                        return true;
                    }else if (args.length > 0 && args[0].equalsIgnoreCase("reward")) {
//                        co = main.getMyConfig();
                        if (co.contains("RewardDefault") && co.contains("RewardItem") && co.contains("RewardCount")) {
                            player.sendMessage(ReportWrong.RW + "是否开启举报回报：" + main.getMyConfig().getString("RewardDefault"));
                            player.sendMessage(ReportWrong.RW + "举报回报物品：" + main.getMyConfig().getString("RewardItem"));
                            player.sendMessage(ReportWrong.RW + "回报数量" + main.getMyConfig().getInt("RewardCount"));
                            return true;
//                            RewardDefault: true
//                            RewardItem: Diamond
//                            RewardCount: 1
                        } else {
                            if (!(co.contains("RewardDefault"))) {
//                                player.sendMessage(ReportWrong.RW + "缺少参数RewardDefault");
//                                co.set("RewardDefault", "true");
                                addArgs(player,co,"RewardDefault",true);
                            }
                            if (!(co.contains("RewardItem"))) {
                                addArgs(player,co,"RewardItem","Diamond");
                            }
                            if (!(co.contains("RewardCount"))) {
                                addArgs(player,co,"RewardCount",1);
                            }
//                            File file = new File(getDataFolder(),"config.yml");
//                            try{getMyConfig().save(file);}
//                            catch
//                                    (IOException e){e.printStackTrace();}
//                            File file = new File(main.getDataFolder(), "config.yml");
//                            try {
//                                main.getMyConfig().save(file);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
                            player.sendMessage(ReportWrong.RW + "是否开启举报回报：" + co.getBoolean("RewardDefault"));
                            player.sendMessage(ReportWrong.RW + "举报回报物品：" + co.getString("RewardItem"));
                            player.sendMessage(ReportWrong.RW + "回报数量" + co.getInt("RewardCount"));
                            return true;
                        }
                    }else if (args.length > 0 && args[0].equalsIgnoreCase("reload")){
                        main.reloadSetting();
                        co = main.getMyConfig();
                        player.sendMessage(ReportWrong.RW + "成功重载配置");
                        return true;
                    }else if (args.length > 0 && args[0].equalsIgnoreCase("check")){
//                        Integer.parseInt([String])
                        if (args.length >= 2 && isInt(args[1])){
                            try {
                                ResultSet rs = SaveSql.checkReport(Integer.parseInt(args[1]));
                                player.teleport(new Location(Bukkit.getWorld(rs.getString("world")),rs.getInt("x"),rs.getInt("y"),rs.getInt("z")));
                                player.sendMessage(ChatColor.GREEN + ReportWrong.RW + "进行检查id:[" + rs.getInt("id") + "]的举报");
                            } catch (SQLException e) {
                                player.sendMessage(ChatColor.RED + ReportWrong.RW + "检查失败！");
                                e.printStackTrace();
                            }
                        }else {
                            sender.sendMessage(ReportWrong.RW + "请输入举报id");
                        }
                        return true;
                    }else {
                        ReportWrong.ShowHelp(sender);
                        return true;
                    }
                }
            }else{
                ReportWrong.ShowHelp(sender);
            }
        }else {
            if (args.length > 0 && args[0].equalsIgnoreCase("reload")){
                main.reloadSetting();
                sender.sendMessage(ReportWrong.RW + "成功重载配置");
                return true;
            }else {
                ReportWrong.ShowHelp(sender);
                sender.sendMessage(ReportWrong.RW + "控制台不能使用此指令！");
                return true;
            }
        }
        return false;
    }

    public static ItemStack[] getMainChest() {
        return mainChest;
    }

    public static ItemStack[] getConfirmChest() {
        return confirmChest;
    }

    public Inventory getConfirmInv() {
        return confirmInv;
    }

    public void addArgs(CommandSender sender,FileConfiguration config,String name,Object value){
//        co.set("RewardCount", "1");
//        player.sendMessage(ReportWrong.RW + ChatColor.RED + "缺少参数RewardCount");
//        try {
//            co.save(new File(main.getDataFolder(),"config.yml"));
//            player.sendMessage(ReportWrong.RW + ChatColor.GREEN + "成功添加参数RewardCount");
//        } catch (IOException e) {
//            e.printStackTrace();
//            player.sendMessage(ReportWrong.RW + ChatColor.RED + "添加参数RewardCount失败");
//        }
        config.set(name,value);
        sender.sendMessage(ReportWrong.RW + ChatColor.RED + "缺少参数" + name);
        try {
            config.save(new File(main.getDataFolder(),"config.yml"));
            sender.sendMessage(ReportWrong.RW + ChatColor.GREEN + "成功添加参数" + name);
        } catch (IOException e) {
            e.printStackTrace();
            sender.sendMessage(ReportWrong.RW + ChatColor.RED + "添加参数" + name + "失败");
        }
    }

    private boolean isInt(String args){
        try{
            Integer.parseInt(args);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
