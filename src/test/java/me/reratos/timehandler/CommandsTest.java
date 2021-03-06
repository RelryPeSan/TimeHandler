package me.reratos.timehandler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;

import edu.emory.mathcs.backport.java.util.Arrays;
import me.reratos.timehandler.custom.ServerMockCustom;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import be.seeseemelk.mockbukkit.scheduler.AsyncTaskException;
import me.reratos.timehandler.core.TimeManager;
import me.reratos.timehandler.core.WorldManager;
import me.reratos.timehandler.enums.MoonPhasesEnum;

@TestMethodOrder(OrderAnnotation.class)
class CommandsTest {
	
	private static ServerMockCustom server;
	private static WorldMock wMock;
	private static TimeHandler timeHandler;

	@BeforeAll
	public static void setUp() {
	    server = MockBukkit.mock(new ServerMockCustom());
	    timeHandler = MockBukkit.load(TimeHandler.class);

	    List<String> aliases = new ArrayList<>();
	    aliases.add("th");
	    timeHandler.getCommand("timehandler").setAliases(aliases);

	    aliases.clear();
	    aliases.add("th info");
		timeHandler.getCommand("timehandler info").setAliases(aliases);

	    wMock = new WorldMock();
	    wMock.setName("test");
	    
	    server.addWorld(wMock);
	}

	@AfterAll
	public static void tearDown() {
		try {
			MockBukkit.unmock();
		} catch (AsyncTaskException ignored) {
		}
	}

	/* ==== TEST /TIMEHANDLER HELP ==== */
	@Test
//	@Disabled ("Unimplemented hasPermission")
	void testCommandTimehandlerHelp_ServerArgs_1() {
		boolean condition;
		String label = "timehandler";
		String[] args = {"help"};
		Command command = server.getPluginCommand(label);
		PluginCommand pluginCommand = timeHandler.getCommand("timehandler info");

		assert pluginCommand != null;
		pluginCommand.setAliases(Collections.singletonList("th info"));

		server.getConsoleSender().addPermission("timehandler.info.use");

		assert command != null;
		condition = timeHandler.onCommand(server.getConsoleSender(), command, label, args);

		assertTrue(condition);
	}

	/* ==== TEST /TIMEHANDLER INFO ==== */
	@Test
	void testCommandTimehandlerInfo_PlayerArgs_1() {
		boolean condition;
		String label = "timehandler";
		Command command = server.getPluginCommand(label);
		PlayerMock pMock = new PlayerMock(server, "player");
		String[] args = {"info"};

		assert command != null;
		condition = timeHandler.onCommand(pMock, command, label, args);
		
		assertTrue(condition);
	}

	@Test
	void testCommandTimehandlerInfo_ServerArgs_1() {
		boolean condition;
		String label = "timehandler";
		String[] args = {"info"};
		Command command = server.getPluginCommand(label);

		assert command != null;
		condition = timeHandler.onCommand(server.getConsoleSender(), command, label, args);
		
		assertFalse(condition);
	}

	@Test
	void testCommandTimehandlerInfo_ServerArgs_2() {
		boolean condition;
		String label = "timehandler";
		String[] args = {"info", "test"};
		Command command = server.getPluginCommand(label);

		assert command != null;
		condition = timeHandler.onCommand(server.getConsoleSender(), command, label, args);
		
		assertTrue(condition);
	}

	/* ==== TEST /TIMEHANDLER LIST ==== */
	@Test
	void testCommandTimehandlerList_ServerArgs_1() {
		boolean condition;
		String label = "timehandler";
		String[] args = {"list"};
		Command command = server.getPluginCommand(label);

		assert command != null;
		condition = timeHandler.onCommand(server.getConsoleSender(), command, label, args);
		
		assertTrue(condition);
	}

	@Test
	void testCommandTimehandlerList_ServerArgs_2() {
		boolean condition;
		String label = "timehandler";
		String[] args = {"list", "test"};
		Command command = server.getPluginCommand(label);

		assert command != null;
		condition = timeHandler.onCommand(server.getConsoleSender(), command, label, args);
		
		assertFalse(condition);
	}

	/* ==== TEST /TIMEHANDLER SET ==== */
	@Test
	@Order(1)
	void testCommandTimehandlerSet_ServerArgs_2() {
		boolean condition;
		String label = "timehandler";
		String[] args = {"set", "test"};
		Command command = server.getPluginCommand(label);

		assert command != null;
		condition = timeHandler.onCommand(server.getConsoleSender(), command, label, args);
		
		assertTrue(condition);
	}

	@Test
	void testCommandTimehandlerSet_ServerArgs_4() {
		boolean condition;
		String label = "timehandler";
		String[] args = {"set", "test", "enabled", "false"};
		Command command = server.getPluginCommand(label);

		assert command != null;
		condition = timeHandler.onCommand(server.getConsoleSender(), command, label, args);
		
		assertTrue(condition);
		
		WorldManager wm = TimeManager.getRunablesWorld().get("test");
		
		assertNotNull(wm);
		assertFalse(wm.isEnabled());
	}

	@Test
	void testCommandTimehandlerSet_ServerArgs_3() {
		boolean condition;
		String label = "timehandler";
		String[] args = {"set", "test", "enabled"};
		Command command = server.getPluginCommand(label);

		assert command != null;
		condition = timeHandler.onCommand(server.getConsoleSender(), command, label, args);
		
		assertFalse(condition);
	}

	/* ==== TEST /TIMEHANDLER UPDATE ==== */
	@Test
	void testCommandTimehandlerUpdate_ServerArgs_0() {
		boolean condition;
		String label = "timehandler";
		String[] args = {"update"};
		Command command = server.getPluginCommand(label);
		
		assert command != null;
		condition = timeHandler.onCommand(server.getConsoleSender(), command, label, args);
		
		assertTrue(condition);
	}

	/* ==== TEST /TIMEHANDLER ANYTHING ==== */
	@Test
	void testCommandTimehandlerAnything_ServerArgs_0() {
		boolean condition;
		String label = "timehandler";
		String[] args = {"testErro"};
		Command command = server.getPluginCommand(label);
		
		assert command != null;
		condition = timeHandler.onCommand(server.getConsoleSender(), command, label, args);
		
		assertFalse(condition);
	}

	/* ==== TEST /DAY ==== */
	@Test
	void testCommandDay_ServerArgs_0() {
		boolean condition;
		String label = "day";
		String[] args = {};
		Command command = server.getPluginCommand(label);
		
		assert command != null;
		condition = timeHandler.onCommand(server.getConsoleSender(), command, label, args);
		
		assertFalse(condition);
	}

	@Test
	void testCommandDay_ServerArgs_1() {
		boolean condition;
		String label = "day";
		String[] args = {"test"};
		Command command = server.getPluginCommand(label);
		
		wMock.setTime(18000);

		assertEquals(wMock.getTime(), 18000);
		
		assert command != null;
		condition = timeHandler.onCommand(server.getConsoleSender(), command, label, args);
		
		assertTrue(condition);
		assertTrue(wMock.getTime() < 12000);
	}

	@Test
	void testCommandDay_PlayerArgs_0() {
		boolean condition;
		String label = "day";
		String[] args = {};
		Command command = server.getPluginCommand(label);
		PlayerMock pMock = new PlayerMock(server, "player");
		World world = pMock.getWorld();

		world.setTime(18000);

		assertEquals(world.getTime(), 18000);
		
		assert command != null;
		condition = timeHandler.onCommand(pMock, command, label, args);
		
		assertTrue(condition);
		assertTrue(world.getTime() < 12000);
	}

	/* ==== TEST /NIGHT ==== */
	@Test
	void testCommandNight_ServerArgs_0() {
		boolean condition;
		String label = "night";
		String[] args = {};
		Command command = server.getPluginCommand(label);
		
		assert command != null;
		condition = timeHandler.onCommand(server.getConsoleSender(), command, label, args);
		
		assertFalse(condition);
	}

	@Test
	void testCommandNight_ServerArgs_1() {
		boolean condition;
		String label = "night";
		String[] args = {"test"};
		Command command = server.getPluginCommand(label);
		
		wMock.setTime(6000);

		assertEquals(wMock.getTime(), 6000);
		
		assert command != null;
		condition = timeHandler.onCommand(server.getConsoleSender(), command, label, args);
		
		assertTrue(condition);
		assertTrue(wMock.getTime() > 12000);
	}

	@Test
	void testCommandNight_PlayerArgs_0() {
		boolean condition;
		String label = "night";
		String[] args = {};
		Command command = server.getPluginCommand(label);
		PlayerMock pMock = new PlayerMock(server, "player");
		World world = pMock.getWorld();

		world.setTime(6000);
		
		assertEquals(world.getTime(), 6000);
		
		assert command != null;
		condition = timeHandler.onCommand(pMock, command, label, args);
		
		assertTrue(condition);
		assertTrue(world.getTime() > 12000);
	}

	/* ==== TEST /MOONPHASE ==== */
	@Test
	void testCommandMoonPhase_PlayerArgs_1() {
		boolean condition;
		String label = "moonphase";
		Set<String> phases = MoonPhasesEnum.getList();
		Command command = server.getPluginCommand(label);
		PlayerMock pMock = new PlayerMock(server, "player");
		World world = pMock.getWorld();

		for(String p : phases) {
			String[] args = {p};
			assert command != null;
			condition = timeHandler.onCommand(pMock, command, label, args);
			
			if(MoonPhasesEnum.getEnumPorValue(p) == MoonPhasesEnum.DEFAULT) {
				assertFalse(condition);
				continue;
			}
			
			assertTrue(condition);
			
			long fullTime = world.getFullTime();
			int days = (int) (fullTime / 24000);
			int actual = days % 8;
			int expected = MoonPhasesEnum.getEnumPorValue(p).ordinal();
			
			assertEquals(expected, actual);
		}

		String[] aux = {"testeErro"};
		assert command != null;
		condition = timeHandler.onCommand(pMock, command, label, aux);
		assertFalse(condition);
		
	}

	@Test
	void testCommandMoonPhase_ServerArgs_1() {
		boolean condition;
		String label = "moonphase";
		Command command = server.getPluginCommand(label);

		String[] testeErro = {"testeErro"};
		assert command != null;
		condition = timeHandler.onCommand(server.getConsoleSender(), command, label, testeErro);
		assertFalse(condition);
		
		String[] testeTest = {"test"};
		condition = timeHandler.onCommand(server.getConsoleSender(), command, label, testeTest);
		assertFalse(condition);
		
	}

	@Test
	void testCommandMoonPhase_ServerArgs_2() {
		boolean condition;
		String label = "moonphase";
		Set<String> phases = MoonPhasesEnum.getList();
		Command command = server.getPluginCommand(label);

		for(String p : phases) {
			String[] args = {p, "test"};
			assert command != null;
			condition = timeHandler.onCommand(server.getConsoleSender(), command, label, args);
			
			if(MoonPhasesEnum.getEnumPorValue(p) == MoonPhasesEnum.DEFAULT) {
				assertFalse(condition);
				continue;
			}
			
			assertTrue(condition);
			
			long fullTime = wMock.getFullTime();
			int days = (int) (fullTime / 24000);
			int actual = days % 8;
			int expected = MoonPhasesEnum.getEnumPorValue(p).ordinal();
			
			assertEquals(expected, actual);
		}

		String[] aux = {"testeErro", "test"};
		assert command != null;
		condition = timeHandler.onCommand(server.getConsoleSender(), command, label, aux);
		assertFalse(condition);
		
	}

	/* ==== TEST /RAIN ==== */
	@Test
	void testCommandRain_ServerArgs_0() {
		boolean condition;
		String label = "rain";
		String[] args = {};
		Command command = server.getPluginCommand(label);
		
		wMock.setStorm(false);
		wMock.setThundering(false);
		
		assertFalse(wMock.hasStorm());
		assertFalse(wMock.isThundering());

		assert command != null;
		condition = timeHandler.onCommand(server.getConsoleSender(), command, label, args);
		
		assertFalse(condition);
	}

	@Test
	void testCommandRain_ServerArgs_1() {
		boolean condition;
		String label = "rain";
		String[] args = {"test"};
		Command command = server.getPluginCommand(label);

		wMock.setStorm(false);
		wMock.setThundering(false);
		
		assertFalse(wMock.hasStorm());
		assertFalse(wMock.isThundering());

		assert command != null;
		condition = timeHandler.onCommand(server.getConsoleSender(), command, label, args);
		
		assertTrue(condition);
		
		assertTrue(wMock.hasStorm());
		assertFalse(wMock.isThundering());
	}

	@Test
	void testCommandRain_ServerArgs_2() {
		boolean condition;
		String label = "rain";
		String[] args = {"test", "20000"};
		Command command = server.getPluginCommand(label);

		wMock.setStorm(false);
		wMock.setThundering(false);
		
		assertFalse(wMock.hasStorm());
		assertFalse(wMock.isThundering());

		assert command != null;
		condition = timeHandler.onCommand(server.getConsoleSender(), command, label, args);
		
		assertTrue(condition);
		
		assertTrue(wMock.hasStorm());
		assertEquals(wMock.getWeatherDuration(), 20000);
		assertFalse(wMock.isThundering());
	}

	@Test
	void testCommandRain_PlayerArgs_0() {
		boolean condition;
		String label = "rain";
		String[] args = {};
		Command command = server.getPluginCommand(label);
		PlayerMock pMock = new PlayerMock(server, "player");
		World world = pMock.getWorld();
		
		world.setStorm(false);
		world.setThundering(false);
		
		assertFalse(world.hasStorm());
		assertFalse(world.isThundering());

		assert command != null;
		condition = timeHandler.onCommand(pMock, command, label, args);
		
		assertTrue(condition);
		
		assertTrue(world.hasStorm());
		assertFalse(world.isThundering());
	}

	/* ==== TEST /THUNDERING ==== */
	@Test
	void testCommandThundering_ServerArgs_0() {
		boolean condition;
		String label = "thundering";
		String[] args = {};
		Command command = server.getPluginCommand(label);
		
		wMock.setStorm(false);
		wMock.setThundering(false);
		
		assertFalse(wMock.hasStorm());
		assertFalse(wMock.isThundering());
		
		assert command != null;
		condition = timeHandler.onCommand(server.getConsoleSender(), command, label, args);
		
		assertFalse(condition);
	}

	@Test
	void testCommandThundering_ServerArgs_1() {
		boolean condition;
		String label = "thundering";
		String[] args = {"test"};
		Command command = server.getPluginCommand(label);

		wMock.setStorm(false);
		wMock.setThundering(false);
		
		assertFalse(wMock.hasStorm());
		assertFalse(wMock.isThundering());
		
		assert command != null;
		condition = timeHandler.onCommand(server.getConsoleSender(), command, label, args);
		
		assertTrue(condition);
		
		assertTrue(wMock.hasStorm());
		assertTrue(wMock.isThundering());
	}

	@Test
	void testCommandThundering_ServerArgs_2() {
		boolean condition;
		String label = "thundering";
		String[] args = {"test", "15000"};
		Command command = server.getPluginCommand(label);

		wMock.setStorm(false);
		wMock.setThundering(false);
		
		assertFalse(wMock.hasStorm());
		assertFalse(wMock.isThundering());
		
		assert command != null;
		condition = timeHandler.onCommand(server.getConsoleSender(), command, label, args);
		
		assertTrue(condition);
		
		assertTrue(wMock.hasStorm());
		assertTrue(wMock.isThundering());
		assertEquals(wMock.getThunderDuration(), 15000);
	}

	@Test
	void testCommandThundering_PlayerArgs_0() {
		boolean condition;
		String label = "thundering";
		String[] args = {};
		Command command = server.getPluginCommand(label);
		PlayerMock pMock = new PlayerMock(server, "player");
		World world = pMock.getWorld();
		
		world.setStorm(false);
		world.setThundering(false);
		
		assertFalse(world.hasStorm());
		assertFalse(world.isThundering());
		
		assert command != null;
		condition = timeHandler.onCommand(pMock, command, label, args);
		
		assertTrue(condition);
		
		assertTrue(world.hasStorm());
		assertTrue(world.isThundering());
	}

	/* ==== TEST /CALM ==== */
	@Test
	void testCommandCalm_ServerArgs_0() {
		boolean condition;
		String label = "calm";
		String[] args = {};
		Command command = server.getPluginCommand(label);
		
		wMock.setStorm(true);
		wMock.setThundering(true);
		
		assertTrue(wMock.hasStorm());
		assertTrue(wMock.isThundering());
		
		assert command != null;
		condition = timeHandler.onCommand(server.getConsoleSender(), command, label, args);
		
		assertFalse(condition);
	}

	@Test
	void testCommandCalm_ServerArgs_1() {
		boolean condition;
		String label = "calm";
		String[] args = {"test"};
		Command command = server.getPluginCommand(label);

		wMock.setStorm(true);
		wMock.setThundering(true);
		
		assertTrue(wMock.hasStorm());
		assertTrue(wMock.isThundering());
		
		assert command != null;
		condition = timeHandler.onCommand(server.getConsoleSender(), command, label, args);
		
		assertTrue(condition);
		
		assertFalse(wMock.hasStorm());
		assertFalse(wMock.isThundering());
	}

	@Test
	void testCommandCalm_ServerArgs_2() {
		boolean condition;
		String label = "calm";
		String[] args = {"test", "30000"};
		Command command = server.getPluginCommand(label);

		wMock.setStorm(true);
		wMock.setThundering(true);
		
		assertTrue(wMock.hasStorm());
		assertTrue(wMock.isThundering());
		
		assert command != null;
		condition = timeHandler.onCommand(server.getConsoleSender(), command, label, args);
		
		assertTrue(condition);

		assertFalse(wMock.hasStorm());
		assertFalse(wMock.isThundering());
		assertEquals(wMock.getWeatherDuration(), 30000);
	}

	@Test
	void testCommandCalm_PlayerArgs_0() {
		boolean condition;
		String label = "calm";
		String[] args = {};
		Command command = server.getPluginCommand(label);
		PlayerMock pMock = new PlayerMock(server, "player");
		World world = pMock.getWorld();

		wMock.setStorm(true);
		wMock.setThundering(true);
		
		assertTrue(wMock.hasStorm());
		assertTrue(wMock.isThundering());
		
		assert command != null;
		condition = timeHandler.onCommand(pMock, command, label, args);
		
		assertTrue(condition);
		
		assertFalse(world.hasStorm());
		assertFalse(world.isThundering());
	}
	
}
