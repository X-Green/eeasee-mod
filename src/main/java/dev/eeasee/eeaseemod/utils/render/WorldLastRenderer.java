package dev.eeasee.eeaseemod.utils.render;

import fi.dy.masa.malilib.interfaces.IRenderer;
import net.minecraft.client.MinecraftClient;

public class WorldLastRenderer implements IRenderer {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    @Override
    public void onRenderWorldLast(float partialTicks, net.minecraft.client.util.math.MatrixStack matrixStack)
    {
        if (mc.player != null)
        {
            this.renderOverlays(matrixStack, mc);
        }
    }


    private void renderOverlays(net.minecraft.client.util.math.MatrixStack matrixStack, MinecraftClient mc)
    {
    }

}
