import model.*;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.util.Comparator;
import java.util.HashMap;

class Utils {
    public static boolean checkForObstaclesBetween(Unit a, Unit b, MyWorld myWorld) {
        // TODO implement

        return false;
    }

    public static class DistanceComparator implements Comparator<Unit> {
        Unit center;

        public DistanceComparator(Unit center) {
            this.center = center;
        }

        @Override
        public int compare(Unit o1, Unit o2) {
            double d1 = center.getDistanceTo(o1);
            double d2 = center.getDistanceTo(o2);
            if (d1 != d2) {
                return d1 < d2 ? -1 : 1;
            }
            if (o1.getId() == o2.getId()) return 0;
            return o1.getId() < o2.getId() ? -1 : 1;
        }
    }

    public static class AbsoluteAngleComparator implements Comparator<Unit> {
        Unit center;

        public AbsoluteAngleComparator(Unit center) {
            this.center = center;
        }

        @Override
        public int compare(Unit o1, Unit o2) {
            double d1 = Math.abs(center.getAngleTo(o1));
            double d2 = Math.abs(center.getAngleTo(o2));
            if (d1 != d2) {
                return d1 < d2 ? -1 : 1;
            }
            if (o1.getId() == o2.getId()) return 0;
            return o1.getId() < o2.getId() ? -1 : 1;
        }
    }

    public static class AbsoluteTurretAngleComparator implements Comparator<Unit> {
        Tank center;

        public AbsoluteTurretAngleComparator(Tank center) {
            this.center = center;
        }

        @Override
        public int compare(Unit o1, Unit o2) {
            double d1 = Math.abs(center.getTurretAngleTo(o1));
            double d2 = Math.abs(center.getTurretAngleTo(o2));
            if (d1 != d2) {
                return d1 < d2 ? -1 : 1;
            }
            if (o1.getId() == o2.getId()) return 0;
            return o1.getId() < o2.getId() ? -1 : 1;
        }
    }

    public static class ScoreComparator implements Comparator<Tank> {
        HashMap<String, Integer> scores;

        public ScoreComparator(HashMap<String, Integer> scores) {
            this.scores = scores;
        }

        @Override
        public int compare(Tank o1, Tank o2) {
            int s1 = 0;
            if (scores.containsKey(o1.getPlayerName()))
                s1 = scores.get(o1.getPlayerName());
            int s2 = 0;
            if (scores.containsKey(o2.getPlayerName()))
                s2 = scores.get(o2.getPlayerName());
            if (s1 != s2) {
                return s1 < s2 ? 1 : -1;
            }
            if (o1.getId() == o2.getId()) return 0;
            return o1.getId() < o2.getId() ? -1 : 1;
        }
    }

    public static Tank getHitPrediction(Tank self, Tank target, boolean premium) {
        //TODO implement

        double speed = premium ? Constants.premiumShellSpeed : Constants.regularShellSpeed;
        double distanceTraveled = self.getVirtualGunLength();

        double x = target.getX();
        double y = target.getY();

        for (int tick = 0; tick < 100; tick++) {
            x += target.getSpeedX() * 0.9;
            y += target.getSpeedY() * 0.9;

            speed *= Constants.frictionFactor;
            distanceTraveled += speed;

            if (Math.abs(self.getDistanceTo(x, y) - distanceTraveled) < target.getWidth()) {
                return new Tank(
                        target.getId(),
                        target.getPlayerName(),
                        target.getTeammateIndex(),
                        x,
                        y,
                        target.getSpeedX(),
                        target.getSpeedY(),
                        target.getAngle(),
                        target.getAngularSpeed(),
                        target.getTurretRelativeAngle(),
                        target.getCrewHealth(),
                        target.getHullDurability(),
                        target.getReloadingTime(),
                        target.getRemainingReloadingTime(),
                        target.getPremiumShellCount(),
                        target.isTeammate(),
                        target.getType()
                );
            }
        }

        return target;
    }

    public static boolean isBlocked(Tank tank, MyWorld myWorld) {
        double x = tank.getX(), y = tank.getY();
        x += tank.getVirtualGunLength() * Math.cos(tank.getAngle());
        y += tank.getVirtualGunLength() * Math.sin(tank.getAngle());

        if (x < 0 || x > myWorld.getWidth() || y < 0 || y > myWorld.getHeight()) return true;

        for (Tank anotherTank : myWorld.getTanks()) {
            if (tank.getId() == anotherTank.getId()) continue;
            if (anotherTank.getDistanceTo(x, y) < Math.min(anotherTank.getWidth(), anotherTank.getHeight()))
                return true;
        }

        return false;
    }

    public static boolean isInside(Unit a, Unit b) {
        // TODO make better
        return a.getDistanceTo(b) <= Math.min(b.getWidth(), b.getHeight());
    }

    public static void invokeApplet(Applet applet) {
        JFrame app = new JFrame("Applet Container");
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setSize(1280, 800);

        applet.init();

        app.setLayout(new BorderLayout());
        // include it as a component.  local testing can now start
        app.getContentPane().add(applet, BorderLayout.CENTER);
        app.setVisible(true);
    }
}
