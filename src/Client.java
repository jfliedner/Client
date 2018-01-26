import java.io.*;
import java.net.*;

public class Client {
  private static int DEFAULT_PORT = 27998;

  public static void main(String[] args) {
    int port;
    String hostName;
    int nuid;

    if (args.length < 2) {
      System.err.println("Invalid input. Needs to be \"<-p port> <-s> [hostname] [NEU ID]\";" +
              "port and -s are optional.");
      System.exit(1);
    }
    if (args[0].equals("-p")) {
      port = Integer.parseInt(args[1]);
      hostName = args[2];
      nuid = Integer.parseInt(args[3]);
    }
    else {
      port = DEFAULT_PORT;
      hostName = args[0];
      nuid = Integer.parseInt(args[1]);
    }

    try {
      Socket opSocket = new Socket(hostName, port);
      DataOutputStream out = new DataOutputStream(opSocket.getOutputStream());
      BufferedReader in = new BufferedReader(new InputStreamReader(opSocket.getInputStream());

      out.writeBytes("cs3700spring2018 HELLO 001234485\n");

      String secretFlag = "";

      String serverResponse = in.readLine();
      while (!serverResponse.contains("STATUS")) {
        if (validStatusMessage(serverResponse)) {
          String[] operation = serverResponse.split(" ");
          int a = Integer.parseInt(operation[2]);
          int b = Integer.parseInt(operation[4]);
          String opSymbol = operation[3];
          int result = doOperation(a, b, opSymbol);
          out.writeBytes("cs3700spring2018 " + result + "\n");
        }
        else {
          System.err.println("Incorrect response");
          System.exit(1);
        }

        serverResponse = in.readLine();
      }

      if (validByeMessage(serverResponse)) {
        System.out.println(secretFlag);
        out.close();
        in.close();
        opSocket.close();
      }
      else {
        System.err.println("Incorrect response");
        System.exit(1);
      }
    }
    catch (IOException e) {
      System.err.println("IOException");
    }
  }

  private static boolean validStatusMessage(String msg) {
    String[] message = msg.split(" ");

    if (!message[0].equals("cs3700spring18") || !message[1].equals("STATUS")) {
      return false;
    }

    try {
      Integer.parseInt(message[2]);
      Integer.parseInt(message[4]);
    }
    catch (NumberFormatException e) {
      return false;
    }

    if (!message[3].equals("+") && !message[3].equals("-") && !message[3].equals("*")
            && !message[3].equals("/") && !message[3].equals("%")) {
      return false;
    }

    return true;
  }

  private static boolean validByeMessage(String msg) {
    String[] message = msg.split(" ");
    return (message.length == 3) && (message[0].equals("cs3700spring2018"))
            && (message[2].equals("BYE"));
  }

  private static int doOperation(int a, int b, String operation) {
    int returnVal = 0;
    switch (operation) {
      case "+":
        returnVal = a + b;
        break;
      case "-":
        returnVal = a - b;
        break;
      case "*":
        returnVal = a * b;
        break;
      case "/":
        returnVal = a / b;
        break;
      case "%":
        returnVal = a % b;
        break;
      default:
        System.err.println("Not a valid operation");
        System.exit(1);
    }
    return returnVal;
  }
}
