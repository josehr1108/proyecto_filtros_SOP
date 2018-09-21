package UI;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class ImageFrame extends JFrame{
    private File currentFile;
    private JLabel imageLabel;
    private JLabel timeLabel;
    
    public ImageFrame(File imageFile){
        this.currentFile = imageFile;
        this.displayImage();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
        this.setSize(800,600);
        this.setTitle("Resultado de filtro");
        this.setVisible(true);
    }

    private void displayImage(){
        byte[] data = null;
        try {
            data = Files.readAllBytes(this.currentFile.toPath());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
		}
        ImageIcon image = new ImageIcon(data);
        this.imageLabel = new JLabel(image); 
        this.imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(imageLabel);

        this.timeLabel =  new JLabel("Tiempo de duración(ms): ");
        //this.timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(timeLabel);
    }
}