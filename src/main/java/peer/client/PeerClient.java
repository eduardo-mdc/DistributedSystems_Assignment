package peer.client;

import java.util.Random;
import java.util.logging.Logger;
import com.proto.peer.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import peer.SocketIdentifier;
import peer.customUtils.PoissonProcess;


public class PeerClient implements Runnable{
    Logger logger;
    String command = "set_token true";
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

    private boolean getToken(ManagedChannel channel) {
        PeerServiceGrpc.PeerServiceBlockingStub stub = PeerServiceGrpc.newBlockingStub(channel);
        GetTokenResponse response = stub.getToken(EmptyRequest.newBuilder()
              .build());
        logger.info("GOT TOKEN : " + response.getResult());
        return response.getResult();
    }

    private void set_token(ManagedChannel channel, Boolean tokenValue) {
        PeerServiceGrpc.PeerServiceBlockingStub stub = PeerServiceGrpc.newBlockingStub(channel);
        SetTokenResponse response = stub.setToken(SetTokenRequest.newBuilder()
              .setToken(tokenValue)
              .build());
        logger.info("SET TOKEN : " + response.getResult());
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

                ManagedChannel channel = ManagedChannelBuilder.forAddress(nextPeerServer.getHost(), nextPeerServer.getPort())
                        .usePlaintext()
                        .build();


                switch (command) {
                    case "set_token false":
                        set_token(channel, false);
                        break;
                    case "set_token true":
                        set_token(channel, true);
                    case "get_token":
                        getToken(channel);
                        break;
                    default:
                        System.out.println("invalid keyword");
                        break;
                }

                } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}