import model.*;

import java.awt.geom.*;

public class Point extends Unit {
    private double priority = Constants.defaultPriority;

    public void clear() {
        priority = Constants.defaultPriority;
    }

    public Point(double x, double y) {
        super(-1L, 0.0, 0.0, x, y, 0.0, 0.0, 0.0, 0.0);
    }

    public double getPriority() {
        return priority;
    }

    public void setPriority(double priority) {
        this.priority = Math.max(0.0, Math.min(1.0, priority));
    }

    public Point2D.Double getPoint2D() {
        return new Point2D.Double(getX(), getY());
    }
}
