package image;

import util.DialogChooser;
import util.StreamContainer;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;

public class ImageManager extends JFrame {
    private static final String DEFAULT_DIR = "/";
    private String filePath;

    private JFileChooser jfc;
    private FileInputStream fis;

    public byte[] uploadImage() {
        openFile();
        setFileOpening();
        byte[] readImage = readImage();

        return readImage;
    }

    private void openFile() {
        jfc = new JFileChooser(DEFAULT_DIR);
        int choice = jfc.showOpenDialog(getParent());

        if (DialogChooser.isApproved(choice)) {
            filePath = jfc.getSelectedFile().getPath();
        }
    }

    private void setFileOpening() {
        fis = StreamContainer.initialize(filePath);
    }

    private byte[] readImage() {
        if (fis != null) {
            try {
                return fis.readAllBytes();
            } catch (IOException e) {
                e.printStackTrace();
            }

            StreamContainer.close(fis);
        }

        return null;
    }
}
