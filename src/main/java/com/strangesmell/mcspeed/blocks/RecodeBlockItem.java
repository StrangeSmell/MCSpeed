package com.strangesmell.mcspeed.blocks;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;


public class RecodeBlockItem extends BlockItem {
    public String mapName;
    public String laps;

    public RecodeBlockItem(Block p_40565_, Properties p_40566_) {
        super(p_40565_, p_40566_);
    }
    public InteractionResult useOn(UseOnContext context) {
        if(!new BlockPlaceContext(context).canPlace()){

            return InteractionResult.CONSUME;
        }else return super.useOn(context);
    }

/*    public InteractionResultHolder<ItemStack> use(Level level, Player p_41433_, InteractionHand p_41434_) {
        ItemStack itemstack = p_41433_.getItemInHand(p_41434_);
        if(level.isClientSide){
            RecodeScreen recodeScreen = new RecodeScreen(Component.translatable("mcspeed.recode_screen"),null);
            Minecraft.getInstance().setScreen(recodeScreen);
        }
        if (itemstack.isEdible()) {
            if (p_41433_.canEat(itemstack.getFoodProperties(p_41433_).canAlwaysEat())) {
                p_41433_.startUsingItem(p_41434_);
                return InteractionResultHolder.consume(itemstack);
            } else {
                return InteractionResultHolder.fail(itemstack);
            }
        } else {
            return InteractionResultHolder.pass(p_41433_.getItemInHand(p_41434_));
        }
    }*/
}
