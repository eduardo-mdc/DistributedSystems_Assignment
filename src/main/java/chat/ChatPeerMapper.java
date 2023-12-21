package chat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import general_utils.SocketIdentifier;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatPeerMapper {
    private static final String JSON_FILE_PATH = "config/chatMapping.json";

    private List<SocketIdentifier> socketList;

    public ChatPeerMapper() {
        this.socketList = new ArrayList<>();
        loadPortMappingFromJson();
    }

    public List<SocketIdentifier> getSocketList() {
        return socketList;
    }

    private void loadPortMappingFromJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            File jsonFile = new File(JSON_FILE_PATH);
            if (jsonFile.exists()) {
                socketList = mapper.readValue(jsonFile, new TypeReference<List<SocketIdentifier>>() {});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
