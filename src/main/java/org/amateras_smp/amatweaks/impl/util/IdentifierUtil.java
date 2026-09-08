// Copyright (c) 2026 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.impl.util;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;

public class IdentifierUtil {
    public static Identifier ofVanilla(String id) {
        //#if MC >= 12100
        return Identifier.withDefaultNamespace(id);
        //#else
        //$$ return new ResourceLocation(id);
        //#endif
    }

    public static Identifier of(String namespace, String path) {
        //#if MC == 12006
        //$$ return ResourceLocation.tryBuild(namespace, path);
        //#elseif MC >= 12100
        return Identifier.fromNamespaceAndPath(namespace, path);
        //#else
        //$$ return new ResourceLocation(namespace, path);
        //#endif
    }

    public static Identifier id(Block block) {
        return BuiltInRegistriesUtil.BLOCK.getKey(block);
    }

    public static Identifier id(Fluid fluid) {
        return BuiltInRegistriesUtil.FLUID.getKey(fluid);
    }

    public static Identifier id(EntityType<?> entityType) {
        return BuiltInRegistriesUtil.ENTITY_TYPE.getKey(entityType);
    }

    public static Identifier id(BlockEntityType<?> blockEntityType) {
        return BuiltInRegistriesUtil.BLOCK_ENTITY_TYPE.getKey(blockEntityType);
    }

    public static Identifier id(PoiType poiType) {
        return BuiltInRegistriesUtil.POINT_OF_INTEREST_TYPE.getKey(poiType);
    }

}
