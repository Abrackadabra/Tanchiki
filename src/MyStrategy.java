import model.*;

public final class MyStrategy implements Strategy {
    private Commander commander = new Commander();

    public Commander getCommander() {
        return commander;
    }

    @Override
    public void move(Tank self, model.World world, Move move) {
        commander.act(self, new MyWorld(world), move);
    }

    @Override
    public TankType selectTank(int tankIndex, int teamSize) {
        return TankType.MEDIUM;
    }
}
