package ring;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import general_utils.SocketIdentifier;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RingPeerMapper {

    private static final String JSON_FILE_PATH = "config/ringMapping.json";

    private Map<String, Integer> portMap;

    public RingPeerMapper() {
        this.portMap = new HashMap<>();
        loadPortMappingFromJson();
    }

    public List<SocketIdentifier> generateSocketList(String hostname) {
        List<SocketIdentifier> socketList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : portMap.entrySet()) {
            socketList.add(new SocketIdentifier(hostname, Integer.parseInt(entry.getKey())));
        }
        return socketList;
    }

    public Integer getNext(Integer peerPort) {
        return portMap.get(peerPort);
    }

    private void loadPortMappingFromJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            File jsonFile = new File(JSON_FILE_PATH);
            if (jsonFile.exists()) {
                portMap = mapper.readValue(jsonFile, new TypeReference<HashMap<String, Integer>>() {});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
