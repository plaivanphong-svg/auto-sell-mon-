package com.example.autosell;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;

public final class AutoSellManager {
    private AutoSellManager(){}
    public static void trySell(MinecraftClient client) {
        if(!AutoSellConfig.enabled || client.player==null || client.getNetworkHandler()==null || AutoSellConfig.selected.isEmpty()) return;
        boolean hasSelected=false;
        for(ItemStack stack:client.player.getInventory().getMainStacks()) if(!stack.isEmpty() && AutoSellConfig.selected.contains(AutoSellConfig.id(stack.getItem()))) { hasSelected=true; break; }
        if(hasSelected) { String cmd=AutoSellConfig.command.trim(); if(cmd.startsWith("/")) cmd=cmd.substring(1); if(!cmd.isBlank()) client.getNetworkHandler().sendChatCommand(cmd); }
    }
}
