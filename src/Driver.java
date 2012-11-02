import model.*;

public class Driver {
    Tank self;
    MyWorld myWorld;
    Move move;

    Unit target;

    final Commander commander;

    public Driver(Commander commander) {
        this.commander = commander;
    }

    void act() {
        this.self = commander.getSelf();
        this.myWorld = commander.getMyWorld();
        this.move = commander.getMove();

        findTarget();

        if (target != null && !Utils.isBlocked(self, myWorld)) {
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

    void findTarget() {
        target = null;
    }
}
