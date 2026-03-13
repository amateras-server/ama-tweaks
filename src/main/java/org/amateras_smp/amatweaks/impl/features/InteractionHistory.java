// Copyright (c) 2025 Amateras-Server
// This file is part of the AmaTweaks project and is licensed under the terms of
// the MIT License. See the LICENSE file for details.

package org.amateras_smp.amatweaks.impl.features;

import fi.dy.masa.malilib.util.StringUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.amateras_smp.amatweaks.config.Configs;
import org.amateras_smp.amatweaks.impl.util.LimitedQueue;
import org.jetbrains.annotations.Nullable;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class InteractionHistory {

    public static final LimitedQueue<BlockInteraction> blockInteractionHistory = new LimitedQueue<>(10);
    public static final LimitedQueue<EntityInteraction> entityInteractionHistory = new LimitedQueue<>(10);

    public static void clear() {
        blockInteractionHistory.clear();
        entityInteractionHistory.clear();
    }

    public static void resize() {
        blockInteractionHistory.setMaxSize(Configs.Generic.INTERACTION_HISTORY_MAX_SIZE.getIntegerValue());
        entityInteractionHistory.setMaxSize(Configs.Generic.INTERACTION_HISTORY_MAX_SIZE.getIntegerValue());
    }

    public static void onBlockInteraction(Block block, BlockPos pos, String interactionType) {
        String name = block.getDescriptionId();
        BlockInteraction interaction = new BlockInteraction(interactionType, name, pos);
        blockInteractionHistory.add(interaction);
    }

    public static void onEntityInteraction(Entity entity) {
        String name = entity.getType().getDescriptionId();
        EntityInteraction interaction = new EntityInteraction(name, entity.position());
        entityInteractionHistory.add(interaction);
    }

    public static void printInteraction() {
        for (BlockInteraction b : blockInteractionHistory) {
            System.out.println(b.toString());
        }
        for (EntityInteraction e : entityInteractionHistory) {
            System.out.println(e.toString());
        }
    }

    public static class BlockInteraction {
        // break or place or interact
        public String type;
        public String blockName;
        public BlockPos pos;

        BlockInteraction(String type, String blockName, @Nullable BlockPos pos) {
            this.type = type;
            this.blockName = blockName;
            this.pos = pos;
        }

        public String toString() {
            return this.type + ": " + StringUtils.translate(this.blockName) + " (" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")";
        }
    }

    public static class EntityInteraction {
        // for attack only
        public String entityName;
        public Vec3 pos;

        EntityInteraction(String entityName, Vec3 pos) {
            this.entityName = entityName;
            this.pos = pos;
        }

        public String toString() {
            return StringUtils.translate(this.entityName) + " (" + roundDouble(pos.x()) + ", " + roundDouble(pos.y()) + ", " + roundDouble(pos.z()) + ")";
        }

        private double roundDouble(double d) {
            BigDecimal bd = new BigDecimal(d);
            BigDecimal rounded = bd.setScale(2, RoundingMode.HALF_UP);
            return rounded.doubleValue();
        }
    }
}
