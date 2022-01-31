import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        int port = 0;
        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress inetAddress = InetAddress.getByName("255.255.255.255");
            ClientUDP clientUDP = new ClientUDP(socket, inetAddress);
            port = Integer.parseInt(clientUDP.sendThenReceive());
            socket.close();
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }

        System.out.println("Port connected " + port);

        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        try {
            Socket socket = new Socket("localhost", port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ReaderThread readerThread = new ReaderThread(in);
            readerThread.start();
            String message;
            System.out.println("Enter the message");
            while (!(message = inFromUser.readLine()).equals("@quit")) {
                out.println(message);
            }
            out.println(message);
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ReaderThread extends Thread {
        private final BufferedReader in;

        public ReaderThread(BufferedReader reader) {
            in = reader;
        }

        @Override
        public void run() {
            String messageFromServer;
            try {
                while ((messageFromServer = in.readLine()) != null) {
                    if (messageFromServer.equals("@samename")) {
                        System.out.println("Error!!! Name is already in use, try again.");
                        continue;
                    }
                    System.out.println(messageFromServer);
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}
