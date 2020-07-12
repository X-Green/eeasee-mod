package dev.eeasee.eeaseemod.tick;


import dev.eeasee.eeaseemod.Main;
import dev.eeasee.eeaseemod.config.Configs;
import dev.eeasee.eeaseemod.config.GuiConfig;
import dev.eeasee.eeaseemod.ticktasks.display.ExplosiveDamageDisplay;
import dev.eeasee.eeaseemod.utils.render.LinesAndBoxes;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;

import java.awt.*;

public class OnClientTick implements ClientTickCallback{

    private static boolean isMenuToOpen = false;

    @Override
    public void tick(MinecraftClient client) {
        if (Main.MASTER_CONTROL.isPressed() || isMenuToOpen) {
            isMenuToOpen = false;
            openGUIConfig(client);
        }

        if (Configs.SHOW_EXPLOSION_DAMAGE.getBooleanValue()){
            ExplosiveDamageDisplay.searchPossibleDamagesToPlayer(client);
        }
    }

    private static void openGUIConfig(MinecraftClient client) {

        if (client.skipGameRender || MinecraftClient.getInstance().world == null) return;
        client.openScreen(new GuiConfig());
    }

    public static void setIsMenuToOpen(boolean b) {
        isMenuToOpen = b;
    }

}
