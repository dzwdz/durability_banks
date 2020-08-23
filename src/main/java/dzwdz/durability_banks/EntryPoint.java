package dzwdz.durability_banks;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.*;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EntryPoint implements ModInitializer {
    public static final String MODID = "durability_banks";

    public static final DurabilityBank DURABILITY_BANK = new DurabilityBank(new Item.Settings().group(ItemGroup.TOOLS).maxDamage(500));

    public static final Identifier CHARGER = new Identifier(MODID, "charger");
    public static final ChargerBlock CHARGER_BLOCK = new ChargerBlock(FabricBlockSettings.of(Material.METAL).hardness(10f));
    public static BlockEntityType<ChargerBlockEntity> CHARGER_BLOCK_ENTITY;
    public static final ScreenHandlerType<ChargerScreenHandler> CHARGER_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(CHARGER, ChargerScreenHandler::new);

    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, new Identifier(MODID, "basic"), DURABILITY_BANK);
        Registry.register(Registry.BLOCK, CHARGER, CHARGER_BLOCK);
        Registry.register(Registry.ITEM, CHARGER, new BlockItem(CHARGER_BLOCK, new Item.Settings().group(ItemGroup.MISC)));

        CHARGER_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, CHARGER, BlockEntityType.Builder.create(ChargerBlockEntity::new, CHARGER_BLOCK).build(null));
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

    public static int getFuelPower(Item item) {
        if (item == Items.LAPIS_LAZULI) return 100;
        return 0;
    }
}
