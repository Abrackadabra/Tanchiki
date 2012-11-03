import model.*;

import java.applet.*;

import java.awt.*;
import java.awt.image.*;

public class Visualizer extends Applet {
    private final double        width         = 1280.0;
    private final double        height        = 800.0;
    private BufferedImage bufferedImage = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB);

    @Override
    public void init() {
        setSize((int) width, (int) height);
    }

    private Commander commander;

    public void redraw(Commander commander) {
        this.commander = commander;
        repaint();
    }

    @Override
    public void paint(Graphics graphics) {
        if (commander != null) {
            bufferedImage.getGraphics().setColor(Color.WHITE);
            bufferedImage.getGraphics().fillRect(0, 0, (int) width, (int) height);
            draw(bufferedImage.getGraphics());
            graphics.drawImage(bufferedImage, 0, 0, null);
        }
    }

    @Override
    public void update(Graphics graphics) {
        paint(graphics);
    }

    void draw(Graphics graphics) {
        graphics.setColor(Color.BLACK);
        for (Tank tank : commander.getWorld().getTanks()) {
            int x1 = (int) Math.round(tank.getX());
            int y1 = (int) Math.round(tank.getY());
            int r = (int) tank.getWidth() / 2;
            graphics.drawOval(x1 - r, y1 - r, r * 2, r * 2);
            double turretAngle = tank.getAngle() + tank.getTurretRelativeAngle();
            int x2 = (int) Math.round(tank.getX() + Math.cos(turretAngle) * tank.getVirtualGunLength());
            int y2 = (int) Math.round(tank.getY() + Math.sin(turretAngle) * tank.getVirtualGunLength());
            graphics.drawLine(x1, y1, x2, y2);
            x2 = (int) Math.round(tank.getX() + Math.cos(tank.getAngle()) * (double) r);
            y2 = (int) Math.round(tank.getY() + Math.sin(tank.getAngle()) * (double) r);
            graphics.drawLine(x1, y1, x2, y2);

            if (tank.getId() == commander.getSelf().getId()) {
                graphics.setColor(Color.GREEN);

                r = 10;

                graphics.fillOval(x1 - r, y1 - r, 2 * r, 2 * r);

                graphics.setColor(Color.BLACK);
            }
        }

        for (Shell shell : commander.getWorld().getShells()) {
            int x1 = (int) Math.round(shell.getX());
            int y1 = (int) Math.round(shell.getY());

            int r = (int) (Math.min(shell.getWidth(), shell.getHeight()) / 2.0);

            graphics.fillOval(x1 - r, y1 - r, r * 2, r * 2);
        }

        for (Bonus bonus : commander.getWorld().getBonuses()) {
            int x1 = (int) Math.round(bonus.getX());
            int y1 = (int) Math.round(bonus.getY());

            if (bonus.getType() == BonusType.MEDIKIT) {
                graphics.setColor(Color.RED);
            }
            if (bonus.getType() == BonusType.REPAIR_KIT) {
                graphics.setColor(Color.BLUE);
            }
            if (bonus.getType() == BonusType.AMMO_CRATE) {
                graphics.setColor(Color.YELLOW);
            }

            int r = (int) (Math.min(bonus.getWidth(), bonus.getHeight()) / 2.0);

            graphics.fillRect(x1 - r, y1 - r, 2 * r, 2 * r);

            graphics.setColor(Color.BLACK);
        }

        commander.getIrina().draw(graphics);
    }
}
