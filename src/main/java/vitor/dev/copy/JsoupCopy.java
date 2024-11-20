package vitor.dev.copy;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import vitor.dev.utils.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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

            // Cria o arquivo na pasta /out
            File outputFile = new File("./out/" + outputFileName);
            try (FileWriter writer = new FileWriter(outputFile)) {
                writer.write(doc.html()); // Salva o conteúdo HTML no arquivo.
            }
        } catch (IOException e) {
            e.printStackTrace(); // Pilha de execução.
        }
    }
}
