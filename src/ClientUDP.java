import java.io.IOException;
import java.net.*;

public class ClientUDP {

    private DatagramSocket socket;
    private InetAddress inetAddress;
    private byte[] buffer = new byte[16];

    public ClientUDP(DatagramSocket socket, InetAddress inetAddress) {
        this.socket = socket;
        this.inetAddress = inetAddress;
    }

    public String sendThenReceive() {
        String mfs = null;
        try {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, inetAddress, 8000);
            socket.send(packet);
            socket.receive(packet);
            mfs = new String(packet.getData(), 0, packet.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mfs;
    }


}
