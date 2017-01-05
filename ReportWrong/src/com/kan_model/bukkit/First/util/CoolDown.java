package com.kan_model.bukkit.First.util;
import com.kan_model.bukkit.First.ReportWrong;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
/**
 * Created by kgdwhsk on 2016/8/19.
 */
public class CoolDown {
    private boolean coolDownNow = true;
    private String playerName;
    public CoolDown(){
        this.coolDownNow = true;
    }
    public CoolDown(Player player, ReportWrong main){
        playerName = player.getName();
        new BukkitRunnable() {
            int time = main.getMyConfig().getInt("ReportCD");  // 六十秒
            @Override
            public void run() {
                if(time == 0) {
                    coolDownNow = false;
                    cancel();  // 终止线程
                    return;
                }
            }
        }.runTaskTimer(main, 0L, 20L);
    }
    public boolean isCoolDownNow() {
        return coolDownNow;
    }
    public String getPlayerName() {
        return playerName;
    }
}