package com.vi7p.autoiron;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;

public class AutoIronMod implements ModInitializer {

    private static final Item[] REQUIRED_TOOLS = {
            Items.IRON_SWORD,
            Items.IRON_PICKAXE,
            Items.IRON_AXE,
            Items.IRON_SHOVEL
    };

    @Override
    public void onInitialize() {
        System.out.println("[AutoIron] Auto Iron Mod loaded!");

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {

                if (player.isSpectator() || player.isCreative()) {
                    continue;
                }

                giveMissingArmor(player);
                giveMissingTools(player);
            }
        });
    }

    private static void giveMissingArmor(ServerPlayerEntity player) {
        giveArmor(player, EquipmentSlot.HEAD, Items.IRON_HELMET);
        giveArmor(player, EquipmentSlot.CHEST, Items.IRON_CHESTPLATE);
        giveArmor(player, EquipmentSlot.LEGS, Items.IRON_LEGGINGS);
        giveArmor(player, EquipmentSlot.FEET, Items.IRON_BOOTS);
    }

    private static void giveArmor(
            ServerPlayerEntity player,
            EquipmentSlot slot,
            Item item
    ) {
        ItemStack equipped = player.getEquippedStack(slot);

        if (!equipped.isEmpty() && equipped.isOf(item)) {
            return;
        }

        player.equipStack(slot, new ItemStack(item));
    }

    private static void giveMissingTools(ServerPlayerEntity player) {

        for (Item requiredTool : REQUIRED_TOOLS) {

            if (!hasTool(player, requiredTool)) {
                player.giveItemStack(new ItemStack(requiredTool));
            }
        }
    }

    private static boolean hasTool(
            ServerPlayerEntity player,
            Item requiredTool
    ) {
        // Main inventory + hotbar
        for (ItemStack stack : player.getInventory().getMainStacks()) {
            if (!stack.isEmpty() && stack.isOf(requiredTool)) {
                return true;
            }
        }

        // Check the currently equipped main-hand item
        ItemStack mainHand = player.getMainHandStack();
        if (!mainHand.isEmpty() && mainHand.isOf(requiredTool)) {
            return true;
        }

        // Check off-hand
        ItemStack offHand = player.getOffHandStack();
        if (!offHand.isEmpty() && offHand.isOf(requiredTool)) {
            return true;
        }

        return false;
    }
}
