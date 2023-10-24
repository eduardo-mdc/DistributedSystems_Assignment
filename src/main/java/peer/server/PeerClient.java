package peer.server;

import java.util.logging.Logger;
import com.proto.peer.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;


public class PeerClient implements Runnable{
    String host;
    Integer nextPeerPort;
    Logger logger;

    String command = "token";

    public PeerClient(String host, Integer nextPeerPort, Logger logger) {
        this.host = host;
        this.nextPeerPort = nextPeerPort;
        this.logger = logger;
    }

    private void sendToken(ManagedChannel channel) {
        PeerServiceGrpc.PeerServiceBlockingStub stub = PeerServiceGrpc.newBlockingStub(channel);
        TokenResponse response = stub.getToken(TokenRequest.newBuilder()
                .setInput(false).build());
        logger.info("GOT TOKEN : " + response.getResult());
    }
    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(1000);
                ManagedChannel channel = ManagedChannelBuilder.forAddress(this.host, this.nextPeerPort)
                        .usePlaintext()
                        .build();
                switch (command) {
                    case "send_token":
                        sendToken(channel);
                        break;
                    default:
                        System.out.println("invalid keword");
                        break;
                }            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}