package ga.ozli.minecraftmods.commontagsdumper;

import ga.ozli.minecraftmods.commontagsdumper.platform.Services;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;

import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
final class CommonClass {
    private CommonClass() {}

    static void dumpTags(MinecraftServer server) {
        dumpTags(server, false);
    }

    static void dumpTags(MinecraftServer server, boolean summary) {
        dumpTags(server, Set.of("c"), summary);
    }

    static void dumpTags(MinecraftServer server, Set<String> namespaces) {
        dumpTags(server, namespaces, false);
    }

    static void dumpTags(MinecraftServer server, Set<String> namespaces, boolean summary) {
        String loader = Services.PLATFORM.getPlatformName() + " `" + Services.PLATFORM.getPlatformVersion() + "`";
        var dumpedTags = new LinkedHashMap<String, List<Map.Entry<? extends TagKey<?>, ? extends HolderSet.Named<?>>>>();

        var tagNames = server.registryAccess().registries()
                .filter(registryEntry -> registryEntry.key().identifier().getNamespace().equals("minecraft"))
                .map(RegistryAccess.RegistryEntry::value)
                .flatMap(Registry::getTags)
                .map(tag -> Map.entry(tag.key(), tag))
                .filter(pair -> namespaces.contains(pair.getKey().location().getNamespace()))
                .sorted(Comparator.comparing(a -> a.getKey().registry().identifier().getPath()))
                .toList();

        for (Map.Entry<? extends TagKey<?>, ? extends HolderSet.Named<?>> pair : tagNames) {
            var tagKey = pair.getKey();
            dumpedTags.computeIfAbsent(tagKey.registry().identifier().getPath(), k -> new ArrayList<>(100)).add(pair);
        }

        var fullOutput = new StringBuilder(1_000);
        fullOutput.append(loader).append(" c tags");
        if (summary) fullOutput.append(" summary");
        fullOutput.append('\n');
        fullOutput.repeat('=', 7 + loader.length()).append('\n');

        for (String tagType : dumpedTags.keySet()) {
            fullOutput.append('\n').append(tagType.replace("_", "")).append('\n');
            fullOutput.repeat('-', tagType.length()).append('\n');

            dumpedTags.get(tagType).stream()
                .sorted(Comparator.comparing(p -> p.getKey().location().toString()))
                .forEachOrdered(pair -> {
                    var tagString = pair.getKey().location().toString();
                    fullOutput.append("- `").append(tagString).append('`').append('\n');
                    if (summary) return;
                    pair.getValue().stream()
                        .sorted(Comparator.comparing(Holder::getRegisteredName))
                        .forEachOrdered(holder -> fullOutput.append("    - `").append(holder.getRegisteredName()).append('`').append('\n'));
                });
        }

        var dumpPath = server.getServerDirectory().resolve(loader.replace("`", "") + " tags dump" + (summary ? " summary" : "") + ".md");
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
        fullOutput.setLength(0);
        fullOutput.trimToSize();
        System.gc();
    }
}
