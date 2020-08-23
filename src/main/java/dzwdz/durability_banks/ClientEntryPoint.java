package dzwdz.durability_banks;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

@Environment(EnvType.CLIENT)
public class ClientEntryPoint implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(EntryPoint.CHARGER_SCREEN_HANDLER, ChargerScreen::new);
    }
}
