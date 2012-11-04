import model.*;

public class Driver {
    private Tank    self;
    private MyWorld world;
    private Move    move;

    private final Commander commander;

    public Driver(Commander commander) {
        this.commander = commander;
    }

    public void act() {
        self = commander.getSelf();
        world = commander.getWorld();
        move = commander.getMove();

        Unit target = commander.getIrina().getTarget();


    }
}
