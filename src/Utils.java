import model.*;

import javax.swing.*;
import java.applet.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;

class Utils {
    public static boolean checkForObstaclesBetween(Unit a, Unit b, MyWorld world) {
        Line2D.Double line = new Line2D.Double(a.getX(), a.getY(), b.getX(), b.getY());

        boolean result = false;

        for (Unit unit : world.getShellStoppers()) {
            Point[] points = getPoints(unit);
            result |= line.intersectsLine(points[0].getX(), points[0].getY(), points[1].getX(), points[1].getY());
            result |= line.intersectsLine(points[1].getX(), points[1].getY(), points[2].getX(), points[2].getY());
            result |= line.intersectsLine(points[2].getX(), points[2].getY(), points[3].getX(), points[3].getY());
            result |= line.intersectsLine(points[3].getX(), points[3].getY(), points[0].getX(), points[0].getY());
        }

        return result;
    }

    public static class DistanceComparator implements Comparator<Unit> {
        private Unit center;

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
        private Unit center;

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
        private Tank center;

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
        JFrame jFrame = new JFrame("Applet Container");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(1280, 800);

        applet.init();

        jFrame.setLayout(new BorderLayout());
        jFrame.getContentPane().add(applet, BorderLayout.CENTER);
        jFrame.setVisible(true);
    }

    public static Point[] getPoints(Unit unit) {
        double l = Math.hypot(unit.getWidth() / 2, unit.getHeight() / 2);
        double angle = Math.asin(unit.getWidth() / 2 / l);
        Point[] result = new Point[4];
        double a;

        a = unit.getAngle() + angle;
        result[0] = new Point(unit.getX() + l * Math.cos(a), unit.getY() + l * Math.sin(a));
        a = unit.getAngle() + Math.PI - angle;
        result[1] = new Point(unit.getX() + l * Math.cos(a), unit.getY() + l * Math.sin(a));
        a = unit.getAngle() - Math.PI + angle;
        result[2] = new Point(unit.getX() + l * Math.cos(a), unit.getY() + l * Math.sin(a));
        a = unit.getAngle() - angle;
        result[3] = new Point(unit.getX() + l * Math.cos(a), unit.getY() + l * Math.sin(a));

        return result;
    }
}
