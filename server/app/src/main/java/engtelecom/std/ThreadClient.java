package engtelecom.std;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ThreadClient extends Thread {
    private Socket clientSocket;
    private String destino;

    public ThreadClient(Socket clientSocket, String destino) {
        this.clientSocket = clientSocket;
        this.destino = destino;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public void run() {
        try {
            DataInputStream entrada = new DataInputStream(getClientSocket().getInputStream());
            DataOutputStream saida = new DataOutputStream(getClientSocket().getOutputStream());
            
            String operacao = entrada.readUTF();
            String arq = entrada.readUTF();
            if (operacao.contains("get")) {
                GetFile getFile = new GetFile(getClientSocket(), getDestino());
                getFile.run(arq, saida, entrada);
            } else if (operacao.contains("set")) {
                SetFile setFile = new SetFile(getClientSocket(), getDestino());
                setFile.set(arq, saida, entrada);
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
}