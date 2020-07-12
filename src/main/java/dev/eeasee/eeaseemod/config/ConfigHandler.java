package dev.eeasee.eeaseemod.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.util.JsonUtils;

import java.io.File;

public class ConfigHandler implements IConfigHandler {
    private static final String FILE_PATH = "./config/eeaseemod.json";
    private static final File CONFIG_DIR = new File("./config");

    @Override
    public void load() {
        loadFile();
    }

    public static void loadFile() {
        File settingFile = new File(FILE_PATH);
        if (settingFile.isFile() && settingFile.exists()) {
            JsonElement jsonElement = JsonUtils.parseJsonFile(settingFile);
            if (jsonElement instanceof JsonObject) {
                ConfigUtils.readConfigBase((JsonObject)jsonElement, "Survival", Configs.Category.SURVIVAL.getConfigs());
                ConfigUtils.readConfigBase((JsonObject)jsonElement, "MindBoom", Configs.Category.MIND_BOOM.getConfigs());
            }
        }
    }

    @Override
    public void save() {
        saveFile();
    }

    private static void saveFile() {
        if ((CONFIG_DIR.exists() && CONFIG_DIR.isDirectory()) || CONFIG_DIR.mkdirs()) {
            JsonObject configRoot = new JsonObject();

            ConfigUtils.writeConfigBase(configRoot, "Survival", Configs.Category.SURVIVAL.getConfigs());
            ConfigUtils.writeConfigBase(configRoot, "MindBoom", Configs.Category.MIND_BOOM.getConfigs());

            JsonUtils.writeJsonToFile(configRoot, new File(FILE_PATH));
        }
    }

}
