package dzwdz.durability_banks;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static net.minecraft.util.math.MathHelper.ceil;

public class ChargerScreen extends HandledScreen<ScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(EntryPoint.MODID, "textures/gui/container/charger.png");
    ChargerScreenHandler screenHandler;

    public ChargerScreen(ScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundHeight = 133;
        this.playerInventoryTitleY = this.backgroundHeight - 94;

        screenHandler = (ChargerScreenHandler) handler;
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        client.getTextureManager().bindTexture(TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);

        int barHeight = ceil(screenHandler.getCharge()*12./EntryPoint.FUEL_POWER);
        if (barHeight > 3) barHeight++;
        if (barHeight > 10) barHeight++;
        drawTexture(matrices, x + 85, y + 35 - barHeight, 0, backgroundHeight + 14 - barHeight, 6, barHeight);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }
}
