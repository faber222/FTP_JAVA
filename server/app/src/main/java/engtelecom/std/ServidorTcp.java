package engtelecom.std;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorTcp {

    public void executar(String[] args) throws IOException {
        while (true) {
            try (ServerSocket socket = new ServerSocket(Integer.parseInt(args[0]))) {
                System.out.println(
                        "Aguardando por conexoes em: " + socket.getInetAddress() + ":" + socket.getLocalPort() +
                                "\n (pressione CTRL+C para encerrar o processo)\n\n");

                try {
                    Socket clientSocket = socket.accept();
                    Thread t = new ThreadClient(clientSocket, args[1]);
                    t.start();
                } catch (Exception e) {
                    System.err.println(e.toString());
                }

            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }
    }
}
