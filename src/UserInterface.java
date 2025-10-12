import bagel.Font;
import bagel.Window;
import bagel.util.Point;

/**
 * Helper methods to display information for the player
 */
public class UserInterface {
    public static void drawStats(double health, double coins, int weaponLevel, int keys) {
        int fontSize = Integer.parseInt(ShadowDungeon.getGameProps().getProperty("playerStats.fontSize"));

        // Draw health
        drawData(String.format("%s %.1f", ShadowDungeon.getMessageProps().getProperty("healthDisplay"), health), fontSize,
                IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("healthStat")));

        // Draw coins
        drawData(String.format("%s %.0f", ShadowDungeon.getMessageProps().getProperty("coinDisplay"), coins), fontSize,
                IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("coinStat")));

        // Draw weapon level
        drawData(String.format("%s %d", ShadowDungeon.getMessageProps().getProperty("weaponDisplay"), weaponLevel), fontSize,
                IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("weaponStat")));

        // Draw keys
        drawData(String.format("%s %d", ShadowDungeon.getMessageProps().getProperty("keyDisplay"), keys), fontSize,
                IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("keyStat")));
    }

    public static void drawStartMessages() {
        drawTextCentered("title", Integer.parseInt(ShadowDungeon.getGameProps().getProperty("title.fontSize")), Double.parseDouble(ShadowDungeon.getGameProps().getProperty("title.y")));
        drawTextCentered("moveMessage", Integer.parseInt(ShadowDungeon.getGameProps().getProperty("prompt.fontSize")), Double.parseDouble(ShadowDungeon.getGameProps().getProperty("moveMessage.y")));
        drawTextCentered("selectMessage", Integer.parseInt(ShadowDungeon.getGameProps().getProperty("prompt.fontSize")), Double.parseDouble(ShadowDungeon.getGameProps().getProperty("selectMessage.y")));
    }

    public static void drawCharacterDescriptions() {
        int fontSize = Integer.parseInt(ShadowDungeon.getGameProps().getProperty("prompt.fontSize"));
        Font font = new Font("res/wheaton.otf", fontSize);

        // Draw Robot description
        String robotDesc = ShadowDungeon.getMessageProps().getProperty("robotDescription");
        Point robotPos = IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("robotMessage"));
        font.drawString(robotDesc, robotPos.x, robotPos.y);

        // Draw Marine description
        String marineDesc = ShadowDungeon.getMessageProps().getProperty("marineDescription");
        Point marinePos = IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("marineMessage"));
        font.drawString(marineDesc, marinePos.x, marinePos.y);
    }

    public static void drawEndMessage(boolean win) {
        drawTextCentered(win ? "gameEnd.won" : "gameEnd.lost", Integer.parseInt(ShadowDungeon.getGameProps().getProperty("title.fontSize")), Double.parseDouble(ShadowDungeon.getGameProps().getProperty("title.y")));
    }

    public static void drawTextCentered(String textPath, int fontSize, double posY) {
        Font font = new Font("res/wheaton.otf", fontSize);
        String text = ShadowDungeon.getMessageProps().getProperty(textPath);
        double posX = (Window.getWidth() - font.getWidth(text)) / 2;
        font.drawString(text, posX, posY);
    }

    public static void drawData(String data, int fontSize, Point location) {
        Font font = new Font("res/wheaton.otf", fontSize);
        font.drawString(data, location.x, location.y);
    }
}
