package dev.eeasee.eeaseemod.command.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.eeasee.eeaseemod.command.IClientCommand;
import dev.eeasee.eeaseemod.utils.Messenger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.NetworkUtils;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandSource.suggestMatching;

public class OpenToLanCommand implements IClientCommand {
    @Override
    public void register(CommandDispatcher<? super ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literalargumentbuilder = literal(":to-lan")
                .executes(OpenToLanCommand::tryOpenToLanDefault)
                .then(argument("gamemode", StringArgumentType.word())
                        .suggests((c, b) -> suggestMatching(new String[]{
                                GameMode.CREATIVE.getName(),
                                GameMode.SURVIVAL.getName(),
                                GameMode.ADVENTURE.getName(),
                                GameMode.SPECTATOR.getName()
                        }, b))
                        .then(argument("allow commands", BoolArgumentType.bool())
                                        .then(argument("port [zero as random]", IntegerArgumentType.integer(0,65535))
                                                .executes(OpenToLanCommand::tryOpenToLanAsInput))));


        ((CommandDispatcher<ServerCommandSource>) dispatcher).register(literalargumentbuilder);
    }

    private static int tryOpenToLanAsInput(CommandContext<ServerCommandSource> serverCommandSourceCommandContext) throws CommandSyntaxException {
        GameMode gameMode = GameMode.byName(StringArgumentType.getString(serverCommandSourceCommandContext, "gamemode"), MinecraftClient.getInstance().interactionManager.getCurrentGameMode());
        boolean allowCommands = BoolArgumentType.getBool(serverCommandSourceCommandContext, "allow commands");
        int port = IntegerArgumentType.getInteger(serverCommandSourceCommandContext, "port [zero as random]");

        Text openInfo = Messenger.c(
                "wb on: "
                ,"c GameMode: " ,"db " + gameMode.getName()
                ,"c   AllowCommands: ", "db " + allowCommands
                ,"c   Port: " ,"db " + port
        );

        if (tryOpenToLan(gameMode, allowCommands, port)) {
            serverCommandSourceCommandContext.getSource().sendFeedback(Messenger.c("wb Opened to Lan ", openInfo), false);
        } else {
            throw new SimpleCommandExceptionType(Messenger.c("r Failed to open ", openInfo)).create();
        }
        return 1;
    }

    private static int tryOpenToLanDefault(CommandContext<ServerCommandSource> serverCommandSourceCommandContext) throws CommandSyntaxException {
        GameMode gameMode = MinecraftClient.getInstance().interactionManager.getCurrentGameMode();
        boolean allowCommands = true;
        int port = NetworkUtils.findLocalPort();
        Text openInfo = Messenger.c(
                "wb on: "
                ,"c GameMode: " ,"db " + gameMode.getName()
                ,"c   AllowCommands: ", "db " + allowCommands
                ,"c   Port: " ,"db " + port
        );

        if (tryOpenToLan(gameMode, allowCommands, port)) {
            serverCommandSourceCommandContext.getSource().sendFeedback(Messenger.c("wb Opened to Lan ", openInfo), false);
        } else {
            throw new SimpleCommandExceptionType(Messenger.c("r Failed to open ", openInfo)).create();
        }
        return 1;
    }

    private static boolean tryOpenToLan(GameMode gameMode, boolean allowCommands, int port) {
        MinecraftClient client = MinecraftClient.getInstance();
        return client.getServer().openToLan(gameMode, allowCommands, port);
    }

}
