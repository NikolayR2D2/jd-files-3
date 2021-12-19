import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Main {
    final static String pathSavegames = "Games/GunRunner/savegames";

    public static void main(String[] args) {
        openZip(pathSavegames + "/saves.zip", pathSavegames);

        File dirSavegames = new File(pathSavegames);
        for (File item : dirSavegames.listFiles()) {
            if (!item.getName().equals("saves.zip")) {
                GameProgress gameProgress = openProgress(pathSavegames + "/" + item.getName());
                System.out.println(gameProgress);
            }
        }
    }

    private static void openZip(String zipFile, String pathSavegames) {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            String name;
            while ((entry = zis.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fos = new FileOutputStream(pathSavegames + "/" + name);
                for (int c = zis.read(); c != -1; c = zis.read()) {
                    fos.write(c);
                }
                fos.flush();
                zis.closeEntry();
                fos.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static GameProgress openProgress(String saveFile) {
        GameProgress gameProgress = null;
        try (FileInputStream fis = new FileInputStream(saveFile);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) ois.readObject();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            return gameProgress;
        }
    }
}
