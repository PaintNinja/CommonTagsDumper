package ga.ozli.minecraftmods.commontagsdumper;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;

public class CommonTagsDumperFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(CommonTagsDumperFabric::onServerStarted);
    }

    public static void onServerStarted(MinecraftServer server) {
        var fabricApiVersion = FabricLoader.getInstance()
                .getModContainer("fabric-api")
                .orElseThrow(() -> new RuntimeException("Can't find Fabric API"))
                .getMetadata()
                .getVersion()
                .getFriendlyString();

        // Note: includes Fabric-specific tags, as the same namespace is used for both common and Fabric-specific tags
        CommonClass.dumpTags(fabricApiVersion, server);
    }
}
