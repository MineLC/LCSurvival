package lc.survival.listeners;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import jdk.tools.jlink.plugin.Plugin;
import lc.survival.LCSurvival;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Random;
import static lc.survival.LCSurvival.prefix;

public class SurvivalListener implements Listener {

    public void WorldVIPCancelCommand(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().startsWith("/sethome") &&
                e.getPlayer().getWorld().getName().equalsIgnoreCase("vip")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(prefix+ChatColor.RED + "No puedes usar este comando aqui.");
        }
    }

    @EventHandler
    public void teleportvipworld(PlayerTeleportEvent e) {
        if (e.getTo() == null)
            return;
        if (((World)Objects.<World>requireNonNull(e.getTo().getWorld())).getName().equalsIgnoreCase("vip") &&
                !e.getPlayer().hasPermission("survival.vipworld")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(prefix+ChatColor.RED + "Debes tener rango VIP o superior para entrar a este mundo.");
        }
    }

    @EventHandler
    public void WeatherChange(WeatherChangeEvent e) {
        if (e.getWorld().getName().equalsIgnoreCase("Spawn")) {
            e.getWorld().setWeatherDuration(0);
            e.setCancelled(true);
        }
    }


    @EventHandler
    public void VoidTeleport(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player &&
                e.getCause() == EntityDamageEvent.DamageCause.VOID && e.getEntity().getWorld().getName().equalsIgnoreCase("spawn")) {
            e.setCancelled(true);
            Bukkit.getScheduler().runTaskLater(LCSurvival.getInstance(), () -> {
                e.getEntity().teleport(((World)Objects.<World>requireNonNull(Bukkit.getWorld("spawn"))).getSpawnLocation());
                e.getEntity().setFallDistance(0.0F);
            },1L);
        }
    }

    @EventHandler
    public void PreventSpawnVillagerZombie(CreatureSpawnEvent e) {
        if (e.getEntityType() == EntityType.ZOMBIE_VILLAGER)
            e.getEntity().remove();
    }

    @EventHandler
    public void PlayerDeadLostMoney(PlayerRespawnEvent e) {
        User usr = ((Essentials)Essentials.getPlugin(Essentials.class)).getUser(e.getPlayer());
        BigDecimal total = usr.getMoney();
        BigDecimal substrac = total.multiply(BigDecimal.valueOf((LCSurvival.getInstance()).money_lost_percentage)).divide(BigDecimal.valueOf(100L));
        total = total.subtract(substrac);
        usr.takeMoney(total);
    }

    @EventHandler
    public void PlayerLostXP(PlayerDeathEvent e) {
        if (e.getEntity().hasPermission("survival.savexp")) {
            e.setKeepLevel(true);
            return;
        }
        int percentage = e.getDroppedExp() - (int)((LCSurvival.getInstance()).xp_lost_percentage * e.getDroppedExp()) / 100;
        e.setNewExp(percentage);
    }

    @EventHandler
    public void preventNetherTeleport(PlayerTeleportEvent e) {
        if (!Objects.requireNonNull(Objects.requireNonNull(e.getTo()).getWorld()).getName().equalsIgnoreCase("nether"))
            return;
        if (e.getPlayer().isOp())
            return;
        if (((Location)Objects.<Location>requireNonNull(e.getTo())).getY() >= 127.0D) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(prefix+ChatColor.RED + "Altura maxima excedida, cancelando teletransporte.");
        }
    }

    @EventHandler
    public void preventArmorStand(PlayerInteractEvent e) {
        if (e.getItem() == null)
            return;
        if (e.getItem().getType() == Material.ARMOR_STAND) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(prefix+ChatColor.RED + "No es posible spawnear este item.");
        }
    }

    @EventHandler
    public void PlayerDropHead(PlayerDeathEvent e) {
        Bukkit.getScheduler().runTaskAsynchronously( LCSurvival.getInstance(), () -> {
            Player killer = e.getEntity().getKiller();
            if (killer == null)
                return;
            int percentage = (LCSurvival.getInstance()).head_drop_percentage;
            ItemStack item = killer.getItemInHand();
            if (item.getType() != Material.AIR && item.getItemMeta() != null)
                percentage += item.getItemMeta().getEnchantLevel(Enchantment.LOOT_BONUS_MOBS);
            boolean val = ((new Random()).nextInt(100) <= percentage);
            try {
                if (val) {
                    ItemStack head = new ItemStack(Material.PLAYER_HEAD);
                    SkullMeta skm = (SkullMeta)head.getItemMeta();
                    ((SkullMeta)Objects.<SkullMeta>requireNonNull(skm)).setOwningPlayer(Bukkit.getOfflinePlayer(((Player)Objects.<Player>requireNonNull(e.getEntity().getPlayer())).getName()));
                    skm.setDisplayName(ChatColor.GRAY + e.getEntity().getName());
                    head.setItemMeta(skm);
                    ((World) Objects.<World>requireNonNull(e.getEntity().getLocation().getWorld())).dropItem(e.getEntity().getLocation(), head);
                }
            } catch (Exception err) {
                err.printStackTrace();
            }
        });
    }

    @EventHandler
    public void CraftRecipeBook(PrepareItemCraftEvent e) {
        if (e.getRecipe() == null)
            return;
        if (e.getRecipe().getResult().getType() == Material.WRITABLE_BOOK || e
                .getRecipe().getResult().getType() == Material.WRITTEN_BOOK) {
            e.getInventory().setResult(new ItemStack(Material.AIR));
            e.getView().getPlayer().sendMessage(prefix+ChatColor.RED + "No puedes craftear esto!");
            e.getView().close();
        }
    }

}
