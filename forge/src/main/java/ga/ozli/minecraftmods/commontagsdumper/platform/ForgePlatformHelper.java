package ga.ozli.minecraftmods.commontagsdumper.platform;

import ga.ozli.minecraftmods.commontagsdumper.platform.services.IPlatformHelper;
import net.minecraftforge.fml.ModList;

public final class ForgePlatformHelper implements IPlatformHelper {
    @Override
    public String getPlatformName() {
        return "Forge";
    }

    @Override
    public String getPlatformVersion() {
        return ModList.getModFileById("forge")
                .getMods()
                .getFirst()
                .getVersion()
                .toString();
    }
}
