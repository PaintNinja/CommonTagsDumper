package ga.ozli.minecraftmods.commontagsdumper;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

@Mod(Constants.MOD_ID)
public class CommonTagsDumperNeoForge {

    public CommonTagsDumperNeoForge(IEventBus eventBus) {
        NeoForge.EVENT_BUS.addListener(CommonTagsDumperNeoForge::onServerStarted);
    }

    public static void onServerStarted(ServerStartedEvent event) {
        // get NeoForge version
        var neoForgeVersion = ModList.get()
                .getModFileById("neoforge")
                .getMods()
                .getFirst()
                .getVersion();
        var neoForgeVersionString = neoForgeVersion.getMajorVersion() + "." + neoForgeVersion.getMinorVersion() + "." + neoForgeVersion.getIncrementalVersion();
        if (neoForgeVersion.getQualifier() != null)
            neoForgeVersionString += "-" + neoForgeVersion.getQualifier();

        CommonClass.dumpTags(neoForgeVersionString, event.getServer());

        // Uncomment to also dump explicitly NeoForge-specific tags
        // Note: Some `c` tags are also NeoForge-specific
        //CommonClass.dumpTags(forgeVersionString, event.getServer(), Set.of("c", "neoforge"));
    }
}
