package me.reratos.timehandler.custom;

import be.seeseemelk.mockbukkit.command.ConsoleCommandSenderMock;
import org.bukkit.permissions.Permission;

import java.util.HashSet;
import java.util.Set;

public class ConsoleCommandSenderMockCustom extends ConsoleCommandSenderMock {

    private Set<Permission> permissions;

    public ConsoleCommandSenderMockCustom() {
        super();
        this.permissions = new HashSet<>();
    }

    @Override
    public boolean isPermissionSet(String name) {
        for(Permission perm : permissions) {
            if(perm.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return permissions.contains(perm);
    }

    @Override
    public boolean hasPermission(String name) {
        for(Permission perm : permissions) {
            if(perm.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return permissions.contains(perm);
    }

    public void addPermission(String permissionName) {
        addPermission(new Permission(permissionName));
    }

    public void addPermission(Permission permission) {
        this.permissions.add(permission);
    }
}
