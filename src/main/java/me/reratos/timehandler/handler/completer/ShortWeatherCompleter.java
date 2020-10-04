package me.reratos.timehandler.handler.completer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.util.StringUtil;

public class ShortWeatherCompleter {

	public static void completer(Command command, String label, String[] params, List<String> collection) {
		Set<String> list = new HashSet<String>();
		
		switch (params.length) {
		case 1: // rain <world>
			List<World> listWorlds = Bukkit.getWorlds();
			for(World w : listWorlds) {
//				if(w.getEnvironment() == Environment.NORMAL) {
					list.add(w.getName());
//				}
			}
			StringUtil.copyPartialMatches(params[0], list, collection);
			break;
			
		case 2:  // rain world {DURATION_TICKS}
			list.add("10000");
			list.add("20000");
			list.add("50000");
			list.add("100000");
			list.add("150000");
			list.add("200000");
			StringUtil.copyPartialMatches(params[1], list, collection);
			break;
			
		default:
			break;
		}
		
		return;
	}
}
