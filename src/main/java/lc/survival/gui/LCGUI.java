package lc.survival.gui;

import lc.survival.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import static lc.survival.utils.MessageUtils.color;

@SuppressWarnings("deprecation")
public class LCGUI implements InventoryHolder {

    private final int id;
    private final String title;
    private final int size;
    private final Inventory inv;

    public LCGUI(int id, String title, int size) {
        this.id = id;
        this.title = title;
        this.size = size;
        inv = Bukkit.createInventory(this, size, color(getTitle()));
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getSize() {
        return size;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }

    public void onClick(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        ItemStack i = e.getCurrentItem();
        int slot = e.getSlot();
        if(getId() == 0){
            for(Map.Entry<Integer, List<GUIAction>> en : GUIManager.MAIN_ITEMS_ACTIONS.entrySet()){
                if(en.getKey() == slot){
                    executeActions(en);
                }
            }
        }
    }

    private void executeActions(Player p, Map.Entry<Integer, List<GUIAction>> en) {
        for(GUIAction action : en.getValue()){
            switch (action.getType()){
                case CMD: {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), action.getValue().replace("/", ""));
                    return;
                }
                case MSG: {
                    MessageUtils.sendMessage(p, action.getValue());
                    return;
                }
                case PLAYER_CMD: {
                    Bukkit.dispatchCommand(p, action.getValue().replace("/", ""));
                    return;
                }
                case GUI: {
                    p.closeInventory();
                    if(action.getValue().equalsIgnoreCase("main")){
                        GUIManager.showMainGUI(p);
                    }
                    if(action.getValue().equalsIgnoreCase("cazeria")) GUIManager.showCazeriaGUI(p);
                    if(action.getValue().equalsIgnoreCase("asesino")) GUIManager.showAsesinoGUI(p);
                    if(action.getValue().equalsIgnoreCase("mineria")) GUIManager.showMineriaGUI(p);

                }
            }
        }
    }
}
