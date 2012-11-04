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
            if (unit.getId() == a.getId() || unit.getId() == b.getId()) {
                continue;
            }
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

        int n = points.length;

        Line2D[] result = new Line2D[n];

        for (int i = 0; i < n; i++) {
            result[i] = new Line2D.Double(points[i].getPoint2D(), points[(i + 1) % n].getPoint2D());
        }

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

    public static Tank getHitPrediction(Tank self, Tank target, boolean premium) {
        double speed = premium ? Constants.PREMIUM_SHELL_SPEED : Constants.REGULAR_SHELL_SPEED;
        double distanceTraveled = self.getVirtualGunLength();

        double x = target.getX();
        double y = target.getY();

        for (int tick = 0; tick < Constants.PREDICTION_TICK_TIME; tick++) {
            x += target.getSpeedX();
            y += target.getSpeedY();

            speed *= Constants.FRICTION_FACTOR;
            distanceTraveled += speed;

            if (self.getDistanceTo(x, y) <= distanceTraveled) {
                return new Tank(target.getId(), target.getPlayerName(), target.getTeammateIndex(), x, y, target.getSpeedX(), target.getSpeedY(), target.getAngle(), target.getAngularSpeed(), target.getTurretRelativeAngle(), target.getCrewHealth(), target.getHullDurability(), target.getReloadingTime(), target.getRemainingReloadingTime(), target.getPremiumShellCount(), target.isTeammate(), target.getType());
            }
        }

        return target;
    }

    public static boolean isInside(Unit a, Line2D[] lines) {
        int hits = 0;
        for (Line2D line : lines) {
            double lastX = line.getX1();
            double lastY = line.getY1();
            double currentX = line.getX2();
            double currentY = line.getY2();

            if (currentY == lastY) {
                continue;
            }

            double leftX;
            if (currentX < lastX) {
                if (a.getX() >= lastX) {
                    continue;
                }
                leftX = currentX;
            } else {
                if (a.getX() >= currentX) {
                    continue;
                }
                leftX = lastX;
            }

            double test1, test2;
            if (currentY < lastY) {
                if (a.getY() < currentY || a.getY() >= lastY) {
                    continue;
                }
                if (a.getX() < leftX) {
                    hits++;
                    continue;
                }
                test1 = a.getX() - currentX;
                test2 = a.getY() - currentY;
            } else {
                if (a.getY() < lastY || a.getY() >= currentY) {
                    continue;
                }
                if (a.getX() < leftX) {
                    hits++;
                    continue;
                }
                test1 = a.getX() - lastX;
                test2 = a.getY() - lastY;
            }

            if (test1 < (test2 / (lastY - currentY) * (lastX - currentX))) {
                hits++;
            }
        }

        return ((hits & 1) != 0);
    }

    // not guaranteed
    public static boolean checkIntersection(Unit a, Unit b) {
        Line2D[] aLines = getBoundingLines(a);
        Line2D[] bLines = getBoundingLines(b);

        for (Line2D x : aLines) {
            for (Line2D y : bLines) {
                if (x.intersectsLine(y)) {
                    return true;
                }
            }
        }

        for (Line2D line : aLines) {
            if (isInside(new Point(line.getX1(), line.getY1()), bLines)) {
                return true;
            }
        }
        for (Line2D line : bLines) {
            if (isInside(new Point(line.getX1(), line.getY1()), aLines)) {
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

        if (l == 0)
            return new Point[]{new Point(unit.getX(), unit.getY())};

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
