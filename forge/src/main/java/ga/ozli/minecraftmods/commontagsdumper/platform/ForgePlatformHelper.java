package ga.ozli.minecraftmods.commontagsdumper.platform;

import ga.ozli.minecraftmods.commontagsdumper.platform.services.IPlatformHelper;
import net.minecraftforge.fml.loading.FMLLoader;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Forge";
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }
}