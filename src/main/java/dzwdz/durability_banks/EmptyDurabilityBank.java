package dzwdz.durability_banks;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class EmptyDurabilityBank extends Item {
    public DurabilityBank base;
    public EmptyDurabilityBank(Settings settings, DurabilityBank durabilityBank) {
        super(settings);
        base = durabilityBank;
    }
}
