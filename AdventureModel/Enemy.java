package AdventureModel;


public abstract class Enemy {

    public int x;
    public int y;
    public int hp;

    public int dmg;
    public int prevX;
    public int prevY;



    public Enemy(int hp, int dmg){
        this.dmg = dmg;
        this.x = 3;
        this.y = 3;
        this.prevX = 3;
        this.prevY = 3;
        this.hp = hp;




    }

    public void move(int nextX, int nextY) {
        prevX = x;
        prevY = y;
        x = nextX;
        y = nextY;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return  this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setPrevX(int x) {
        this.prevX = x;
    }

    public void setPrevY(int y) {
        this.prevY = y;
    }

    public int getPrevX() {
        return  this.prevX;
    }

    public int getPrevY() {
        return this.prevY;
    }

    public int setHp(int hp) {
        return this.hp = hp;
    }




}

