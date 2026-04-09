package ga.ozli.minecraftmods.commontagsdumper.platform;

import ga.ozli.minecraftmods.commontagsdumper.platform.services.IPlatformHelper;
import net.neoforged.fml.ModList;

public final class NeoForgePlatformHelper implements IPlatformHelper {
    @Override
    public String getPlatformName() {
        return "NeoForge";
    }

    @Override
    public String getPlatformVersion() {
        return ModList.get()
                .getModFileById("neoforge")
                .getMods()
                .getFirst()
                .getVersion()
                .toString();
    }
}
