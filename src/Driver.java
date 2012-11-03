import model.*;

public class Driver {
    private Tank self;
    private MyWorld world;
    private Move move;

    private final Commander commander;

    public Driver(Commander commander) {
        this.commander = commander;
    }

    public void act() {
        this.self = commander.getSelf();
        this.world = commander.getWorld();
        this.move = commander.getMove();

        Point target = commander.getIrina().getTarget();

        if (target != null && !Utils.isBlocked(self, world)) {
            double angle = self.getAngleTo(target);
            if (Math.abs(angle) < Math.PI / 2) {
                double leftPower = 1.0;
                double rightPower = 1.0;
                if (angle > 0) {
                    rightPower -= Math.abs(angle) / (Math.PI / 2) * 2;
                } else {
                    leftPower -= Math.abs(angle) / (Math.PI / 2) * 2;
                }
                if (leftPower < 0.3) leftPower = -1;
                if (rightPower < 0.3) rightPower = -1;
                move.setLeftTrackPower(leftPower);
                move.setRightTrackPower(rightPower);
            } else {
                if (angle < 0) {
                    angle += Math.PI;
                } else {
                    angle -= Math.PI;
                }
                double leftPower = -1.0;
                double rightPower = -1.0;
                if (angle > 0) {
                    leftPower += Math.abs(angle) / (Math.PI / 2) * 2;
                } else {
                    leftPower += Math.abs(angle) / (Math.PI / 2) * 2;
                }
                if (leftPower > -0.3) leftPower = 1;
                if (rightPower > -0.3) rightPower = 1;
                move.setLeftTrackPower(leftPower);
                move.setRightTrackPower(rightPower);
            }
        } else {
            move.setLeftTrackPower(-0.5);
            move.setRightTrackPower(-0.5);
        }
    }
}
