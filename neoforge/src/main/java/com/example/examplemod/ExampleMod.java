package com.example.examplemod;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

@Mod(Constants.MOD_ID)
public class ExampleMod {

    public ExampleMod(IEventBus eventBus) {

        // This method is invoked by the NeoForge mod loader when it is ready
        // to load your mod. You can access NeoForge and Common code in this
        // project.

        // Use NeoForge to bootstrap the Common mod.
        Constants.LOG.info("Hello NeoForge world!");
        CommonClass.init();

        NeoForge.EVENT_BUS.addListener(ExampleMod::onServerStarted);

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
    }
}
