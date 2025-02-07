<a align=center>
  <img src=https://raw.githubusercontent.com/amateras-server/ama-tweaks/main/src/main/resources/assets/ama-tweaks/ama_alpha_white_1280.png width=256>
</a>

# AmaTweaks

[English Readme here](README.md)

[![Dev Builds](https://github.com/amateras-server/ama-tweaks/actions/workflows/gradle.yml/badge.svg)](https://github.com/amateras-server/ama-tweaks/actions/workflows/gradle.yml)
[![License](https://img.shields.io/github/license/amateras-server/ama-tweaks.svg)](https://opensource.org/licenses/MIT)
[![Issues](https://img.shields.io/github/issues/amateras-server/ama-tweaks.svg)](https://github.com/amateras-server/ama-tweaks/issues)
[![Modrinth](https://img.shields.io/modrinth/dt/amatweaks?label=Modrinth%20Downloads)](https://modrinth.com/mod/amatweaks)
[![Discord](https://img.shields.io/discord/1157213775791935539)](https://discord.gg/YFJff2Bkx8)

Amateras SMPのためにつくられたクライアント機能系mod。

## Features


### tweakAutoEat

> 設定された閾値より食料ゲージが少なくなったときに、インベントリから食料を探して自動で食べます。
閾値はconfigの`Generic`タブにある`autoEatThreshold`で設定できます。
<br><br>

### tweakAutoFireworkGlide

> エリトラで滑空中、`Generic`タブの`autoGlideSpeedThreshold`で設定された値よりもプレイヤーの速度が小さくなったときに自動でインベントリからロケット花火を取り出して使用します。
<br><br>

### tweakAutoRestockHotbar

> チェストやシュルカーボックスなどのコンテナ系ブロックを開いたとき、設定されたアイテムリスト内のアイテムをコンテナから探し出してホットバー(インベントリ)に補充します。
補充するアイテムのリストはconfigの`List`タブにある`hotbarRestockList`で設定できます。デフォルトではロケット花火と金のニンジンがリストに設定されています。
<br><br>

### tweakCompactScoreboard

> サイドバーに表示されるスコアボードの数値をフォーマットしてコンパクトにします。1.20.4以上のバージョンにおける実装は[techutil](https://github.com/Kikugie/techutils)からお借りしました。
<br><br>

### tweakHoldBack

> 後退キーを自動で押し続けます。
<br><br>

### tweakHoldForward

> 前進キーを自動で押し続けます。
<br><br>

### tweakHoldLeft

> 左に進むキーを自動で押し続けます。
<br><br>

### tweakHoldRight

> 右に進むキーを自動で押し続けます。
<br><br>

### tweakInteractionHistory

> プレイヤーによるインタラクションを指定数記録します。
記録したインタラクションは`/history`コマンドで確認でき、`clearhistory`で破棄できます。
記録するインタラクションの最大数はGenericタブの`interactionHistoryMaxSize`で指定でき、これをオーバーした場合は最も古いインタラクションが破棄されて新しいものが記録されます。
<br><br>

### tweakMonoChat

> チャットをモノクロにします。
プレイヤーに送られたすべてのテキストチャットは白色として表示されるようになります。
<br><br>

### tweakMonoGui

> guiに表示されるすべてのテキストを白色として表示します。
<br><br>

### tweakPickBlockRedirect

> litematicaでpickを行う時に、リダイレクトが指定されていればpickするべきブロックをそれに置き換えます。リダイレクトマップはconfigの`List`タブにある`pickRedirectMap`で設定できます。(この機能は耕地ブロックや土の道などの入手不可能アイテムを土としてpickしたり、水を氷としてpickするなどの用途を意図しています。)
<br><br>

### tweakPreventBreakingAdjacentPortal

> ネザーポータルの枠組みにあたる位置のブロックが壊せなくなります。
<br><br>

### tweakPreventPlacementOnPortalSides

> スライスされたネザーポータルの側面にブロックを置けなくなります。
この機能は[taichi-tweaks](https://github.com/TaichiServer/taichi-tweaks)から移植されました。
<br><br>

### tweakSafeStepProtection

> 前、または横に動いているときに自分より下のブロックを壊せなくなります。
露天掘りのような場面での使用を想定しています。
<br><br>

### tweakSelectiveBlockRendering

> [!CAUTION]
> この機能はブロックエンティティのカスタマイズをまだサポートしていません。<br>

> ブロックの種類ごとに描画を行うかどうかカスタムできます。ブロックの設定はリストタブで指定できます。
この機能はリストの設定が変わるたびにワールドレンダラーを再読み込みします。
リストに追加するエントリーの記法は、ネームスペースを含めたブロックidを採用しています。(例: `minecraft:black_stained_glass`, `minecraft:grass_block`, `minecraft:bedrock`など)
<br><br>

### tweakSelectiveEntityRendering

> エンティティの種類ごとに描画を行うかどうかカスタムできます。基本は`tweakSelectiveBlockRendering`のエンティティ版と捉えて問題ないですが、リストの記法が異なるので注意してください。
リストの記法はネームスペースを含めないエンティティタイプ名を採用しています(例: `player`, `tnt`, `zombie`, `item`など)。
<br>この機能は[taichi-tweaks](https://github.com/TaichiServer/taichi-tweaks)から移植されました。
<br><br>