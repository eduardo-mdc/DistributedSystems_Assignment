package ring.client;

import java.util.Random;
import java.util.logging.Logger;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import general_utils.SocketIdentifier;
import general_utils.PoissonProcess;
import ring.utils.Requester;


public class PeerClient implements Runnable{
    Logger logger;
    Integer command = 0;
    //defines on average, how many events happen per minute.
    static final int lambda = 5;

    SocketIdentifier currentPeerServer;
    SocketIdentifier nextPeerServer;
    SocketIdentifier centralServer;

    public PeerClient(SocketIdentifier currentPeerServer, SocketIdentifier nextPeerServer, SocketIdentifier centralServer, Logger logger) {
        this.currentPeerServer = currentPeerServer;
        this.nextPeerServer = nextPeerServer;
        this.centralServer = centralServer;
        this.logger = logger;
    }

    private void createAlgebraRequest(String operation, Double number1, Double number2){
        Requester.algebraJobRequest(currentPeerServer,operation,number1,number2);
        logger.info("- PEER CLIENT: Algebra (" + operation + ") job request sent to peer server");
    }

    @Override
    public void run() {
        logger.info("client: endpoint running ...\n");
        PoissonProcess poisson = new PoissonProcess(lambda, new Random());
        while (true){
            try {
                double time = poisson.timeForNextEvent() * 60.0 * 1000.0;
                System.out.println("next event in -> " + (int)time + " ms");
                Thread.sleep((int)time);

                Random rand = new Random();
                Double number1 = rand.nextDouble(1000);
                Double number2 = rand.nextDouble(1000);
                command = rand.nextInt(4);


                switch (command) {
                    case 3:
                        createAlgebraRequest("sub",number1,number2);
                        break;
                    case 2:
                        createAlgebraRequest("mul",number1,number2);
                        break;
                    case 1:
                        createAlgebraRequest("div",number1,number2);
                        break;
                    case 0:
                        createAlgebraRequest("add",number1,number2);
                        break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}