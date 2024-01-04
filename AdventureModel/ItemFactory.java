package AdventureModel;

public class ItemFactory {

    /**
        Create a new inventory item. (FACTORY PATTERN)

        @param name The type of item to be created.
        @param model The adventure game model context.

        @return An appropriate InventoryItem object if the name
                 exists, null otherwise
    */
    public static InventoryItem getItem(String name, AdventureGame model) {
        return switch (name) {
            case "SWORD" -> new Sword("SWORD", model);
            case "HEALTHPOTION" -> new HealthPotion("HP POTION", model);
            case "KEY" -> new Key("KEY", model);
            case "GOLD" -> new Gold("GOLD", model);
            case "DIAMOND" -> new Diamond("DIAMOND", model);
            default -> null;
        };
    }
}
