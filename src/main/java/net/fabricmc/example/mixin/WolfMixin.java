package net.fabricmc.example.mixin;

import btw.client.fx.BTWEffectManager;
import btw.entity.mob.WolfEntity;
import btw.item.BTWItems;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(WolfEntity.class)
public abstract class WolfMixin {

	@Inject(method = "isBreedingItem", at = @At("TAIL"), cancellable = true)
	private void injectIsBreedingItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (stack != null && (stack.itemID == BTWItems.rawMysteryMeat.itemID || stack.itemID == BTWItems.cookedMysteryMeat.itemID || stack.itemID == Item.fishRaw.itemID || stack.itemID == Item.fishCooked.itemID)) {
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "isEdibleItem", at = @At("HEAD"), cancellable = true)
	private void isEdibleItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (stack.getItem() == Item.fishRaw || stack.getItem() == Item.fishCooked) {
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "onEat", at = @At("HEAD"), cancellable = true)
	public void onEat(Item food, CallbackInfo ci) {
		WolfEntity wolf = (WolfEntity) (Object) this;
		World world = wolf.worldObj;

		if (food.itemID == BTWItems.rawMysteryMeat.itemID || food.itemID == BTWItems.cookedMysteryMeat.itemID || food.itemID == Item.fishRaw.itemID || food.itemID == Item.fishCooked.itemID) {

			wolf.heal(food.getWolfHealAmount());

			int iHungerLevel = wolf.getHungerLevel();
			if (iHungerLevel > 0) {
				wolf.setHungerLevel(iHungerLevel - 1);
			}

			wolf.resetHungerCountdown();

			if (!world.isRemote) {
				world.playAuxSFX(BTWEffectManager.WOLF_EATING_EFFECT_ID,
						MathHelper.floor_double(wolf.posX), (int) (wolf.posY + wolf.height),
						MathHelper.floor_double(wolf.posZ), 0);
			}

			wolf.onEatBreedingItem();

			ci.cancel();
		}
	}
}