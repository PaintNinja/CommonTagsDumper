package ga.ozli.minecraftmods.commontagsdumper;

import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

@Mod(Constants.MOD_ID)
public final class CommonTagsDumperNeoForge {
    public CommonTagsDumperNeoForge() {
        NeoForge.EVENT_BUS.addListener(CommonTagsDumperNeoForge::onServerStarted);
    }

    private static void onServerStarted(ServerStartedEvent event) {
        CommonClass.dumpTags(event.getServer());

        // Uncomment to also dump explicitly NeoForge-specific tags
        // Note: Some `c` tags are also NeoForge-specific
        //CommonClass.dumpTags(event.getServer(), Set.of("c", "neoforge"));
    }
}
