package com.kan_model.bukkit.First.Listener;

import com.kan_model.bukkit.First.Command;
import com.kan_model.bukkit.First.ReportWrong;
import com.kan_model.bukkit.First.SQL.SaveSql;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * Created by kgdwhsk on 2016/8/15.
 */
public class GuiListener implements Listener{

    private Inventory confirmInv;
    private Inventory mainInv;
    private Player player;
    private ItemStack targetItem;

//    @EventHandler(priority = EventPriority.HIGHEST)
    @EventHandler
    public void onClickItem(InventoryClickEvent event) {
        if (event.getInventory().getTitle().equalsIgnoreCase(ChatColor.RED + "ReportWrong主界面")) {
            if (event.getWhoClicked() instanceof Player) {
                event.setCancelled(true);
                player = (Player) event.getWhoClicked();
                PlayerInventory inventory = player.getInventory(); // The player's inventory
                ItemStack itemstack = new ItemStack(Material.DIAMOND, 64); // A stack of diamonds
                targetItem = event.getCurrentItem();
                if (targetItem.isSimilar(Command.getMainChest()[0])) {
//                    player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.GOLD + "请输入你所要举报的内容或者不填");
                    player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.GOLD + "你选择了举报偷窃");
//                    player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.GOLD + "确定此处发生偷窃？选择木棍确定，选择指南针返回,选择书本提交详细信息！");
//                    String world = player.getWorld().getName();
//                    int x = player.getLocation().getBlockX();
//                    int y = player.getLocation().getBlockY();
//                    int z = player.getLocation().getBlockZ();
////                    player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.LIGHT_PURPLE + "成功举报世界 " + world + "坐标X:" + x + " Y:" + y + " Z:" + z + "处的举报！");
//                    if (SaveSql.addReport(player.getName(),world,x,y,z)) {
//                        player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.LIGHT_PURPLE + "成功举报世界 " + world + "坐标X:" + x + " Y:" + y + " Z:" + z + "处的举报！");
//                    }else {
//                        player.sendMessage(ChatColor.RED + ReportWrong.RW + "举报失败!");
//                    }
//                    player.closeInventory();
//                    mainInv = Bukkit.createInventory(player, 9,ChatColor.RED + "ReportWrong主界面");
                    confirmInv = Bukkit.createInventory(player, 9,ChatColor.ITALIC +  "ReportWrong确认界面");
                    confirmInv.addItem(Command.getConfirmChest()[0]);
                    confirmInv.addItem(Command.getConfirmChest()[1]);
                    confirmInv.setItem(8,Command.getConfirmChest()[2]);
                    player.openInventory(confirmInv);
                    player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.GOLD + "确定此处发生偷窃？选择木棍确定，选择指南针返回,选择书本提交详细信息！");

//                    player.openInventory(Command.getConfirmChest());
                } else if (targetItem.isSimilar(Command.getMainChest()[1])) {
                    player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.GOLD + "你选择了举报偷窃");
                    confirmInv = Bukkit.createInventory(player, 9,ChatColor.ITALIC +  "ReportWrong确认界面");
                    confirmInv.addItem(Command.getConfirmChest()[0]);
                    confirmInv.addItem(Command.getConfirmChest()[1]);
                    confirmInv.setItem(8,Command.getConfirmChest()[2]);
                    player.openInventory(confirmInv);
                    player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.GOLD + "确定此处发生破坏？选择木棍确定，选择指南针返回,选择书本提交详细信息！");
                } else if (targetItem.isSimilar(Command.getMainChest()[2])) {
                    player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.GOLD + "你选择了举报偷窃");
                    player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.GOLD + "发现服务器有Bug？选择书本提交详细信息，选择指南针返回！");
                    player.closeInventory();
                    confirmInv = Bukkit.createInventory(player, 9,ChatColor.ITALIC +  "ReportWrong确认界面");
//                    confirmInv.addItem(Command.getConfirmChest()[0]);
                    confirmInv.addItem(Command.getConfirmChest()[1]);
                    confirmInv.setItem(8,Command.getConfirmChest()[2]);
                    player.openInventory(confirmInv);;
//                    player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.GOLD + "请输入你所要举报的内容或者不填");
//                    inventory.addItem(Command.getMainChest()[2]);
                }
//                switch (id){
//                    case 2:
//                        player.sendMessage("Welcome! You seem to be reeeally poor, so we gave you some diamonds!");
//                        inventory.addItem(itemstack);
//                        break;
//                    case 1:
//                        player.chat("Stone 1");
//                        break;
//                }
            }
        }else if(event.getInventory().getTitle().equalsIgnoreCase(ChatColor.ITALIC + "ReportWrong确认界面")){
            if (event.getWhoClicked() instanceof Player){
                event.setCancelled(true);
                player = (Player) event.getWhoClicked();
                targetItem = event.getCurrentItem();
                if (targetItem.isSimilar(Command.getConfirmChest()[0])){
                    player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.GOLD + "确认举报当前位置");
                    player.closeInventory();
//                    player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.GOLD + "确定此处发生破坏？选择木棍确定，选择指南针返回,选择书本提交详细信息！");
                    String world = player.getWorld().getName();
                    int x = player.getLocation().getBlockX();
                    int y = player.getLocation().getBlockY();
                    int z = player.getLocation().getBlockZ();
                    if (SaveSql.addReport(player.getName(),world,x,y,z)) {
                        player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.LIGHT_PURPLE + "成功举报世界 " + world + "坐标X:" + x + " Y:" + y + " Z:" + z + "处的举报！");
                    }else {
                        player.sendMessage(ChatColor.RED + ReportWrong.RW + "举报失败!");
                    }
                    player.closeInventory();
                }else if (targetItem.isSimilar(Command.getConfirmChest()[1])){
                    player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.GOLD + "取消了此次举报!");
//                    player.closeInventory();
                    mainInv = Bukkit.createInventory(player, 9,ChatColor.RED + "ReportWrong主界面");
                    for (ItemStack x : Command.getMainChest()) {
                        mainInv.addItem(x);
                    }
                    player.openInventory(mainInv);
                }else if (targetItem.isSimilar(Command.getConfirmChest()[2])){
                    player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.GOLD + "请输入详细信息");
                    player.closeInventory();
                }
            }
        }

    }
}
