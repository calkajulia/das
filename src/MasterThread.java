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
        int initialNumber = Integer.parseInt(number);
        numbers.add(initialNumber);
        Log.log(prefix, "Initial number: " + initialNumber);
    }

    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        while(true) {
            try {
                Log.log(prefix, "Waiting for message...");
                socket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength());
                Log.log(prefix, "Message received: " + message);

                if(message.equals("0")) {
                    double average = calculateAverage(numbers);
                    String averageStr = String.valueOf(average);
                    Log.log(prefix, "Calculated average: " + averageStr);
                    sendBroadcast(averageStr);
                } else if(message.equals("-1")) {
                    Log.log(prefix, "Terminating.");
                    sendBroadcast(message);
                    break;
                } else {
                    try {
                        numbers.add(Integer.parseInt(message));
                    } catch (NumberFormatException e) {
                        Log.log(prefix, "Received invalid number: " + message);
                    }
                }
            } catch (IOException e) {
                Log.log(prefix, "Error receiving message: " + e.getMessage());
                break;
            }
        }
    }

    private double calculateAverage(List<Integer> numbers) {
        double sum = 0;
        int count = 0;

        for(Integer number : numbers) {
            if(number != 0) {
                sum += number;
                count++;
            }
        }
        return count > 0 ? sum / count : 0;
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
            Log.log(prefix, "Error sending broadcast: " + e.getMessage());
        }
    }
}
