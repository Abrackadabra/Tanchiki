import model.*;

import java.awt.*;
import java.util.*;

public class Irina {
    private final int n, m;
    private final ArrayList<Point> points = new ArrayList<Point>();
    private final Commander commander;

    private Tank    self;
    private MyWorld world;


    public Irina(Commander commander) {
        this.commander = commander;
        n = (int) (commander.getWorld().getHeight() / Constants.TILE_SIZE);
        m = (int) (commander.getWorld().getWidth() / Constants.TILE_SIZE);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                points.add(new Point((j + 0.5) * Constants.TILE_SIZE, (i + 0.5) * Constants.TILE_SIZE));
            }
        }
    }

    private Point target;

    public void act() {
        Collections.shuffle(points);
        long start = System.nanoTime();

        world = commander.getWorld();
        self = commander.getSelf();

        target = null;
        double max = 0.0;
        for (Point point : points) {
            point.setPriority(evaluatePoint(point));
            if (point.getPriority() > max) {
                max = point.getPriority();
                target = point;
            }
        }

        long total = System.nanoTime() - start;
        System.out.println(total / 1e9);
    }

    private double evaluatePoint(Point point) {
        double value = 0.3;

        for (Bonus bonus : world.getBonuses()) {
            if (Utils.checkIntersection(point, bonus)) {
                if (bonus.getType() == BonusType.MEDIKIT) {
                    value = 1.0;
                }
                if (bonus.getType() == BonusType.AMMO_CRATE) {
                    value = 0.8;
                }
                if (bonus.getType() == BonusType.REPAIR_KIT) {
                    value = 0.6;
                }
            }
        }

        value -= (self.getDistanceTo(point) / Math.hypot(world.getWidth(), world.getHeight()) + Math.abs(self.getAngleTo(point)) / Math.PI) / 2.0 * 0.3;

        double sum = 0.0;
        for (Tank tank : world.getEnemies()) {
            sum += tank.getDistanceTo(point) / Math.hypot(world.getWidth(), world.getHeight()) / 5.0;
        }
        value -= Math.min(0.5, sum);

        return Math.min(1.0, Math.max(0.0, value));
    }

    public void draw(Graphics graphics) {
        for (Point point : points) {
            double value = point.getPriority();
            int x = (int) point.getX();
            int y = (int) point.getY();

            Color color = Color.getHSBColor(0.33f, (float) value, 1.0f);
            graphics.setColor(color);

            int r = 3;

            graphics.fillOval(x - r, y - r, 2 * r, 2 * r);

            graphics.setColor(Color.BLACK);
        }

        graphics.setColor(Color.RED);
        int x = (int) target.getX();
        int y = (int) target.getY();
        graphics.drawLine(x - 100, y, x + 100, y);
        graphics.drawLine(x, y - 100, x, y + 100);
    }

    public Point getTarget() {
        return target;
    }
}