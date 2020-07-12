package dev.eeasee.eeaseemod.command.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.eeasee.eeaseemod.command.IClientCommand;
import net.minecraft.server.command.ServerCommandSource;

import static com.mojang.brigadier.arguments.FloatArgumentType.floatArg;
import static com.mojang.brigadier.arguments.FloatArgumentType.getFloat;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ClientTickSpeedCommand implements IClientCommand {
    public static float clientTickSpeed = 50.0f;
    public static boolean isClientTickSpeedChanged = false;

    @Override
    public void register(CommandDispatcher<? super ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literalargumentbuilder = literal(":tickspeed");
        literalargumentbuilder = literalargumentbuilder.
                then(argument("times", floatArg(0.1F, 10.0F))
                        .executes((c) -> setClientTickSpeed(getFloat(c, "times"))));
        ((CommandDispatcher<ServerCommandSource>) dispatcher).register(literalargumentbuilder);
    }

    private int setClientTickSpeed(float times) {
        clientTickSpeed = 50.0f / times;
        isClientTickSpeedChanged = (times != 1.0f);
        return 1;
    }
}
