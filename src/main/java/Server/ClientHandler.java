package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ClientHandler implements Runnable{
    Scanner scanner = new Scanner(System.in);
    private String name;
    final DataInputStream dataInputStream;
    final DataOutputStream dataOutputStream;
    Socket socket;
    boolean isloggedin;

    // constructor
    public ClientHandler(Socket s, String name,DataInputStream dis, DataOutputStream dos) {
        this.dataInputStream = dis;
        this.dataOutputStream = dos;
        this.name = name;
        this.socket = s;
        this.isloggedin=true;
    }

    @Override
    public void run() {

        String received;
        while (true)
        {
            try
            {
                // receive the string
                received = dataInputStream.readUTF();

                System.out.println(received);

                if(received.equals("logout")){
                    this.isloggedin=false;
                    this.socket.close();
                    break;
                }

                // break the string into message and recipient part
                StringTokenizer st = new StringTokenizer(received, "#");
                String MsgToSend = st.nextToken();
                String recipient = st.nextToken();

                // search for the recipient in the connected devices list.
                for (ClientHandler client : Server.clientList)
                {
                    // if the recipient is found, write on its
                    // output stream
                    if (client.name.equals(recipient) && client.isloggedin==true)
                    {
                        client.dataOutputStream.writeUTF(this.name+" : "+MsgToSend);
                        break;
                    }
                }
            } catch (IOException e) {

                e.printStackTrace();
            }

        }
        try
        {
            // closing resources
            this.dataInputStream.close();
            this.dataOutputStream.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
