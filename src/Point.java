import model.*;

public class Point extends Unit {
    private double priority = Constants.defaultPriority;

    public void clear() {
        priority = Constants.defaultPriority;
    }

    public Point(double x, double y) {
        super(-1, 0, 0, x, y, 0, 0, 0, 0);
    }

    public double getPriority() {
        return priority;
    }

    public void setPriority(double priority) {
        this.priority = Math.max(0, Math.min(1, priority));
    }
}
