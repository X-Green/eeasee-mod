package dev.eeasee.eeaseemod.ticktasks.display;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import dev.eeasee.eeaseemod.mixin.IMixinEntityCreeper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.List;
import java.util.Map;

public class ExplosiveDamageDisplay {

    private static Map<EntityType, Float> EXPLOSION_POWER = ImmutableMap.of(
            EntityType.TNT, 4.0F,
            EntityType.TNT_MINECART, 4.0F,
            EntityType.END_CRYSTAL, 6.0F,
            EntityType.CREEPER, 3.0F
            );

    public static void searchPossibleDamagesToPlayer(MinecraftClient client){
        PlayerEntity player = client.player;
        World world = client.world;
        if (world == null){return;}

        EntityType explosiveType = null;
        List<Entity> explosives = getClosestExplosives(player.getPos(), world);
        double damageCaused = 0.0;
        for (Entity explosive: explosives){
            if (explosive == null){continue;}
            EntityType t = explosive.getType();
            double explosionHeight = 0.0D;
            float explosionPower = EXPLOSION_POWER.get(t);
            if (t == EntityType.TNT){
                explosionHeight = (double)(0.98F / 16.0F);
            }else if (t == EntityType.CREEPER){
                explosionPower = ((IMixinEntityCreeper) explosive).getExplosionRadius();
                explosionPower = explosionPower * (((CreeperEntity)explosive).shouldRenderOverlay() ? 2.0F: 1.0F);
            }

            double d = explosionDamagePlayerEntity(player, explosive.getPos().add(0,explosionHeight,0), explosionPower, explosive);
            if (d > damageCaused){
                damageCaused = d;

                explosiveType = explosive.getType();
            }
        }
        if (damageCaused > 0){
            showInfoOnHUD(damageCaused, client, explosiveType);
        }
    }

    private static void showInfoOnHUD(double damage, MinecraftClient client, EntityType entityType){
        client.player.addChatMessage(new LiteralText(damage + "   " + entityType.getTranslationKey()), true);

        //drawTextOverlayHoveringOnBar(damage + "   " + entityType.getTranslationKey(), client);

        //System.out.println(1);
    }



    private static List<Entity> getClosestExplosives(Vec3d playerPos, World world){
        Box boxAroundPlayer = new Box(playerPos.add(20,20,20), playerPos.add(-20,-20,-20));
        List<Entity> explosives = world.getEntities((Entity)null, boxAroundPlayer);
        List<Entity> closests = Lists.newArrayList();
        closests.add(getClosestEntityWithType(playerPos, explosives, EntityType.TNT));
        closests.add(getClosestEntityWithType(playerPos, explosives, EntityType.TNT_MINECART));
        closests.add(getClosestEntityWithType(playerPos, explosives, EntityType.END_CRYSTAL));
        closests.add(getClosestEntityWithType(playerPos, explosives, EntityType.CREEPER));
        return closests;
    }

    private static Entity getClosestEntityWithType(Vec3d playerPos, List<Entity> entities, EntityType type){
        double r = Double.MAX_VALUE;
        Entity e = null;
        for (Entity entity: entities) {
            if (entity.getType() == type){
                if (entity.getPos().distanceTo(playerPos) < r){
                    e = entity;
                    r = entity.getPos().distanceTo(playerPos);
                }
            }
        }
        return e;
    }


    private static float explosionDamagePlayerEntity(PlayerEntity playerEntity, Vec3d explosionPos, float power, Entity entityFrom) {
        float r = power * 2.0F;
        float damageAmount = 0.0F;
        //if (!playerEntity.isImmuneToExplosion())
        {
            double z = (double)(MathHelper.sqrt(playerEntity.squaredDistanceTo(explosionPos)) / r);
            if (z <= 1.0D) {
                double aa = playerEntity.getX() - explosionPos.x;
                double ab = playerEntity.getY() + (double)playerEntity.getStandingEyeHeight() - explosionPos.y;
                double ac = playerEntity.getZ() - explosionPos.z;
                double ad = (double)MathHelper.sqrt(aa * aa + ab * ab + ac * ac);
                if (ad != 0.0D) {
                    double ae = (double)getExposure(explosionPos, playerEntity);
                    double af = (1.0D - z) * ae;
                    damageAmount =  (float)((int)((af * af + af) / 2.0D * 7.0D * (double)r + 1.0D));

                    damageAmount = damagePlayer(damageAmount, playerEntity , entityFrom, power);
                }
            }
        }
        return damageAmount;
    }

    private static float getExposure(Vec3d source, Entity entity) {
        Box box = entity.getBoundingBox();
        double d = 1.0D / ((box.x2 - box.x1) * 2.0D + 1.0D);
        double e = 1.0D / ((box.y2 - box.y1) * 2.0D + 1.0D);
        double f = 1.0D / ((box.z2 - box.z1) * 2.0D + 1.0D);
        double g = (1.0D - Math.floor(1.0D / d) * d) / 2.0D;
        double h = (1.0D - Math.floor(1.0D / f) * f) / 2.0D;
        if (d >= 0.0D && e >= 0.0D && f >= 0.0D) {
            int i = 0;
            int j = 0;

            for(float k = 0.0F; k <= 1.0F; k = (float)((double)k + d)) {
                for(float l = 0.0F; l <= 1.0F; l = (float)((double)l + e)) {
                    for(float m = 0.0F; m <= 1.0F; m = (float)((double)m + f)) {
                        double n = MathHelper.lerp(k, box.x1, box.x2);
                        double o = MathHelper.lerp(l, box.y1, box.y2);
                        double p = MathHelper.lerp(m, box.z1, box.z2);
                        Vec3d vec3d = new Vec3d(n + g, o, p + h);
                        if (entity.world.rayTrace(new RayTraceContext(vec3d, source, RayTraceContext.ShapeType.OUTLINE, RayTraceContext.FluidHandling.NONE, entity)).getType() == HitResult.Type.MISS) {
                            ++i;
                        }

                        ++j;
                    }
                }
            }

            return (float)i / (float)j;
        } else {
            return 0.0F;
        }
    }

    private static float damagePlayer(float amount, PlayerEntity player, Entity entityFrom, float power){
        World world = player.world;

        if (world.getDifficulty() == Difficulty.PEACEFUL) {
            amount = 0.0F;
        }

        if (world.getDifficulty() == Difficulty.EASY) {
            amount = Math.min(amount / 2.0F + 1.0F, amount);
        }

        if (world.getDifficulty() == Difficulty.HARD) {
            amount = amount * 3.0F / 2.0F;
        }

        amount = DamageUtil.getDamageLeft(amount, (float)player.getArmor(), (float)player.getAttributeInstance(EntityAttributes.ARMOR_TOUGHNESS).getValue());
        amount = applyMagicProtection(amount, player, entityFrom, power);

        if (player.isImmuneToExplosion()){
            amount = 0.0F;
        }

        return amount;
    }

    private static float applyMagicProtection(float amount, PlayerEntity player, Entity entityFrom, float power){
        int k;
        if (player.hasStatusEffect(StatusEffects.RESISTANCE)) {
            k = (player.getStatusEffect(StatusEffects.RESISTANCE).getAmplifier() + 1) * 5;
            int j = 25 - k;
            float f = amount * (float)j;
            float g = amount;
            amount = Math.max(f / 25.0F, 0.0F);
        }

        if (amount <= 0.0F) {
            return 0.0F;
        } else {
            k = EnchantmentHelper.getProtectionAmount(player.getArmorItems(), DamageSource.explosion(new Explosion(entityFrom.world, entityFrom, entityFrom.getX(), entityFrom.getY(), entityFrom.getZ(), power, false, Explosion.DestructionType.NONE)));
            if (k > 0) {
                amount = DamageUtil.getInflictedDamage(amount, (float)k);
            }

            return amount;
        }
    }


}
