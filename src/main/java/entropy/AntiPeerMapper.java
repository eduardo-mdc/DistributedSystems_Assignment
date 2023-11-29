package entropy;

import general_utils.SocketIdentifier;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AntiPeerMapper {
    private static final String JSON_FILE_PATH = "config/entropyMapping.json";

    public static Map<Integer, List<SocketIdentifier>> readPeerMapFromJson() {
        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(JSON_FILE_PATH)));
            JSONObject jsonMap = new JSONObject(jsonContent);
            return parseJsonToPeerMap(jsonMap);
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    public static void writePeerMapToJson(Map<Integer, List<SocketIdentifier>> peerMap) {
        JSONObject jsonMap = createJsonFromPeerMap(peerMap);
        try {
            Files.write(Paths.get(JSON_FILE_PATH), jsonMap.toString(4).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<Integer, List<SocketIdentifier>> parseJsonToPeerMap(JSONObject jsonMap) {
        Map<Integer, List<SocketIdentifier>> peerMap = new HashMap<>();
        for (String key : jsonMap.keySet()) {
            int port = Integer.parseInt(key);
            JSONArray socketArray = jsonMap.getJSONArray(key);
            List<SocketIdentifier> socketList = new ArrayList<>();
            for (int i = 0; i < socketArray.length(); i++) {
                JSONObject socketJson = socketArray.getJSONObject(i);
                String host = socketJson.getString("host");
                int portValue = socketJson.getInt("port");
                socketList.add(new SocketIdentifier(host, portValue));
            }
            peerMap.put(port, socketList);
        }
        return peerMap;
    }

    private static JSONObject createJsonFromPeerMap(Map<Integer, List<SocketIdentifier>> peerMap) {
        JSONObject jsonMap = new JSONObject();
        for (Map.Entry<Integer, List<SocketIdentifier>> entry : peerMap.entrySet()) {
            int port = entry.getKey();
            List<SocketIdentifier> socketList = entry.getValue();
            JSONArray socketArray = new JSONArray();
            for (SocketIdentifier socketIdentifier : socketList) {
                JSONObject socketJson = new JSONObject();
                socketJson.put("host", socketIdentifier.getHostname());
                socketJson.put("port", socketIdentifier.getPort());
                socketArray.put(socketJson);
            }
            jsonMap.put(Integer.toString(port), socketArray);
        }
        return jsonMap;
    }

    public static void main(String[] args) {
        // Create or read the peerMap from/to JSON
        Map<Integer, List<SocketIdentifier>> peerMap = map();
        writePeerMapToJson(peerMap);

        // Read the peerMap from JSON
        Map<Integer, List<SocketIdentifier>> readPeerMap = readPeerMapFromJson();

        // Use the readPeerMap as needed
        System.out.println("Read Peer Map: " + readPeerMap);
    }

    public static Map<Integer, List<SocketIdentifier>> map() {
        // Your existing mapping logic
        Map<Integer, List<SocketIdentifier>> peerMap = new HashMap<>();
        peerMap.put(5001, generateSocketList(new Integer[]{5002}));
        peerMap.put(5002, generateSocketList(new Integer[]{5003, 5004}));
        peerMap.put(5003, generateSocketList(new Integer[]{5002}));
        peerMap.put(5004, generateSocketList(new Integer[]{5002, 5005, 5006}));
        peerMap.put(5005, generateSocketList(new Integer[]{5004}));
        peerMap.put(5006, generateSocketList(new Integer[]{5004}));

        return peerMap;
    }

    private static List<SocketIdentifier> generateSocketList(Integer[] numbers) {
        List<SocketIdentifier> socketList = new ArrayList<>();
        for (Integer number : numbers)
            socketList.add(new SocketIdentifier("localhost", number));
        return socketList;
    }
}
