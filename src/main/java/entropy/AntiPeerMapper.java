package entropy;

import general_utils.SocketIdentifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AntiPeerMapper {
    public static Map<Integer,List<SocketIdentifier>> map(){
        Map<Integer,List<SocketIdentifier>> peerMap = new HashMap<>();

        // Add service name-port mappings, where the last digits of the port number identify the peer.
        peerMap.put(5001,generateSocketList(new Integer[]{5002}));
        peerMap.put(5002,generateSocketList(new Integer[]{5003,5004}));
        peerMap.put(5003,generateSocketList(new Integer[]{5002}));
        peerMap.put(5004,generateSocketList(new Integer[]{5002,5005,5006}));
        peerMap.put(5005,generateSocketList(new Integer[]{5004}));
        peerMap.put(5006,generateSocketList(new Integer[]{5004}));

        return peerMap;
    }

    private static List<SocketIdentifier> generateSocketList(Integer[] numbers) {
        List<SocketIdentifier> socketList = new ArrayList<>();
        for (Integer number : numbers)
            socketList.add(new SocketIdentifier("localhost",number));
        return socketList;
    }
}
