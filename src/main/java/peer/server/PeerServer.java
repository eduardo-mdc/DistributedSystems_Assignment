package peer.server;


import java.util.logging.Logger;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import peer.Peer;
import peer.SocketIdentifier;

import java.io.IOException;

public class PeerServer implements Runnable{
    String host;
    int port;
    Logger logger;


    public PeerServer(SocketIdentifier server, Logger logger) throws Exception {
        this.host = server.getHost();
        this.port = server.getPort();
        this.logger = logger;

    }

    @Override
    public void run() {
        try {
            logger.info("server: endpoint running at port " + port + " ...");
            Server server = ServerBuilder.forPort(port).addService(new PeerServerImplementation()).build();
            server.start();
            System.out.println("server started. Listening on port : " + port);
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