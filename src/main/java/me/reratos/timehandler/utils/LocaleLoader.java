package me.reratos.timehandler.utils;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Properties;

import org.bukkit.ChatColor;

import me.reratos.timehandler.TimeHandler;

public final class LocaleLoader {
	private static Locale locale;
	private static Properties properties;

    private LocaleLoader() {};

    public static String getString(String key) {
    	return getString(key, (Object[])null);
    }
    
    public static String getString(String key, Object... params) {
    	String message = properties.getProperty(key);
    	
    	try {
    		message = formatString(message, params);
    	} catch (Exception e) {
    		message = ChatColor.RED + "Error: Not found key '" + key + "'.\n" + e;
    		e.printStackTrace();
		}
    	
    	return message;
    }

    public static void reloadLocale() {
    	locale = null;
    	properties = null;
        initialize();
    }

    private static String formatString(String string, Object... params) {
        if (params != null) {
            MessageFormat formatter = new MessageFormat("");
            formatter.applyPattern(string.replace("'", "''"));
            string = formatter.format(params);
        }

        string = addColors(string);

        return string;
    }

    public static Locale getCurrentLocale() {
    	return locale;
    }

    public static void initialize() {
    	if(properties == null) {
    		String ret = TimeHandler.config.getString(ConstantsConfig.DEFAULT_LANGUAGE);
    		
    		if(ret == null) {
    			locale = new Locale("en", "US");
    		} else {
    			String[] arrayLocale = ret.split("_");
    			
    			if(arrayLocale.length == 2) {
        			locale = new Locale(arrayLocale[0], arrayLocale[1]);
    			}
    		}
    		
    		if (locale == null) {
                throw new IllegalStateException("Failed to parse locale string '" + ret + "'");
            }
    		
    		Properties propertiesDefault = new Properties();
    		
    		InputStream locInput = TimeHandler.plugin.getResource("lang/locale.properties");
    		try {
    			propertiesDefault.load(locInput);
				locInput.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
    		
    		properties = new Properties(propertiesDefault);
    		
    		if(locale.toString().equals("en_US")) {
    			return;
    		}
    		
    		locInput = TimeHandler.plugin.getResource("lang/locale_" + locale.toString() + ".properties");
    		try {
				properties.load(locInput);
				locInput.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }

    private static String addColors(String input) {
    	
    	// Colors
    	input = input.replaceAll("\\Q[[AQUA]]\\E", 		ChatColor.AQUA.toString());
    	input = input.replaceAll("\\Q[[BLACK]]\\E", 	ChatColor.BLACK.toString());
    	input = input.replaceAll("\\Q[[BLUE]]\\E", 		ChatColor.BLUE.toString());
    	input = input.replaceAll("\\Q[[DARK_AQUA]]\\E", ChatColor.DARK_AQUA.toString());
        input = input.replaceAll("\\Q[[DARK_BLUE]]\\E", ChatColor.DARK_BLUE.toString());
        input = input.replaceAll("\\Q[[DARK_GRAY]]\\E", ChatColor.DARK_GRAY.toString());
        input = input.replaceAll("\\Q[[DARK_GREEN]]\\E", ChatColor.DARK_GREEN.toString());
        input = input.replaceAll("\\Q[[DARK_PURPLE]]\\E", ChatColor.DARK_PURPLE.toString());
        input = input.replaceAll("\\Q[[DARK_RED]]\\E", 	ChatColor.DARK_RED.toString());
        input = input.replaceAll("\\Q[[GOLD]]\\E", 		ChatColor.GOLD.toString());
        input = input.replaceAll("\\Q[[GRAY]]\\E", 		ChatColor.GRAY.toString());
        input = input.replaceAll("\\Q[[GREEN]]\\E", 	ChatColor.GREEN.toString());
        input = input.replaceAll("\\Q[[LIGHT_PURPLE]]\\E", ChatColor.LIGHT_PURPLE.toString());
        input = input.replaceAll("\\Q[[RED]]\\E", 		ChatColor.RED.toString());
        input = input.replaceAll("\\Q[[YELLOW]]\\E", 	ChatColor.YELLOW.toString());
        input = input.replaceAll("\\Q[[WHITE]]\\E", 	ChatColor.WHITE.toString());
        
        // Specials
        input = input.replaceAll("\\Q[[BOLD]]\\E", 		ChatColor.BOLD.toString());
        input = input.replaceAll("\\Q[[UNDERLINE]]\\E", ChatColor.UNDERLINE.toString());
        input = input.replaceAll("\\Q[[ITALIC]]\\E", 	ChatColor.ITALIC.toString());
        input = input.replaceAll("\\Q[[STRIKE]]\\E", 	ChatColor.STRIKETHROUGH.toString());
        input = input.replaceAll("\\Q[[MAGIC]]\\E", 	ChatColor.MAGIC.toString());
        input = input.replaceAll("\\Q[[RESET]]\\E", 	ChatColor.RESET.toString());

        return input;
    }
}
