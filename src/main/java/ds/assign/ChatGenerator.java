package ds.assign;

import ds.assign.chat.ChatPeer;
import ds.assign.chat.ChatPeerMapper;
import ds.assign.general_utils.SocketIdentifier;

import java.util.ArrayList;
import java.util.List;

public class ChatGenerator {
    public static void main(String[] args) {
        ChatPeerMapper chatPeerMapper = new ChatPeerMapper();
        List<SocketIdentifier> socketList = chatPeerMapper.getSocketList();

        for (SocketIdentifier socketId : socketList) {
            List<SocketIdentifier> neighbors = new ArrayList<>();
            for (SocketIdentifier subSocketId : socketList) {
                if (!subSocketId.equals(socketId)) {
                    neighbors.add(subSocketId);
                }
            }
            ChatPeer chatPeer = new ChatPeer(socketId, neighbors);
            chatPeer.start();
        }
    }
}
