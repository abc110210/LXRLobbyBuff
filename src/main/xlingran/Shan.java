package main.xlingran;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class Shan extends JavaPlugin implements Listener {

    private static final int TICKS_PER_MINUTE = 1200;
    private static final int INFINITE_DURATION = Integer.MAX_VALUE;
    private static final int DEFAULT_AMPLIFIER = 0;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "欢迎使用寄寄の家 " + ChatColor.AQUA + "大厅效果" + ChatColor.GREEN + " 插件,交流群: 943446220");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "插件 " + ChatColor.AQUA + "大厅效果" + ChatColor.RED + " 已卸载，感谢使用寄寄の家插件!");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        applyBuffs(event.getPlayer());
    }

    private void applyBuffs(Player player) {
        FileConfiguration config = getConfig();
        ConfigurationSection buffSection = config.getConfigurationSection("Buff");

        if (buffSection == null) {
            return;
        }

        int duration = getDurationTicks(config.getInt("Time", 0));

        for (String effectName : buffSection.getKeys(false)) {
            PotionEffectType effectType = PotionEffectType.getByName(effectName);
            if (effectType == null) {
                getLogger().warning("未知的效果类型: " + effectName);
                continue;
            }

            int amplifier = buffSection.getInt(effectName, DEFAULT_AMPLIFIER);
            if (amplifier < 1) {
                getLogger().warning("效果" + effectName + " 的等级必须 >= 1，当前: " + amplifier);
                continue;
            }

            player.addPotionEffect(new PotionEffect(
                effectType,
                duration,
                amplifier - 1,
                false,
                false,
                true
            ));
        }
    }

    private static int getDurationTicks(int minutes) {
        return minutes == 0 ? INFINITE_DURATION : minutes * TICKS_PER_MINUTE;
    }
}
