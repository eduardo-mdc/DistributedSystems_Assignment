package ds.assign.ring;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ds.assign.general_utils.SocketIdentifier;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RingPeerMapper {

    private static final String JSON_FILE_PATH = "config/ringMapping.json";

    public Map<Integer, Integer> portMap;

    public RingPeerMapper() {
        this.portMap = new HashMap<>();
        loadPortMappingFromJson();
    }

    public List<SocketIdentifier> generateSocketList(String hostname) {
        List<SocketIdentifier> socketList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : portMap.entrySet()) {
            socketList.add(new SocketIdentifier(hostname, entry.getKey()));
        }
        return socketList;
    }


    private void loadPortMappingFromJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            File jsonFile = new File(JSON_FILE_PATH);
            if (jsonFile.exists()) {
                portMap = mapper.readValue(jsonFile, new TypeReference<HashMap<Integer, Integer>>() {});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
