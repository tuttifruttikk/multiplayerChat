import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Server {
    private static final UserList users = new UserList();


    public static void main(String[] args) {
        System.out.println("Enter the number of port for listening: ");
        Scanner scanner = new Scanner(System.in);
        int portNumber = scanner.nextInt();
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(8000);

        }   catch (SocketException e) {
            e.printStackTrace();
        }
        ServerUDP serverUDP = new ServerUDP(socket, portNumber);


        String userName;
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            while (true) {
                System.out.println("Wait connecting new client... ");
                serverUDP.receiveThenSend();
                Socket clientSocket = serverSocket.accept();
                userName = "User" + clientSocket.getPort();
                ClientThread clientThread = new ClientThread(clientSocket, userName, users);
                clientThread.start();
                users.add(userName, clientThread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        socket.close();
    }
}
