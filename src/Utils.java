import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.List;

public class Utils {
    public static boolean validateCommand(String[] splitCommand) {
        return splitCommand.length == 4
                && splitCommand[0].equals("java")
                && splitCommand[1].equals("DAS")
                && isNumber(splitCommand[2])
                && isNumber(splitCommand[3]);
    }

    private static boolean isNumber(String s) {
        try {
            int i = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean isPortAvailable(int port) {
        try(DatagramSocket socket = new DatagramSocket(port)) {
            return true;
        } catch (SocketException e) {
            return false;
        }
    }

    public static String calculateAverage(List<Integer> numbers) {
        double sum = 0;
        for(Integer number : numbers) {
            if(!number.equals(0) ) {
                sum += number;
            }
        }
        return String.valueOf((int)(sum/numbers.size()));
    }
}
