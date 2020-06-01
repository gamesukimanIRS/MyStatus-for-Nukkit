package cf.gamesukimanirs.mystatus;


//java
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;

//command
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
//base
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.Player;
//utils
import cn.nukkit.utils.Config;
//task
import cn.nukkit.scheduler.TaskHandler;

//economy
import tedo.EconomySystemAPI.EconomySystemAPI;
import net.bbo51dog.ecokkit.api.EcokkitAPI;
import net.comorevi.moneyapi.MoneySAPI;


public class Main extends PluginBase implements Listener{

	private EconomySystemAPI economy;
	private EcokkitAPI ecokkit;
	private MoneySAPI moneys;
	private Config msg;
	private Config stg;
	private String PluginName = "MyStatus for Nukkit";
	private String version = "v2.0.0";
	private TaskHandler statusTask;

	@SuppressWarnings({ "deprecation", "serial" })
	public void onEnable(){
		getLogger().info(PluginName + "-v" + version + "を読み込みました。作者:gamesukimanIRS");
		getLogger().warning("製作者偽りと二次配布、友人用を除いた他人用の改造、改造配布、プラグインの横流し、悪用等はおやめ下さい。");
		getLogger().warning("また、このプラグインを使用して発生した如何なる問題に対しての責任は負いかねます。");
		getLogger().info("このプラグインを使用する際はどこかにプラグイン名「" + PluginName + "」と作者名「gamesukimanIRS」を記載して頂けると光栄です。");
		getLogger().warning("初期設定では所持金は表示されません。所持金を表示したい場合、初回起動後にMyStatus_for_Nukkitフォルダ内のsettings.ymlにて使用している経済APIの設定を行ってください。");
		getLogger().notice("対応している経済APIは以下の3種です。(複数選択はできません。)");
		getLogger().notice("・EconomySystemAPI(tedo0627様)");
		getLogger().notice("https://github.com/tedo0627/Horizon-2nd");
		getLogger().notice("https://github.com/tedo0627/Horizon-2nd/blob/master/Plugins/EconomySystemAPI.jar");
		getLogger().notice("・Ecokkit(bbo51dog様)");
		getLogger().notice("https://forum.mcbe.jp/resources/476/");
		getLogger().notice("・MoneySAPI(CoSSeDevelopmentTeam様");
		getLogger().notice("https://github.com/CoSSeDevelopmentTeam/MoneySAPI/releases");
		//config
		getDataFolder().mkdir();
		msg = new Config(new File(getDataFolder(), "messages.yml"), Config.YAML,
				new LinkedHashMap<String,Object>() {{
			put("#ステータスのメッセージ","");
			put("Message1","MyStatus");
			put("#画面上部のメッセージ","");
			put("Message2","このメッセージ・ステータスはMyStatus for Nukkitが表示しています。");
		}});
		stg = new Config(new File(getDataFolder(), "settings.yml"), Config.YAML,
				new LinkedHashMap<String,Object>() {{
			put("#設定。trueもしくはfalse。","");
			put("#これらの設定を変更ではなく削除すると、正常な動作を妨げる原因となりますのでおやめください。","");
			put("#設定の初期化を行いたい場合、ymlごと削除を推奨します。","");
			put("#経済APIプラグイン設定について。初期設定は無効のfalse。","");
			put("#現在、EconomySystemAPI、Ecokkit、MoneySAPIに対応。所持金表示不要の場合、false。","");
			put("#EconomySystemAPI Ecokkit MoneySAPI false から選ぶこと。誤りがある場合動作せず。","");
			put("Economy","false");
			put("#デバッグモード。通常時はfalse推奨。","");
			put("debugmode","false");
		}});
		//settings
		///debug
		Object debugv = stg.get("debugmode");
		if(!debugv.equals(true)&&!debugv.equals(false)) {stg.set("debugmode", false);stg.save();}
		///Economy
		Object eav = stg.get("Economy");
		if(!eav.equals("EconomySystemAPI")&&!eav.equals("Ecokkit")&&!eav.equals("MoneySAPI")&&!eav.equals(false)) {
			stg.set("Economy", false);stg.save();
			getLogger().error("settings.ymlのEconomyで設定された経済API\""+eav+"\"は対応していないため、所持金表示は無効化されました。");
			getLogger().error("所持金を表示したい場合、対応している経済APIに書き換えてサーバーを再起動してください。");
		}
		if(eav.equals(false)) {
			getLogger().warning("経済APIの使用が無効化されています。所持金は表示されませんのでご注意ください。");
			getLogger().notice("所持金を表示したい場合、settings.yml内の\"Economy\"の項目にて使用している経済APIを設定することで、所持金が表示されます。");
		/////EconomySystemAPI
		}else if(eav.equals("EconomySystemAPI")){
			try {
				economy = (EconomySystemAPI) getServer().getPluginManager().getPlugin("EconomySystemAPI");
				enableLogger(economy,eav);
			}catch(NullPointerException e) {
				stg.set("Economy", false);
				stg.save();
				getLogger().error("エラー:005 settings.ymlに設定された経済API\""+eav+"\"が見つからなかったため、所持金表示は無効化されました。");
				getLogger().error("所持金を表示したい場合、再度\""+eav+"\"が導入されていることを確認しサーバーを再起動してください。");
			}
		/////Ecokkit
		}else if(eav.equals("Ecokkit")) {
			try {
				ecokkit = EcokkitAPI.getInstance();
				enableLogger(ecokkit,eav);
			}catch(NullPointerException e) {
				stg.set("Economy", false);
				stg.save();
				getLogger().error("エラー:005 settings.ymlに設定された経済API\""+eav+"\"が見つからなかったため、所持金表示は無効化されました。");
				getLogger().error("所持金を表示したい場合、再度\""+eav+"\"が導入されていることを確認しサーバーを再起動してください。");
			}
		/////MoneySAPI
		}else if(eav.equals("MoneySAPI")) {
			try{
				moneys = MoneySAPI.getInstance();
				enableLogger(moneys,eav);
			}catch(NoClassDefFoundError e) {
				stg.set("Economy", false);
				stg.save();
				getLogger().error("エラー:005 settings.ymlに設定された経済API\""+eav+"\"が見つからなかったため、所持金表示は無効化されました。");
				getLogger().error("所持金を表示したい場合、再度\""+eav+"\"が導入されていることを確認しサーバーを再起動してください。");
			}
		}
		//message
		if(!(msg.get("Message1") instanceof String)){msg.set("Message1", "Mystatus");msg.save();}
		if(!(msg.get("Message2") instanceof String)) {msg.set("Message2", "このメッセージ・ステータスはMystatus for Nukkitが表示しています。");msg.save();}
		//scheduler
		statusTask = getServer().getScheduler().scheduleRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				task();
			}
		}, 20);
		getLogger().debug("taskId:"+statusTask.getTaskId());
		if(stg.get("debugmode").equals(true)) {
			getLogger().info("[DEBUG]ステータスタスクID:"+statusTask.getTaskId());
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String Label, String[] args) {
		if(sender.isOp()) {
			try {
				switch(cmd.getName()) {
					case "setmessage1":
						if(args[0] != null) {
							String argsmsg1 = String.join(" ", args);
							msg.set("Message1", argsmsg1);
							msg.save();
							getLogger().notice(sender.getName()+"によってメッセージ1(ステータス上部)が「"+argsmsg1+"」になりました。");
							sender.sendMessage("§a[MyStatus]§bメッセージ1の設定が正常に完了しました。");
							return true;
						}else {
							sender.sendMessage("§a[MyStatus]§cエラー:002 メッセージを入力してください");
							return false;
						}
					case "setmessage2":
						if(args[0] != null) {
							String argsmsg2 = String.join(" ", args);
							msg.set("Message2", argsmsg2);
							msg.save();
							getLogger().notice(sender.getName()+"によってメッセージ2(画面上部)が「"+argsmsg2+"」になりました。");
							sender.sendMessage("§a[MyStatus]§bメッセージ2の設定が正常に完了しました。");
							return true;
						}else {
							sender.sendMessage("§a[MyStatus]§cエラー:002 メッセージを入力してください");
							return false;
						}
				}
				return false;
			}catch(ArrayIndexOutOfBoundsException e) {
				sender.sendMessage("§a[Mystatus]§cエラー:002 メッセージを入力してください");
				return false;
			}
		}else {
			sender.sendMessage("§a[Mystatus]§cエラー：001 OP権限がありません");
			return true;
		}
	}

	//経済APIのexistsチェック
	private void enableLogger(Object economyapi,Object eav) {
		String eavs = eav.toString();
		if(economyapi == null) {
			stg.set("Economy", false);
			stg.save();
			getLogger().error("エラー:005 settings.ymlに設定された経済API\""+eavs+"\"が見つからなかったため、所持金表示は無効化されました。");
			getLogger().error("所持金を表示したい場合、再度\""+eavs+"\"が導入されていることを確認しサーバーを再起動してください。");
		}else {
			getLogger().info("経済API\""+eavs+"\"が読み込まれました。");
			getLogger().info("ステータスの所持金には、\""+eavs+"\"のものが表示されます。");
		}
	}

	//所持金
	private void status1(Player player,String pm2,String msg1,String msg2) {
		player.sendTip
		("§l§aメッセージ: §f" + msg2 + "\n\n\n\n\n\n\n\n\n"
			+"                                     §r§b["+msg1+"§r§b] Name: " + player.getName() + "\n"
			+ "                                                  §e所持金: §f¥" + pm2 + "\n"
			+ "                                                  §a現在時刻: §f" + new SimpleDateFormat("yyyy年MM月dd日HH時mm分ss秒").format(Calendar.getInstance().getTime()) + "\n"
			+ "                                                  §7所持アイテムのID: §f" + player.getInventory().getItemInHand().getId() + ":" + player.getInventory().getItemInHand().getDamage() + "\n"
			+ "                                                  §6現在地: §2ワールド: §f" + player.getLevel().getFolderName() + "\n"
			+ "                                                               §cX: §f" + Math.round(player.getX()) + "\n"
			+ "                                                               §2Y: §f" + Math.round(player.getY()) + "\n"
			+ "                                                               §9Z: §f" + Math.round(player.getZ()) + "\n\n\n\n\n\n\n\n\n\n");
	}

	//所持金取得失敗時
	private void status2(Player player,String msg1,String msg2) {
		player.sendTip
		("§l§aメッセージ: §f" + msg2 + "\n\n\n\n\n\n\n\n\n"
				+"                                 §r§b["+msg1+"§r§b] Name: " + player.getName() + "\n"
				+ "                                              §e所持金: §fおおがねもちだとおもいます(取得失敗)\n"
				+ "                                              §a現在時刻: §f" + new SimpleDateFormat("yyyy年MM月dd日HH時mm分ss秒").format(Calendar.getInstance().getTime()) + "\n"
				+ "                                              §7所持アイテムのID: §f" + player.getInventory().getItemInHand().getId() + ":" + player.getInventory().getItemInHand().getDamage() + "\n"
				+ "                                              §6現在地: §2ワールド: §f" + player.getLevel().getFolderName() + "\n"
				+ "                                                           §cX: §f" + Math.round(player.getX()) + "\n"
				+ "                                                           §2Y: §f" + Math.round(player.getY()) + "\n"
				+ "                                                           §9Z: §f" + Math.round(player.getZ()) + "\n\n\n\n\n\n\n\n\n\n");
	}

	//所持金表示なし時
	private void status3(Player player,String msg1,String msg2) {
		player.sendTip
		("§l§aメッセージ: §f" + msg2 + "\n\n\n\n\n\n\n\n\n\n"
				+"                                 §r§b["+msg1+"§r§b] Name: " + player.getName() + "\n"
				+ "                                              §a現在時刻: §f" + new SimpleDateFormat("yyyy年MM月dd日HH時mm分ss秒").format(Calendar.getInstance().getTime()) + "\n"
				+ "                                              §7所持アイテムのID: §f" + player.getInventory().getItemInHand().getId() + ":" + player.getInventory().getItemInHand().getDamage() + "\n"
				+ "                                              §6現在地: §2ワールド: §f" + player.getLevel().getFolderName() + "\n"
				+ "                                                           §cX: §f" + Math.round(player.getX()) + "\n"
				+ "                                                           §2Y: §f" + Math.round(player.getY()) + "\n"
				+ "                                                           §9Z: §f" + Math.round(player.getZ()) + "\n\n\n\n\n\n\n\n\n\n");
	}

	public void task() {
		//各プレイヤーに表示
		String msg1 = (String)msg.get("Message1");
		String msg2 = (String)msg.get("Message2");
		getServer().getOnlinePlayers().forEach((UUID, player) -> {
			player.getLoginChainData().getGuiScale();
			String eav = stg.get("Economy").toString();
			if(eav.equals("false")) {
				status3(player, msg1, msg2);
			}else if(eav.equals("EconomySystemAPI")){
				try {
					long pm = economy.getMoney(player.getName());
					String pm2 = String.valueOf(pm);
					status1(player, pm2, msg1, msg2);
				}catch(NullPointerException e) {
					status2(player, msg1, msg2);
				}
			}else if(eav.equals("Ecokkit")) {
				try {
					long pm = ecokkit.getMoney(player.getLoginChainData().getXUID());
					String pm2 = String.valueOf(pm);
					status1(player, pm2, msg1, msg2);
				}catch(NullPointerException e) {
					status2(player, msg1, msg2);
				}
			}else if(eav.equals("MoneySAPI")) {
				try {
					long pm = moneys.getMoney(player.getName());
					String pm2 = String.valueOf(pm);
					status1(player, pm2, msg1, msg2);
				}catch(NullPointerException e) {
					status2(player, msg1, msg2);
				}
			}
		});
	}
}