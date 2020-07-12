package dev.eeasee.eeaseemod.command.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.eeasee.eeaseemod.command.IClientCommand;
import dev.eeasee.eeaseemod.tick.OnClientTick;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.literal;

public class MenuCommand implements IClientCommand {

    private static int openMenu(CommandContext<ServerCommandSource> context) {
        OnClientTick.setIsMenuToOpen(true);
        return 1;
    }

    @Override
    public void register(CommandDispatcher<? super ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literalargumentbuilder = literal(":menu").executes(MenuCommand::openMenu);
        ((CommandDispatcher<ServerCommandSource>) dispatcher).register(literalargumentbuilder);
    }
}
