package FilterClasses;

import java.awt.*;
import java.awt.image.BufferedImage;

import java.io.*;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;


public class GaussianBlurScale {

	BufferedImage image;
	int width;
	int height;
	int[] filter = {1, 2, 1, 2, 1, 2, 1, 2, 1,
		2, 1, 2, 1, 2, 1, 2, 1, 2,
		1, 2, 1, 2, 1, 2, 1, 2, 1,
		2, 1, 2, 1, 2, 1, 2, 1, 2,
		1, 2, 1, 2, 4, 2, 1, 2, 1,
		2, 1, 2, 1, 2, 1, 2, 1, 2,
		1, 2, 1, 2, 1, 2, 1, 2, 1,
		2, 1, 2, 1, 2, 1, 2, 1, 2,
		1, 2, 1, 2, 1, 2, 1, 2, 1};

	int filterWidth = 27;

	int sum;

	public GaussianBlurScale(){
			try {
				File iput = new File("/home/kenneth/Documentos/repos/proyecto_filtros_SOP/src/main/java/imgs/digital_image_processing.jpg");
				image = ImageIO.read(iput);
				width = image.getWidth();
				height = image.getHeight();	
				sum = IntStream.of(filter).sum();


				int[] input = image.getRGB(0, 0, width, height, null, 0, width);
	
				int[] output = new int[input.length];
			


				final int pixelIndexOffset = width - filterWidth;
				final int centerOffsetX = filterWidth / 2;
				final int centerOffsetY = filter.length / filterWidth / 2;
			
				// apply filter
				for (int h = height - filter.length / filterWidth + 1, w = width - filterWidth + 1, y = 0; y < h; y++) {
					for (int x = 0; x < w; x++) {

						int rgb = image.getRGB(x, y);
					Color color = new Color(rgb, true);
						int r = 0;
						int g = 0;
						int b = 0;
						for (int filterIndex = 0, pixelIndex = y * width + x;
								filterIndex < filter.length;
								pixelIndex += pixelIndexOffset) {
							for (int fx = 0; fx < filterWidth; fx++, pixelIndex++, filterIndex++) {
								int col = input[pixelIndex];
								int factor = filter[filterIndex];

								// sum up color channels seperately
								r += ((col >>> 16) & 0xFF) * factor;
								g += ((col >>> 8) & 0xFF) * factor;
								b += (col & 0xFF) * factor;
							}
						}
						r /= sum;
						g /= sum;
						b /= sum;
						// combine channels with full opacity
						output[x + centerOffsetX + (y + centerOffsetY) * width] = (r << 16) | (g << 8) | b | 0xFF000000;
					


					color = new Color(r, g, b, color.getAlpha());
					image.setRGB(x, y, color.getRGB());
					}

				}
			
				File ouptut = new File("/home/kenneth/Documentos/repos/proyecto_filtros_SOP/src/main/java/imgs/gaussianblurscale.jpg");
				ImageIO.write(image, "jpg", ouptut);

			} catch (Exception e) {
				//TODO: handle exception
			}


	}

	
}
