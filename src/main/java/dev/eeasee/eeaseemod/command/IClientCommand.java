package dev.eeasee.eeaseemod.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;

public interface IClientCommand {
    void register(CommandDispatcher<? super ServerCommandSource> dispatcher);
}
