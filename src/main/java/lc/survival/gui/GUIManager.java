package lc.survival.gui;

import lc.survival.utils.MessageUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUIManager {

    private static LCGUI main;
    public static int MAIN_SiZE = 9;
    public static Map<Integer, ItemStack> MAIN_ITEMS = new HashMap<>();
    public static Map<Integer, List<GUIAction>> MAIN_ITEMS_ACTIONS = new HashMap<>();

    public static String MAIN_TITLE = "&8Menú de Misiones";

    public static void showMainGUI(Player player){
        if(main == null) createMainGUI();
        player.openInventory(main.getInventory());
    }

    private static void createMainGUI() {
        main = new LCGUI(0, MAIN_TITLE, MAIN_SiZE);
        Inventory inv = main.getInventory();
        for(Map.Entry<Integer, ItemStack> e : MAIN_ITEMS.entrySet()){
            if(e.getKey() >= main.getSize()){
                MessageUtils.console("&c[LCSurvival] El SLOT "+e.getKey()+" es mayor que el tamaño de la GUI \"main\" ("+main.getSize()+")");
                continue;
            }
            inv.setItem(e.getKey(), e.getValue());
        }
    }
}
