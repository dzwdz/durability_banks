package dzwdz.durability_banks.mixin;

import dzwdz.durability_banks.DurabilityBank;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class EnchantmentMixin {
    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/enchantment/Enchantment;isAcceptableItem(Lnet/minecraft/item/ItemStack;)Z", cancellable = true)
    public void disallowEnchantingBanks(ItemStack itemStack, CallbackInfoReturnable callbackInfo) {
        if (itemStack.getItem() instanceof DurabilityBank) callbackInfo.setReturnValue(false);
    }
}
