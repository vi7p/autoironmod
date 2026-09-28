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

    private static final Item[] REQUIRED_TOOLS = {
            Items.IRON_SWORD,
            Items.IRON_PICKAXE,
            Items.IRON_AXE,
            Items.IRON_SHOVEL
    };

    private static final Item[] REQUIRED_ARMOR = {
            Items.IRON_HELMET,
            Items.IRON_CHESTPLATE,
            Items.IRON_LEGGINGS,
            Items.IRON_BOOTS
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

        if (!hasArmor(client, Items.IRON_HELMET)) {
            give(client, "minecraft:iron_helmet");
        }

        if (!hasArmor(client, Items.IRON_CHESTPLATE)) {
            give(client, "minecraft:iron_chestplate");
        }

        if (!hasArmor(client, Items.IRON_LEGGINGS)) {
            give(client, "minecraft:iron_leggings");
        }

        if (!hasArmor(client, Items.IRON_BOOTS)) {
            give(client, "minecraft:iron_boots");
        }
    }

    private static void checkTools(MinecraftClient client) {

        for (Item tool : REQUIRED_TOOLS) {

            if (!hasItem(client, tool)) {
                give(client, getItemId(tool));
            }
        }
    }

    private static boolean hasArmor(
            MinecraftClient client,
            Item item
    ) {

        for (ItemStack stack :
                client.player.getInventory().getArmorStacks()) {

            if (!stack.isEmpty() && stack.isOf(item)) {
                return true;
            }
        }

        return false;
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

    private static String getItemId(Item item) {

        if (item == Items.IRON_SWORD) {
            return "minecraft:iron_sword";
        }

        if (item == Items.IRON_PICKAXE) {
            return "minecraft:iron_pickaxe";
        }

        if (item == Items.IRON_AXE) {
            return "minecraft:iron_axe";
        }

        if (item == Items.IRON_SHOVEL) {
            return "minecraft:iron_shovel";
        }

        return "minecraft:air";
    }
}