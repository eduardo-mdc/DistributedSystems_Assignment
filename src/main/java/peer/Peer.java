package peer;

import peer.PortMapper;
import peer.server.PeerClient;
import peer.server.PeerServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

public class Peer {

    Integer nextPeerPort;
    String host;
    Logger logger;
    Integer port;



    public Peer(String hostname, Integer port) {
        host   = hostname;
        this.port = port;

        PortMapper portMapper = new PortMapper();
        nextPeerPort = portMapper.getNext(port.toString());

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
            System.out.printf("new peer @ host=%s:%s\n", host, port);
            new Thread(new PeerServer(host, port, logger)).start();
            new Thread(new PeerClient(host, nextPeerPort,logger)).start();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }
}

