package peer.server;
import com.proto.peer.*;
import com.proto.peer.PeerServiceGrpc;
import io.grpc.stub.StreamObserver;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

import com.google.protobuf.Empty;
import peer.SocketIdentifier;


public class PeerServerImplementation extends PeerServiceGrpc.PeerServiceImplBase {
    Boolean token = false;
    Queue<Runnable> CommandQueue = new LinkedList<>();

    String serverName = "server_name";
    SocketIdentifier nextPeer;
    Logger logger;

    public PeerServerImplementation(SocketIdentifier nextPeer, Logger logger) {
        this.nextPeer = nextPeer;
        this.logger = logger;
    }

    @Override
    public void getToken(EmptyRequest request, StreamObserver < GetTokenResponse > responseObserver) {

        responseObserver.onNext(GetTokenResponse.newBuilder().setResult(
                token
        ).build());
        responseObserver.onCompleted();
    }

    @Override
    public void setToken(SetTokenRequest request, StreamObserver < SetTokenResponse > responseObserver) {
        // Change this to obtain the request value.
        final boolean newToken = request.getToken();

        token = newToken;
        logger.info("- PEER SERVER: Got token with value <" + newToken + ">");

        processCommandQueue();

        // Respond to the gRPC request.
        responseObserver.onNext(SetTokenResponse.newBuilder().setResult(newToken).build());
        responseObserver.onCompleted();
    }

    @Override
    public void sendHello(HelloRequest request, StreamObserver <Empty> responseObserver) {
        // Change this to obtain the request value.
        final String peerName = request.getPeerName();

        // Create a Runnable task to print the peer name.
        Runnable printPeerNameTask = new Runnable() {
            @Override
            public void run() {
                logger.info("- PEER SERVER (RESPONSE): Hello " + peerName + ",I am " + serverName);
            }
        };
        //logger.info("- PEER SERVER: Received hello job request");

        // Add the task to the command queue.
        CommandQueue.offer(printPeerNameTask);

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    // You can have a method to process the command queue in your class.
    public void processCommandQueue() {
        if (CommandQueue.isEmpty()) {
            logger.info("- PEER SERVER: Command queue is empty.");
            return;
        }
        logger.info("- PEER SERVER: Processing command queue.");
        while (!CommandQueue.isEmpty()) {
            Runnable task = CommandQueue.poll();
            task.run();
        }
    }



}