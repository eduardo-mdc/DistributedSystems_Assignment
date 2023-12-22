package ring;

import general_utils.SocketIdentifier;
import ring.client.PeerClient;
import ring.server.PeerServer;

import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

public class RingPeer {

    Integer nextPeerPort;
    Logger logger;

    String centralServerHost = "localhost";
    Integer centralServerPort = 8000;

    SocketIdentifier centralServer;
    SocketIdentifier currentPeerServer;
    SocketIdentifier nextPeerServer;

    public RingPeer(SocketIdentifier thisPeer, SocketIdentifier connectedPeer) {
        String hostname = thisPeer.getHostname();
        Integer port = thisPeer.getPort();
        centralServer = new SocketIdentifier(centralServerHost, centralServerPort);
        currentPeerServer = new SocketIdentifier(hostname, port);


        nextPeerServer = new SocketIdentifier("127.0.0.1",connectedPeer.getPort());

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

    public void start(){
        try {
            System.out.printf("new peer @ host=%s:%s\n", currentPeerServer.getHostname(), currentPeerServer.getPort());
            new Thread(new PeerServer(currentPeerServer,nextPeerServer,centralServer,logger)).start();
            new Thread(new PeerClient(currentPeerServer,nextPeerServer,centralServer,logger)).start();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}

