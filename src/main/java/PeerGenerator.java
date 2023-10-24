import peer.Peer;

public class PeerGenerator {
    public static void main(String[] args) {

        Peer peer1 = new Peer("127.0.0.1", 5001);
        peer1.start();
        Peer peer2 = new Peer("127.0.0.1", 5000);
        peer2.start();
    }
}
