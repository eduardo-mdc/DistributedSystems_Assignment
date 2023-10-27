package ring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PortMapper {

    private Map<String, Integer> portMap;
    public PortMapper(){
        // Create a static port mapping using a HashMap
        this.portMap = new HashMap<>();

        // Add service name-port mappings
        portMap.put("5000", 5001);
        portMap.put("5001", 5002);
        portMap.put("5002", 5003);
        portMap.put("5004", 5005);
        portMap.put("5006", 5007);
        portMap.put("5007", 5008);
        portMap.put("5008", 5000);

    }

    public List<SocketIdentifier> generateSocketList(String hostname){
        List<SocketIdentifier> socketList = new ArrayList<>();
        for(Map.Entry<String, Integer> entry : portMap.entrySet()){
            socketList.add(new SocketIdentifier(hostname, Integer.parseInt(entry.getKey())));
        }
        return socketList;
    }

    public Integer getNext(String peerPort){
        return portMap.get(peerPort);
    }
}