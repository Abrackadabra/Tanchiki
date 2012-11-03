import model.*;

import javax.swing.*;
import java.applet.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;

class Utils {
    private Utils() {}

    public static boolean checkForObstaclesBetween(Unit a, Unit b, MyWorld world) {
        Line2D line = new Line2D.Double(a.getX(), a.getY(), b.getX(), b.getY());

        for (Unit unit : world.getShellStoppers()) {
            for (Line2D line2D : getBoundingLines(unit)) {
                if (line.intersectsLine(line2D)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static Line2D[] getBoundingLines(Unit unit) {
        Point[] points = getPoints(unit);
        Line2D[] result = new Line2D[4];

        result[0] = new Line2D.Double(points[0].getPoint2D(), points[1].getPoint2D());
        result[1] = new Line2D.Double(points[1].getPoint2D(), points[2].getPoint2D());
        result[2] = new Line2D.Double(points[2].getPoint2D(), points[3].getPoint2D());
        result[3] = new Line2D.Double(points[3].getPoint2D(), points[0].getPoint2D());

        return result;
    }

    public static class DistanceComparator implements Comparator<Unit> {
        private final Unit center;

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
            if (o1.getId() == o2.getId()) {
                return 0;
            }
            return o1.getId() < o2.getId() ? -1 : 1;
        }
    }

    public static class AbsoluteAngleComparator implements Comparator<Unit> {
        private final Unit center;

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
            if (o1.getId() == o2.getId()) {
                return 0;
            }
            return o1.getId() < o2.getId() ? -1 : 1;
        }
    }

    public static class AbsoluteTurretAngleComparator implements Comparator<Unit> {
        private final Tank center;

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
            if (o1.getId() == o2.getId()) {
                return 0;
            }
            return o1.getId() < o2.getId() ? -1 : 1;
        }
    }

    public static Unit getHitPrediction(Tank self, Unit target, boolean premium) {
        double speed = premium ? Constants.premiumShellSpeed : Constants.regularShellSpeed;
        double distanceTraveled = self.getVirtualGunLength();

        double x = target.getX();
        double y = target.getY();

        for (int tick = 0; tick < Constants.predictionTickTime; tick++) {
            x += target.getSpeedX();
            y += target.getSpeedY();

            speed *= Constants.frictionFactor;
            distanceTraveled += speed;

            if (self.getDistanceTo(x, y) <= distanceTraveled) {
                return new Point(x, y);
            }
        }

        return target;
    }

    @Deprecated
    public static boolean isBlocked(Tank tank, MyWorld myWorld) {
        double x = tank.getX(), y = tank.getY();
        x += tank.getVirtualGunLength() * Math.cos(tank.getAngle());
        y += tank.getVirtualGunLength() * Math.sin(tank.getAngle());

        if (x < 0.0 || x > myWorld.getWidth() || y < 0.0 || y > myWorld.getHeight()) {
            return true;
        }

        for (Tank anotherTank : myWorld.getTanks()) {
            if (tank.getId() == anotherTank.getId()) {
                continue;
            }
            if (anotherTank.getDistanceTo(x, y) < Math.min(anotherTank.getWidth(), anotherTank.getHeight())) {
                return true;
            }
        }

        return false;
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
        double l = Math.hypot(unit.getWidth() / 2.0, unit.getHeight() / 2.0);
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
