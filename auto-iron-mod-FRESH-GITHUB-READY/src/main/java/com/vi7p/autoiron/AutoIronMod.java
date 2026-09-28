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
        System.out.println("[AutoIron] Loaded!");

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                if (player.isSpectator() || player.isCreative() || !player.isAlive()) {
                    continue;
                }

                boolean changed = false;
                changed |= ensureArmor(player, EquipmentSlot.HEAD, Items.IRON_HELMET);
                changed |= ensureArmor(player, EquipmentSlot.CHEST, Items.IRON_CHESTPLATE);
                changed |= ensureArmor(player, EquipmentSlot.LEGS, Items.IRON_LEGGINGS);
                changed |= ensureArmor(player, EquipmentSlot.FEET, Items.IRON_BOOTS);

                for (Item tool : REQUIRED_TOOLS) {
                    if (!hasItem(player, tool)) {
                        player.giveItemStack(new ItemStack(tool));
                        changed = true;
                    }
                }

                if (changed) {
                    player.playerScreenHandler.sendContentUpdates();
                }
            }
        });
    }

    private static boolean ensureArmor(ServerPlayerEntity player, EquipmentSlot slot, Item item) {
        ItemStack equipped = player.getEquippedStack(slot);
        if (!equipped.isEmpty() && equipped.isOf(item) && equipped.getDamage() < equipped.getMaxDamage()) {
            return false;
        }
        player.equipStack(slot, new ItemStack(item));
        return true;
    }

    private static boolean hasItem(ServerPlayerEntity player, Item item) {
        for (ItemStack stack : player.getInventory().main) {
            if (!stack.isEmpty() && stack.isOf(item)) {
                return true;
            }
        }
        for (ItemStack stack : player.getInventory().offHand) {
            if (!stack.isEmpty() && stack.isOf(item)) {
                return true;
            }
        }
        return false;
    }
}
