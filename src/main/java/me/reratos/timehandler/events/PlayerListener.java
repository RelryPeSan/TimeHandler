package me.reratos.timehandler.events;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.world.TimeSkipEvent;
import org.bukkit.event.world.TimeSkipEvent.SkipReason;

import me.reratos.timehandler.core.TimeManager;
import me.reratos.timehandler.core.WorldManager;
import me.reratos.timehandler.enums.TimeEnum;

public class PlayerListener implements Listener {

	@EventHandler
	public void onPlayerBedLeaveEvent(PlayerBedLeaveEvent e) {
		World w = e.getPlayer().getWorld();
		WorldManager wm = TimeManager.getRunablesWorld().get(w.getName());
		
		if(wm != null && wm.isEnabled() && wm.getTime().equals(TimeEnum.CONFIGURED)) {
			for(Player p : w.getPlayers()) {
				// p.getSleepTicks() sleep normal == 100 ticks
				if(!p.isSleeping() && !p.equals(e.getPlayer()) || p.getSleepTicks() < 100) {
					return;
				}
			}

			int dif = (int) (24000 - w.getTime());
			TimeSkipEvent timeSkipEvent = new TimeSkipEvent(w, SkipReason.NIGHT_SKIP, dif);
			Bukkit.getPluginManager().callEvent(timeSkipEvent);
		}
	}
}
