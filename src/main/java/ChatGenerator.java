import chat.ChatPeer;
import chat.ChatPeerMapper;
import general_utils.SocketIdentifier;
import ring.RingPeer;
import ring.RingPeerMapper;
import ring.central.CentralServer;
import ring.utils.Requester;

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
