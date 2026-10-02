package com.fernando.modding;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class NotebookCraftingRecipe extends CustomRecipe {
    public NotebookCraftingRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        int countGold = 0;
        int countLeather = 0;
        int countPaper = 0;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }

            if (stack.is(Items.GOLD_INGOT)) {
                countGold += stack.getCount();
            } else if (stack.is(Items.LEATHER)) {
                countLeather += stack.getCount();
            } else if (stack.is(Items.PAPER)) {
                countPaper += stack.getCount();
            } else {
                return false;
            }
        }

        return countGold >= 1 && countLeather >= 2 && countPaper >= 10;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        return new ItemStack(Spirit.NOTEBOOK.get());
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return new ItemStack(Spirit.NOTEBOOK.get());
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Spirit.NOTEBOOK_RECIPE_SERIALIZER.get();
    }
}
