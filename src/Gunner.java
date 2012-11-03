import model.*;

public class Gunner {
    private       Tank      self;
    private       MyWorld   myWorld;
    private       Move      move;
    private final Commander commander;

    public Gunner(Commander commander) {
        this.commander = commander;
    }

    public void act() {
        self = commander.getSelf();
        myWorld = commander.getWorld();
        move = commander.getMove();

        fireAtWill(findTarget());
        double angle;
        if (target != null) {
            angle = self.getTurretAngleTo(target);
            if (Math.abs(angle) < Constants.dangerousAngle) {
                move.setFireType(FireType.PREMIUM_PREFERRED);
            } else {
                move.setFireType(FireType.NONE);
            }
        } else {
            angle = -self.getTurretRelativeAngle();
            move.setFireType(FireType.NONE);
        }
        move.setTurretTurn(angle * 10.0);
    }

    private Unit target;

    private Unit findTarget() {
        target = null;
        return null;
        // TODO implement
        /*
        int n = myWorld.getEnemies().length;
        Tank[] enemies = new Tank[n];
        for (int i = 0; i < n; i++)
            enemies[i] = Utils.getHitPrediction(self, myWorld.getEnemies()[i], self.getPremiumShellCount() > 0);

        HashMap<Long, Integer> localID = new HashMap<Long, Integer>();
        for (int i = 0; i < n; i++) {
            localID.put(enemies[i].getId(), i);
        }
        double[] priority = new double[n];

        Arrays.sort(enemies, new Utils.AbsoluteTurretAngleComparator(self));
        for (int i = 0; i < n; i++) {
            priority[localID.get(enemies[i].getId())] += 4 - i * 0.4;
        }
        Arrays.sort(enemies, new Utils.DistanceComparator(self));
        for (int i = 0; i < n; i++) {
            priority[localID.get(enemies[i].getId())] += 3 - i * 0.4;
        }

        for (int i = 0; i < n; i++) {
            if (Utils.checkForObstaclesBetween(self, enemies[i], myWorld)) {
                priority[localID.get(enemies[i].getId())] -= 6;
            }
        }

        double best = -1e9;
        for (int i = 0; i < n; i++) {
            if (priority[localID.get(enemies[i].getId())] > best) {
                best = priority[localID.get(enemies[i].getId())];
                target = enemies[i];
            }
        }         */
    }

    private void fireAtWill(Unit target) {

    }

    public Unit getTarget() {
        return target;
    }
}