import model.Move;
import model.PlayerContext;
import model.Tank;
import model.TankType;

import java.io.IOException;

public final class Runner {
    private final RemoteProcessClient remoteProcessClient;
    private final String              token;

    public static void main(String[] args) throws IOException {
        if (args.length == 3) {
            new Runner(args).run();
        } else {
            new Runner(new String[] {"localhost", "31000", "0000000000000000"}).run();
        }
    }

    private Runner(String[] args) throws IOException {
        remoteProcessClient = new RemoteProcessClient(args[0], Integer.parseInt(args[1]));
        token = args[2];
    }

    public void run() throws IOException {
        try {
            remoteProcessClient.writeToken(token);
            int teamSize = remoteProcessClient.readTeamSize();

            MyStrategy[] strategies = new MyStrategy[teamSize];
            Visualizer[] visualizers = new Visualizer[teamSize];
            TankType[] tankTypes = new TankType[teamSize];

            for (int strategyIndex = 0; strategyIndex < teamSize; ++strategyIndex) {
                MyStrategy strategy = new MyStrategy();
                strategies[strategyIndex] = strategy;
                visualizers[strategyIndex] = new Visualizer();
                Utils.invokeApplet(visualizers[strategyIndex]);
                tankTypes[strategyIndex] = strategy.selectTank(strategyIndex, teamSize);
            }

            remoteProcessClient.writeSelectedTanks(tankTypes);

            PlayerContext playerContext;

            while ((playerContext = remoteProcessClient.readPlayerContext()) != null) {
                Tank[] playerTanks = playerContext.getTanks();
                if (playerTanks.length != teamSize) {
                    break;
                }

                Move[] moves = new Move[teamSize];

                for (int strategyIndex = 0; strategyIndex < teamSize; strategyIndex++) {
                    Move move = new Move();
                    moves[strategyIndex] = move;
                    strategies[strategyIndex].move(playerTanks[strategyIndex], playerContext.getWorld(), move);
                    visualizers[strategyIndex].redraw(strategies[strategyIndex].getCommander());
                }

                remoteProcessClient.writeMoves(moves);
            }
        } finally {
            remoteProcessClient.close();
        }
    }
}
