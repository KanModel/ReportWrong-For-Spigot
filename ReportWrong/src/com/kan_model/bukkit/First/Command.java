package com.kan_model.bukkit.First;

import com.kan_model.bukkit.First.Listener.OpenGUIListener;
import com.kan_model.bukkit.First.SQL.SaveSql;
import org.bukkit.*;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
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

    public static final int  THEFT = 1;
    public static final int DESTROY = 2;
    public static final int SBUG = 3;
    private ReportWrong main;
    private FileConfiguration co;
    private Inventory mainInv;
    private Inventory confirmInv;
    private Player player;
    private FileConfiguration lang = ReportWrong.getLang();

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
        itemMeta.setDisplayName(ChatColor.DARK_GREEN + lang.getString("report.theft"));
        mainChest[0].setItemMeta(itemMeta);
        ItemMeta itemMeta2 = mainChest[1].getItemMeta();
        itemMeta2.setDisplayName(ChatColor.AQUA + lang.getString("report.destroy"));
        mainChest[1].setItemMeta(itemMeta2);
        itemMeta.setDisplayName(ChatColor.GOLD + lang.getString("report.sbug"));
        mainChest[2].setItemMeta(itemMeta);
        this.co = main.getMyConfig();
        itemMeta.setDisplayName(ChatColor.YELLOW + lang.getString("report.confirm"));
        confirmChest[0].setItemMeta(itemMeta);
        itemMeta.setDisplayName(ChatColor.RED + lang.getString("report.deny"));
        confirmChest[1].setItemMeta(itemMeta);
        itemMeta.setDisplayName(ChatColor.RED + lang.getString("report.more"));
        List<String> lores = new ArrayList<String>();
//        lores.add("打开对话框");
//        lores.add("输入你举报的具体内容");
//        for (ItemStack x : mainChest) {
//            mainInv.addItem(x);
//        }
        for(String x : lang.getStringList("report.morelore")){
            lores.add(x);
        }
        itemMeta.setLore(lores);
        confirmChest[2].setItemMeta(itemMeta);
        itemMeta.setLore(null);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] args) {
//        player commands
        if (sender instanceof Player){
            player = (Player) sender;
            if (s.equalsIgnoreCase("rp") || s.equalsIgnoreCase("rw") || s.equalsIgnoreCase("reportwrong")){
                if (!(player.hasPermission("reportwrong.*"))) {
                    player.sendMessage(ChatColor.RED + ReportWrong.RW + lang.getString("permission.not") + "reportwrong.*");
                    return true;
                } else {
                    mainInv = Bukkit.createInventory((InventoryHolder) player, 9,ChatColor.RED + lang.getString("gui.main"));
                    confirmInv = Bukkit.createInventory(player, 9,ChatColor.ITALIC + lang.getString("gui.confirm"));
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
                        List(player,args);
                        /*if (args.length > 1 && isInt(args[1])){

                        }else if (args.length > 1 && !(isInt(args[1]))){
                            player.sendMessage(ChatColor.RED + ReportWrong.RW + "你输入[页数(数字)]!");
                        }*/
                        return true;
                    }else if (args.length > 0 && args[0].equalsIgnoreCase("version")){
                        player.sendMessage(ChatColor.GOLD + ReportWrong.RW+ "Version:" + main.getDescription().getVersion());
                        return true;
                    }else if (args.length > 0 && args[0].equalsIgnoreCase("reward")) {
                        Reward(player);
                        return true;
                    }else if (args.length > 0 && args[0].equalsIgnoreCase("reload")){
                        Reload(player);
                        return true;
                    }else if (args.length > 0 && args[0].equalsIgnoreCase("check")){
                        Check(player,args);
                        return true;
                    }else if (args.length > 0 && args[0].equalsIgnoreCase("deal")){
                        Deal(player,args);
                        return true;
                    }else if (args.length > 0 && args[0].equalsIgnoreCase("item")){
                        player.getInventory().addItem(OpenGUIListener.getRwItem());
                        return true;
                    } else {
                        ReportWrong.ShowHelp(sender);
                        return true;
                    }
                }
            }else{
                ReportWrong.ShowHelp(sender);
            }
//            console commands
        }else {
            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                Reload(sender);
                return true;
            }else if (args.length > 0 && args[0].equalsIgnoreCase("list")){
                List(sender,args);
                return true;
            }else if (args.length > 0 && args[0].equalsIgnoreCase("deal")){
                Deal(sender,args);
                return true;
            }else if (args.length > 0 && args[0].equalsIgnoreCase("reward")){
                Reward(sender);
                return true;
            }else if (args.length > 0 && args[0].equalsIgnoreCase("gui")){
                sender.sendMessage(ChatColor.GREEN + ReportWrong.RW + "§c" +lang.getString("consle"));
                return true;
            }else if (args.length > 0 && args[0].equalsIgnoreCase("item")){
                sender.sendMessage(ChatColor.GREEN + ReportWrong.RW + "§c" +lang.getString("consle"));
                return true;
            }else if (args.length > 0 && args[0].equalsIgnoreCase("check")){
                sender.sendMessage(ChatColor.GREEN + ReportWrong.RW + "§c" +lang.getString("consle"));
                return true;
            }else if (args.length > 0 && args[0].equalsIgnoreCase("version")){
                sender.sendMessage(ChatColor.GOLD + ReportWrong.RW+ "Version:" + main.getDescription().getVersion());
                return true;
            }else {
                ReportWrong.ShowHelp(sender);
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
//        sender.sendMessage(ReportWrong.RW + ChatColor.RED + "缺少参数" + name);
        try {
            config.save(new File(main.getDataFolder(),"config.yml"));
//            sender.sendMessage(ReportWrong.RW + ChatColor.GREEN + "成功添加参数" + name);
        } catch (IOException e) {
            e.printStackTrace();
//            sender.sendMessage(ReportWrong.RW + ChatColor.RED + "未完成添加参数" + name);
        }
    }

    private boolean isInt(Object args){
        try{
            Integer.parseInt((String)args);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public void List(CommandSender sender,String[] args){
        if (sender.hasPermission("reportwrong.list")) {
            /*try {
                if (SaveSql.getUndoneCount() == 0){
                    sender.sendMessage(ChatColor.GREEN + ReportWrong.RW + "§c没有举报!");
                }else {
                    sender.sendMessage(ChatColor.GOLD + "---------------------------" + ReportWrong.RW + "List " + SaveSql.getUndoneCount() + "---------------------------");
                    SaveSql.listReport(sender);
                }
            }catch (Exception e){
                e.printStackTrace();
                sender.sendMessage(ChatColor.RED + ReportWrong.RW + lang.getString("check.failure"));
            }*/
            if (args.length == 1 || (args.length >1 && isInt(args[1]) && Integer.parseInt(args[1]) == 1)) {
                listReport(SaveSql.listReport(), sender);
            }else if (args.length > 1 && !(isInt(args[1]))){
                player.sendMessage(ChatColor.RED + ReportWrong.RW + "请输入rw list [页数(数字)]!");
            }else if (args.length > 1 && isInt(args[1])){
                listReport(SaveSql.listReport(),sender, Integer.parseInt(args[1]));
            }
        }else {
            sender.sendMessage(ChatColor.RED + ReportWrong.RW + lang.getString("permission.not") + "reportwrong.list");
        }
    }

    public void Deal(CommandSender sender,String args[]){
        if (sender.hasPermission("reportwrong.deal")) {
            if (args.length == 1){
                sender.sendMessage(ChatColor.RED + ReportWrong.RW + "/reportwrong deal [举报id] [空 给予回报/c 关闭]解决举报");
                return;
            }
            if (args.length >= 2 && isInt(args[1])) {
                if (args.length >= 3 && args[2].equalsIgnoreCase("c")) {
                    try {
                        int id = Integer.parseInt(args[1]);
                        ResultSet rs = SaveSql.checkReport(id);
                        boolean success = SaveSql.setCompleted(rs, id);
//                                        player.sendMessage(ChatColor.RED + ReportWrong.RW + "c");
                        if (success) {
                            sender.sendMessage(ChatColor.GREEN + ReportWrong.RW + lang.getString("check.deal") + "[" + id + "]");
                        }else {
                            sender.sendMessage(ChatColor.RED + ReportWrong.RW + lang.getString("check.failure"));
//                                            player.sendMessage(ChatColor.RED + ReportWrong.RW + "ccccc");
                        }
                    } catch (SQLException e) {
                        sender.sendMessage(ChatColor.RED + ReportWrong.RW + lang.getString("check.failure"));
                        e.printStackTrace();
                    }
                }else {
//                                    player.sendMessage(ChatColor.RED + ReportWrong.RW + "bushic");
                    try {
                        int id = Integer.parseInt(args[1]);
                        ResultSet rs = SaveSql.checkReport(id);
//                                        SaveSql.setCompleted(rs, id);
                        boolean success = SaveSql.setCompleted(rs, id);
                        if (success) {
                            sender.sendMessage(ChatColor.GREEN + ReportWrong.RW + lang.getString("check.deal") + "[" + id + "]");
                            rs = SaveSql.checkReport(id);
                            Player rewardPlayer = Bukkit.getPlayer(rs.getString("name"));
                            if (rewardPlayer != null) {
//                                                if (isInt((int)co.get("RewardItem"))) {
////                                                    rewardPlayer.getInventory().addItem(
////                                                            new ItemStack(Material.getMaterial(co.getInt("RewardItem")),co.getInt("RewardCount")));
////                                                    player.sendMessage(ChatColor.GREEN + ReportWrong.RW + lang.getString("check.reward"));
//                                                } else {
                                String item = co.getString("RewardItem").toUpperCase();
                                int count = co.getInt("RewardCount");
//                                                    rewardPlayer.getInventory().addItem(
//                                                            new ItemStack(Material.getMaterial(item),co.getInt("RewardCount")));
//                                                    player.sendMessage(ChatColor.GREEN + ReportWrong.RW + lang.getString("check.reward"));
                                if (count != 0) {
                                    try {
                                        ItemStack rewardItem = new ItemStack(Material.getMaterial(item), co.getInt("RewardCount"));
                                        rewardPlayer.getWorld().dropItemNaturally(rewardPlayer.getLocation(), rewardItem);
                                        rewardPlayer.sendMessage(ChatColor.GREEN + ReportWrong.RW + "你被给予了回报！");
                                    } catch (Exception e) {
                                        sender.sendMessage(ChatColor.RED + ReportWrong.RW + "错误物品名称:" + item);
                                    }
                                }else {
                                    rewardPlayer.sendMessage(ChatColor.RED + ReportWrong.RW + "你的举报被解决不带回报");
                                }
//                                                }
                            } else {
                                                /*玩家不在线存入数据库*/
                                SaveSql.recordReward(rs.getString("name"),co.getString("RewardItem").toUpperCase(),co.getInt("RewardCount"));
                                sender.sendMessage(ChatColor.RED + ReportWrong.RW + "玩家不在线，当玩家上线是给予回报！");
//                                                player.sendMessage(ChatColor.RED + ReportWrong.RW + "rewardPlayer != null");
                            }
                        }else {
                            sender.sendMessage(ChatColor.RED + ReportWrong.RW + "已经解决或不存在ID");
                        }
                    } catch (SQLException e) {
                        sender.sendMessage(ChatColor.RED + ReportWrong.RW + lang.getString("check.failure"));
                        e.printStackTrace();
                    }
                }
            }else {
                sender.sendMessage(ChatColor.RED + ReportWrong.RW + "请填写真确数字ID！");
            }
        }else {
            sender.sendMessage(ChatColor.RED + ReportWrong.RW + lang.getString("permission.not") + "reportwrong.deal");
        }
    }

    public void Reload(CommandSender sender){
        if (sender.hasPermission("reportwrong.reload")) {
            main.reloadSetting();
            co = main.getMyConfig();
            sender.sendMessage(ChatColor.GREEN + ReportWrong.RW + lang.getString("reload"));
        }else {
            sender.sendMessage(ChatColor.RED + ReportWrong.RW + lang.getString("permission.not") + "reportwrong.reload");
        }
    }

    public void Reward(CommandSender sender){
        //                        co = main.getMyConfig();
        if (co.contains("RewardDefault") && co.contains("RewardItem") && co.contains("RewardCount")) {
//            sender.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.WHITE + lang.getString("reward.switch") + co.getBoolean("RewardDefault"));
            sender.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.WHITE + lang.getString("reward.item") + (co.getString("RewardItem")).toString().toUpperCase());
            sender.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.WHITE + lang.getString("reward.count") + co.getInt("RewardCount"));
            sender.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.WHITE + lang.getString("reward.cd") + co.getInt("ReportCD"));
//                            RewardDefault: true
//                            RewardItem: Diamond
//                            RewardCount: 1
        } else {
//                            if (!(co.contains("RewardDefault"))) {
//                                addArgs(player,co,"RewardDefault",true);
//                            }
            if (!(co.contains("RewardItem"))) {
                addArgs(sender,co,"RewardItem","Diamond");
            }
            if (!(co.contains("RewardCount"))) {
                addArgs(sender,co,"RewardCount",1);
            }
            if (!(co.contains("ReportCD"))) {
                addArgs(sender,co,"ReportCD",600);
            }
//                            player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.WHITE + lang.getString("reward.switch") + co.getBoolean("RewardDefault"));
            sender.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.WHITE + lang.getString("reward.item") + (co.getString("RewardItem")).toString().toUpperCase());
            sender.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.WHITE + lang.getString("reward.count") + co.getInt("RewardCount"));
            sender.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.WHITE + lang.getString("reward.cd") + co.getInt("ReportCD"));
        }
    }

    public void Check(CommandSender sender,String args[]){
        //                        Integer.parseInt([String])
        if (player.hasPermission("reportwrong.check")) {
            if (args.length >= 2 && isInt(args[1])) {
                try {
                    ResultSet rs = SaveSql.checkReport(Integer.parseInt(args[1]));
                    boolean more = false;
                    String type = new String();
                    switch (rs.getInt("Rtype")){
                        case THEFT:
                            type = "偷窃";
                            break;
                        case DESTROY:
                            type = "破坏";
                            break;
                        case SBUG:
                            type = "举报Bug";
                            break;
                    }
                    if (!(rs.getString("playerword").equalsIgnoreCase("None"))){
                        more = true;
                        player.sendMessage(ChatColor.GREEN + ReportWrong.RW + lang.getString("check.check") + "由[" + rs.getString("name") + "]举报的 [" + type + "]id:[" + rs.getInt("id") + "]");
                        player.sendMessage(ChatColor.GREEN + ReportWrong.RW + "详情如下:" + ChatColor.WHITE + rs.getString("playerword"));
                    }else {
                        player.sendMessage(ChatColor.GREEN + ReportWrong.RW + lang.getString("check.check") + "id:[" + rs.getInt("id") + "]不带详情");
                    }
                    player.teleport(new Location(Bukkit.getWorld(rs.getString("world")), rs.getInt("x"), rs.getInt("y"), rs.getInt("z")));
                } catch (SQLException e) {
                    player.sendMessage(ChatColor.RED + ReportWrong.RW + lang.getString("check.failure"));
//                    return true;
//                                    e.printStackTrace();
                }
            } else {
                sender.sendMessage(ReportWrong.RW + ChatColor.RED + lang.getString("check.id"));
//                return true;
            }
//            return true;
        }else {
            player.sendMessage(ChatColor.RED + ReportWrong.RW + lang.getString("permission.not") + "reportwrong.check");
//            return true;
        }
    }

    public void listReport(ArrayList<String> list,CommandSender sender){
        /*
        * 10line
        * 1 title
        * 8 content
        * 1 page info
        * */
        int count = list.size();
        int page = pageCount(count);
        if (count == 0){
            sender.sendMessage(ChatColor.GREEN + ReportWrong.RW + "§c没有举报!");
        }else {
            sender.sendMessage(ChatColor.GOLD + "---------------------------" + ReportWrong.RW + "List " + count + "---------------------------");
            if (count > 9){
//                int page = count/8;
                for (int i = 0;i < 8;i++){
                    sender.sendMessage(list.get(i));
                }
                sender.sendMessage(ChatColor.GREEN + "这是第1页共" + page + "页.");
            }else {
                for (String s:list){
                    sender.sendMessage(s);
                }
            }
        }
//        sender.sendMessage(String.valueOf(count));
    }

    public void listReport(ArrayList<String> list,CommandSender sender,int p){
        /*
        * 10line
        * 1 title
        * 8 content
        * 1 page info
        * */
        int count = list.size();
        int page = pageCount(count);
        if (p > page){
            sender.sendMessage(ChatColor.RED + "请输入正确页数!");
        }else {
            if (count == 0) {
                sender.sendMessage(ChatColor.GREEN + ReportWrong.RW + "§c没有举报!");
            } else {
                sender.sendMessage(ChatColor.GOLD + "---------------------------" + ReportWrong.RW + "List " + count + "---------------------------");
                if (count > 9) {
//                    int page = count / 8;
//                    int page = (int) Math.ceil(count / 8);
                    if (p * 8 < list.size()) {
                        for (int i = (p - 1) * 8; i < p * 8; i++) {
                            sender.sendMessage(list.get(i));
                        }
                    } else {
                        for (int i = p * 8 - 8; i < list.size(); i++) {
                            sender.sendMessage(list.get(i));
                        }
                    }
                    sender.sendMessage(ChatColor.GREEN + "这是第" + p + "页共" + page + "页.");
                } else {
                    for (String s : list) {
                        sender.sendMessage(s);
                    }
                }
            }
        }
//        sender.sendMessage(String.valueOf(count));
    }

    /*public void listReportS(String[] list,CommandSender sender,int p){
        *//*
        * 10line
        * 1 title
        * 8 content
        * 1 page info
        * *//*
        int count = 0;
        try {
            count = SaveSql.getUndoneCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int page = pageCount(count);
        if (p > page){
            sender.sendMessage(ChatColor.RED + "请输入正确页数!");
        }else {
            if (count == 0) {
                sender.sendMessage(ChatColor.GREEN + ReportWrong.RW + "§c没有举报!");
            } else {
                sender.sendMessage(ChatColor.GOLD + "---------------------------" + ReportWrong.RW + "List " + count + "---------------------------");
                if (count > 9) {
//                    int page = count / 8;
//                    int page = (int) Math.ceil(count / 8);
                    if (p * 8 < count) {
                        for (int i = 0; i < 8; i++) {
                            sender.sendMessage(list[i]);
                        }
                    } else {
                        for (int i = p * 8 - 8; i < list.size(); i++) {
                            sender.sendMessage(list[i]);
                        }
                    }
                    sender.sendMessage(ChatColor.GREEN + "这是第" + p + "页共" + page + "页.");
                } else {
                    for (String s : list) {
                        sender.sendMessage(s);
                    }
                }
            }
        }
//        sender.sendMessage(String.valueOf(count));
    }*/

    private int pageCount(int count){
        int page = count/8;
        if (count%8 >0){
            page++;
        }
        return page;
    }
}
