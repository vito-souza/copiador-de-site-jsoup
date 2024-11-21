package vitor.dev.copy;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import vitor.dev.utils.FileUtils;

public class JsoupCopy {

    private JsoupCopy() {
    }

    /**
     * Método que copia o conteúdo de um site através do Jsoup.
     * 
     * @param siteUrl URL do site a ser copiado.
     */
    public static void copy(String siteUrl) {
        String outputFileName = FileUtils.generateFileName(siteUrl); // Nome do arquivo de saída.

        try {
            // Faz a conexão e obtém o conteúdo HTML do site.
            Document doc = Jsoup.connect(siteUrl).get();

            // Baixar imagens
            Elements images = doc.select("img");
            for (Element image : images) {
                String imgUrl = image.absUrl("src");
                if (imgUrl != null && !imgUrl.isEmpty()) {
                    String imageName = imgUrl.substring(imgUrl.lastIndexOf("/") + 1);
                    downloadFile(imgUrl, "images/" + imageName); // Baixar a imagem
                    image.attr("src", "images/" + imageName); // Caminho relativo para a pasta images
                }
            }

            // Baixar CSS
            Elements stylesheets = doc.select("link[rel=stylesheet]");
            for (Element stylesheet : stylesheets) {
                String cssUrl = stylesheet.absUrl("href");
                if (cssUrl != null && !cssUrl.isEmpty()) {
                    String cssName = cssUrl.substring(cssUrl.lastIndexOf("/") + 1);
                    downloadFile(cssUrl, "css/" + cssName); // Baixar o CSS
                    stylesheet.attr("href", "css/" + cssName); // Caminho relativo para a pasta css
                }
            }

            // Baixar JS
            Elements scripts = doc.select("script[src]");
            for (Element script : scripts) {
                String jsUrl = script.absUrl("src");
                if (jsUrl != null && !jsUrl.isEmpty()) {
                    String jsName = jsUrl.substring(jsUrl.lastIndexOf("/") + 1);
                    downloadFile(jsUrl, "js/" + jsName); // Baixar o JS
                    script.attr("src", "js/" + jsName); // Caminho relativo para a pasta js
                }
            }

            // Cria o arquivo na pasta /out
            File outputFile = new File("./out/" + outputFileName);
            FileWriter writer = new FileWriter(outputFile);

            writer.write(doc.html()); // Salva o conteúdo HTML no arquivo.
            writer.close();
        } catch (IOException e) {
            e.printStackTrace(); // Pilha de execução.
        }
    }

    /**
     * Método genérico para baixar arquivos (imagens, CSS, JS) a partir de uma URL.
     *
     * @param fileUrl     URL do arquivo a ser baixado.
     * @param destination Caminho relativo onde o arquivo será armazenado.
     */
    private static void downloadFile(String fileUrl, String destination) {
        try {
            if (fileUrl == null || fileUrl.isEmpty()) {
                System.out.println("URL inválida: " + fileUrl);
                return;
            }

            URL url = new URL(fileUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000); // Timeout de 5 segundos para a conexão
            connection.setReadTimeout(5000); // Timeout de 5 segundos para leitura

            String contentType = connection.getContentType();
            if (contentType != null) {
                // Determina o diretório com base no tipo de conteúdo
                String dirPath = destination.substring(0, destination.lastIndexOf("/"));
                File dir = new File("./out/" + dirPath);
                if (!dir.exists()) {
                    dir.mkdirs(); // Cria o diretório se não existir
                }

                InputStream inputStream = connection.getInputStream();
                BufferedOutputStream outputStream = new BufferedOutputStream(
                        new FileOutputStream("./out/" + destination));

                byte[] buffer = new byte[4096];
                int bytes;

                while ((bytes = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytes);
                }

                outputStream.close();
                inputStream.close();
            } else {
                System.out.println("Conteúdo inválido: " + fileUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
