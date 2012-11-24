import model.*;

import java.util.ArrayList;
import java.util.Collections;

public final class MyWorld {
    private final int tick;
    private final double width;
    private final double height;
    private ArrayList<Player> players = new ArrayList<Player>();
    private ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
    private ArrayList<Tank> tanks = new ArrayList<Tank>();
    private ArrayList<Shell> shells = new ArrayList<Shell>();
    private ArrayList<Bonus> bonuses = new ArrayList<Bonus>();

    public MyWorld(World world) {
        this.tick = world.getTick();
        this.width = world.getWidth();
        this.height = world.getHeight();
        Collections.addAll(players, world.getPlayers());
        Collections.addAll(obstacles, world.getObstacles());
        Collections.addAll(tanks, world.getTanks());
        Collections.addAll(shells, world.getShells());
        Collections.addAll(bonuses, world.getBonuses());
    }

    public int getTick() {
        return tick;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }

    public ArrayList<Tank> getTanks() {
        return tanks;
    }

    public ArrayList<Shell> getShells() {
        return shells;
    }

    public ArrayList<Bonus> getBonuses() {
        return bonuses;
    }

    private ArrayList<Tank> corpses;
    public ArrayList<Tank> getCorpses() {
        if (corpses == null) {
            corpses = new ArrayList<Tank>();
            for (Tank tank : tanks) {
                if (tank.getCrewHealth() == 0 || tank.getHullDurability() == 0) {
                    corpses.add(tank);
                }
            }
        }
        return corpses;
    }

    private ArrayList<Tank> survivors;
    public ArrayList<Tank> getSurvivors() {
        if (survivors == null) {
            survivors = new ArrayList<Tank>();
            for (Tank tank : tanks) {
                if (tank.getCrewHealth() != 0 && tank.getHullDurability() != 0) {
                    survivors.add(tank);
                }
            }
        }
        return survivors;
    }

    private ArrayList<Tank> enemies;
    public ArrayList<Tank> getEnemies() {
        if (enemies == null) {
            for (Tank tank : getSurvivors()) {
                if (!tank.isTeammate()) {
                    enemies.add(tank);
                }
            }
        }
        return enemies;
    }

    private ArrayList<Unit> shellStoppers;
    public ArrayList<Unit> getShellStoppers() {
        if (shellStoppers == null) {
            shellStoppers = new ArrayList<Unit>();
            shellStoppers.addAll(getSurvivors());
            shellStoppers.addAll(getBonuses());
            shellStoppers.addAll(getObstacles());
        }
        return shellStoppers;
    }
}
