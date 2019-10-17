import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Solution {
    static Path folderPath = Paths.get("Лицензии отсортированные");
    static HashMap<String, String> map = new HashMap<>();
    static boolean isOk = true;

    static {
        map.put("186061.zip", "30");
        map.put("186062.zip", "35");
        map.put("186063.zip", "34");
        map.put("186064.zip", "27");
        map.put("186068.zip", "33");
        map.put("186069.zip", "32");
        map.put("186118.zip", "38");
        map.put("186894.zip", "28");
        map.put("186898.zip", "25");
        map.put("187704.zip", "29");
        map.put("188034.zip", "31");
        map.put("188035.zip", "26");
        map.put("188768.zip", "44");
        map.put("189328.zip", "45");
        map.put("190140.zip", "39");
        map.put("190141.zip", "40");
        map.put("190166.zip", "37");
        map.put("193240.zip", "48");
    }

    public static void main(String[] args) throws Exception {
        if (Files.exists(folderPath)) {
            JOptionPane.showMessageDialog(null,
                    "Папка \"Лицензии отсортированные\" уже существует." +
                    "\r\nУдалите её и запустите это приложение заново! ",
                    "Ошибка!", JOptionPane.ERROR_MESSAGE);
            throw new Exception();
        }
        Files.createDirectory(folderPath);
        Stream<Path> files = Files.list(Paths.get(""));
        files.forEach(p -> getLicense(p));
        if (isOk) JOptionPane.showMessageDialog(null,
                "Создана папка \"Лицензии отсортированные\"." +
                        "\r\nВ ней лежат папки с лицензиями. " +
                        "\r\n№ папки соответствует номеру машины на производстве.", "Раблта программы успешно завершена!", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void getLicense(Path p) {
        String path = p.getFileName().toString();
        if (map.containsKey(path)) {
            try {
                Path unzipFolder = Paths.get(folderPath + "/" + map.get(path));
                Files.createDirectory(unzipFolder);
                try (ZipInputStream zis = new ZipInputStream(new FileInputStream(p.toFile()))) {
                    byte[] buffer = new byte[1024];
                    ZipEntry zipEntry = zis.getNextEntry();
                    File file = new File(unzipFolder.toFile(), zipEntry.toString());
                    FileOutputStream fos = new FileOutputStream(file);
                    while (zipEntry != null) {
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                        fos.close();
                        zipEntry = zis.getNextEntry();
                    }
                }
            } catch (IOException e) {
                isOk = false;
                JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка во время разархивирования", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
