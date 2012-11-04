import model.*;

import java.util.*;

public class MyWorld {
    private final int    tick;
    private final double width;
    private final double height;
    private final ArrayList<Player>   players   = new ArrayList<Player>();
    private final ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();
    private final ArrayList<Tank>     tanks     = new ArrayList<Tank>();
    private final ArrayList<Shell>    shells    = new ArrayList<Shell>();
    private final ArrayList<Bonus>    bonuses   = new ArrayList<Bonus>();

    public MyWorld(World world) {
        tick = world.getTick();
        width = world.getWidth();
        height = world.getHeight();
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
            enemies = new ArrayList<Tank>();
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

    private HashMap<String, Integer> scoreboard;

    public int getScoreboardPosition(String player) {
        if (scoreboard == null) {
            scoreboard = new HashMap<String, Integer>();
            ArrayList<Player> players = new ArrayList<Player>();
            for (Player p : getPlayers()) {
                players.add(p);
            }
            Collections.sort(players, new Comparator<Player>() {
                @Override
                public int compare(Player o1, Player o2) {
                    if (o1.getScore() == o2.getScore()) return 0;
                    return o1.getScore() < o2.getScore() ? 1 : -1;
                }
            });
            int place = -1;
            int prevScore = -1;
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i).getScore() != prevScore) {
                    prevScore = players.get(i).getScore();
                    place = i;
                }
                scoreboard.put(players.get(i).getName(), place);
            }
        }
        return scoreboard.get(player);
    }
}
