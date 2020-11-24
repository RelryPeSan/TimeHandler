package me.reratos.timehandler.custom;

import be.seeseemelk.mockbukkit.ServerMock;

public class ServerMockCustom extends ServerMock {

    private ConsoleCommandSenderMockCustom consoleSender;

    @Override
    public ConsoleCommandSenderMockCustom getConsoleSender() {
        if (this.consoleSender == null) {
            this.consoleSender = new ConsoleCommandSenderMockCustom();
        }

        return this.consoleSender;
    }
}
