package me.reratos.timehandler.custom;

import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;

import java.util.UUID;

public class PlayerMockCustom extends PlayerMock {
    private boolean sleeping = false;
    private int sleepTicks = 0;

    public PlayerMockCustom(ServerMock server, String name) {
        super(server, name);
    }

    public PlayerMockCustom(ServerMock server, String name, UUID uuid) {
        super(server, name, uuid);
    }

    public void setSleepTicks(int sleepTicks) {
        if(sleepTicks > 100) {
            this.sleepTicks = 100;
        } else if(sleepTicks < 0) {
            this.sleepTicks = 0;
        } else {
            this.sleepTicks = sleepTicks;
        }
    }

    public void setSleeping(boolean sleeping) {
        this.sleeping = sleeping;
    }

    @Override
    public boolean isSleeping() {
        return this.sleeping;
    }

    @Override
    public int getSleepTicks() {
        return this.sleepTicks;
    }
}
