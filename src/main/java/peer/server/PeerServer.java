package peer.server;


import java.net.Socket;
import java.util.logging.Logger;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import peer.Peer;
import peer.SocketIdentifier;

import java.io.IOException;

public class PeerServer implements Runnable{
    String host;
    Logger logger;
    SocketIdentifier nextPeer;
    SocketIdentifier currentPeerServer;
    SocketIdentifier centralServer;


    public PeerServer(SocketIdentifier currentPeerServer, SocketIdentifier nextPeer, SocketIdentifier centralServer, Logger logger) throws Exception {
        this.nextPeer = nextPeer;
        this.logger = logger;
        this.currentPeerServer = currentPeerServer;
        this.centralServer = centralServer;
    }

    @Override
    public void run() {
        try {
            logger.info("server: endpoint running at port " + currentPeerServer.getPort() + " ...");
            Server server = ServerBuilder.forPort(currentPeerServer.getPort()).addService(new PeerServerImplementation(currentPeerServer,nextPeer,logger)).build();
            server.start();
            System.out.println("server started. Listening on port : " + currentPeerServer.getPort());
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Received shutdown request.");
                server.shutdown();
                System.out.println("server stopped.");
            }));
            server.awaitTermination();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}