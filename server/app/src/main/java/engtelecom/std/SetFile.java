package engtelecom.std;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SetFile extends ThreadClient {

    public SetFile(final Socket clientSocket, final String destino) {
        super(clientSocket, destino);
    }

    public Socket getClientSocket() {
        return super.getClientSocket();
    }

    public String getDestino() {
        return super.getDestino();
    }

    public void set(String arq, DataOutputStream saida, DataInputStream inputStreamReader) {
        FileOutputStream outputStream = null;
        try {
            String caminhoArquivo = arq;
            String destinoAbsoluto = getDestino() + caminhoArquivo;
            File fileDestino = new File(destinoAbsoluto);
            
            if (!fileDestino.exists()) {
                saida.writeUTF("falso");
                outputStream = new FileOutputStream(fileDestino);

                int byteLido;
                byte[] buffer = new byte[1024];
                while ((byteLido = inputStreamReader.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, byteLido);
                }
                System.out.println("Arquivo Recebido!");
            } else {
                String erro = "Arquivo ja existe, encerrando conexao";
                System.out.println(erro + " do cliente");
                saida.writeUTF(erro);
            }

        } catch (Exception e) {
            System.err.println(e.toString());
        } finally {
            try {
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (getClientSocket() != null) {
                    getClientSocket().close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
