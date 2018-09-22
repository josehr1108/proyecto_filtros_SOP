package FilterClasses;

import java.awt.*;
import java.awt.image.BufferedImage;

import java.io.*;

import javax.imageio.ImageIO;


public class SepiaScale {

	BufferedImage image;
	int width;
	int height;


	public SepiaScale() {

		try {
			File input = new File("/home/kenneth/Documentos/repos/proyecto_filtros_SOP/src/main/java/imgs/digital_image_processing.jpg");
			image = ImageIO.read(input);
			width = image.getWidth();
			height = image.getHeight();
			int sepiaDepth = 20;

			int w = image.getWidth();
			int h = image.getHeight();
	
			// We need 3 integers (for R,G,B color values) per pixel.
			int[] pixels = new int[w * h * 3];
			image.getRaster().getPixels(0, 0, w, h, pixels);
       
			for (int i = 0; i < height; i++) {

				for (int j = 0; j < width; j++) {

					int rgb = image.getRGB(j, i);
					Color color = new Color(rgb, true);
					int r = color.getRed();
					int g = color.getGreen();
					int b = color.getBlue();
					int gry = (r + g + b) / 3;
	
					r = g = b = gry;
					r = r + (sepiaDepth * 2);
					g = g + sepiaDepth;
	
					if (r > 255) { r = 255; }
					if (g > 255) { g = 255; }
					if (b > 255) { b = 255; }
	
					// Darken blue color to increase sepia effect
					
	
					// normalize if out of bounds
					if (b < 0)   { b = 0; }
					if (b > 255) { b = 255; }
	
					color = new Color(r, g, b, color.getAlpha());
					image.setRGB(j, i, color.getRGB());


				}
			}

			File ouptut = new File("/home/kenneth/Documentos/repos/proyecto_filtros_SOP/src/main/java/imgs/sepiascale.jpg");
			ImageIO.write(image, "jpg", ouptut);

		} catch (Exception e) {
			System.out.print("Exception: "+e.toString());
		}
	}
}