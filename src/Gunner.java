import model.*;

public class Gunner {
    private       Tank      self;
    private       MyWorld   world;
    private       Move      move;
    private final Commander commander;

    public Gunner(Commander commander) {
        this.commander = commander;
    }

    public void act() {
        self = commander.getSelf();
        world = commander.getWorld();
        move = commander.getMove();

        findTarget();
        fireAtWill();
    }

    private Tank target;

    double evaluateTarget(Tank target) {
        if (Utils.checkForObstaclesBetween(self, target, world)) {
            return 0.0;
        }

        double value = 1.0;

        value -= Math.abs(self.getTurretAngleTo(target)) / Math.PI * 0.4;

        value -= Math.min(self.getDistanceTo(target) / world.getHeight(), 1.0) * 0.4;

        value -= Math.min(world.getScoreboardPosition(target.getPlayerName()) / 3.0, 1.0) * 0.2;

        return value;
    }

    private void findTarget() {
        target = null;
        boolean premium = self.getPremiumShellCount() > 0;
        double max = 0.0;
        for (Tank tank : world.getEnemies()) {
            Tank candidate = Utils.getHitPrediction(self, tank, premium);
            double score = evaluateTarget(candidate);
            if (score > max) {
                max = score;
                target = candidate;
            }
        }
    }

    private void fireAtWill() {
        move.setFireType(FireType.NONE);
        double angle = -self.getTurretRelativeAngle();
        if (target != null) {
            angle = self.getTurretAngleTo(target);
            if (Math.abs(angle) < Constants.SHOOTING_ANGLE) {
                move.setFireType(FireType.PREMIUM_PREFERRED);
            }
        }
        move.setTurretTurn(angle);
    }

    public Tank getTarget() {
        return target;
    }
}