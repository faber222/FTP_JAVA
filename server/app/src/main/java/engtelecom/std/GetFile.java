package engtelecom.std;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class GetFile extends ThreadClient {

    public GetFile(Socket clientSocket, String destino) {
        super(clientSocket, destino);
    }

    public Socket getClientSocket() {
        return super.getClientSocket();
    }

    public String getDestino() {
        return super.getDestino();
    }

    public void run(String arq, DataOutputStream saida, DataInputStream inputStreamReader) {
        FileInputStream inputStream = null;
        try {
            String caminhoArquivo = arq;
            String destinoAbsoluto = getDestino() + caminhoArquivo;
            File fileDestino = new File(destinoAbsoluto);

            if (fileDestino.exists()) {
                saida.writeUTF("true");
                inputStream = new FileInputStream(fileDestino);
                byte[] buffer = new byte[1024];

                int byteLido;
                while ((byteLido = inputStream.read(buffer)) != -1) {
                    saida.write(buffer, 0, byteLido);
                }
                System.out.println("Arquivo enviado!");
            } else {
                System.out.println("Arquivo não existe, encerrando conexão do cliente");
                saida.writeUTF("Arquivo não existe, encerrando conexão");
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        } finally {
            try {
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (getClientSocket() != null) {
                    getClientSocket().close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setDestino(String destino) {
        super.setDestino(destino);
    }

}
