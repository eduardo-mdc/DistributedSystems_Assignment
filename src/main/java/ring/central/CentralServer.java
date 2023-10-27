package ring.central;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import ring.SocketIdentifier;
import ring.server.CentralServerImplementation;
import ring.server.PeerServerImplementation;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class CentralServer implements Runnable{

    Logger logger;
    String hostname;

    Integer port;

    public CentralServer(SocketIdentifier server) {
        hostname = server.getHost();
        port = server.getPort();

        logger = Logger.getLogger("logfile_" + hostname + ":"  + port);
        try {
            FileHandler handler = new FileHandler("./" + hostname + ":" + port + "_peer.log", true);
            logger.addHandler(handler);
            SimpleFormatter formatter = new SimpleFormatter();
            handler.setFormatter(formatter);
        } catch ( Exception e ) {
            e.printStackTrace();
        }

    }
    @Override
    public void run() {
        try {
            logger.info("- CENTRAL SERVER: endpoint running at port " + port + " ...");
            Server server = ServerBuilder.forPort(port).addService(new CentralServerImplementation(logger)).build();
            server.start();
            System.out.println("Central Server started. Listening on port : " + port);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Received shutdown request.");
                server.shutdown();
                System.out.println("server stopped.");
            }));
            server.awaitTermination();
        } catch (Exception e) {
            e.printStackTrace();
        }    }

}
