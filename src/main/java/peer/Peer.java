package peer;

import peer.client.PeerClient;
import peer.server.PeerServer;

import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

public class Peer {

    Integer nextPeerPort;
    Logger logger;

    String centralServerHost = "127.0.0.1";
    Integer centralServerPort = 8000;

    SocketIdentifier centralServer;
    SocketIdentifier currentPeerServer;
    SocketIdentifier nextPeerServer;

    public Peer(String hostname, Integer port) {
        centralServer = new SocketIdentifier(centralServerHost, centralServerPort);
        currentPeerServer = new SocketIdentifier(hostname, port);

        PortMapper portMapper = new PortMapper();
        nextPeerServer = new SocketIdentifier("127.0.0.1",portMapper.getNext(currentPeerServer.getPort().toString()));

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
            System.out.printf("new peer @ host=%s:%s\n", currentPeerServer.getHost(), currentPeerServer.getPort());
            new Thread(new PeerServer(currentPeerServer,nextPeerServer,centralServer,logger)).start();
            new Thread(new PeerClient(currentPeerServer,nextPeerServer,centralServer,logger)).start();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}

