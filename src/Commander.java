import model.*;

public class Commander {
    private Gunner gunner;
    private Driver driver;
    private Irina  irina;

    private Tank    self;
    private MyWorld world;
    private Move    move;

    public void act(Tank self, MyWorld world, Move move) {
        this.self = self;
        this.world = world;
        this.move = move;

        if (gunner == null) {
            gunner = new Gunner(this);
        }
        if (driver == null) {
            driver = new Driver(this);
        }
        if (irina == null) {
            irina = new Irina(this);
        }

        irina.act();

        gunner.act();
        driver.act();
    }

    public Gunner getGunner() {
        return gunner;
    }

    public Driver getDriver() {
        return driver;
    }

    public Irina getIrina() {
        return irina;
    }

    public Tank getSelf() {
        return self;
    }

    public MyWorld getWorld() {
        return world;
    }

    public Move getMove() {
        return move;
    }
}
