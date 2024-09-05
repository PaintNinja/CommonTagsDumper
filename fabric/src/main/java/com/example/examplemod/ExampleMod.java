package com.example.examplemod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;

public class ExampleMod implements ModInitializer {
    
    @Override
    public void onInitialize() {
        
        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        Constants.LOG.info("Hello Fabric world!");
        CommonClass.init();

        ServerLifecycleEvents.SERVER_STARTED.register(ExampleMod::onServerStarted);
    }

    public static void onServerStarted(MinecraftServer server) {
        var fabricApiVersion = FabricLoader.getInstance()
                .getModContainer("fabric-api")
                .orElseThrow(() -> new RuntimeException("Can't find Fabric API"))
                .getMetadata()
                .getVersion()
                .getFriendlyString();

        CommonClass.dumpTags(fabricApiVersion, server);
    }
}
