package entropy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import general_utils.SocketIdentifier;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AntiPeerMapper {
    private static final String JSON_FILE_PATH = "config/entropyMapping.json";

    static {
        // Register the SocketIdentifierDeserializer with the ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(SocketIdentifier.class, new SocketIdentifierDeserializer());
        objectMapper.registerModule(module);
    }

    public static Map<Integer, List<SocketIdentifier>> map() {
        Map<Integer, List<SocketIdentifier>> peerMap = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            File jsonFile = new File(JSON_FILE_PATH);
            if (!jsonFile.exists()) {
                // Create the file with the default mapping
                mapper.writeValue(jsonFile, peerMap);
            } else {
                // Load the data from the existing file
                peerMap = mapper.readValue(jsonFile, new TypeReference<HashMap<Integer, List<SocketIdentifier>>>() {});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return peerMap;
    }

    private static List<SocketIdentifier> generateSocketList(Integer[] numbers) {
        List<SocketIdentifier> socketList = new ArrayList<>();
        for (Integer number : numbers)
            socketList.add(new SocketIdentifier("localhost", number));
        return socketList;
    }

    // Inner class for deserializing SocketIdentifier
    private static class SocketIdentifierDeserializer extends com.fasterxml.jackson.databind.JsonDeserializer<SocketIdentifier> {
        @Override
        public SocketIdentifier deserialize(com.fasterxml.jackson.core.JsonParser jsonParser, com.fasterxml.jackson.databind.DeserializationContext deserializationContext) throws IOException, com.fasterxml.jackson.core.JsonProcessingException {
            com.fasterxml.jackson.databind.JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
            String hostname = jsonNode.get("hostname").asText();
            int port = jsonNode.get("port").asInt();
            return new SocketIdentifier(hostname, port);
        }
    }
}
