package me.reratos.timehandler.handler.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import me.reratos.timehandler.TimeHandler;
import me.reratos.timehandler.core.WorldManager;
import me.reratos.timehandler.enums.MoonPhasesEnum;
import me.reratos.timehandler.enums.ThunderEnum;
import me.reratos.timehandler.enums.TimeEnum;
import me.reratos.timehandler.enums.WeatherEnum;
import me.reratos.timehandler.utils.ConstantsWorldsConfig;

//@Disabled
class SetCommandsTest {
	
	private static ServerMock server;
	private static WorldMock wMock;
	private static WorldManager worldManager;
	
	@BeforeAll
	public static void setUp() {
	    server = MockBukkit.mock();
	    MockBukkit.load(TimeHandler.class);
	    
	    wMock = new WorldMock();
	    wMock.setName("test");
	    
	    server.addWorld(wMock);
	    worldManager = new WorldManager(wMock.getName());
	}

	@AfterAll
	public static void tearDown() {
	    MockBukkit.unmock();
	}
	
	@Test
	void testCommandSetDefault() {
		
		boolean retorno = SetCommand.commandSetDefault(server.addPlayer(), "test");
		
		assertTrue(retorno);
	}

	@Test
	void testCommandSetBase() {
		boolean condition = false;
		Field[] fields = null;
		
		// Recupera os campos da classe ConstantsWorldsConfig
		try {
			fields = Class.forName("me.reratos.timehandler.utils.ConstantsWorldsConfig").getFields(); 
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail("Não foi possivel pegar os campos.");
		}
		
		assertNotNull(fields);
		assertTrue(fields.length > 0);
		
		// verificando retorno falso
		for(Field f : fields) {
			String fieldValue;
			
			try {
				fieldValue = (String) f.get((Object)f);
				
				condition = SetCommand.commandSetBase(server.getConsoleSender(), worldManager, fieldValue, "");
				
				assertTrue(condition);
			} catch (Exception e) {
			}
		}
		
		// verificando ret verdadeiro
		condition = SetCommand.commandSetBase(server.getConsoleSender(), worldManager, 
				ConstantsWorldsConfig.ENABLED, "true");
		
		assertTrue(condition);
		

		// verificando retorno falso
		condition = SetCommand.commandSetBase(server.getConsoleSender(), worldManager, "testFalse", "");
		
		assertFalse(condition);
	}

	@Test
	void testCommandSetTime() {
		boolean condition = false;
		Set<String> times = TimeEnum.getList();

		// testando com todos os valores do enum TIME
		for(String time : times) {
			condition = SetCommand.commandSetTime(server.getConsoleSender(), worldManager, 
					ConstantsWorldsConfig.TIME, time);
			
			assertTrue(condition);
		}

		// testando com valor falso
		condition = SetCommand.commandSetTime(server.getConsoleSender(), worldManager, 
				ConstantsWorldsConfig.TIME, "testFalse");
		
		assertFalse(condition);
	}

	@Test
	void testCommandSetTimeFixed() {
		boolean condition = false;
		
		// testando valor não numerico
		condition = SetCommand.commandSetTimeFixed(server.getConsoleSender(), worldManager,  
				ConstantsWorldsConfig.TIME_FIXED, "abc");
		
		assertFalse(condition);

		// testando valor menor que 0
		condition = SetCommand.commandSetTimeFixed(server.getConsoleSender(), worldManager,  
				ConstantsWorldsConfig.TIME_FIXED, "-100");

		assertFalse(condition);

		// testando valor maior que 24000
		condition = SetCommand.commandSetTimeFixed(server.getConsoleSender(), worldManager,  
				ConstantsWorldsConfig.TIME_FIXED, "25000");

		assertFalse(condition);
		
		// testando valor entre 0 e 24000
		condition = SetCommand.commandSetTimeFixed(server.getConsoleSender(), worldManager,  
				ConstantsWorldsConfig.TIME_FIXED, "0");

		assertTrue(condition);
	}

	@Test
	void testCommandSetDurationDay() {
		boolean condition = false;
		
		// menor que o minimos - false
		condition = SetCommand.commandSetDurationDay(server.getConsoleSender(), worldManager, 
				ConstantsWorldsConfig.DURATION_DAY, "" + (int)(worldManager.getDurationDefaultDay()/10 - 1));
		
		assertFalse(condition);
		
		// maior que o maximo - false
		condition = SetCommand.commandSetDurationDay(server.getConsoleSender(), worldManager, 
				ConstantsWorldsConfig.DURATION_DAY, "" + (int)(worldManager.getDurationDefaultDay()*10 + 1));
		
		assertFalse(condition);
		
		// entre minimo e maximo - true
		condition = SetCommand.commandSetDurationDay(server.getConsoleSender(), worldManager, 
				ConstantsWorldsConfig.DURATION_DAY, "" + (int)worldManager.getDurationDefaultDay());
		
		assertTrue(condition);
	}

	@Test
	void testCommandSetDurationNight() {
		boolean condition = false;
		
		// menor que o minimos - false
		condition = SetCommand.commandSetDurationNight(server.getConsoleSender(), worldManager, 
				ConstantsWorldsConfig.DURATION_NIGHT, "" + (int)(worldManager.getDurationDefaultNight()/10 - 1));
		
		assertFalse(condition);
		
		// maior que o maximo - false
		condition = SetCommand.commandSetDurationNight(server.getConsoleSender(), worldManager, 
				ConstantsWorldsConfig.DURATION_NIGHT, "" + (int)(worldManager.getDurationDefaultNight()*10 + 1));
		
		assertFalse(condition);
		
		// entre minimo e maximo - true
		condition = SetCommand.commandSetDurationNight(server.getConsoleSender(), worldManager, 
				ConstantsWorldsConfig.DURATION_NIGHT, "" + (int)worldManager.getDurationDefaultNight());
		
		assertTrue(condition);
	}

	@Test
	void testCommandSetMoonPhase() {
		boolean condition = false;
		
		Set<String> set = MoonPhasesEnum.getList();

		// testando com todos os valores do enum MOON_PHASE
		for(String phase : set) {
			condition = SetCommand.commandSetMoonPhase(server.getConsoleSender(), worldManager, 
					ConstantsWorldsConfig.MOON_PHASE, phase);
			
			assertTrue(condition);
		}

		// testando com valor falso
		condition = SetCommand.commandSetMoonPhase(server.getConsoleSender(), worldManager, 
				ConstantsWorldsConfig.MOON_PHASE, "testFalse");
		
		assertFalse(condition);
	}

	@Test
	void testCommandSetThunder() {
		boolean condition = false;
		
		Set<String> set = ThunderEnum.getList();

		// testando com todos os valores do enum THUNDER
		for(String phase : set) {
			condition = SetCommand.commandSetThunder(server.getConsoleSender(), worldManager, 
					ConstantsWorldsConfig.THUNDER, phase);
			
			assertTrue(condition);
		}

		// testando com valor falso
		condition = SetCommand.commandSetThunder(server.getConsoleSender(), worldManager, 
				ConstantsWorldsConfig.THUNDER, "testFalse");
		
		assertFalse(condition);
	}

	@Test
	void testCommandSetWeather() {
		boolean condition = false;
		
		Set<String> set = WeatherEnum.getList();
		
		// testando com todos os valores do enum WEATHER
		for(String phase : set) {
			condition = SetCommand.commandSetWeather(server.getConsoleSender(), worldManager, 
					ConstantsWorldsConfig.WEATHER, phase);
			
			assertTrue(condition);
		}
		
		// testando com valor falso
		condition = SetCommand.commandSetWeather(server.getConsoleSender(), worldManager, 
				ConstantsWorldsConfig.WEATHER, "testFalse");
		
		assertFalse(condition);
	}

	@Test
	void testCommandSetEnabled() {
		boolean condition = false;

		// testando com valor falso
		condition = SetCommand.commandSetEnabled(server.getConsoleSender(), worldManager, 
				ConstantsWorldsConfig.ENABLED, "");
		
		assertFalse(condition);

		// testando com valor "true"
		condition = SetCommand.commandSetEnabled(server.getConsoleSender(), worldManager, 
				ConstantsWorldsConfig.ENABLED, "true");
		
		assertTrue(condition);

		// testando com valor "false"
		condition = SetCommand.commandSetEnabled(server.getConsoleSender(), worldManager, 
				ConstantsWorldsConfig.ENABLED, "false");
		
		assertTrue(condition);
	}

}
