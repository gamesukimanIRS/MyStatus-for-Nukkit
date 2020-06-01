# MyStatus for Nukkit
自分のステータスを画面下部に表示します。  
[Download](https://forum.mcbe.jp/resources/422/download)  
  
![画像0](picture/200pic1.png)  
![画像1](picture/200pic2.png)  
  
  
### 概要
自分の名前、自分の所持金、現在時刻、自分の現在地を画面下部に表示します。  
また、画面上部にお知らせを掲示したり、ステータスの上部にメッセージを付け加えたりできます。  
これらのメッセージ・お知らせは、Config又はコマンドで変更できます。  
  
表示する所持金はConfigで[EonomySystem](https://github.com/tedo0627/Horizon-2nd/blob/master/Plugins/EconomySystemAPI.jar)([配布先](https://github.com/tedo0627/Horizon-2nd))、[Ecokkit](https://forum.mcbe.jp/resources/476/)、[MoneySAPI](https://github.com/CoSSeDevelopmentTeam/MoneySAPI/releases)から選択でき、表示しないことも可能です。  
![画像1.5](picture/200pic5.png)  
  
##### 各部の説明   
![画像2](picture/200pic6.png)  

### コマンド
|コマンド|説明|初期値|権限|
|:-:|:--|:--|:-:|
|`/setmessage1`|ステータス上部の文字列を変更します。|MyStatus|OP|
|`/setmessage2`|画面上部の文字列を変更します。|このメッセージ・ステータスはMyStatus for Nukkitが表示しています。|OP|
  
全てのコマンドについては、ダブルクォーテーションを付けずスペースを使用することが可能です。   
![画像3](picture/200pic3.png)  
![画像4](picture/200pic4.png)  
  
### Config
メッセージ設定ファイルとして、`message.yml`が生成されます。  
```yaml
'#ステータスのメッセージ': ''
Message1: MyStatus
'#画面上部のメッセージ': ''
Message2: このメッセージ・ステータスはMyStatus for Nukkitが表示しています。
```
値を変えると、メッセージを変更することができます。  
Messageの1、2については各部の説明を参考にしてください。  

全般設定ファイルとして、`settings.yml`が生成されます。  
```yaml
'#設定。trueもしくはfalse。': ''
'#これらの設定を変更ではなく削除すると、正常な動作を妨げる原因となりますのでおやめください。': ''
'#設定の初期化を行いたい場合、ymlごと削除を推奨します。': ''
'#経済APIプラグイン設定について。初期設定は無効のfalse。': ''
'#現在、EconomySystemAPI、Ecokkit、MoneySAPIに対応。所持金表示不要の場合、false。': ''
'#EconomySystemAPI Ecokkit MoneySAPI false から選ぶこと。誤りがある場合動作せず。': ''
Economy: false
'#デバッグモード。通常時はfalse推奨。': ''
debugmode: false
```
ここで表示する経済APIもしくは表示無効を選ぶことができます。  
デバッグモードはオンにするとスケジューラーのタスクIDを表示します。  

### 使用している機能
このプラグインは20tickのリピートタスク（スケジューラー）を利用しています。  
常時表示にTipを利用しています。  
そのため、Tipに常に何かを表示させるプラグインとは競合します。  
また、Popup・ActionBarに常に何かを表示させるプラグインとは表示が重なる場合があります。  

### ライセンス
二次配布、改造配布、プラグインの横流し、悪用を禁止します。  
自分用、又は友人用の改造は許可します。  
このプラグインで発生したすべての問題に対して製作者は責任は負いません。  
都合により、プラグインの破棄を要請する場合があります。その際は、要請に従いプラグインを破棄してください。
各経済APIについては、それぞれのプラグインのライセンスに則ってご使用ください。  
