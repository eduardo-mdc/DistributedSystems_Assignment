package ds.assign.entropy;

import ds.assign.general_utils.SocketIdentifier;
import ds.assign.entropy.client.PeerClient;
import ds.assign.entropy.server.PeerServer;

import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class AntiEntropyPeer {
    Logger logger;
    SocketIdentifier thisPeer;
    List<SocketIdentifier> neighbours;
    public AntiEntropyPeer(SocketIdentifier thisPeer, List<SocketIdentifier> neighbours){
        this.thisPeer = thisPeer;
        this.neighbours = neighbours;
        logger = Logger.getLogger("logfile_" + thisPeer.getHostname() + ":"  + thisPeer.getPort());  logger = Logger.getLogger("logfile_" + thisPeer);
        try {
            FileHandler handler = new FileHandler("./" + thisPeer + "_peer.log", true);
            logger.addHandler(handler);
            SimpleFormatter formatter = new SimpleFormatter();
            handler.setFormatter(formatter);
        } catch ( Exception e ) {
            e.printStackTrace();
        }
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
