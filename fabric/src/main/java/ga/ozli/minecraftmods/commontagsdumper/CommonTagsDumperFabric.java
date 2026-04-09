package ga.ozli.minecraftmods.commontagsdumper;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public final class CommonTagsDumperFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            // Note: includes Fabric-specific tags, as the same namespace is used for both common and Fabric-specific tags
            CommonClass.dumpTags(server);
        });
    }
}
