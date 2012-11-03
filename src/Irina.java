import model.*;

import java.awt.*;
import java.util.*;

public class Irina {
    private final int n, m;
    private final Point[][] points;
    private ArrayList<Point> allPoints = new ArrayList<Point>();
    private final Commander commander;

    private MyWorld world;

    public Irina(Commander commander) {
        this.commander = commander;
        n = (int) (commander.getWorld().getHeight() / Constants.tileSize);
        m = (int) (commander.getWorld().getWidth() / Constants.tileSize);
        points = new Point[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                points[i][j] = new Point(((double) j + 0.5) * Constants.tileSize, ((double) i + 0.5) * Constants.tileSize);
                allPoints.add(points[i][j]);
            }
        }
    }

    public void update() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                points[i][j].clear();
            }
        }

        for (Shell shell : commander.getWorld().getShells()) {
            for (Point point : allPoints) {

            }
        }
    }

    private Point target = new Point(0.0, 0.0);

    public void draw(Graphics graphics) {
        for (Point point : allPoints) {
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