package lc.survival.listeners;

import lc.survival.gui.LCGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event){
        if(event.getWhoClicked() instanceof Player){
            if(event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR){
                if(event.getClickedInventory().getHolder() instanceof LCGUI){
                    LCGUI gui = (LCGUI) event.getClickedInventory().getHolder();
                    gui.onClick(event);
                }
            }
        }
    }
}
