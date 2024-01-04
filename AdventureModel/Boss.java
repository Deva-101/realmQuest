package AdventureModel;

public class Boss extends Enemy {

    public int x;
    public int y;
    public final int hp = 5;

    public final int dmg = 3;
    public int prevX;
    public int prevY;

    private static Boss instance;

    private Boss() {
        super(5, 3);
    }

    public static Boss getInstance(int hp, int dmg) {
        if(instance == null) {
            instance = new Boss();
        }

        return instance;
    }
}
