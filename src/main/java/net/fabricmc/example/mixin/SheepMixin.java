package net.fabricmc.example.mixin;

import btw.entity.mob.SheepEntity;
import btw.item.BTWItems;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(SheepEntity.class)
public class SheepMixin {

	@Inject(method = "isBreedingItem", at = @At("TAIL"), cancellable = true)
	private void injectIsBreedingItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (stack !=null && (stack.itemID == Item.pumpkinPie.itemID || stack.itemID == BTWItems.wheat.itemID)) {
			cir.setReturnValue(true);
		}
	}
}