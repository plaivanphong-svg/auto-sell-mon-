package com.example.autosell;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class AutoSellClient implements ClientModInitializer {
    private static boolean keyWasDown=false;
    private static int tickCounter=0;
    @Override public void onInitializeClient() {
        AutoSellConfig.load();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            long window=client.getWindow().getHandle(); boolean down=GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT_SHIFT)==GLFW.GLFW_PRESS;
            if(down && !keyWasDown && client.currentScreen==null) client.setScreen(new AutoSellScreen());
            keyWasDown=down;
            if(++tickCounter >= Math.max(40,AutoSellConfig.delaySeconds*20)) { tickCounter=0; AutoSellManager.trySell(client); }
        });
    }
}
