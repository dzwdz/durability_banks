package dzwdz.durability_banks;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EntryPoint implements ModInitializer {
    public static final String MODID = "durability_banks";

    public static final DurabilityBank DURABILITY_BANK = new DurabilityBank(new Item.Settings().group(ItemGroup.TOOLS).maxDamage(500));

    public static final ChargerBlock CHARGER_BLOCK = new ChargerBlock(FabricBlockSettings.of(Material.METAL).hardness(10f));
    public static BlockEntityType<ChargerBlockEntity> CHARGER_BLOCK_ENTITY;

    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, new Identifier(MODID, "basic"), DURABILITY_BANK);
        Registry.register(Registry.BLOCK, new Identifier(MODID, "charger"), CHARGER_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(MODID, "charger"), new BlockItem(CHARGER_BLOCK, new Item.Settings().group(ItemGroup.MISC)));

        CHARGER_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "charger"), BlockEntityType.Builder.create(ChargerBlockEntity::new, CHARGER_BLOCK).build(null));
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
