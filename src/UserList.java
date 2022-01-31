import java.util.HashMap;

public class UserList {
    private final HashMap<String, ClientThread> currentUsers;

    public UserList() {
        currentUsers = new HashMap<>();
    }

    public synchronized boolean isOnline(String name) {
        if (currentUsers.containsKey(name)) {
            return true;
        }
        return false;
    }

    public synchronized void add(String name, ClientThread user) {
        currentUsers.put(name, user);
    }

    public synchronized void remove(String name) {
        currentUsers.remove(name);
    }

    public HashMap<String, ClientThread> getList() {
        return currentUsers;
    }
}
