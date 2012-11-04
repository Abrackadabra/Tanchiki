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

        double angle = self.getAngleTo(target);

        double power = 1.0 - angle * angle * 10;

        if (power < 0.2) power = -1.0;

        if (angle > 0) {
            move.setLeftTrackPower(1.0);
            move.setRightTrackPower(power);
        } else {
            move.setLeftTrackPower(power);
            move.setRightTrackPower(1.0);
        }
    }
}
