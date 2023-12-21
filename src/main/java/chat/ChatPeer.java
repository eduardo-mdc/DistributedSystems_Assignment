package chat;

import chat.client.PeerClient;
import general_utils.SocketIdentifier;

import java.util.List;
import java.util.logging.Logger;

public class ChatPeer {
    Logger logger;
    SocketIdentifier thisPeer;
    List<SocketIdentifier> neighbours;
    public ChatPeer(SocketIdentifier thisPeer, List<SocketIdentifier> neighbours){
        this.thisPeer = thisPeer;
        this.neighbours = neighbours;
        logger = Logger.getLogger("logfile_" + thisPeer.getHostname() + ":"  + thisPeer.getPort());
    }

    public void start() {
        try {
            logger.info("Starting new peer - Host: " + thisPeer.getHostname() + ", Port: " + thisPeer.getPort());
            Thread clientThread = new Thread(new PeerClient(thisPeer, neighbours, logger));
            clientThread.start();


            /*
                        Thread serverThread = new Thread(new PeerServer(thisPeer, neighbours, logger));
                        serverThread.start();
            */
            logger.info("Peer started successfully");
        } catch (Exception e) {
            logger.severe("Error starting peer: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
