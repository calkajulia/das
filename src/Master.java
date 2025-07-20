import java.net.DatagramSocket;
import java.net.SocketException;

public class Master {

    private static final String prefix = "MASTER";

    private int port;
    private String number;

    public Master(int port, String number) {
        this.port = port;
        this.number = number;
    }

    public void startMaster() {
        try {
            DatagramSocket socket = new DatagramSocket(port);
            Log.log(prefix, "UDP socket started on port " + port + ".");

            MasterThread receiver = new MasterThread(port, number, socket);
            receiver.start();
        } catch (SocketException e) {
            Log.log(prefix, "Program failed on starting MASTER." + e.getMessage());
        }
    }
}
