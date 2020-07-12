package dev.eeasee.eeaseemod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.entity.mob.CreeperEntity;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CreeperEntity.class)
public interface IMixinEntityCreeper {
    @Accessor(value = "explosionRadius")
    int getExplosionRadius();
}
