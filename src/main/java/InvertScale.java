import java.awt.*;
import java.awt.image.BufferedImage;

import java.io.*;

import javax.imageio.ImageIO;


public class InvertScale {

	BufferedImage image;
	int width;
	int height;

	public InvertScale() {

		try {
			File input = new File("/home/kenneth/Documentos/repos/proyecto_filtros_SOP/src/main/java/imgs/digital_image_processing.jpg");
			image = ImageIO.read(input);
			width = image.getWidth();
			height = image.getHeight();
       
			for (int i = 0; i < height; i++) {

				for (int j = 0; j < width; j++) {


					int rgb = image.getRGB(j, i);
					Color color = new Color(rgb, true);
					int r = 255 - color.getRed();
					int g = 255 - color.getGreen();
					int b = 255 - color.getBlue();
	
					color = new Color(r, g, b, color.getAlpha());
					image.setRGB(j, i, color.getRGB());
				}
			}

			File ouptut = new File("/home/kenneth/Documentos/repos/proyecto_filtros_SOP/src/main/java/imgs/invertscale.jpg");
			ImageIO.write(image, "jpg", ouptut);

		} catch (Exception e) {
			System.out.print("Exception: "+e.toString());
		}
	}
}