package net.rose.satchels.common.init;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.rose.satchels.common.Satchels;

public class ModItemTags {
    public static final TagKey<Item> SATCHELS = TagKey.of(Registries.ITEM.getKey(), Satchels.identifier("satchels"));

    public static void initialize() {
    }
}
