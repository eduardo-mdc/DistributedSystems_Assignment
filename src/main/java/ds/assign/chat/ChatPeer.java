package ds.assign.chat;

import ds.assign.chat.client.PeerClient;
import ds.assign.chat.server.PeerServer;
import ds.assign.general_utils.SocketIdentifier;

import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ChatPeer {
    Logger logger;
    SocketIdentifier thisPeer;
    List<SocketIdentifier> neighbours;
    public ChatPeer(SocketIdentifier thisPeer, List<SocketIdentifier> neighbours){
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
            Thread clientThread = new Thread(new PeerClient(thisPeer, neighbours, logger));
            clientThread.start();
            Thread serverThread = new Thread(new PeerServer(thisPeer, neighbours, logger));
            serverThread.start();
            logger.info("Peer started successfully");
        } catch (Exception e) {
            logger.severe("Error starting peer: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
