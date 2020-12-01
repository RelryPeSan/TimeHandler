package me.reratos.timehandler.events;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.block.BlockMock;
import be.seeseemelk.mockbukkit.scheduler.AsyncTaskException;
import me.reratos.timehandler.TimeHandler;
import me.reratos.timehandler.custom.PlayerMockCustom;
import me.reratos.timehandler.handler.CommandHandler;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerListenerTest {

    private static ServerMock serverMock;
    private static TimeHandler timeHandler;
    public static WorldMock worldMock;
    public static PlayerMockCustom playerMockCustom;
    public static PlayerListener playerListener;

    @BeforeAll
    public static void setUp() {
        serverMock = MockBukkit.mock();
        timeHandler = MockBukkit.load(TimeHandler.class);
        playerListener = new PlayerListener();

        // configurando mundo
        worldMock = new WorldMock();
        worldMock.setName("world_test_1");

        serverMock.addWorld(worldMock);

        //configure world in the plugin
        CommandHandler.set(serverMock.getConsoleSender(), worldMock.getName());
        CommandHandler.set(serverMock.getConsoleSender(), worldMock.getName(), "time", "configured");
        CommandHandler.set(serverMock.getConsoleSender(), worldMock.getName(), "durationDay", "28000");
        CommandHandler.set(serverMock.getConsoleSender(), worldMock.getName(), "durationNight", "20000");
    }

    @AfterAll
    public static void tearDown() {
        try {
            MockBukkit.unmock();
        } catch (AsyncTaskException ignored) {
        }
    }

    @BeforeEach
    public void beforeEach() {
        playerMockCustom = new PlayerMockCustom(serverMock, "Player_test_1");

        //reset players for next test
        serverMock.setPlayers(0);
        serverMock.addPlayer(playerMockCustom);

        //set time to night 20000 ticks
        worldMock.setTime(20000);
    }

    @Test
    public void testOnPlayerBedLeaveEventWhen1PlayerSleepTicks100() {
        List<PlayerMockCustom> listPlayers = (List<PlayerMockCustom>) serverMock.getOnlinePlayers();

        assertEquals(1, listPlayers.size());

        PlayerMockCustom player_1 = (PlayerMockCustom) listPlayers.get(0);
        player_1.setSleeping(true);
        player_1.setSleepTicks(100);

        Block bedBlue = new BlockMock(Material.BLUE_BED);
        PlayerBedLeaveEvent playerBedLeaveEvent = new PlayerBedLeaveEvent(player_1, bedBlue, false);

        playerListener.onPlayerBedLeaveEvent(playerBedLeaveEvent);

        assertTrue(worldMock.getTime() < 20000);
    }

    @Test
    public void testOnPlayerBedLeaveEventWhen1PlayerSleepTicksLess100() {
        List<PlayerMockCustom> listPlayers = (List<PlayerMockCustom>) serverMock.getOnlinePlayers();

        assertEquals(1, listPlayers.size());

        PlayerMockCustom player_1 = (PlayerMockCustom) listPlayers.get(0);
        player_1.setSleeping(true);
        player_1.setSleepTicks(50);

        Block bedBlue = new BlockMock(Material.BLUE_BED);
        PlayerBedLeaveEvent playerBedLeaveEvent = new PlayerBedLeaveEvent(player_1, bedBlue, false);

        playerListener.onPlayerBedLeaveEvent(playerBedLeaveEvent);

        assertFalse(worldMock.getTime() < 20000);
    }

    @Test
    public void testOnPlayerBedLeaveEventWhen2PlayersSleepTicks100() {
        PlayerMockCustom playerMockCustom2 = new PlayerMockCustom(serverMock, "Player_test_2");
        serverMock.addPlayer(playerMockCustom2);

        List<PlayerMockCustom> listPlayers = (List<PlayerMockCustom>) serverMock.getOnlinePlayers();

        assertTrue(listPlayers.size() > 1);

        for(PlayerMockCustom pmc : listPlayers) {
            pmc.setSleeping(true);
            pmc.setSleepTicks(100);
        }

        PlayerMockCustom player_1 = listPlayers.get(0);

        Block bedBlue = new BlockMock(Material.BLUE_BED);
        PlayerBedLeaveEvent playerBedLeaveEvent = new PlayerBedLeaveEvent(player_1, bedBlue, false);

        playerListener.onPlayerBedLeaveEvent(playerBedLeaveEvent);

        assertTrue(worldMock.getTime() < 20000);
    }

    @Test
    public void testOnPlayerBedLeaveEventWhen1PlayersSleepTicks100AndAnotherPlayerNot() {
        PlayerMockCustom playerMockCustom2 = new PlayerMockCustom(serverMock, "Player_test_2");
        serverMock.addPlayer(playerMockCustom2);

        List<PlayerMockCustom> listPlayers = (List<PlayerMockCustom>) serverMock.getOnlinePlayers();

        // deve conter mais de um player no servidor
        assertTrue(listPlayers.size() > 1);

        PlayerMockCustom player_1 = listPlayers.get(0);
        player_1.setSleeping(true);
        player_1.setSleepTicks(100);

        Block bedBlue = new BlockMock(Material.BLUE_BED);
        PlayerBedLeaveEvent playerBedLeaveEvent = new PlayerBedLeaveEvent(player_1, bedBlue, false);

        playerListener.onPlayerBedLeaveEvent(playerBedLeaveEvent);

        assertFalse(worldMock.getTime() < 20000);
    }

}
