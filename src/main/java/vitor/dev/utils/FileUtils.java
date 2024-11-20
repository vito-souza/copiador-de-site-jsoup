package vitor.dev.utils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Contém métodos utilizados na cópia de arquivos e conteúdo de sites para
 * arquivo.
 */
public class FileUtils {

    private FileUtils() {
    }

    /**
     * Método que gera uma URI do site a ser copiado.
     * 
     * @param urlString Endereço do site.
     * @return URI do site.
     */
    public static URI createUri(String urlString) {
        try {
            return new URI(urlString);
        } catch (URISyntaxException e) {
            e.printStackTrace(); // Pilha de execução
            return null; // Retorna null
        }
    }

    /**
     * Método que converte a URI para URL.
     * 
     * @param uri URI a ser convertida.
     * @return URI convertida em URL.
     */
    public static URL convertToUrl(URI uri) {
        try {
            return uri.toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace(); // Pilha de execução
            return null; // Retorna null
        }
    }

    /**
     * Gera um nome para o arquivo com base no dominio.
     * 
     * @param siteUrl Endereço do site.
     * @return Nome do arquivo.
     */
    public static String generateFileName(String siteUrl) {
        URL url = convertToUrl(createUri(siteUrl)); // Endereço do site

        if (url == null)
            return null; // NullPointerException

        // Gerando o nome do arquivo
        String domain = url.getHost(); // Pegando o nome do dominio
        domain = domain.replaceAll("[^a-zA-Z0-9.-]", "-"); // Removendo caracteres inválidos

        return domain + ".html"; // Retornando o nome e extensão do arquivo
    }
}
