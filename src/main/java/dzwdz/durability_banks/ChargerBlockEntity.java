package dzwdz.durability_banks;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;

public class ChargerBlockEntity extends BlockEntity implements ImplementedInventory, NamedScreenHandlerFactory, Tickable {
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(2, ItemStack.EMPTY);

    private int charge = 0;

    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            return charge;
        }

        @Override
        public void set(int index, int value) {
            charge = value;
        }

        @Override
        public int size() {
            return 1;
        }
    };

    public ChargerBlockEntity() {
        super(EntryPoint.CHARGER_BLOCK_ENTITY);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        Inventories.fromTag(tag, items);
        charge = tag.getInt("charge");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        Inventories.toTag(tag, items);
        tag.putInt("charge", charge);
        return super.toTag(tag);
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new ChargerScreenHandler(syncId, inv, this, propertyDelegate);
    }

    @Override
    public void tick() {
        if (charge <= 0) {
            ItemStack fuel = items.get(1);
            if (fuel.getItem().isIn(EntryPoint.FUEL)) {
                fuel.decrement(1);
                charge += EntryPoint.FUEL_POWER;
            }
        } else {
            ItemStack bank = items.get(0);
            // i'm pretty sure i shouldn't do that this way
            if (bank.getItem() instanceof EmptyDurabilityBank) {
                bank = new ItemStack(((EmptyDurabilityBank)bank.getItem()).base);
                bank.setDamage(bank.getMaxDamage());
                items.set(0, bank);
            }
            if (bank.getItem() instanceof DurabilityBank && bank.getDamage() > 0) {
                bank.setDamage(bank.getDamage() - 1);
                charge -= 1;
            }
        }
    }
}
