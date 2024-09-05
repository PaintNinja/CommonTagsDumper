package com.example.examplemod;

import com.example.examplemod.platform.Services;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagFile;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class CommonClass {

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {

        Constants.LOG.info("Hello from Common init on {}! we are currently in a {} environment!", Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());
        Constants.LOG.info("The ID for diamonds is {}", BuiltInRegistries.ITEM.getKey(Items.DIAMOND));

        // It is common for all supported loaders to provide a similar feature that can not be used directly in the
        // common code. A popular way to get around this is using Java's built-in service loader feature to create
        // your own abstraction layer. You can learn more about this in our provided services class. In this example
        // we have an interface in the common code and use a loader specific implementation to delegate our call to
        // the platform specific approach.
        if (Services.PLATFORM.isModLoaded("examplemod")) {

            Constants.LOG.info("Hello to examplemod");
        }
    }

    public static void dumpTags(String loaderApiVersion, MinecraftServer server) {
        //dumpTags(server);
        String loader = Services.PLATFORM.getPlatformName() + " `" + loaderApiVersion + "`";
        var dumpedTags = new LinkedHashMap<String, List<TagKey<?>>>();

        var tagNames = server.registryAccess().registries()
                .filter(registryEntry -> registryEntry.key().location().getNamespace().equals("minecraft"))
                .map(RegistryAccess.RegistryEntry::value)
                .flatMap(Registry::getTagNames)
                .filter(tagKey -> tagKey.location().getNamespace().equals("c"))
                .sorted(Comparator.comparing(a -> a.registry().location().getPath()))
                .toList();

        for (TagKey<?> tagKey : tagNames) {
            dumpedTags.computeIfAbsent(tagKey.registry().location().getPath(), k -> new ArrayList<>(100)).add(tagKey);
        }

        var fullOutput = new StringBuilder(1_000);
        fullOutput.append(loader).append(" c tags").append('\n');
        fullOutput.repeat('=', 7 + loader.length()).append('\n');

        for (String tagType : dumpedTags.keySet()) {
            fullOutput.append('\n').append(tagType.replace("_", "")).append('\n');
            fullOutput.repeat('-', tagType.length()).append('\n');

            dumpedTags.get(tagType).stream()
                    .map(TagKey::location)
                    .map(ResourceLocation::toString)
                    .sorted()
                    .forEachOrdered(tagString -> fullOutput.append("- `").append(tagString).append('`').append('\n'));
        }

        var dumpPath = server.getServerDirectory().resolve(loader.replace("`", "") + " tags dump.md");
        try {
            Files.writeString(dumpPath, fullOutput.toString());
        } catch (IOException e) {
            Constants.LOG.error("Failed to dump tags to {}, so dumping to the console instead", dumpPath, e);
            Constants.LOG.info("Dumped tags:\n{}", fullOutput);
            return;
        }
        Constants.LOG.info("Dumped tags to \"{}\"", dumpPath);

        dumpedTags.forEach((tagType, tags) -> tags.clear());
        dumpedTags.clear();
        System.gc();
    }

    public static void dumpTags(MinecraftServer server) {
        var loadedBlockTags = server.registryAccess().registryOrThrow(Registries.BLOCK).getTags()
                .toList();
        for (var tag : loadedBlockTags) {
            Constants.LOG.info("Block tag: {}", tag);
        }
    }

    private static <T> List<String> getTag(MinecraftServer server, ResourceKey<Registry<T>> registryKey, TagKey<T> tagKey) {
        return server.registryAccess().registryOrThrow(registryKey).getTag(tagKey).orElseThrow()
                .stream()
                .map(Holder::value)
                .distinct()
                .map(Object::toString)
                .toList();
    }
}
