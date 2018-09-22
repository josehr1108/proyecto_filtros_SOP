package UI;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Timer;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class ImageFrame extends JFrame{
    private File currentFile;
    private JLabel imageLabel;
    private JLabel timeLabel;
    private long elapsedMs;
    private int elapsedSeconds;

    public ImageFrame(File imageFile,long elapsedMs){
        this.elapsedMs = elapsedMs;
        elapsedSeconds = (int) (elapsedMs / 1000) % 60 ;
        this.currentFile = imageFile;
        this.displayImage();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
        this.setSize(800,650);
        this.setTitle("Resultado de filtro");
        this.setVisible(true);
    }

    private void displayImage(){
        BufferedImage img = null;
        try {
            img = ImageIO.read(this.currentFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image dimg = img.getScaledInstance(800, 600,Image.SCALE_SMOOTH);
        ImageIcon imageIcon = new ImageIcon(dimg);
        this.imageLabel = new JLabel(imageIcon);
        this.imageLabel.setSize(800,600);
        add(imageLabel);

        this.timeLabel =  new JLabel("Tiempo de duración: ~" + elapsedSeconds + " segundos(" + elapsedMs + " ms)");
        add(timeLabel);
    }
}