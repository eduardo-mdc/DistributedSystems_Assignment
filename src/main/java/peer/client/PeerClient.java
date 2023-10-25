package peer.client;

import java.util.Random;
import java.util.logging.Logger;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import peer.SocketIdentifier;
import peer.utils.PoissonProcess;
import peer.utils.Requester;


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


    private ManagedChannel createChannel(SocketIdentifier server) {
        return ManagedChannelBuilder.forAddress(server.getHost(), server.getPort())
                .usePlaintext()
                .build();
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

                switch (command) {
                    case 0:
                        Requester.helloJobRequest(currentPeerServer, currentPeerServer.getPort());
                        logger.info("- PEER CLIENT: Hello message sent to peer server");
                        break;
                }



            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}