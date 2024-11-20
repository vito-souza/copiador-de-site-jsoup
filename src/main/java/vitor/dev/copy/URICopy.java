package vitor.dev.copy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import vitor.dev.utils.FileUtils;

/**
 * Classe que cuida dos métodos de copia de sites para arquivos.
 */
public class URICopy {

    private URICopy() {
    }

    /**
     * Método que lê o conteúdo do site e o retorna em String.
     * 
     * @param siteUrl Endereço do site.
     * @return Conteúdo do site em String.
     */
    public static String read(String siteUrl) {
        StringBuilder content = new StringBuilder(); // Para transformar o conteúdo do site em String
        URL url = FileUtils.convertToUrl(FileUtils.createUri(siteUrl)); // URL do site a ser lido

        if (url == null)
            return null; // NullPointerException

        // Lendo e concatenando o conteúdo do site
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            String line;

            // Enquanto ainda houverem linhas para copiar:
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator()); // Pega o separador de linhas do sistema
            }
        } catch (IOException e) {
            e.printStackTrace(); // Pilha de execução
        }

        return content.toString(); // Retornando o conteúdo do site
    }

    /**
     * Escreve o conteúdo do site em um arquivo .html
     * 
     * @param siteUrl Endereço do site.
     */
    public static void copy(String siteUrl) {
        String content = read(siteUrl); // Pegando o conteúdo do site a ser copiado

        if (content == null)
            return; // Não foi possível copiar o conteúdo do site

        // Gerar o nome do arquivo baseado na URL do site
        String outputFileName = FileUtils.generateFileName(siteUrl);

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter("./out/" + outputFileName))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}