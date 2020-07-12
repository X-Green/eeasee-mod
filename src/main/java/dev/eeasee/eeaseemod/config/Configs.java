package dev.eeasee.eeaseemod.config;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigDouble;

import java.util.ArrayList;
import java.util.List;

public class Configs {

    public static final ConfigBoolean   SHOW_EXPLOSION_DAMAGE;

    public static final ConfigBoolean   ENABLE_CUSTOM_DINNERBONES;
    public static final ConfigDouble    ENTITY_Z_ROTATION;
    public static final ConfigDouble    ENTITY_Y_SHIFT;
    public static final ConfigBoolean   ENTITY_Y_ROTATE_WITH_POSITION;
    public static final ConfigDouble    ENTITY_SPINNING_SPEED;

    public static final ConfigBoolean   SHI_JIAN_GUAN_LI;


    static {
        SHOW_EXPLOSION_DAMAGE           = Category.SURVIVAL.add(new ConfigBoolean("eeaseemod.config.isExplosionDamageDisplayed", false, "......"));

        ENABLE_CUSTOM_DINNERBONES       = Category.MIND_BOOM.add(new ConfigBoolean("eeaseemod.config.enableCustomDinnerbone", false, "......"));
        ENTITY_Z_ROTATION               = Category.MIND_BOOM.add(new ConfigDouble("eeaseemod.config.entityRotation", 0.0D,-180.0D, 180.0D, true,"......"));
        ENTITY_Y_SHIFT                  = Category.MIND_BOOM.add(new ConfigDouble("eeaseemod.config.entityYShift", 0.0D, -1.0D, 1.0D, true,"......"));
        ENTITY_Y_ROTATE_WITH_POSITION   = Category.MIND_BOOM.add(new ConfigBoolean("eeaseemod.config.enableYRotateWithPosition", false, "......"));
        ENTITY_SPINNING_SPEED           = Category.MIND_BOOM.add(new ConfigDouble("eeaseemod.config.entitySpinningSpeed", 1.0D, 0.0D, 2.0D, true, "......"));

        SHI_JIAN_GUAN_LI                = Category.MIND_BOOM.add(new ConfigBoolean("eeaseemod.config.shiJianGuanLi", false, "时间管理秘术"));
    }

    public enum Category {
        SURVIVAL("eeaseemod.gui.setting_screen.survival"),
        MIND_BOOM("eeaseemod.gui.setting_screen.mind_boom");

        private final String key;
        private final List<IConfigBase> configs;

        private Category(String key) {
            this.key = key;
            configs = new ArrayList<>();
        }

        protected <T extends IConfigBase> T add(T config) {
            this.configs.add(config);
            return config;
        }

        public List<IConfigBase> getConfigs() {
            return ImmutableList.copyOf(this.configs);
        }

        public String getKey() {
            return this.key;
        }
    }

}
