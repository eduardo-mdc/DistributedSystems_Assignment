package chat.server;

import chat.server.PeerServerImplementation;
import general_utils.SocketIdentifier;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.util.List;
import java.util.logging.Logger;

public class PeerServer implements Runnable{
    SocketIdentifier thisPeer;
    List<SocketIdentifier> neighbours;
    Logger logger;
    public PeerServer(SocketIdentifier thisPeer, List<SocketIdentifier> neighbours, Logger logger){
        this.thisPeer = thisPeer;
        this.neighbours = neighbours;
        this.logger = logger;
    }

    @Override
    public void run() {
        try {
            Server server = ServerBuilder.forPort(thisPeer.getPort()).addService(new PeerServerImplementation(thisPeer,neighbours,logger)).build();
            server.start();
            System.out.println("<PEER SERVER>: server started. Listening on port : " + thisPeer.getPort());;

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("<PEER SERVER>: Received shutdown request.");
                server.shutdown();
                System.out.println("<PEER SERVER>: Server stopped.");
            }));
            server.awaitTermination();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
