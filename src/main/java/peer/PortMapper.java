package peer;

import java.util.HashMap;
import java.util.Map;

public class PortMapper {

    private Map<String, Integer> portMap;
    public PortMapper(){
        // Create a static port mapping using a HashMap
        this.portMap = new HashMap<>();

        // Add service name-port mappings
        portMap.put("5000", 5001);
        portMap.put("5001", 5000);
    }
    
    public Integer getNext(String peerPort){
        return portMap.get(peerPort);
    }
}