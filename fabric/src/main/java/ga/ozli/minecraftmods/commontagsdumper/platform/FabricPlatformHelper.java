package ga.ozli.minecraftmods.commontagsdumper.platform;

import ga.ozli.minecraftmods.commontagsdumper.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

public final class FabricPlatformHelper implements IPlatformHelper {
    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public String getPlatformVersion() {
        return FabricLoader.getInstance().getModContainer("fabric-api")
                .orElseThrow()
                .getMetadata()
                .getVersion()
                .getFriendlyString();
    }
}
