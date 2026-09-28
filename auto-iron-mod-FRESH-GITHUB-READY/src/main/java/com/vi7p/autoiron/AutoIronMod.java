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
            checkShield(client);
        });
    }

    private static void checkArmor(MinecraftClient client) {

        if (!hasEqualOrBetterArmor(client, ArmorType.HELMET)) {
            give(client, "minecraft:iron_helmet");
        }

        if (!hasEqualOrBetterArmor(client, ArmorType.CHESTPLATE)) {
            give(client, "minecraft:iron_chestplate");
        }

        if (!hasEqualOrBetterArmor(client, ArmorType.LEGGINGS)) {
            give(client, "minecraft:iron_leggings");
        }

        if (!hasEqualOrBetterArmor(client, ArmorType.BOOTS)) {
            give(client, "minecraft:iron_boots");
        }
    }

    private static void checkTools(MinecraftClient client) {

        if (!hasEqualOrBetterTool(client, ToolType.SWORD)) {
            give(client, "minecraft:iron_sword");
        }

        if (!hasEqualOrBetterTool(client, ToolType.PICKAXE)) {
            give(client, "minecraft:iron_pickaxe");
        }

        if (!hasEqualOrBetterTool(client, ToolType.AXE)) {
            give(client, "minecraft:iron_axe");
        }

        if (!hasEqualOrBetterTool(client, ToolType.SHOVEL)) {
            give(client, "minecraft:iron_shovel");
        }
    }

    private static void checkShield(MinecraftClient client) {

        if (!hasItem(client, Items.SHIELD)) {
            give(client, "minecraft:shield");
        }
    }

    /*
     * Checks every inventory slot:
     *
     * 0-8   = Hotbar
     * 9-35  = Main inventory
     * 36-39 = Armor
     * 40    = Off-hand
     */
    private static boolean hasItem(
            MinecraftClient client,
            Item item
    ) {

        for (int slot = 0; slot <= 40; slot++) {

            ItemStack stack =
                    client.player.getInventory().getStack(slot);

            if (!stack.isEmpty() && stack.isOf(item)) {
                return true;
            }
        }

        return false;
    }

    /*
     * ARMOR
     */

    private enum ArmorType {
        HELMET,
        CHESTPLATE,
        LEGGINGS,
        BOOTS
    }

    private static boolean hasEqualOrBetterArmor(
            MinecraftClient client,
            ArmorType armorType
    ) {

        switch (armorType) {

            case HELMET:

                if (hasItem(client, Items.IRON_HELMET)) {
                    return true;
                }

                if (hasItem(client, Items.DIAMOND_HELMET)) {
                    return true;
                }

                if (hasItem(client, Items.NETHERITE_HELMET)) {
                    return true;
                }

                break;

            case CHESTPLATE:

                if (hasItem(client, Items.IRON_CHESTPLATE)) {
                    return true;
                }

                if (hasItem(client, Items.DIAMOND_CHESTPLATE)) {
                    return true;
                }

                if (hasItem(client, Items.NETHERITE_CHESTPLATE)) {
                    return true;
                }

                break;

            case LEGGINGS:

                if (hasItem(client, Items.IRON_LEGGINGS)) {
                    return true;
                }

                if (hasItem(client, Items.DIAMOND_LEGGINGS)) {
                    return true;
                }

                if (hasItem(client, Items.NETHERITE_LEGGINGS)) {
                    return true;
                }

                break;

            case BOOTS:

                if (hasItem(client, Items.IRON_BOOTS)) {
                    return true;
                }

                if (hasItem(client, Items.DIAMOND_BOOTS)) {
                    return true;
                }

                if (hasItem(client, Items.NETHERITE_BOOTS)) {
                    return true;
                }

                break;
        }

        return false;
    }

    /*
     * TOOLS
     */

    private enum ToolType {
        SWORD,
        PICKAXE,
        AXE,
        SHOVEL
    }

    private static boolean hasEqualOrBetterTool(
            MinecraftClient client,
            ToolType toolType
    ) {

        switch (toolType) {

            case SWORD:

                if (hasItem(client, Items.IRON_SWORD)) {
                    return true;
                }

                if (hasItem(client, Items.DIAMOND_SWORD)) {
                    return true;
                }

                if (hasItem(client, Items.NETHERITE_SWORD)) {
                    return true;
                }

                break;

            case PICKAXE:

                if (hasItem(client, Items.IRON_PICKAXE)) {
                    return true;
                }

                if (hasItem(client, Items.DIAMOND_PICKAXE)) {
                    return true;
                }

                if (hasItem(client, Items.NETHERITE_PICKAXE)) {
                    return true;
                }

                break;

            case AXE:

                if (hasItem(client, Items.IRON_AXE)) {
                    return true;
                }

                if (hasItem(client, Items.DIAMOND_AXE)) {
                    return true;
                }

                if (hasItem(client, Items.NETHERITE_AXE)) {
                    return true;
                }

                break;

            case SHOVEL:

                if (hasItem(client, Items.IRON_SHOVEL)) {
                    return true;
                }

                if (hasItem(client, Items.DIAMOND_SHOVEL)) {
                    return true;
                }

                if (hasItem(client, Items.NETHERITE_SHOVEL)) {
                    return true;
                }

                break;
        }

        return false;
    }

    /*
     * Sends the normal Minecraft /give command.
     */
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