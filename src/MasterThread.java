import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class MasterThread extends Thread {

    private static final String prefix = "MASTER";

    private int port;
    private DatagramSocket socket;
    private List<Integer> numbers;

    public MasterThread(int port, String number, DatagramSocket socket) {
        this.port = port;
        this.socket = socket;

        this.numbers = new ArrayList<>();
        numbers.add(Integer.parseInt(number));
    }

    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        while(DAS.isRunning) {
            try {
                Log.log(prefix, "Waiting for message...");
                socket.setSoTimeout(10000);
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());

                if(message.equals("0")) {
                    String average = Utils.calculateAverage(numbers);
                    Log.log(prefix, "Average of received messages: " + average);
                    sendBroadcast(average);
                } else if(message.equals("-1")) {
                    Log.log(prefix, "Message received: " + message);
                    sendBroadcast(message);
                    socket.close();
                    Log.log(prefix, "UDP socket closed.");
                    break;
                } else {
                    Log.log(prefix, "Message received: " + message);
                    numbers.add(Integer.parseInt(message));
                }
            } catch (IOException e) {
                Log.log(prefix, "MASTER failed on receiving message from SLAVE." + e.getMessage());
                break;
            }
        }
    }

    private void sendBroadcast(String message) {
        try {
            socket.setBroadcast(true);
            byte[] broadcastBuffer = message.getBytes();
            InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");
            DatagramPacket broadcastPacket = new DatagramPacket(broadcastBuffer, broadcastBuffer.length, broadcastAddress, port);
            socket.send(broadcastPacket);
            Log.log(prefix, "Sent broadcast with message " + message + ".");
            socket.setBroadcast(false);
        } catch (IOException e) {
            Log.log(prefix, "MASTER failed on sending broadcast message." + e.getMessage());
        }
    }
}
