package me.reratos.timehandler.core;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.scheduler.AsyncTaskException;
import me.reratos.timehandler.TimeHandler;
import me.reratos.timehandler.custom.ServerMockCustom;
import me.reratos.timehandler.enums.TimeEnum;
import me.reratos.timehandler.handler.CommandHandler;
import me.reratos.timehandler.handler.commands.SetCommand;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class WorldManagerTest {

    private static ServerMockCustom server;
    private static WorldMock wMock;
    private static String worldName;
    private static TimeHandler timeHandler;

    @BeforeAll
    public static void setUp() {
        worldName = "testWorld";
        server = MockBukkit.mock(new ServerMockCustom());

        // criando mundo e adicionando ao servidor de teste
        wMock = new WorldMock();
        wMock.setName(worldName);
        server.addWorld(wMock);

        // inicializando o plugin
        timeHandler = MockBukkit.load(TimeHandler.class);

        if(!timeHandler.getDataFolder().exists()) {
            timeHandler.getDataFolder().mkdir();
        }

        File file = new File(timeHandler.getDataFolder(), "worldsConfig.yml");

        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                fail();
            }
        }

        // configurando mundo no plugin
//        CommandHandler.set(server.getConsoleSender(), worldName);
    }

    @AfterAll
    public static void tearDown() {
        try {
            MockBukkit.unmock();
        } catch (AsyncTaskException ignored) {
        }
    }

    @Test
    void testRunTimeConfigured() {
        int initTimeDay = 1000;
        int initTimeNight = 15000;
        WorldManager wm = new WorldManager(worldName);
        assertNotNull(wm);

        wm.setEnabled(true);
        wm.setTime(TimeEnum.CONFIGURED);
        wm.getWorld().setTime(initTimeDay);
        wm.setDurationDay(28000);
        wm.setDurationNight(1000);

        float auxTicksDay = wm.getDurationDefaultDay() / (float) wm.getDurationDay();
        float auxTicksNight = wm.getDurationDefaultNight() / (float) wm.getDurationNight();

        wm.run();

        for(int i = 0; i < 100; i++) {
            wm.getRunnableTask().run();
        }

        assertEquals(initTimeDay + auxTicksDay * 100, wm.getWorld().getTime());

        wm.getWorld().setTime(15000);

        for(int i = 0; i < 100; i++) {
            wm.getRunnableTask().run();
        }

        assertEquals(initTimeNight + auxTicksNight * 100, wm.getWorld().getTime());

        // definindo time para padrão para verificar o fim da Runnable task
        wm.setTime(TimeEnum.DEFAULT);
        wm.run();

        assertNull(wm.getRunnableTask());
    }

    @Test
    void testRunTimeFixed() {
        WorldManager wm = new WorldManager(worldName);
        assertNotNull(wm);

        wm.setTime(TimeEnum.FIXED);
        wm.setTimeFixed(10000);

        wm.run();

        assertNull(wm.getRunnableTask());
        assertEquals(10000, wm.getWorld().getTime());
    }

}
