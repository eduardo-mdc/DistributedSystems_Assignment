package entropy;

import general_utils.SocketIdentifier;
import entropy.client.PeerClient;
import entropy.server.PeerServer;

import java.util.List;
import java.util.logging.Logger;

public class AntiEntropyPeer {
    Logger logger;
    SocketIdentifier thisPeer;
    List<SocketIdentifier> neighbours;
    public AntiEntropyPeer(SocketIdentifier thisPeer, List<SocketIdentifier> neighbours){
        this.thisPeer = thisPeer;
        this.neighbours = neighbours;
        logger = Logger.getLogger("logfile_" + thisPeer.getHostname() + ":"  + thisPeer.getPort());
    }

    public void start() {
        try {
            logger.info("Starting new peer - Host: " + thisPeer.getHostname() + ", Port: " + thisPeer.getPort());

            Thread serverThread = new Thread(new PeerServer(thisPeer, neighbours, logger));
            Thread clientThread = new Thread(new PeerClient(thisPeer, neighbours, logger));

            serverThread.start();
            clientThread.start();

            logger.info("Peer started successfully");
        } catch (Exception e) {
            logger.severe("Error starting peer: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
