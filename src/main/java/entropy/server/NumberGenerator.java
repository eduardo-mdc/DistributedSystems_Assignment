package entropy.server;

import general_utils.PoissonProcess;

import java.util.Random;

public class NumberGenerator implements Runnable {
    PeerServerImplementation peerServer;

    public NumberGenerator(PeerServerImplementation peerServer){
        this.peerServer = peerServer;
    }

    @Override
    public void run() {
        double lambda = 5;
        PoissonProcess poisson = new PoissonProcess(lambda, new Random());
        Random rand = new Random();
        peerServer.server_values.add(Math.floor(rand.nextDouble(1.0)*100) / 100.0);
        while (true){
            try {
                double time = poisson.timeForNextEvent() * 60.0 * 1000.0;
                peerServer.logger.info("<PEER SERVER>: Adding new Value to List In -> " + (int)time);

                Thread.sleep((int)time);

                double value = rand.nextDouble(1.0);
                value = Math.floor(value * 100) / 100; // Truncate to two decimal places

                peerServer.logger.info("<PEER SERVER>: Added new Value to List -> " + value);
                peerServer.server_values.add(value);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
