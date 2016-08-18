package com.kan_model.bukkit.First.Listener;

import com.kan_model.bukkit.First.Command;
import com.kan_model.bukkit.First.ReportWrong;
import com.kan_model.bukkit.First.SQL.SaveSql;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
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
    private static final int  THEFT = 1;
    private static final int DESTROY = 2;
    private static final int SBUG = 3;
    private int Type = 0;
    private FileConfiguration lang = ReportWrong.getLang();

//    @EventHandler(priority = EventPriority.HIGHEST)
    @EventHandler
    public void onClickItem(InventoryClickEvent event) {
//        event.getClick().isRightClick();
        if (event.getRawSlot() == -999){
            return;
        }
        if (event.getClick().isRightClick() || event.getClick().isLeftClick()) {
            if (event.getInventory().getTitle().equalsIgnoreCase(ChatColor.RED + lang.getString("gui.main"))) {
                if (event.getWhoClicked() instanceof Player) {
                    event.setCancelled(true);
                    player = (Player) event.getWhoClicked();
                    PlayerInventory inventory = player.getInventory(); // The player's inventory
                    ItemStack itemstack = new ItemStack(Material.DIAMOND, 64); // A stack of diamonds
                    targetItem = event.getCurrentItem();
//                    if(e.getRawSlot()==-999)
//                    {
//                        return;
//                    }
                    if (targetItem.isSimilar(Command.getMainChest()[0])) {
//                    player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.GOLD + "请输入你所要举报的内容或者不填");
                        player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.GOLD + lang.getString("gui.m.theft"));
                        confirmInv = Bukkit.createInventory(player, 9, ChatColor.ITALIC + lang.getString("gui.confirm"));
                        confirmInv.addItem(Command.getConfirmChest()[0]);
                        confirmInv.addItem(Command.getConfirmChest()[1]);
                        confirmInv.setItem(8, Command.getConfirmChest()[2]);
                        player.openInventory(confirmInv);
                        player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.GOLD + lang.getString("gui.m.confirm.theft"));
                        Type = DESTROY;
//                    player.openInventory(Command.getConfirmChest());
                    } else if (targetItem.isSimilar(Command.getMainChest()[1])) {
                        player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.GOLD + lang.getString("gui.m.destroy"));
                        confirmInv = Bukkit.createInventory(player, 9, ChatColor.ITALIC + lang.getString("gui.confirm"));
                        confirmInv.addItem(Command.getConfirmChest()[0]);
                        confirmInv.addItem(Command.getConfirmChest()[1]);
                        confirmInv.setItem(8, Command.getConfirmChest()[2]);
                        player.openInventory(confirmInv);
                        player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.GOLD + lang.getString("gui.m.confirm.destroy"));
                        Type = THEFT;
                    } else if (targetItem.isSimilar(Command.getMainChest()[2])) {
                        player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.GOLD + lang.getString("gui.m.sbug"));
                        player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.GOLD + lang.getString("gui.m.confirm.sbug"));
                        player.closeInventory();
                        confirmInv = Bukkit.createInventory(player, 9, ChatColor.ITALIC + lang.getString("gui.confirm"));
//                    confirmInv.addItem(Command.getConfirmChest()[0]);
                        confirmInv.addItem(Command.getConfirmChest()[1]);
                        confirmInv.setItem(8, Command.getConfirmChest()[2]);
                        player.openInventory(confirmInv);
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
            } else if (event.getInventory().getTitle().equalsIgnoreCase(ChatColor.ITALIC + lang.getString("gui.confirm"))) {
                if (event.getWhoClicked() instanceof Player) {
                    event.setCancelled(true);
                    player = (Player) event.getWhoClicked();
                    targetItem = event.getCurrentItem();
                    if (targetItem.isSimilar(Command.getConfirmChest()[0])) {
//                        player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.GOLD + "确认举报当前位置");
                        player.closeInventory();
//                    player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.GOLD + "确定此处发生破坏？选择木棍确定，选择指南针返回,选择书本提交详细信息！");
                        String world = player.getWorld().getName();
                        int x = player.getLocation().getBlockX();
                        int y = player.getLocation().getBlockY();
                        int z = player.getLocation().getBlockZ();
                        if (SaveSql.addReport(player.getName(), world, x, y, z,Type)) {
                            switch (Type) {
                                case THEFT:
                                    player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.LIGHT_PURPLE + lang.getString("gui.c.confirm.theft")
                                            + world + " [X]:" + x + " [Y]:" + y + " [Z]:" + z);
                                    break;
                                case DESTROY:
                                    player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.LIGHT_PURPLE + lang.getString("gui.c.confirm.destroy")
                                            + world + " [X]:" + x + " [Y]:" + y + " [Z]:" + z);
                                    break;
                                case SBUG:
                                    player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.LIGHT_PURPLE + lang.getString("gui.c.confirm.sbug"));
                                    break;
                            }
                        } else {
                            player.sendMessage(ChatColor.RED + ReportWrong.RW + lang.getString("gui.c.confirm.failure"));
                        }
                        player.closeInventory();
                    } else if (targetItem.isSimilar(Command.getConfirmChest()[1])) {
                        player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.GOLD + lang.getString("gui.c.deny"));
//                    player.closeInventory();
                        mainInv = Bukkit.createInventory(player, 9, ChatColor.RED + lang.getString("gui.main"));
                        for (ItemStack x : Command.getMainChest()) {
                            mainInv.addItem(x);
                        }
                        player.openInventory(mainInv);
                    } else if (targetItem.isSimilar(Command.getConfirmChest()[2])) {
                        player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.GOLD + lang.getString("gui.c.more"));
                        player.closeInventory();
                    }
                }
            }
        }
    }
}
