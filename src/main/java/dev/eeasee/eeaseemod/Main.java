package dev.eeasee.eeaseemod;

import dev.eeasee.eeaseemod.config.ConfigHandler;
import dev.eeasee.eeaseemod.config.Configs;
import dev.eeasee.eeaseemod.tick.OnClientTick;
import dev.eeasee.eeaseemod.utils.render.WorldLastRenderer;
import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.RenderEventHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.fabricmc.fabric.impl.client.keybinding.KeyBindingRegistryImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

public class Main implements ModInitializer {


	public static final String MOD_ID = "eeaseemod";

	public static final FabricKeyBinding MASTER_CONTROL = FabricKeyBinding.Builder.create(
			new Identifier(MOD_ID, "master_control"),
			InputUtil.Type.KEYSYM,
			InputUtil.fromName("key.keyboard.u").getKeyCode(),
			"key.categories.ui").build();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		System.out.println("It's so eeasee!");
		ClientTickCallback.EVENT.register(new OnClientTick());
		RenderEventHandler.getInstance().registerWorldLastRenderer(new WorldLastRenderer());

		ConfigManager.getInstance().registerConfigHandler(MOD_ID, new ConfigHandler());
		new Configs();
		ConfigHandler.loadFile();
		KeyBindingRegistryImpl.INSTANCE.register(MASTER_CONTROL);
	}


}
