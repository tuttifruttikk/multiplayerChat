import java.io.IOException;
import java.net.*;

public class ServerUDP {
    private DatagramSocket socket;
    private byte[] buffer = new byte[16];
    private int tcpPort;

    public ServerUDP(DatagramSocket socket, int tcpPort) {
        this.tcpPort = tcpPort;
        this.socket = socket;
    }

    public void receiveThenSend() {
        try {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            InetAddress inetAddress = InetAddress.getByName("255.255.255.255");
            int port = packet.getPort();
            byte[] data;
            data = String.valueOf(tcpPort).getBytes();
            packet = new DatagramPacket(data, data.length, inetAddress, port);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
