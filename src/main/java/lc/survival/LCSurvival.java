package lc.survival;

import lc.survival.configuration.LCConfig;
import lc.survival.listeners.SurvivalListener;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;
import java.util.Random;

public final class LCSurvival extends JavaPlugin {

    private static LCSurvival instance;

    public int money_lost_percentage = 100;
    public final static String prefix = ChatColor.translateAlternateColorCodes('&', "&8[&6&lSURVIVAL&r&8] &r");

    public double xp_lost_percentage = 100.0D;

    public int head_drop_percentage = 1;

    public LCConfig config;

    public static LCSurvival getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        this.config = new LCConfig("config.yml", null, this);
        this.config.registerConfig();
        this.money_lost_percentage = this.config.getConfig().getInt("percentage.dead_money");
        this.xp_lost_percentage = this.config.getConfig().getDouble("percentage.lost_xp");
        this.head_drop_percentage = this.config.getConfig().getInt("percentage.head_drop");
        announcer();
        cleaner();
        Bukkit.getPluginManager().registerEvents(new SurvivalListener(), this);
        ((PluginCommand) Objects.<PluginCommand>requireNonNull(getCommand("armorclear"))).setExecutor(new ArmorCleanCommand());
        checkEntities();
    }

    public void checkEntities() {
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (World w : Bukkit.getWorlds()) {
                int size = w.getEntities().size();
                if (size < 200)
                    return;
                for (org.bukkit.entity.Entity ent : w.getEntities()) {
                    EntityType type = ent.getType();
                    if (!type.isSpawnable())
                        return;
                    if (type == EntityType.ENDERMITE || type == EntityType.ARMOR_STAND || type == EntityType.DONKEY || type == EntityType.PLAYER || type == EntityType.HORSE || type == EntityType.SKELETON_HORSE || type == EntityType.ZOMBIE_HORSE || type == EntityType.LLAMA || type == EntityType.OCELOT || type == EntityType.WOLF || type == EntityType.VILLAGER || type == EntityType.PAINTING || type == EntityType.PARROT || type.name().contains("MINECART") || type == EntityType.ITEM_FRAME)
                        continue;
                    ent.remove();
                    size--;
                }
            }
            getLogger().info("Limpiando entidades");
        },200L, 6000L);
    }

    public void announcer() {
        (new BukkitRunnable() {
            public void run() {
                int index = (new Random()).nextInt(config.getConfig().getStringList("announcer.list").size());
                String str = ChatColor.translateAlternateColorCodes('&', config.getConfig().getStringList("announcer.list").get(index));
                for (Player pall : Bukkit.getOnlinePlayers()) {
                    String placeholder = PlaceholderAPI.setPlaceholders(pall, str);
                    if (placeholder.contains(";")) {
                        for (String list : placeholder.split(";")) {
                            pall.sendMessage(prefix+list);
                            pall.playSound(pall.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 5.0F, 5.0F);
                        }
                        continue;
                    }
                    pall.sendMessage(prefix+placeholder);
                    pall.playSound(pall.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 5.0F, 5.0F);
                }
            }
        }).runTaskTimerAsynchronously((Plugin)this, 200L, 20L * this.config.getConfig().getInt("announcer.time"));
    }

    public void cleaner() {
        (new BukkitRunnable() {
            public void run() {
                cleanertime();
            }
        }).runTaskTimerAsynchronously((Plugin)this, 250L, 20L * this.config.getConfig().getInt("clear_lag.time"));
    }

    public void removearmorstands() {
        long init = System.currentTimeMillis();
        int value = 0;
        for (World world : Bukkit.getWorlds()) {
            for (Entity ent : world.getEntities()) {
                if (ent.getType() == EntityType.ARMOR_STAND) {
                    ArmorStand armorStand = (ArmorStand)ent;
                    ent.getLocation().getBlock().setType(Material.CHEST);
                    Chest chest = (Chest)ent.getLocation().getBlock().getState();
                    chest.getBlockInventory().addItem(new ItemStack[] { armorStand.getBoots() });
                    chest.getBlockInventory().addItem(new ItemStack[] { armorStand.getHelmet() });
                    chest.getBlockInventory().addItem(new ItemStack[] { armorStand.getLeggings() });
                    chest.getBlockInventory().addItem(new ItemStack[] { armorStand.getChestplate() });
                    chest.getBlockInventory().addItem(new ItemStack[] { armorStand.getItemInHand() });
                    ent.remove();
                    value++;
                }
            }
        }
        long end = System.currentTimeMillis();
        Bukkit.getConsoleSender().sendMessage(prefix+ChatColor.RED + "" + value + "entidades removidas en (" + (end - init) + ")");
    }

    private void cleanertime() {
        (new BukkitRunnable() {
            int time = 60;

            public void run() {
                this.time--;
                if (this.time == 30 || this.time == 10)
                    Bukkit.getOnlinePlayers().forEach(p -> {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+config.getConfig().getString("clear_lag.faltan").replaceAll("%time%",  String.valueOf(time))));
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 5.0F, 5.0F);
                    });
                if (this.time > 0)
                    return;
                int counter = 0;
                for (World wlrd : Bukkit.getWorlds()) {
                    for (Entity ent : wlrd.getEntities()) {
                        if (ent.getType() == EntityType.DROPPED_ITEM) {
                            counter++;
                            ent.remove();
                        }
                    }
                }
                if (counter != 0)
                    for (Player pall : Bukkit.getOnlinePlayers()) {
                        pall.playSound(pall.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 5.0F, 5.0F);
                        pall.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix+config.getConfig().getString("clear_lag.announcer").replaceAll("%amount%", counter + "")));
                    }
                cancel();
            }
        }).runTaskTimer((Plugin)this, 0L, 20L);
    }
}
