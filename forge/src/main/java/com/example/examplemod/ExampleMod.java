package com.example.examplemod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class ExampleMod {

    public ExampleMod() {

        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.

        // Use Forge to bootstrap the Common mod.
        Constants.LOG.info("Hello Forge world!");
        CommonClass.init();

        MinecraftForge.EVENT_BUS.addListener(ExampleMod::onServerStarted);

    }

    public static void onServerStarted(ServerStartedEvent event) {
        // get NeoForge version
        var forgeVersion = ModList.get()
                .getModFileById("forge")
                .getMods()
                .getFirst()
                .getVersion();
        var forgeVersionString = forgeVersion.getMajorVersion() + "." + forgeVersion.getMinorVersion() + "." + forgeVersion.getIncrementalVersion();
        if (forgeVersion.getQualifier() != null)
            forgeVersionString += "-" + forgeVersion.getQualifier();

        CommonClass.dumpTags(forgeVersionString, event.getServer());
    }
}
