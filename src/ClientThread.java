import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class ClientThread extends Thread {
    private final Socket socket;
    private final PrintWriter outClient;
    private String userName;
    private final BufferedReader inClient;
    private final UserList users;

    public ClientThread(Socket socket, String userName ,UserList users) throws IOException {
        this.users = users;
        this.socket = socket;
        System.out.println("Client connected " + socket.getPort());
        this.userName = userName;
        outClient = new PrintWriter(socket.getOutputStream(), true);
        inClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            String message;
            while (true) {
                message = inClient.readLine();
                if (message.contains("@name")) {
                    users.remove(userName);
                    String name = message.replaceFirst("@name ", "");
                    if (users.isOnline(name)) {
                        outClient.println("@samename");
                    } else {
                        ClientThread clientThread = this;
                        userName = name;
                        users.getList().put(userName, clientThread);
                    }
                    continue;
                }

                if (message.contains("@senduser")) {
                    message = message.replaceFirst("@senduser ", "");
                    int indexOfSpace = message.indexOf(' ');
                    String recipient = message.substring(0, indexOfSpace);
                    message = message.substring(indexOfSpace + 1);
                    sendMessage(userName + " :" + message, recipient);
                    continue;
                }

                if (message.contains("@quit")) {
                    sendMessage("User " + userName + " left the chat.");
                    users.remove(userName);
                    socket.close();
                    return;
                }

                sendMessage("@" + userName + ": " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message) {
        PrintWriter outClient;
        synchronized (users) {
            for (Map.Entry<String, ClientThread> clientThread: users.getList().entrySet()) {
                if (clientThread.getValue() == this) {
                    continue;
                }
                outClient = clientThread.getValue().getWriter();
                outClient.println(message);
            }
        }
    }

    private void sendMessage(String message, String name) {
        PrintWriter outClient;
        synchronized (users) {
            if (users.getList().containsKey(name)) {
                ClientThread clientThread = users.getList().get(name);
                outClient = clientThread.getWriter();
                outClient.println(message);
            }
        }
    }

    public PrintWriter getWriter() {
        return outClient;
    }


    public String getUsername() {
        return userName;
    }
}
