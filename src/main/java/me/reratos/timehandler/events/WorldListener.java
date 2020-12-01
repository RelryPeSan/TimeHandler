package me.reratos.timehandler.events;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.TimeSkipEvent;
import org.bukkit.event.world.TimeSkipEvent.SkipReason;

import me.reratos.timehandler.core.TimeManager;
import me.reratos.timehandler.core.WorldManager;
import me.reratos.timehandler.enums.TimeEnum;

public class WorldListener implements Listener {

//	@EventHandler
//	public void onWorldChangeWeather(WeatherChangeEvent e) {
//		World w = e.getWorld();
//		WorldManager wm = TimeManager.getRunablesWorld().get(w.getName());
//
//		if(wm != null) {
//		}
//	}
	
	@EventHandler
	public void onTimeSkip(TimeSkipEvent event) {
		World w = event.getWorld();
		WorldManager wm = TimeManager.getRunablesWorld().get(w.getName());
		SkipReason skipReason = event.getSkipReason();
		
		if(skipReason == SkipReason.NIGHT_SKIP) {
			if(wm != null && wm.isEnabled() && wm.getTime().equals(TimeEnum.CONFIGURED)) {
				w.setFullTime(w.getFullTime() + event.getSkipAmount());				
			}
		}
	}
}
