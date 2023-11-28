package entropy.client;

import general_utils.PoissonProcess;
import general_utils.SocketIdentifier;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class PeerClient implements Runnable{
    SocketIdentifier thisPeer;
    List<SocketIdentifier> neighbours;
    Logger logger;

    final int lambda = 5;

    public PeerClient(SocketIdentifier thisPeer, List<SocketIdentifier> neighbours, Logger logger){
        this.thisPeer = thisPeer;
        this.neighbours = neighbours;
        this.logger = logger;

    }

    private void sendPush(SocketIdentifier destination, String message){


        logger.info("<PEER CLIENT>: Fetched data from local server");

        logger.info("<PEER CLIENT>: Push request sent to peer server");
    }
    @Override
    public void run() {
        logger.info("<PEER CLIENT>: Client Running at " + thisPeer.getHost() + ":" + thisPeer.getPort());
        PoissonProcess poisson = new PoissonProcess(lambda, new Random());
        while (true){
            try {
                double time = poisson.timeForNextEvent() * 60.0 * 1000.0;
                System.out.println("next event in -> " + (int)time + " ms");
                Thread.sleep((int)time);

                Random rand = new Random();
                int command = rand.nextInt(3);


                switch (command) {
                    case 2:
                        //TODO Push&Pull
                        break;
                    case 1:
                        //TODO Push
                        break;
                    case 0:
                        //TODO Pull
                        break;
                }



            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
