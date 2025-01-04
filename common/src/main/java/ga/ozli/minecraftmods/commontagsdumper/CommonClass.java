package ga.ozli.minecraftmods.commontagsdumper;

import ga.ozli.minecraftmods.commontagsdumper.platform.Services;
import com.mojang.datafixers.util.Pair;
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
public class CommonClass {
    private CommonClass() {}

    public static void dumpTags(String loaderApiVersion, MinecraftServer server) {
        dumpTags(loaderApiVersion, server, Set.of("c"));
    }

    public static void dumpTags(String loaderApiVersion, MinecraftServer server, Set<String> namespaces) {
        String loader = Services.PLATFORM.getPlatformName() + " `" + loaderApiVersion + "`";
        var dumpedTags = new LinkedHashMap<String, List<Pair<? extends TagKey<?>, ? extends HolderSet.Named<?>>>>();

        var tagNames = server.registryAccess().registries()
                           .filter(registryEntry -> registryEntry.key().location().getNamespace().equals("minecraft"))
                           .map(RegistryAccess.RegistryEntry::value)
                           .flatMap(r -> r.getTags().map(t -> Pair.of(t.key(), t)))
                           .filter(pair -> namespaces.contains(pair.getFirst().location().getNamespace()))
                           .sorted(Comparator.comparing(a -> a.getFirst().registry().location().getPath()))
                           .toList();

        for (Pair<? extends TagKey<?>, ? extends HolderSet.Named<?>> pair : tagNames) {
            var tagKey = pair.getFirst();
            dumpedTags.computeIfAbsent(tagKey.registry().location().getPath(), k -> new ArrayList<>(100)).add(pair);
        }

        var fullOutput = new StringBuilder(1_000);
        fullOutput.append(loader).append(" c tags").append('\n');
        fullOutput.repeat('=', 7 + loader.length()).append('\n');

        for (String tagType : dumpedTags.keySet()) {
            fullOutput.append('\n').append(tagType.replace("_", "")).append('\n');
            fullOutput.repeat('-', tagType.length()).append('\n');

            dumpedTags.get(tagType).stream()
                .sorted(Comparator.comparing(p -> p.getFirst().location().toString()))
                .forEachOrdered(pair -> {
                    var tagString = pair.getFirst().location().toString();
                    fullOutput.append("- `").append(tagString).append('`').append('\n');
                    pair.getSecond().stream()
                        .sorted(Comparator.comparing(Holder::getRegisteredName))
                        .forEachOrdered(holder -> fullOutput.append("    - `").append(holder.getRegisteredName()).append('`').append('\n'));
                });
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
}
