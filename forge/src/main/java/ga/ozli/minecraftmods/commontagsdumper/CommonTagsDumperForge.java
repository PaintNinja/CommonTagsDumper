package ga.ozli.minecraftmods.commontagsdumper;

import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public final class CommonTagsDumperForge {
    public CommonTagsDumperForge() {
        ServerStartedEvent.BUS.addListener(event -> {
            CommonClass.dumpTags(event.getServer());

            // Uncomment to also dump Forge-specific tags
            //CommonClass.dumpTags(event.getServer(), Set.of("c", "forge"));
        });
    }
}
