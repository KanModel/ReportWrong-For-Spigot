package com.kan_model.bukkit.First.Listener;

import com.kan_model.bukkit.First.Command;
import com.kan_model.bukkit.First.ReportWrong;
import com.kan_model.bukkit.First.SQL.SaveSql;
import com.kan_model.bukkit.First.util.Type;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by kgdwhsk on 2016/8/15.
 */
public class GuiListener implements Listener{

    private Inventory confirmInv;
    private Inventory mainInv;
    private Player player;
    private ItemStack targetItem;
    public static final int  THEFT = 1;
    public static final int DESTROY = 2;
    public static final int SBUG = 3;
    private int Type = 0;
    private FileConfiguration lang = ReportWrong.getLang();
    private static Type untiType;
//    private LinkedList<CoolDown> list;
//    private ListIterator<CoolDown> listIterator;
    private ReportWrong main;
    private static final HashMap<Player, Long> cooldown = new HashMap();

    public GuiListener(ReportWrong main){
//        list = new LinkedList<CoolDown>();
//        listIterator = list.listIterator();
        this.main = main;
    }

    //    @EventHandler(priority = EventPriority.HIGHEST)
    @EventHandler
    public void onClickItem(InventoryClickEvent event) {
        if (event.getRawSlot() == -999){
            return;
        }
        if (event.getClick().isRightClick() || event.getClick().isLeftClick()) {
            if (event.getInventory().getTitle().equalsIgnoreCase(ChatColor.RED + lang.getString("gui.main"))) {
                if (event.getWhoClicked() instanceof Player) {
                    event.setCancelled(true);
                    player = (Player) event.getWhoClicked();
                    targetItem = event.getCurrentItem();
                    if (targetItem.isSimilar(Command.getMainChest()[0])) {
                        player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.GOLD + lang.getString("gui.m.theft"));
                        confirmInv = Bukkit.createInventory(player, 9, ChatColor.ITALIC + lang.getString("gui.confirm"));
                        confirmInv.addItem(Command.getConfirmChest()[0]);
                        confirmInv.addItem(Command.getConfirmChest()[1]);
                        confirmInv.setItem(8, Command.getConfirmChest()[2]);
                        player.openInventory(confirmInv);
                        player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.GOLD + lang.getString("gui.m.confirm.theft"));
                        untiType = new Type(THEFT,player);
                    } else if (targetItem.isSimilar(Command.getMainChest()[1])) {
                        player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.GOLD + lang.getString("gui.m.destroy"));
                        confirmInv = Bukkit.createInventory(player, 9, ChatColor.ITALIC + lang.getString("gui.confirm"));
                        confirmInv.addItem(Command.getConfirmChest()[0]);
                        confirmInv.addItem(Command.getConfirmChest()[1]);
                        confirmInv.setItem(8, Command.getConfirmChest()[2]);
                        player.openInventory(confirmInv);
                        player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.GOLD + lang.getString("gui.m.confirm.destroy"));
                        untiType = new Type(DESTROY,player);
                    } else if (targetItem.isSimilar(Command.getMainChest()[2])) {
                        player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.GOLD + lang.getString("gui.m.sbug"));
                        player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.GOLD + lang.getString("gui.m.confirm.sbug"));
                        player.closeInventory();
                        confirmInv = Bukkit.createInventory(player, 9, ChatColor.ITALIC + lang.getString("gui.confirm"));
                        confirmInv.addItem(Command.getConfirmChest()[1]);
                        confirmInv.setItem(8, Command.getConfirmChest()[2]);
                        player.openInventory(confirmInv);
                        untiType = new Type(SBUG,player);
                    }
                }
            } else if (event.getInventory().getTitle().equalsIgnoreCase(ChatColor.ITALIC + lang.getString("gui.confirm"))) {
                try {

                    if (event.getWhoClicked() instanceof Player) {
                        event.setCancelled(true);
                        player = (Player) event.getWhoClicked();
                        targetItem = event.getCurrentItem();
                        if (targetItem.isSimilar(Command.getConfirmChest()[0])) {
                            if (this.cooldown.containsKey(player)) {
//                                int time = (int)(((Long)this.cooldown.get(player)).longValue()/1000);
                                if(System.currentTimeMillis() - ((Long)this.cooldown.get(player)).longValue() > 1000L * main.getMyConfig().getInt("ReportCD")) {
                                    this.cooldown.remove(player);
                                    if (untiType.getPlayer().getName().equalsIgnoreCase(player.getName())) {
                                        addReport(player);
                                    } else {
                                        player.sendMessage(ChatColor.RED + ReportWrong.RW + lang.getString("gui.c.confirm.failure"));
                                    }
                                    player.closeInventory();
                                }else if(!player.isOp()) {
                                    player.sendMessage(ChatColor.RED + ReportWrong.RW + "你刚刚举报过，等等再举报吧!");
                                    event.setCancelled(true);
                                }
                            } else if(!player.isOp()) {
                                this.cooldown.put(player, Long.valueOf(System.currentTimeMillis()));
                                if (untiType.getPlayer().getName().equalsIgnoreCase(player.getName())) {
                                    addReport(player);
                                } else {
                                    player.sendMessage(ChatColor.RED + ReportWrong.RW + lang.getString("gui.c.confirm.failure"));
                                }
                                player.closeInventory();
                            }
//                            CoolDown ls;
//                            listIterator = list.listIterator();
//                            while (listIterator.hasNext()) {
//                                ls = listIterator.previous();
//                                if (player.getName().equalsIgnoreCase(ls.getPlayerName()) && ls.isCoolDownNow()) {
//                                    player.closeInventory();
//                                    player.sendMessage(ChatColor.RED + ReportWrong.RW + "你刚刚举报过，等等举报吧！");
//                                } else if (player.getName().equalsIgnoreCase(ls.getPlayerName()) && !(ls.isCoolDownNow())) {
////                                listIterator.add(cd);
//                                    listIterator.remove();
//                                    if (untiType.getPlayer().getName().equalsIgnoreCase(player.getName())) {
//                                        addReport(player);
//                                    } else {
//                                        player.sendMessage(ChatColor.RED + ReportWrong.RW + lang.getString("gui.c.confirm.failure"));
//                                    }
//                                } else {
//                                    listIterator.add(cd);
//                                    listIterator = list.listIterator();
//                                    if (untiType.getPlayer().getName().equalsIgnoreCase(player.getName())) {
//                                        addReport(player);
//                                    } else {
//                                        player.sendMessage(ChatColor.RED + ReportWrong.RW + lang.getString("gui.c.confirm.failure"));
//                                    }
//                                }
//                            }
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
                        /*
                        * 需要延时
                        * */
                            player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.GOLD + lang.getString("gui.c.more"));
                            player.closeInventory();
//                        String world = player.getWorld().getName();
//                        int x = player.getLocation().getBlockX();
//                        int y = player.getLocation().getBlockY();
//                        int z = player.getLocation().getBlockZ();
                            if (untiType.getPlayer().getName().equalsIgnoreCase(player.getName())) {
//                            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//                            switch (untiType.getType()) {
//                                case THEFT:
//                                    player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.LIGHT_PURPLE + time
//                                            + lang.getString("gui.c.confirm.theft")
//                                            + world + " [X]:" + x + " [Y]:" + y + " [Z]:" + z);
//                                    if (!(SaveSql.addReport(player.getName(), world, x, y, z, untiType.getType(), time,untiType.getMoreInformation()))){
//                                        player.sendMessage(ChatColor.RED + ReportWrong.RW + lang.getString("gui.c.confirm.failure"));
//                                    }else {
//                                        player.sendMessage(ChatColor.GREEN + ReportWrong.RW + "举报详情如下:" + ChatColor.WHITE + untiType.getMoreInformation());
//                                    }
//                                    break;
//                                case DESTROY:
//                                    player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.LIGHT_PURPLE + time
//                                            + lang.getString("gui.c.confirm.destroy")
//                                            + world + " [X]:" + x + " [Y]:" + y + " [Z]:" + z);
//                                    if (!(SaveSql.addReport(player.getName(), world, x, y, z, untiType.getType(), time,untiType.getMoreInformation()))){
//                                        player.sendMessage(ChatColor.RED + ReportWrong.RW + lang.getString("gui.c.confirm.failure"));
//                                    }else {
//                                        player.sendMessage(ChatColor.GREEN + ReportWrong.RW + "举报详情如下:" + ChatColor.WHITE + untiType.getMoreInformation());
//                                    }
//                                    break;
//                                case SBUG:
//                                    player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.LIGHT_PURPLE + time
//                                            + lang.getString("gui.c.confirm.sbug"));
//                                    if (!(SaveSql.addReport(player.getName(), world, x, y, z, untiType.getType(), time,untiType.getMoreInformation()))){
//                                        player.sendMessage(ChatColor.RED + ReportWrong.RW + lang.getString("gui.c.confirm.failure"));
//                                    }else {
//                                        player.sendMessage(ChatColor.GREEN + ReportWrong.RW + "举报详情如下:" + ChatColor.WHITE + untiType.getMoreInformation());
//                                    }
//                                    break;
//                            }
                                untiType = new Type(untiType.getType(),player,true);
                            } else {
                                player.sendMessage(ChatColor.RED + ReportWrong.RW + lang.getString("gui.c.confirm.failure"));
                            }
                            player.closeInventory();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static com.kan_model.bukkit.First.util.Type getUntiType() {
        return untiType;
    }

    public void addReport(Player player) {
        player.closeInventory();
        String world = player.getWorld().getName();
        int x = player.getLocation().getBlockX();
        int y = player.getLocation().getBlockY();
        int z = player.getLocation().getBlockZ();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//                            if (SaveSql.addReport(player.getName(), world, x, y, z, untiType.getType(), time)) {
        switch (untiType.getType()) {
            case THEFT:
                player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.LIGHT_PURPLE + time
                        + lang.getString("gui.c.confirm.theft")
                        + world + " [X]:" + x + " [Y]:" + y + " [Z]:" + z);
                if (!(SaveSql.addReport(player.getName(), world, x, y, z, untiType.getType(), time))){
                    player.sendMessage(ChatColor.RED + ReportWrong.RW + lang.getString("gui.c.confirm.failure"));
                }else {
                    player.sendMessage(ChatColor.GREEN + ReportWrong.RW + "举报完成！");
                }
                break;
            case DESTROY:
                player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.LIGHT_PURPLE + time
                        + lang.getString("gui.c.confirm.destroy")
                        + world + " [X]:" + x + " [Y]:" + y + " [Z]:" + z);
                if (!(SaveSql.addReport(player.getName(), world, x, y, z, untiType.getType(), time))){
                    player.sendMessage(ChatColor.RED + ReportWrong.RW + lang.getString("gui.c.confirm.failure"));
                }else {
                    player.sendMessage(ChatColor.GREEN + ReportWrong.RW + "举报完成！");
                }
                break;
            case SBUG:
                player.sendMessage(ChatColor.GREEN + ReportWrong.RW + ChatColor.LIGHT_PURPLE + time
                        + lang.getString("gui.c.confirm.sbug"));
                break;
        }
    }

    public static HashMap<Player, Long> getCooldown() {
        return cooldown;
    }
}

