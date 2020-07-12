package dev.eeasee.eeaseemod.mixin;

import dev.eeasee.eeaseemod.command.commands.ClientTickSpeedCommand;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderTickCounter.class)
public class MixinRenderTickCounter_tickSpeed {
    @Shadow
    public float lastFrameDuration;

    @Shadow
    private long prevTimeMillis;

    @Inject(method = "beginRenderTick", at = @At(
            value = "FIELD", target = "Lnet/minecraft/client/render/RenderTickCounter;lastFrameDuration:F", opcode = 181, shift = At.Shift.AFTER
    ))
    private void adjustTickSpeed(long timeMillis, CallbackInfo ci) {
        if (ClientTickSpeedCommand.isClientTickSpeedChanged)
        {
            this.lastFrameDuration = (float)(timeMillis - this.prevTimeMillis) / ClientTickSpeedCommand.clientTickSpeed;
        }
    }
}
