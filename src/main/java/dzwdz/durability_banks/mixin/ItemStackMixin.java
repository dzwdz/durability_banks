package dzwdz.durability_banks.mixin;

import dzwdz.durability_banks.DurabilityBank;
import dzwdz.durability_banks.EntryPoint;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract Item getItem();

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getDamage()I", ordinal = 1),
            method = "Lnet/minecraft/item/ItemStack;damage(ILjava/util/Random;Lnet/minecraft/server/network/ServerPlayerEntity;)Z", cancellable = true)
    public void hijackDamage(int amount, Random random, @Nullable ServerPlayerEntity player, CallbackInfoReturnable callbackInfoReturnable) {
        if (this.getItem() instanceof DurabilityBank) return;

        ItemStack bank = EntryPoint.getActiveDurabilityBank(player, amount);
        if (bank == null) return;

        bank.setDamage(bank.getDamage() + amount);
        callbackInfoReturnable.setReturnValue(false);
    }
}
