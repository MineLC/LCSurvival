package lc.survival.gui;

public class GUIAction {

    private final GUIActionType type;
    private final String value;


    public GUIAction(GUIActionType type, String value) {
        this.type = type;
        this.value = value;
    }

    public GUIActionType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
