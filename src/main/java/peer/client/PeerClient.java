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

    private void addHelloJob(ManagedChannel channel, Integer peerPort) {
        PeerServiceGrpc.PeerServiceBlockingStub stub = PeerServiceGrpc.newBlockingStub(channel);
        HelloRequest request = HelloRequest.newBuilder()
                .setPeerName(peerPort.toString())
                .build();

        stub.sendHello(request);
        // Since there's no response data, you may want to log that the operation was successful.
        logger.info("- PEER CLIENT: Hello message sent to peer server");
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

                ManagedChannel channel = createChannel(currentPeerServer);

                switch (command) {
                    case 0:
                        addHelloJob(channel, currentPeerServer.getPort());
                        break;
                }



            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}