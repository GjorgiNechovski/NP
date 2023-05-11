import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.TreeSet;

public class ChatSystemTest {

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchRoomException {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) {
            ChatRoom cr = new ChatRoom(jin.next());
            int n = jin.nextInt();
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr.addUser(jin.next());
                if ( k == 1 ) cr.removeUser(jin.next());
                if ( k == 2 ) System.out.println(cr.hasUser(jin.next()));
            }
            System.out.println("");
            System.out.println(cr.toString());
            n = jin.nextInt();
            if ( n == 0 ) return;
            ChatRoom cr2 = new ChatRoom(jin.next());
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr2.addUser(jin.next());
                if ( k == 1 ) cr2.removeUser(jin.next());
                if ( k == 2 ) cr2.hasUser(jin.next());
            }
            System.out.println(cr2.toString());
        }
        if ( k == 1 ) {
            ChatSystem cs = new ChatSystem();
            Method mts[] = cs.getClass().getMethods();
            while ( true ) {
                String cmd = jin.next();
                if ( cmd.equals("stop") ) break;
                if ( cmd.equals("print") ) {
                    System.out.println(cs.getRoom(jin.next())+"\n");continue;
                }
                for ( Method m : mts ) {
                    if ( m.getName().equals(cmd) ) {
                        String params[] = new String[m.getParameterTypes().length];
                        for ( int i = 0 ; i < params.length ; ++i ) params[i] = jin.next();
                        m.invoke(cs,params);
                    }
                }
            }
        }
    }

}

class NoSuchRoomException extends Exception{
    public NoSuchRoomException(String name) {
        super(name);
    }
}

class NoSuchRoomExcеption extends Exception{
    public NoSuchRoomExcеption(String message) {
        super(message);
    }
}

class NoSuchUserException extends Exception{
    public NoSuchUserException(String message) {
        super(message);
    }
}

class ChatRoom implements Comparable<ChatRoom>{
    String name;
    TreeSet<String> users;

    public ChatRoom(String name) {
        this.name = name;
        users = new TreeSet<>();
    }

    void addUser(String username){
        users.add(username);
    }

    void removeUser(String username){
        users.remove(username);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s\n", name));

        if (users.isEmpty())
            sb.append("EMPTY\n");
        else
            users.forEach(user -> sb.append(String.format("%s\n", user)));
        return sb.toString();
    }

    boolean hasUser(String username){
        return users.contains(username);
    }

    int numUsers(){
        return users.size();
    }

    @Override
    public int compareTo(ChatRoom o) {
        return name.compareTo(o.name);
    }

    public String getName() {
        return name;
    }
}

class ChatSystem {
    HashSet<String> users;
    TreeMap<String, ChatRoom> rooms;

    public ChatSystem() {
        this.rooms = new TreeMap<>();
        this.users = new HashSet<>();
    }

    public void addRoom(String roomName) {
        rooms.put(roomName, new ChatRoom(roomName));
    }

    public void removeRoom(String roomName) {
        rooms.remove(roomName);
    }

    public ChatRoom getRoom(String roomName) throws NoSuchRoomException {
        if (rooms.containsKey(roomName))
            return rooms.get(roomName);

        throw new NoSuchRoomException(roomName);
    }

    private ChatRoom findMinUsersRoom() {
        if (rooms.isEmpty())
            return null;

        ChatRoom min = rooms.get(rooms.firstKey());
        for (String roomName : rooms.keySet()) {
            ChatRoom currentRoom = rooms.get(roomName);
            if (currentRoom.numUsers() < min.numUsers())
                min = currentRoom;
        }
        return min;
    }

    public void register(String userName) throws NoSuchRoomException, NoSuchUserException {
        users.add(userName);
        ChatRoom minUsersRoom = findMinUsersRoom();
        if (minUsersRoom == null)
            return;
        joinRoom(userName, minUsersRoom.getName());
    }

    public void registerAndJoin(String userName, String roomName) throws NoSuchRoomException, NoSuchUserException {
        users.add(userName);
        joinRoom(userName, roomName);
    }

    private void checkIfExists(String userName, String roomName) throws NoSuchRoomException, NoSuchUserException {
        if (!rooms.containsKey(roomName))
            throw new NoSuchRoomException(roomName);

        if (!users.contains(userName))
            throw new NoSuchUserException(userName);
    }

    public void joinRoom(String userName, String roomName) throws NoSuchRoomException, NoSuchUserException {
        checkIfExists(userName, roomName);

        rooms.computeIfPresent(roomName, (rName, room) -> {
            room.addUser(userName);
            return room;
        });
    }

    public void leaveRoom(String userName, String roomName) throws NoSuchUserException, NoSuchRoomException {
        checkIfExists(userName, roomName);

        rooms.computeIfPresent(roomName, (rName, room) -> {
            room.removeUser(userName);
            return room;
        });
    }


    public void followFriend(String username, String friendUsername) throws NoSuchUserException {
        if (!users.contains(username))
            throw new NoSuchUserException(username);
        LinkedList<ChatRoom> chatRooms = new LinkedList<>(rooms.values());

        for (ChatRoom room : chatRooms)
            if (room.hasUser(friendUsername))
                room.addUser(username);
    }

}
