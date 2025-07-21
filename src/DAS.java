import java.net.DatagramSocket;
import java.net.SocketException;

public class DAS {

    private static final String prefix = "DAS";

    public static void main(String[] args) {
        if(!validateArgs(args)) {
            System.err.println("Usage: java DAS <port> <number>");
            System.err.println("Where <port> is UDP port number and <number> is an integer");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);
        String number = args[1];

        if(isPortAvailable(port)) {
            Log.log(prefix, "Starting MASTER...");
            Master master = new Master(port, number);
            master.startMaster();
        } else {
            Log.log(prefix, "Starting SLAVE...");
            Slave slave = new Slave(port, number);
            slave.startSlave();
        }
    }

    private static boolean validateArgs(String[] args) {
        if(args.length != 2) {
            return false;
        }

        try {
            int port = Integer.parseInt(args[0]);
            if(port < 1 || port > 65535) {
                return false;
            }
            Integer.parseInt(args[1]);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isPortAvailable(int port) {
        try(DatagramSocket socket = new DatagramSocket(port)) {
            return true;
        } catch (SocketException e) {
            return false;
        }
    }
}
