package dev.eeasee.eeaseemod.command.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.eeasee.eeaseemod.command.IClientCommand;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class IntegratedServerCommand implements IClientCommand {
    @Override
    public void register(CommandDispatcher<? super ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literalargumentbuilder = literal(":integrated-server")
                .then(literal("online-verification").then(argument("value", BoolArgumentType.bool())
                        .executes(IntegratedServerCommand::setOnlineMode)));
        ((CommandDispatcher<ServerCommandSource>) dispatcher).register(literalargumentbuilder);
    }

    private static int setOnlineMode(CommandContext<ServerCommandSource> serverCommandSourceCommandContext) {
        MinecraftClient client = MinecraftClient.getInstance();
        client.getServer().setOnlineMode(
                BoolArgumentType.getBool(serverCommandSourceCommandContext, "value")
        );
        return 1;
    }


}
