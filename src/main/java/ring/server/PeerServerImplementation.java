package ring.server;
import com.proto.peer.*;
import com.proto.peer.PeerServiceGrpc;
import io.grpc.stub.StreamObserver;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

import com.google.protobuf.Empty;
import ring.SocketIdentifier;
import ring.utils.Requester;


public class PeerServerImplementation extends PeerServiceGrpc.PeerServiceImplBase {
    Boolean token = false;
    Queue<Runnable> CommandQueue = new LinkedList<>();

    String serverName = "server_name";
    SocketIdentifier nextPeer;
    SocketIdentifier currentPeerServer;
    SocketIdentifier centralServer;
    Logger logger;

    public PeerServerImplementation(SocketIdentifier currentPeerServer, SocketIdentifier nextPeer,SocketIdentifier centralServer, Logger logger) {
        this.nextPeer = nextPeer;
        this.logger = logger;
        this.currentPeerServer = currentPeerServer;
        this.centralServer = centralServer;
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
        Runnable helloTask = new Runnable() {
            @Override
            public void run() {
                HelloResponse response = Requester.processHelloRequest(centralServer, peerName);
                logger.info("- PEER SERVER (RESPONSE) : " + response.getGreeting());
            }
        };
        logger.info("- PEER SERVER <" + currentPeerServer.getPort() + ">:  Received hello job request");

        // Add the task to the command queue.
        CommandQueue.offer(helloTask);

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    public void sendAlgebra(AlgebraRequest request, StreamObserver<Empty> responseObserver){
        Runnable algebraTask = new Runnable() {
            @Override
            public void run() {
                AlgebraResponse response = Requester.processHelloRequest(
                        centralServer,
                        request.getOperation(),
                        request.getNumber1(),
                        request.getNumber2()
                );
                logger.info("- PEER SERVER (RESPONSE) : " + response.getResult());
            }
        };


        logger.info("- PEER SERVER <" + currentPeerServer.getPort() + ">:  Received algebra ("+ request.getOperation()+ ") job request");

        // Add the task to the command queue.
        CommandQueue.offer(algebraTask);

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