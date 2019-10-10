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

//config
import cn.nukkit.utils.Config;

//economy
import tedo.EconomySystemAPI.EconomySystemAPI;

public class Main extends PluginBase implements Listener{

	public EconomySystemAPI economy;
	public Config cfg;

	@SuppressWarnings({ "deprecation", "serial" })
	public void onEnable(){
		getLogger().info("起動に必要なプラグインを検索中...");
		try {
			this.economy = (EconomySystemAPI) getServer().getPluginManager().getPlugin("EconomySystemAPI");
			if(this.economy == null) {
				getLogger().error("§c起動に必要なプラグイン\"EconomySysytemAPI\"が検出されませんでした。導入済みか確認してから、再度お試し下さい。");
				getLogger().notice("tedo0627様のプラグイン\"EconomySystemAPI\"はこちらからダウンロードできます。：");
				getLogger().notice("https://github.com/tedo0627/Horizon-2nd");
				getLogger().notice("https://github.com/tedo0627/Horizon-2nd/blob/master/Plugins/EconomySystemAPI.jar");
				getLogger().critical("§cプラグインを起動できませんでした。");
				getServer().getPluginManager().disablePlugin(this);
			}else {
				getLogger().info("起動に必要なプラグイン\"EconomySystemAPI\"を検出しました。");
				String PluginName = "MyStatus for Nukkit";
				String version = "1.0.0";
				getLogger().info(PluginName + "-v" + version + "を読み込みました。作者:gamesukimanIRS");
		    	getLogger().warning("製作者偽りと二次配布、他人用の改造、改造配布はおやめ下さい。また、このプラグインを使用して発生した如何なる問題に対しての責任は負いかねます。");
		    	getLogger().info("このプラグインを使用する際はどこかにプラグイン名「" + PluginName + "」と作者名「gamesukimanIRS」を記載する事を推奨します。");
		    	getLogger().notice("このプラグインは、前提プラグインとしてtedo様のEconomySystemAPIを使用しています。");
		    	getLogger().notice("URL: https://github.com/tedo0627/Horizon-2nd");
		    	getServer().getScheduler().scheduleRepeatingTask(this, new Runnable() {
		    		@Override
		    		public void run() {
		    			task();
		    		}
		    	}, 20);
		    	this.cfg = new Config(new File(this.getDataFolder(), "message.yml"), Config.YAML,
		    			new LinkedHashMap<String, Object>() {{put("Message1","MyStatus");put("Message2", "このメッセージ・ステータスはMyStatus for Nukkitが表示しています。");}});
			}
		}catch(Exception e) {
			getLogger().error("§c起動に必要なプラグイン\"EconomySysytemAPI\"が検出されませんでした。導入済みか確認してから、再度お試し下さい。");
			getLogger().notice("tedo0627様のプラグイン\"EconomySystemAPI\"はこちらからダウンロードできます。：");
			getLogger().notice("https://github.com/tedo0627/Horizon-2nd");
			getLogger().notice("https://github.com/tedo0627/Horizon-2nd/blob/master/Plugins/EconomySystemAPI.jar");
			getLogger().critical("§cプラグインを起動できませんでした。");
			getServer().getPluginManager().disablePlugin(this);
		}
	}

	public String msg1;
	public String msg2;

	public void task() {
		if(cfg.exists("Message1")) {
			try {
				if(this.cfg.get("Message1").toString() != null) {
					msg1 = this.cfg.get("Message1").toString();
				}else {
					msg1 = "Mystatus";
					this.cfg.set("Message1", "Mystatus");
					this.cfg.save();
				}
			}catch(NullPointerException e) {
				msg1 = "Mystatus";
				this.cfg.set("Message1", "Mystatus");
				this.cfg.save();
			}
		}else {
			msg1 = "Mystatus";
			this.cfg.set("Message1", "Mystatus");
			this.cfg.save();
		}
		if(cfg.exists("Message2")) {
			try {
				if(this.cfg.get("Message2").toString() != null) {
					msg2 = this.cfg.get("Message2").toString();
				}else {
					msg2 = "このメッセージ・ステータスはMystatus for Nukkitが表示しています。";
					this.cfg.set("Message2", "このメッセージ・ステータスはMystatus for Nukkitが表示しています。");
					this.cfg.save();
				}
			}catch(NullPointerException e) {
				msg2 = "このメッセージ・ステータスはMystatus for Nukkitが表示しています。";
				this.cfg.set("Message2", "このメッセージ・ステータスはMystatus for Nukkitが表示しています。");
				this.cfg.save();
			}
		}else {
			msg2 = "このメッセージ・ステータスはMystatus for Nukkitが表示しています。";
			this.cfg.set("Message2", "このメッセージ・ステータスはMystatus for Nukkitが表示しています。");
			this.cfg.save();
		}
		getServer().getOnlinePlayers().forEach((UUID, player) -> {
			try {
				long pm = this.economy.getMoney(player.getName());
				String pm2 = String.valueOf(pm);
				player.sendTip
				("							§b["+msg1+"§r§b] Name: " + player.getName() + "\n"
						+ "			  				§e所持金: §f¥" + pm2 + "\n"
						+ "	  						§a現在時刻: §f" + new SimpleDateFormat("yyyy年MM月dd日HH時mm分ss秒").format(Calendar.getInstance().getTime()) + "\n"
						+ "	  						§7所持アイテムのID: §f" + player.getInventory().getItemInHand().getId() + ":" + player.getInventory().getItemInHand().getDamage() + "\n"
						+ "	  						§6現在地: §2ワールド: §f" + player.getLevel().getName() + "\n"
						+ "	          						§cX: §f" + Math.round(player.getX()) + "\n"
						+ "           						 §2Y: §f" + Math.round(player.getY()) + "\n"
						+ "           						 §9Z: §f" + Math.round(player.getZ()) + "\n\n\n");
				player.sendActionBar("§l§aメッセージ: §f" + msg2 + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			}catch(NullPointerException e) {
				player.sendTip
				("							§b["+msg1+"§r§b] Name: " + player.getName() + "\n"
						+ "			  				§e所持金: §fおおがねもちだとおもいます(取得失敗)\n"
						+ "	  						§a現在時刻: §f" + new SimpleDateFormat("yyyy年MM月dd日HH時mm分ss秒").format(Calendar.getInstance().getTime()) + "\n"
						+ "	  						§7所持アイテムのID: §f" + player.getInventory().getItemInHand().getId() + ":" + player.getInventory().getItemInHand().getDamage() + "\n"
						+ "	  						§6現在地: §dワールド: §f" + player.getLevel().getName() + "\n"
						+ "	          						§cX: §f" + Math.round(player.getX()) + "\n"
						+ "           						 §2Y: §f" + Math.round(player.getY()) + "\n"
						+ "           						 §9Z: §f" + Math.round(player.getZ()) + "\n\n\n");
				player.sendActionBar("§l§aメッセージ: §f" + msg2 + "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			}
		});
	}

	public boolean onCommand(CommandSender sender, Command cmd, String Label, String[] args) {
		if(sender.isOp()) {
			try {
				switch(cmd.getName()) {
					case "setmessage1":
						if(args[0] != null) {
							String argsmsg1 = String.join(" ", args);
							this.cfg.set("Message1", argsmsg1);
							this.cfg.save();
							sender.sendMessage("§a[Mystatus]§bメッセージ1の設定が正常に完了しました。");
							return true;
						}else {
							sender.sendMessage("§a[Mystatus]§cエラー:002 メッセージを入力してください");
							return false;
						}
					case "setmessage2":
						if(args[0] != null) {
							String argsmsg2 = String.join(" ", args);
							this.cfg.set("Message2", argsmsg2);
							this.cfg.save();
							sender.sendMessage("§a[Mystatus]§bメッセージ2の設定が正常に完了しました。");
							return true;
						}else {
							sender.sendMessage("§a[Mystatus]§cエラー:002 メッセージを入力してください");
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
			return false;
		}
	}
}