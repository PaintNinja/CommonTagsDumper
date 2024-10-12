package ga.ozli.minecraftmods.commontagsdumper;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class CommonTagsDumperForge {

    public CommonTagsDumperForge() {
        MinecraftForge.EVENT_BUS.addListener(CommonTagsDumperForge::onServerStarted);
    }

    public static void onServerStarted(ServerStartedEvent event) {
        var forgeVersion = ModList.get()
                .getModFileById("forge")
                .getMods()
                .getFirst()
                .getVersion();
        var forgeVersionString = forgeVersion.getMajorVersion() + "." + forgeVersion.getMinorVersion() + "." + forgeVersion.getIncrementalVersion();
        if (forgeVersion.getQualifier() != null)
            forgeVersionString += "-" + forgeVersion.getQualifier();

        CommonClass.dumpTags(forgeVersionString, event.getServer());

        // Uncomment to also dump Forge-specific tags
        //CommonClass.dumpTags(forgeVersionString, event.getServer(), Set.of("c", "forge"));
    }
}
