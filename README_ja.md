<img src="https://raw.githubusercontent.com/amateras-server/ama-tweaks/main/src/main/resources/assets/ama-tweaks/ama_alpha_white_1280.png" width="256" style="display: block; margin: auto;">

# AmaTweaks

[English README](README.md)

[![Dev Builds](https://github.com/amateras-server/ama-tweaks/actions/workflows/gradle.yml/badge.svg)](https://github.com/amateras-server/ama-tweaks/actions/workflows/gradle.yml)
[![License](https://img.shields.io/github/license/amateras-server/ama-tweaks.svg)](https://opensource.org/licenses/MIT)
[![Issues](https://img.shields.io/github/issues/amateras-server/ama-tweaks.svg)](https://github.com/amateras-server/ama-tweaks/issues)
[![Modrinth](https://img.shields.io/modrinth/dt/amatweaks?label=Modrinth%20Downloads)](https://modrinth.com/mod/amatweaks)
[![Discord](https://img.shields.io/discord/1157213775791935539)](https://discord.gg/px7wHEMUpd )

Amateras SMPのためにつくられたクライアント機能系mod。<br>
現在マインクラフト1.18から1.21.4をサポートしています。

## Dependencies

- [malilib](https://modrinth.com/mod/malilib) (必須)
- [modmenu](https://modrinth.com/mod/modmenu) (必須)
- [item-scroller](https://modrinth.com/mod/item-scroller) (任意)
- [litematica](https://modrinth.com/mod/litematica) (任意)
- [syncmatica](https://modrinth.com/mod/syncmatica) (任意)
- [tweakermore](https://modrinth.com/mod/tweakermore) (任意)
- [tweakeroo](https://modrinth.com/mod/tweakeroo) (任意)

## Features


### tweakAutoEat

> 設定された閾値より食料ゲージが少なくなったときに、インベントリから食料を探して自動で食べる。
閾値はconfigの`Generic`タブにある`autoEatThreshold`で設定できる。<br>

### tweakAutoFireworkGlide

> エリトラで滑空中、`Generic`タブの`autoGlideSpeedThreshold`で設定された値よりもプレイヤーの速度が小さくなったときに自動でインベントリからロケット花火を取り出して使用する。<br>

### tweakAutoRestockInventory

> チェストやシュルカーボックスなどのコンテナ系ブロックを開いたとき、設定されたアイテムリスト内のアイテムをコンテナから探し出してホットバー(インベントリ)に補充する。
補充するアイテムのリストはconfigの`List`タブにある`inventoryRestockList`で設定できる。デフォルトではロケット花火と金のニンジンがリストに設定されている。<br>

### tweakCompactScoreboard

> サイドバーに表示されるスコアボードの数値をフォーマットしてコンパクトにする。1.20.4以上のバージョンにおける実装は[techutil](https://github.com/Kikugie/techutils)から拝借。<br>

### tweakHoldBack

> 後退キーを自動で押し続ける。<br>

### tweakHoldForward

> 前進キーを自動で押し続ける。<br>

### tweakHoldLeft

> 左に進むキーを自動で押し続ける。<br>

### tweakHoldRight

> 右に進むキーを自動で押し続ける。<br>

### tweakInteractionHistory

> プレイヤーによるインタラクションを指定数記録する。
記録したインタラクションは`/history`コマンドで確認でき、`clearhistory`で破棄できる。
記録するインタラクションの最大数はGenericタブの`interactionHistoryMaxSize`で指定でき、これをオーバーした場合は最も古いインタラクションが破棄されて新しいものが記録されていく。<br>

### tweakMonoGui

> guiに表示されるすべてのテキストを白色として表示する。<br>

### tweakMonoTeam

> チームの色を白色に置き換えて表示する.<br>

### tweakPickBlockRedirect

> litematicaでpickを行う時に、リダイレクトが指定されていればpickするべきブロックをそれに置き換える。リダイレクトマップはconfigの`List`タブにある`pickRedirectMap`で設定できる。<br>
この機能は耕地ブロックや土の道などの入手不可能アイテムを土としてpickしたり、水を氷としてpickするなどの用途を意図している。<br>

### tweakPreventBreakingAdjacentPortal

> ネザーポータルの枠組みにあたる位置のブロックが壊せなくなる。<br>

### tweakPreventPlacementOnPortalSides

> スライスされたネザーポータルの側面にブロックを置けなくなる。
この機能は[taichi-tweaks](https://github.com/TaichiServer/taichi-tweaks)より移植。<br>

### tweakSafeStepProtection

> 前方、または横に動いているときに自分の足元よりy座標が低いブロックを壊せなくなる。

### tweakSelectiveBlockRendering

> [!CAUTION]
> この機能はブロックエンティティや液体のレンダーセレクションをサポートしていない。<br>

> ブロックの種類ごとに描画を行うかどうかカスタムできる。ブロックの設定はconfigの`List`タブで指定できる。<br>
configの`Generic`タブにある`refreshWorldRendererOnRenderBlocksChanged`が`true`であるとき、リストの設定が変わるたびにワールドレンダラーを再読み込みする。
リストに追加するエントリーの記法は、ネームスペースを<strong>含めた</strong>ブロックidを採用。(例: `minecraft:black_stained_glass`, `minecraft:grass_block`, `minecraft:bedrock`など)<br>

### tweakSelectiveEntityRendering

> エンティティの種類ごとに描画を行うかどうかカスタムできる。<br>
リストの記法はネームスペースを<strong><font color=FF5555>含めない</font></strong>エンティティidを採用しています(例: `player`, `tnt`, `zombie`, `item`など)。<br>
この機能は[taichi-tweaks](https://github.com/TaichiServer/taichi-tweaks)より移植。<br>