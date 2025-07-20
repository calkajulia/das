import java.util.Scanner;

public class DAS {

    private static final String prefix = "DAS";

    public static boolean isRunning;

    public static void main(String[] args) {
        Scanner consoleInput = new Scanner(System.in);

        while(true) {
            String command = consoleInput.nextLine();
            String[] splitCommand = command.split(" ");

            if(!Utils.validateCommand(splitCommand)) {
                isRunning = false;
                throw new InvalidCommandException("Command \"" + command + "\" is invalid. Correct command format: java DAS <port> <number>.");
            }
            else {
                isRunning = true;
                int port = Integer.parseInt(splitCommand[2]);

                if(Utils.isPortAvailable(port)) {
                    Log.log(prefix, "Starting MASTER...");
                    Master master = new Master(port, splitCommand[3]);
                    master.startMaster();
                }
                else {
                    Log.log(prefix, "Starting SLAVE...");
                    Slave slave = new Slave(port, splitCommand[3]);
                    slave.startSlave();
                }
            }
        }
    }
}
