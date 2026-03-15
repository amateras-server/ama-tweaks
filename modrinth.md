<div align=center>
  <img src="https://raw.githubusercontent.com/amateras-server/ama-tweaks/main/src/main/resources/assets/ama-tweaks/ama_alpha_white_1280.png" width=256 alt="Amateras SMP logo image">
</div>


# AmaTweaks

[日本語](https://github.com/amateras-server/ama-tweaks/blob/main/README_ja.md)

[![Dev Builds](https://github.com/amateras-server/ama-tweaks/actions/workflows/gradle.yml/badge.svg)](https://github.com/amateras-server/ama-tweaks/actions/workflows/gradle.yml)
[![License](https://img.shields.io/github/license/amateras-server/ama-tweaks.svg)](https://opensource.org/licenses/MIT)
[![Issues](https://img.shields.io/github/issues/amateras-server/ama-tweaks.svg)](https://github.com/amateras-server/ama-tweaks/issues)
[![Modrinth](https://img.shields.io/modrinth/dt/amatweaks?label=Modrinth%20Downloads)](https://modrinth.com/mod/amatweaks)
[![Discord](https://img.shields.io/discord/1157213775791935539)](https://discord.gg/px7wHEMUpd)

A client-side utility mod made for Amateras SMP.  
Currently supports 1.18~1.21.11.

## Dependencies

- [fabric-api](https://modrinth.com/mod/fabric-api) (required)
- [malilib](https://modrinth.com/mod/malilib) (required)
- [modmenu](https://modrinth.com/mod/modmenu) (optional)
- [item-scroller](https://modrinth.com/mod/item-scroller) (optional)
- [litematica](https://modrinth.com/mod/litematica) (optional)
- [syncmatica](https://modrinth.com/mod/syncmatica) (optional)
- [tweakermore](https://modrinth.com/mod/tweakermore) (optional)
- [tweakeroo](https://modrinth.com/mod/tweakeroo) (optional)

## Features


### tweakAutoEat

> Automatically eat food from your inventory when your food level drops below a set threshold.
The `autoEatThreshold` can be configured in the `Generic` tab of the config.  

### tweakAutoFireworkGlide

> Automatically glide with firework rocket in you inventory when you're flying and flying speed is less than the value `autoGlideSpeedThreshold` that can be configured in the config `Generic`.  

### tweakAutoRestockInventory

> Automatically restocks items from a container block (like chests, shulker-boxes, etc.) when you open it.
The `inventoryRestockList` can be customized in the `List` tab of the config.  

### tweakCompactScoreboard

> Displays formatted scoreboard value in the sidebar.
Implementation of >=mc1.20.4 was ported from [techutils](https://github.com/Kikugie/techutils).  

### tweakHoldBack

> Automatically keeps you moving back.  

### tweakHoldForward

> Automatically keeps you moving forward.  

### tweakHoldLeft

> Automatically keeps you moving left.  

### tweakHoldRight

> Automatically keeps you moving right.  

### tweakInteractionHistory

> Cache specified number of player interactions.
The interactions can be checked with `/history` command in the game and cleared with `/clearinteractions`.
The number of interaction to keep can be set by `interactionHistoryMaxSize` in config `Generic`.  

### tweakMonoGui

> Overrides text color in gui with white.  

### tweakMonoTeam

> Overrides text color of team with white.  

### tweakPickBlockRedirect

> Replace the block to be picked with litematica's pick block feature. `pickRedirectMap` can be configured in the `List` tab of the config.  

### tweakPreventBreakingAdjacentPortal

> Prevents breaking blocks that are adjacent to a nether portal.  

### tweakPreventPlacementOnPortalSides

> Prevents block placement on sliced nether portal sides.  
This feature was ported from [taichi-tweaks](https://github.com/TaichiServer/taichi-tweaks).  

### tweakSafeStepProtection

> Prevent breaking blocks below you while you're moving forward or sideways.  
This can be useful for activities like perimeter digging.  

### tweakSelectiveBlockRendering

> Renders only specified blocks. The blocks can be configured in the `List` tab so check it. This feature will reload the entire world (renderer) on settings changed.  
List entries example: `minecraft:white_stained_glass`, `minecraft:dirt`, `minecraft:bedrock`, etc.  

### tweakSelectiveEntityRendering

> Renders only specified entities. The entities can be configured in the `List` tab.
List entries example: `player`, `tnt`, `slime`, `item`.  
This feature was ported from [taichi-tweaks](https://github.com/TaichiServer/taichi-tweaks).  
