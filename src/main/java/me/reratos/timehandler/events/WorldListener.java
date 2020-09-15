package me.reratos.timehandler.events;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import me.reratos.timehandler.core.TimeManager;
import me.reratos.timehandler.core.WorldManager;

public class WorldListener implements Listener {

	@EventHandler (priority = EventPriority.LOWEST)
	public void whenWorldChangeWeather(WeatherChangeEvent e) {
		World w = e.getWorld();
		WorldManager wm = TimeManager.getRunablesWorld().get(w.getName());

		if(wm != null) {
//			if(wm.getWeather() != WeatherEnum.DEFAULT) {
//				TimeHandler.sendMessage("AVISO! o mundo " + e.getWorld().getName() + " não pode ter o tempo mudado, pois está configurado no plugin TimeHandler");
//				TimeHandler.sendMessage("Para alterar o tempo do mundo, mude a propriedade enabled para false ou mude a propriedade weather para default");
//			}
		}
	}
}
