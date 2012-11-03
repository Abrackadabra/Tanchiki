import model.*;

public class Gunner {
    Tank self;
    MyWorld myWorld;
    Move move;
    final Commander commander;

    Tank target;

    public Gunner(Commander commander) {
        this.commander = commander;
    }

    void act() {
        this.self = commander.getSelf();
        this.myWorld = commander.getWorld();
        this.move = commander.getMove();

        findTarget();
        double angle;
        if (target != null) {
            angle = self.getTurretAngleTo(target);
            if (Math.abs(angle) < Constants.dangerousAngle)
                move.setFireType(FireType.PREMIUM_PREFERRED);
            else
                move.setFireType(FireType.NONE);
        } else {
            angle = -self.getTurretRelativeAngle();
            move.setFireType(FireType.NONE);
        }
        move.setTurretTurn(angle * 10);
    }

    void findTarget() {
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

    public Tank getTarget() {
        return target;
    }
}