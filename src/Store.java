import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;

/**
 * Store/Shop that allows player to purchase health and weapon upgrades
 */
public class Store {
    private final Image storeImage;
    private final Point position;
    private boolean isOpen = false;
    private final int healthCost;
    private final int healthBonus;
    private final int weaponCost;

    public Store() {
        this.storeImage = new Image("res/store.png");
        this.position = IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("store"));
        this.healthCost = Integer.parseInt(ShadowDungeon.getGameProps().getProperty("healthPurchase"));
        this.healthBonus = Integer.parseInt(ShadowDungeon.getGameProps().getProperty("healthBonus"));
        this.weaponCost = Integer.parseInt(ShadowDungeon.getGameProps().getProperty("weaponPurchase"));
    }

    public void update(Input input, Player player) {
        // Toggle store with Space key
        if (input.wasPressed(Keys.SPACE)) {
            isOpen = !isOpen;
        }

        // Handle purchases when store is open
        if (isOpen) {
            // Purchase health with E key
            if (input.wasPressed(Keys.E)) {
                purchaseHealth(player);
            }

            // Purchase weapon upgrade with L key
            if (input.wasPressed(Keys.L)) {
                purchaseWeaponUpgrade(player);
            }

            // Restart game with P key
            if (input.wasPressed(Keys.P)) {
                ShadowDungeon.resetGameState(ShadowDungeon.getGameProps());
            }
        }
    }

    public void draw() {
        if (isOpen) {
            storeImage.draw(position.x, position.y);
        }
    }

    private void purchaseHealth(Player player) {
        if (player.getCoins() >= healthCost) {
            player.spendCoins(healthCost);
            player.addHealth(healthBonus);
        }
    }

    private void purchaseWeaponUpgrade(Player player) {
        if (player.getCoins() >= weaponCost && player.getWeaponLevel().canUpgrade()) {
            player.spendCoins(weaponCost);
            player.upgradeWeapon();
        }
    }

    public boolean isOpen() {
        return isOpen;
    }
}
