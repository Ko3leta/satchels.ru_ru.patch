package net.rose.satchels.common.init;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;

import net.rose.satchels.common.Satchels;

public class ModItemTags {
    public static final TagKey<Item> SATCHELS = item("satchels");
    public static final TagKey<Item> SATCHEL_EXCLUDED = item("satchel_excluded");

    private static TagKey<Item> item(String name) {
        return TagKey.of(
                Registries.ITEM.getKey(),
                Satchels.identifier(name)
        );
    }

    public static void initialize() {
    }
}
