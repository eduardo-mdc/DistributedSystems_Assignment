package peer.server;
import com.proto.peer.*;
import com.proto.peer.PeerServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

import com.google.protobuf.Empty;
import peer.SocketIdentifier;
import peer.utils.Requester;


public class PeerServerImplementation extends PeerServiceGrpc.PeerServiceImplBase {
    Boolean token = false;
    Queue<Runnable> CommandQueue = new LinkedList<>();

    String serverName = "server_name";
    SocketIdentifier nextPeer;
    SocketIdentifier currentPeerServer;
    Logger logger;

    public PeerServerImplementation(SocketIdentifier currentPeerServer, SocketIdentifier nextPeer, Logger logger) {
        this.nextPeer = nextPeer;
        this.logger = logger;
        this.currentPeerServer = currentPeerServer;
    }

    @Override
    public void setToken(SetTokenRequest request, StreamObserver < Empty > responseObserver) {
        // Change this to obtain the request value.
        final boolean newToken = request.getToken();

        token = newToken;
        //logger.info("- PEER SERVER <" + currentPeerServer.getPort() + ">:  Got token with value <" + newToken + ">");

        if (token){
            processCommandQueue();
            Requester.setTokenRequest(nextPeer, true);
        }

        token = false;

        // Respond to the gRPC request.
        responseObserver.onNext(Empty.getDefaultInstance());
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
        logger.info("- PEER SERVER <" + currentPeerServer.getPort() + ">:  Received hello job request");

        // Add the task to the command queue.
        CommandQueue.offer(printPeerNameTask);

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    // You can have a method to process the command queue in your class.
    public void processCommandQueue() {
        if (!CommandQueue.isEmpty()) {
            logger.info("- PEER SERVER <" + currentPeerServer.getPort() + ">: Started Processing command queue.");
            while (!CommandQueue.isEmpty()) {
                Runnable task = CommandQueue.poll();
                task.run();
            }
            logger.info("- PEER SERVER <" + currentPeerServer.getPort() + ">:  Finished Processing command queue.");
        }
    }



}