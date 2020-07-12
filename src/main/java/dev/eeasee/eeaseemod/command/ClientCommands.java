package dev.eeasee.eeaseemod.command;

import com.google.common.collect.Sets;
import dev.eeasee.eeaseemod.command.commands.ClientTickSpeedCommand;
import dev.eeasee.eeaseemod.command.commands.MenuCommand;
import dev.eeasee.eeaseemod.command.commands.OpenToLanCommand;
import dev.eeasee.eeaseemod.command.commands.IntegratedServerCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ClientCommands {
    public static List<IClientCommand> commandList = new ArrayList<>();
    private static Set<String> commandSet = Sets.newHashSet();

    public static IClientCommand MENU;
    public static IClientCommand TICK_SPEED;
    public static IClientCommand OPEN_TO_LAN;
    public static IClientCommand INTEGRATED_SERVER;

    static{
        MENU = register(new MenuCommand(), ":menu");
        TICK_SPEED = register(new ClientTickSpeedCommand(), ":tickspeed");
        OPEN_TO_LAN = register(new OpenToLanCommand(), ":to-lan");
        INTEGRATED_SERVER = register(new IntegratedServerCommand(), ":integrated-server");
    }

    private static IClientCommand register(IClientCommand command, String name) {
        commandList.add(command);
        commandSet.add(name);
        return command;
    }

    public static boolean isClientOnlyCommand(String name) {
        try {
            return commandSet.contains(name.split(" ")[0].substring(1));
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }
}
