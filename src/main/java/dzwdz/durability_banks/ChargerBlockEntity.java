package dzwdz.durability_banks;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.util.math.MathHelper.ceil;

public class ChargerBlockEntity extends BlockEntity implements SidedInventory, ImplementedInventory, NamedScreenHandlerFactory, Tickable {
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(2, ItemStack.EMPTY);

    public final static int BANK_SLOT = 0;
    public final static int FUEL_SLOT = 1;
    
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
            ItemStack fuel = items.get(FUEL_SLOT);
            if (fuel.getItem().isIn(EntryPoint.FUEL)) {
                fuel.decrement(1);
                charge += EntryPoint.FUEL_POWER;
            }
        } else {
            ItemStack bank = items.get(BANK_SLOT);
            // i'm pretty sure i shouldn't do that this way
            if (bank.getItem() instanceof EmptyDurabilityBank) {
                bank = new ItemStack(((EmptyDurabilityBank)bank.getItem()).base);
                bank.setDamage(bank.getMaxDamage());
                items.set(BANK_SLOT, bank);
            }
            if (bank.getItem() instanceof DurabilityBank && bank.getDamage() > 0) {
                int initialComparatorOutput = getComparatorOutput();
                bank.setDamage(bank.getDamage() - 1);
                charge -= 1;
                if (getComparatorOutput() != initialComparatorOutput) markDirty();
            }
        }
    }

    public int getComparatorOutput() {
        ItemStack bank = getStack(ChargerBlockEntity.BANK_SLOT);
        if (!bank.isEmpty()) {
            return 15-ceil(14.*bank.getDamage()/bank.getMaxDamage());
        }
        return 0;
    }

    public static boolean _isValid(int slot, ItemStack stack) {
        if (slot == BANK_SLOT) return stack.getItem() instanceof DurabilityBank
                || stack.getItem() instanceof EmptyDurabilityBank;
        if (slot == FUEL_SLOT) return stack.getItem().isIn(EntryPoint.FUEL);
        return true;
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        return _isValid(slot, stack);
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return new int[]{BANK_SLOT, FUEL_SLOT};
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return _isValid(slot, stack);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return slot == BANK_SLOT;
    }
}
