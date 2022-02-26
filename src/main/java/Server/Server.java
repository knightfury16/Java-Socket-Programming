package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
    // Vector to store active clients
    static Vector<ClientHandler> clientList = new Vector<>();

    // ID for unique client
    static int clientID = 0;

    public static void main(String[] args) throws IOException
    {
        // server is listening on port 1234
        ServerSocket serversocket = new ServerSocket(1234);

        Socket socket;

        while (true)
        {
            // Accept the incoming request
            socket = serversocket.accept();

            System.out.println("New client request received : " + socket);

            // obtain input and output streams
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

            System.out.println("Creating a new handler for this client...");

            dataOutputStream.writeUTF("Connected to server.");

            // Create a new handler object for handling this request.
            ClientHandler clientHandler = new ClientHandler(socket,"client " + clientID, dataInputStream, dataOutputStream);

            // Create a new Thread with this object.
            Thread thread = new Thread(clientHandler);

            // add this client to active clients list
            clientList.add(clientHandler);

            // start the thread.
            thread.start();

            clientID++;

        }
    }
}
