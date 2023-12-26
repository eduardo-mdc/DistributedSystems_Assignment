package ds.assign;

import ds.assign.entropy.AntiEntropyPeer;
import ds.assign.entropy.AntiPeerMapper;
import ds.assign.general_utils.SocketIdentifier;

import java.util.List;
import java.util.Map;

public class AntiEntropyGenerator {
    public static void main(String[] args) {
        Map<Integer, List<SocketIdentifier>> peerMap = AntiPeerMapper.readPeerMapFromJson();

        for (Map.Entry<Integer, List<SocketIdentifier>> entry : peerMap.entrySet()) {
            SocketIdentifier currentPeer = new SocketIdentifier("localhost",entry.getKey());
            List<SocketIdentifier> neighborPeers = entry.getValue();

            new AntiEntropyPeer(currentPeer, neighborPeers).start();
        }
    }

}
