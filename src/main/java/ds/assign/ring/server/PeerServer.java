package ds.assign.ring.server;


import java.util.logging.Logger;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import ds.assign.general_utils.SocketIdentifier;

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
            Server server = ServerBuilder.forPort(currentPeerServer.getPort()).addService(new PeerServerImplementation(currentPeerServer,nextPeer,centralServer,logger)).build();
            server.start();
            logger.info("Server started. Listening on port : " + currentPeerServer.getPort() + ", connected to " + nextPeer);
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