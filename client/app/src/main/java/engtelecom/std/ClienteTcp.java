package engtelecom.std;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClienteTcp {
    public void executar(String[] args) throws IOException {
        try {
            int servidorPorta = Integer.parseInt(args[1]);
            String ipDoServidor = args[0];
            File fileOrigem = new File(args[3]);
        
            if (!fileOrigem.exists() && args[2].contains("set")) {
                System.out.println("O arquivo não existe na sua máquina, encerrando!");
                System.exit(0);;
            }
            try (Socket conexao = new Socket(ipDoServidor, servidorPorta)) {
                DataOutputStream saida = new DataOutputStream(conexao.getOutputStream());
                DataInputStream entrada = new DataInputStream(conexao.getInputStream());

                FileInputStream entradaArquivo = null;
                System.out.println("Conectado! " + conexao);

                saida.writeUTF(args[2]);
                saida.writeUTF(fileOrigem.getName());

                String ackServidor = entrada.readUTF();
                switch (args[2]) {
                    case "set":

                        if (ackServidor.equals("falso")) {
                            try {
                                entradaArquivo = new FileInputStream(fileOrigem);
                                byte[] buffer = new byte[1024];

                                int byteLido;
                                while ((byteLido = entradaArquivo.read(buffer)) != -1) {
                                    saida.write(buffer, 0, byteLido);
                                }
                                System.out.println("Arquivo enviado!");
                            } catch (Exception e) {
                                if (e.getMessage().contains("Broken pipe")) {
                                    System.out.println("O servidor desconectou.");
                                } else {
                                    throw e;
                                }
                            } finally {
                                try {
                                    if (entrada != null) {
                                        entrada.close();
                                    }
                                    if (entradaArquivo != null) {
                                        entradaArquivo.close();
                                    }
                                    if (conexao != null) {
                                        conexao.close();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            System.out.println("Servidor> " + ackServidor);
                        }
                        break;
                    case "get":
                        if (ackServidor.equals("true")) {
                            FileOutputStream outputStream = null;
                            try {
                                if (!fileOrigem.exists()) {
                                    outputStream = new FileOutputStream(fileOrigem);

                                    int byteLido;
                                    byte[] buffer = new byte[1024];
                                    while ((byteLido = entrada.read(buffer)) != -1) {
                                        outputStream.write(buffer, 0, byteLido);
                                    }
                                    System.out.println("Arquivo Recebido!");
                                } else {
                                    System.out.println("Cliente> Arquivo ja existe, encerrando conexao!\n");
                                }
                            } catch (Exception e) {
                                System.err.println(e.toString());
                            } finally {
                                try {
                                    if (entrada != null) {
                                        entrada.close();
                                    }
                                    if (outputStream != null) {
                                        outputStream.close();
                                    }
                                    if (conexao != null) {
                                        conexao.close();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            System.out.println("Servidor> " + ackServidor);
                        }
                        break;
                    default:
                        conexao.close();
                        System.err.println("Comando inválido!");
                        break;
                }

            } catch (Exception e) {
                System.err.println(e.toString());
            }
        } catch (Exception e) {
           System.err.println(e.toString());
        }

    }
}
