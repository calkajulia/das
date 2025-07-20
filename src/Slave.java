import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Slave {

    private static final String prefix = "SLAVE";

    private int port;
    private String number;

    public Slave(int port, String number) {
        this.port = port;
        this.number = number;
    }

    public void startSlave() {
        try {
            DatagramSocket socket = new DatagramSocket();
            Log.log(prefix, "UDP socket started on port " + socket.getLocalPort() + ".");
            Log.log(prefix, "Message to send " + number + ".");

            byte[] buffer = number.getBytes();
            try {
                InetAddress receiver = InetAddress.getByName("localhost");
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, receiver, port);
                socket.send(packet);
                Log.log(prefix, "Sent " + number + " to MASTER on port " + port + ".");

            } catch (IOException e) {
                Log.log(prefix, "SLAVE failed on sending message to MASTER." + e.getMessage());
            }
            socket.close();
            Log.log(prefix, "UDP socket closed.");

        } catch (SocketException e) {
            Log.log(prefix, "Program failed on starting SLAVE." + e.getMessage());
        }
    }
}
