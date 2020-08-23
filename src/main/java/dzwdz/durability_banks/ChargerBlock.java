package dzwdz.durability_banks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

public class ChargerBlock extends Block implements BlockEntityProvider {
    public ChargerBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new ChargerBlockEntity();
    }
}
