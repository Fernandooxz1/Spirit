package com.fernando.modding;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.protocol.game.ClientboundOpenBookPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.item.component.WrittenBookContent;
import net.minecraft.world.level.Level;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.ArrayList;

public class Bible extends Item {

    public Bible(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand){
        ItemStack itemstack = player.getItemInHand(hand);


        if (level.isClientSide()){
            net.minecraft.client.Minecraft.getInstance().setScreen(new BibleScreen());
        }

        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}
