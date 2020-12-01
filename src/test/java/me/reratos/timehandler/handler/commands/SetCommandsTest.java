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
import be.seeseemelk.mockbukkit.scheduler.AsyncTaskException;
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
	private static WorldManager worldManager;
//	private static TimeHandler timeHandler;
	
	@BeforeAll
	public static void setUp() {
	    server = MockBukkit.mock();
//	    timeHandler = MockBukkit.load(TimeHandler.class);
	    MockBukkit.load(TimeHandler.class);

		WorldMock wMock = new WorldMock();
	    wMock.setName("test");
	    
	    server.addWorld(wMock);
	    worldManager = new WorldManager(wMock.getName());
	}

	@AfterAll
	public static void tearDown() {
		try {
			MockBukkit.unmock();
		} catch (AsyncTaskException ignored) {
		}
	}
	
	@Test
	void testCommandSetDefault() {
		
		boolean retorno = SetCommand.commandSetDefault(server.addPlayer(), "test");
		
		assertTrue(retorno);
	}

	@Test
	void testCommandSetBase() {
		boolean condition;
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
				fieldValue = (String) f.get(f);
				
				condition = SetCommand.commandSetBase(server.getConsoleSender(), worldManager, fieldValue, "");
				
				assertTrue(condition);
			} catch (Exception ignored) {
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
		boolean condition;
		Set<String> times = TimeEnum.getList();

		// testando com todos os valores do enum TIME
		for(String time : times) {
			condition = SetCommand.commandSetTime(server.getConsoleSender(), worldManager, time);
			
			assertTrue(condition);
		}

		// testando com valor falso
		condition = SetCommand.commandSetTime(server.getConsoleSender(), worldManager, "testFalse");
		
		assertFalse(condition);
	}

	@Test
	void testCommandSetTimeFixed() {
		boolean condition;
		
		// testando valor não numerico
		condition = SetCommand.commandSetTimeFixed(server.getConsoleSender(), worldManager, "abc");
		
		assertFalse(condition);

		// testando valor menor que 0
		condition = SetCommand.commandSetTimeFixed(server.getConsoleSender(), worldManager, "-100");

		assertFalse(condition);

		// testando valor maior que 24000
		condition = SetCommand.commandSetTimeFixed(server.getConsoleSender(), worldManager, "25000");

		assertFalse(condition);
		
		// testando valor entre 0 e 24000
		condition = SetCommand.commandSetTimeFixed(server.getConsoleSender(), worldManager, "0");

		assertTrue(condition);
	}

	@Test
	void testCommandSetDurationDay() {
		boolean condition;
		
		// menor que o minimos - false
		condition = SetCommand.commandSetDurationDay(server.getConsoleSender(), worldManager,
				"" + (int)(worldManager.getDurationDefaultDay()/10 - 1));
		
		assertFalse(condition);
		
		// maior que o maximo - false
		condition = SetCommand.commandSetDurationDay(server.getConsoleSender(), worldManager,
				"" + (int)(worldManager.getDurationDefaultDay()*10 + 1));
		
		assertFalse(condition);
		
		// entre minimo e maximo - true
		condition = SetCommand.commandSetDurationDay(server.getConsoleSender(), worldManager,
				"" + (int)worldManager.getDurationDefaultDay());
		
		assertTrue(condition);
	}

	@Test
	void testCommandSetDurationNight() {
		boolean condition;
		
		// menor que o minimos - false
		condition = SetCommand.commandSetDurationNight(server.getConsoleSender(), worldManager,
				"" + (int)(worldManager.getDurationDefaultNight()/10 - 1));
		
		assertFalse(condition);
		
		// maior que o maximo - false
		condition = SetCommand.commandSetDurationNight(server.getConsoleSender(), worldManager,
				"" + (int)(worldManager.getDurationDefaultNight()*10 + 1));
		
		assertFalse(condition);
		
		// entre minimo e maximo - true
		condition = SetCommand.commandSetDurationNight(server.getConsoleSender(), worldManager,
				"" + (int)worldManager.getDurationDefaultNight());
		
		assertTrue(condition);
	}

	@Test
	void testCommandSetMoonPhase() {
		boolean condition;
		
		Set<String> set = MoonPhasesEnum.getList();

		// testando com todos os valores do enum MOON_PHASE
		for(String phase : set) {
			condition = SetCommand.commandSetMoonPhase(server.getConsoleSender(), worldManager, phase);
			
			assertTrue(condition);
		}

		// testando com valor falso
		condition = SetCommand.commandSetMoonPhase(server.getConsoleSender(), worldManager, "testFalse");
		
		assertFalse(condition);
	}

	@Test
	void testCommandSetThunder() {
		boolean condition;
		
		Set<String> set = ThunderEnum.getList();

		// testando com todos os valores do enum THUNDER
		for(String phase : set) {
			condition = SetCommand.commandSetThunder(server.getConsoleSender(), worldManager, phase);
			
			assertTrue(condition);
		}

		// testando com valor falso
		condition = SetCommand.commandSetThunder(server.getConsoleSender(), worldManager, "testFalse");
		
		assertFalse(condition);
	}

	@Test
	void testCommandSetWeather() {
		boolean condition;
		
		Set<String> set = WeatherEnum.getList();
		
		// testando com todos os valores do enum WEATHER
		for(String phase : set) {
			condition = SetCommand.commandSetWeather(server.getConsoleSender(), worldManager, phase);
			
			assertTrue(condition);
		}
		
		// testando com valor falso
		condition = SetCommand.commandSetWeather(server.getConsoleSender(), worldManager, "testFalse");
		
		assertFalse(condition);
	}

	@Test
	void testCommandSetEnabled() {
		boolean condition;

		// testando com valor falso
		condition = SetCommand.commandSetEnabled(server.getConsoleSender(), worldManager, "");
		
		assertFalse(condition);

		// testando com valor "true"
		condition = SetCommand.commandSetEnabled(server.getConsoleSender(), worldManager, "true");
		
		assertTrue(condition);

		// testando com valor "false"
		condition = SetCommand.commandSetEnabled(server.getConsoleSender(), worldManager, "false");
		
		assertTrue(condition);
	}

}
