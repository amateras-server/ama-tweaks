<a align=center>
  <img src=https://raw.githubusercontent.com/amateras-server/ama-tweaks/main/src/main/resources/assets/ama-tweaks/ama_alpha_white_1280.png width=256>
</a>

# AmaTweaks

[日本語の説明はこちら](README_ja.md)

[![Dev Builds](https://github.com/amateras-server/ama-tweaks/actions/workflows/gradle.yml/badge.svg)](https://github.com/amateras-server/ama-tweaks/actions/workflows/gradle.yml)
[![License](https://img.shields.io/github/license/amateras-server/ama-tweaks.svg)](https://opensource.org/licenses/MIT)
[![Issues](https://img.shields.io/github/issues/amateras-server/ama-tweaks.svg)](https://github.com/amateras-server/ama-tweaks/issues)
[![Modrinth](https://img.shields.io/modrinth/dt/amatweaks?label=Modrinth%20Downloads)](https://modrinth.com/mod/amatweaks)
[![Discord](https://img.shields.io/discord/1157213775791935539)](https://discord.gg/YFJff2Bkx8)

client-side utility mod for Amateras SMP.

## Features


### tweakAutoEat

> Automatically eat food from your inventory when your food level drops below a set threshold.
The `autoEatThreshold` can be configured in the `Generic` tab of the configs.
<br><br>

### tweakAutoFireworkGlide

> Automatically glide with firework rocket in you inventory when you're flying and flying speed is less than the value `autoGlideSpeedThreshold` that can be configured in the config `Generic`.
<br><br>

### tweakAutoRestockInventory

> Automatically restocks items from a container block (like chests, shulker-boxes, etc.) when you open it.
The `inventoryRestockList` can be customized in the `List` tab of the config.
<br><br>

### tweakCompactScoreboard

> Displays formatted scoreboard value in the sidebar.
Implementation of >=mc1.20.4 was ported from [techutils](https://github.com/Kikugie/techutils).
<br><br>

### tweakHoldBack

> Automatically keeps you moving back.
<br><br>

### tweakHoldForward

> Automatically keeps you moving forward.
<br><br>

### tweakHoldLeft

> Automatically keeps you moving left.
<br><br>

### tweakHoldRight

> Automatically keeps you moving right.
<br><br>

### tweakInteractionHistory

> Cache specified number of player interactions.
The interactions can be checked with `/history` command in the game and cleared with `/clearinteractions`.
The number of interaction to keep can be set by `interactionHistoryMaxSize` in config generic.
<br><br>

### tweakMonoGui

> Overrides text color in gui with white.
<br><br>

### tweakMonoTeam

> Overrides text color of team with white.
<br><br>

### tweakPickBlockRedirect

> Replace the block to be picked with litematica's pick block feature. `pickRedirectMap` can be configured in the `List` tab of the config.
<br><br>

### tweakPreventBreakingAdjacentPortal

> Prevents breaking blocks that are adjacent to a nether portal.
<br><br>

### tweakPreventPlacementOnPortalSides

> Prevents block placement on sliced nether portal sides.
This feature was ported from [taichi-tweaks](https://github.com/TaichiServer/taichi-tweaks)
<br><br>

### tweakSafeStepProtection

> Prevent breaking blocks below you while you're moving forward or sideways.
This can be useful for activities like perimeter digging.
<br><br>

### tweakSelectiveBlockRendering

> [!CAUTION]
> This feature has not supported block entity render selection yet.<br>

> Renders only specified blocks. The blocks can be configured in the list tab so check it. This feature will reload the entire world (renderer) on settings changed.
List entries example: `minecraft:white_stained_glass`, `minecraft:dirt`, `minecraft:bedrock`, etc.
<br><br>

### tweakSelectiveEntityRendering

> Renders only specified entities. The entities can be configured in the list tab.
List entries example: `player`, `tnt`, `slime`, `item`.
<br> This feature was ported from [taichi-tweaks](https://github.com/TaichiServer/taichi-tweaks)
<br><br>