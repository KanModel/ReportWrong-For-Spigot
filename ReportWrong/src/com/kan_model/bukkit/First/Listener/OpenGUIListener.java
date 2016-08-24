package com.kan_model.bukkit.First.Listener;

import com.kan_model.bukkit.First.Command;
import com.kan_model.bukkit.First.ReportWrong;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by kgdwhsk on 2016/8/19.
 */
public class OpenGUIListener implements Listener{

    ItemStack[] mainChest = null;
//    mainInv = Bukkit.createInventory((InventoryHolder) player, 9,ChatColor.RED + lang.getString("gui.main"));
    Inventory mainInv;
    static ItemStack rwItem;
    private FileConfiguration lang = ReportWrong.getLang();

    public OpenGUIListener(){
        mainChest = Command.getMainChest();
        rwItem = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta itemMeta = mainChest[0].getItemMeta();
        itemMeta.setDisplayName(ChatColor.AQUA + "举报之书");
        rwItem.setItemMeta(itemMeta);
    }

    @EventHandler
    public void openGui(PlayerInteractEvent event){
//        ENCHANTED_BOOK(403, 1),
        Player player = event.getPlayer();
//        event.getAction();
        if (event.getItem() == null){
            return;
        }
        if (event.getItem().isSimilar(rwItem)) {
            mainInv = Bukkit.createInventory((InventoryHolder) player, 9,ChatColor.RED + lang.getString("gui.main"));
            if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR) {
                for (ItemStack x : mainChest) {
                    mainInv.addItem(x);
                }
                player.closeInventory();
                player.openInventory(mainInv);
            }
        }
    }

    public static ItemStack getRwItem() {
        return rwItem;
    }
}
