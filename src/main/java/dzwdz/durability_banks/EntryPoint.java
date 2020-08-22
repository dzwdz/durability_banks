package dzwdz.durability_banks;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EntryPoint implements ModInitializer {
    public static final DurabilityBank DURABILITY_BANK = new DurabilityBank(new Item.Settings().group(ItemGroup.TOOLS).maxDamage(500));

    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, new Identifier("durability_banks", "basic"), DURABILITY_BANK);
    }

    public static ItemStack getActiveDurabilityBank(ServerPlayerEntity player, int minimumCharge) {
        if (player == null) return null;
        for (ItemStack itemStack : player.getItemsHand()) {
            if (itemStack.getItem() instanceof DurabilityBank && itemStack.getDamage() + minimumCharge <= itemStack.getMaxDamage()) {
                return itemStack;
            }
        }
        return null;
    }
}
