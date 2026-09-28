package com.vi7p.autoiron;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class AutoIronMod implements ClientModInitializer {

    private static final int CHECK_INTERVAL = 20;
    private static int tickCounter = 0;

    private static final Item[] REQUIRED_ARMOR = {
            Items.IRON_HELMET,
            Items.IRON_CHESTPLATE,
            Items.IRON_LEGGINGS,
            Items.IRON_BOOTS
    };

    private static final Item[] REQUIRED_TOOLS = {
            Items.IRON_SWORD,
            Items.IRON_PICKAXE,
            Items.IRON_AXE,
            Items.IRON_SHOVEL
    };

    @Override
    public void onInitializeClient() {

        System.out.println("[AutoIron] Client Auto Iron loaded!");

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (client.player == null ||
                    client.world == null ||
                    client.getNetworkHandler() == null) {
                return;
            }

            tickCounter++;

            if (tickCounter < CHECK_INTERVAL) {
                return;
            }

            tickCounter = 0;

            checkArmor(client);
            checkTools(client);
        });
    }

    private static void checkArmor(MinecraftClient client) {

        if (!hasItem(client, Items.IRON_HELMET)) {
            give(client, "minecraft:iron_helmet");
        }

        if (!hasItem(client, Items.IRON_CHESTPLATE)) {
            give(client, "minecraft:iron_chestplate");
        }

        if (!hasItem(client, Items.IRON_LEGGINGS)) {
            give(client, "minecraft:iron_leggings");
        }

        if (!hasItem(client, Items.IRON_BOOTS)) {
            give(client, "minecraft:iron_boots");
        }
    }

    private static void checkTools(MinecraftClient client) {

        if (!hasItem(client, Items.IRON_SWORD)) {
            give(client, "minecraft:iron_sword");
        }

        if (!hasItem(client, Items.IRON_PICKAXE)) {
            give(client, "minecraft:iron_pickaxe");
        }

        if (!hasItem(client, Items.IRON_AXE)) {
            give(client, "minecraft:iron_axe");
        }

        if (!hasItem(client, Items.IRON_SHOVEL)) {
            give(client, "minecraft:iron_shovel");
        }
    }

    private static boolean hasItem(
            MinecraftClient client,
            Item item
    ) {

        // Main inventory + hotbar
        for (ItemStack stack :
                client.player.getInventory().getMainStacks()) {

            if (!stack.isEmpty() && stack.isOf(item)) {
                return true;
            }
        }

        // Armour slots
        for (ItemStack stack :
                client.player.getInventory().getArmorStacks()) {

            if (!stack.isEmpty() && stack.isOf(item)) {
                return true;
            }
        }

        // Off-hand
        ItemStack offHand =
                client.player.getOffHandStack();

        if (!offHand.isEmpty() && offHand.isOf(item)) {
            return true;
        }

        return false;
    }

    private static void give(
            MinecraftClient client,
            String item
    ) {

        if (client.getNetworkHandler() == null) {
            return;
        }

        client.getNetworkHandler().sendChatCommand(
                "give @s " + item
        );

        System.out.println(
                "[AutoIron] Requested: " + item
        );
    }
}