package me.reratos.timehandler.handler.completer;

import java.util.List;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.util.StringUtil;

import me.reratos.timehandler.enums.MoonPhasesEnum;
import me.reratos.timehandler.handler.CommandHandler;

public class MoonPhaseCompleter {

	public static void completer(Command command, String label, String[] params, List<String> collection) {
		
		switch (params.length) {
		case 1: // moonPhase {MOON_PHASE}
			Set<String> list = MoonPhasesEnum.getList();
			list.remove(MoonPhasesEnum.DEFAULT.getValue());
			StringUtil.copyPartialMatches(params[0], list, collection);
			break;
			
		case 2:  // moonPhase MOON_PHASE {WORLD}
			StringUtil.copyPartialMatches(params[1], CommandHandler.getWorldsTimeHandler(), collection);
			break;
			
		default:
			break;
		}
		
		return;
	}
}
