package net.fabricmc.example.mixin;

import btw.entity.mob.ChickenEntity;
import btw.item.BTWItems;
import net.minecraft.src.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(ChickenEntity.class)
public abstract class ChickenMixin {

	@Inject(method = "isBreedingItem", at = @At("TAIL"), cancellable = true)
	private void injectIsBreedingItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (stack !=null && (stack.itemID == BTWItems.chickenFeed.itemID || stack.itemID == BTWItems.hempSeeds.itemID)) {
			cir.setReturnValue(true);
		}
	}
}