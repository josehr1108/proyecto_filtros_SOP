package FilterClasses;
import java.awt.*;
import java.awt.image.BufferedImage;

import java.io.*;

import javax.imageio.ImageIO;



public class SmoothScale {
	File inputFile;
	File outputFile;
;
	;
	public SmoothScale(File inputFile,String opMode) {
		this.inputFile = inputFile;
		this.SmoothMage();

	}

	private void sequentialProcess(BufferedImage imge,int[][][] rgb_buffer){
		try {

			for (int i = 0; i < imge.getHeight(); i++) {
				for (int j = 0; j < imge.getWidth(); j++) {

					Color c = new Color(imge.getRGB(j, i));
					rgb_buffer[0][i][j]=c.getRed();
					rgb_buffer[1][i][j]=c.getGreen();
					rgb_buffer[2][i][j]=c.getBlue();
				}
			}

		} catch (Exception e) {
			System.out.print("Exception: "+e.toString());
		}
	}


	private void SmoothMage(){
	
				BufferedImage imge;
				int[][][] rgb_buffer ;
				File tFile;
					try {
						imge = ImageIO.read(this.inputFile);
						rgb_buffer = new int[3][imge.getHeight()][imge.getWidth()];
					    sequentialProcess(imge,rgb_buffer);
				
					for (int i = 1; i < imge.getHeight() -1; i++) {
						for (int j = 1; j <imge.getWidth() -1; j++) {
		
							int r = rgb_buffer[0][i-1][j-1]+
									rgb_buffer[0][i-1][j]+
									rgb_buffer[0][i-1][j+1]+

									rgb_buffer[0][i][j-1]+
									rgb_buffer[0][i][j]+
									rgb_buffer[0][i][j+1]+

									rgb_buffer[0][i+1][j-1]+
									rgb_buffer[0][i+1][j]+
									rgb_buffer[0][i+1][j+1];


							int g = rgb_buffer[1][i-1][j-1]+
									rgb_buffer[1][i-1][j]+
									rgb_buffer[1][i-1][j+1]+

									rgb_buffer[1][i][j-1]+
									rgb_buffer[1][i][j]+
									rgb_buffer[1][i][j+1]+

									rgb_buffer[1][i+1][j-1]+
									rgb_buffer[1][i+1][j]+
									rgb_buffer[1][i+1][j+1];

							int b = rgb_buffer[2][i-1][j-1]+
									rgb_buffer[2][i-1][j]+
									rgb_buffer[2][i-1][j+1]+

									rgb_buffer[2][i][j-1]+
									rgb_buffer[2][i][j]+
									rgb_buffer[2][i][j+1]+

									rgb_buffer[2][i+1][j-1]+
									rgb_buffer[2][i+1][j]+
									rgb_buffer[2][i+1][j+1];

							Color nc = new Color(r/9,g/9,b/9);
							imge.setRGB(j,i,nc.getRGB());


						}
					}
					this.outputFile = new File("src/main/java/imgs/smoothscale.jpg");
					ImageIO.write(imge, "jpg", this.outputFile);	
				}
				catch (Exception e) {
					System.out.print("Exception: "+e.toString());
				}		
			
			for (int n = 0 ;  n < 30; n++) {

				try {

					tFile = new File("src/main/java/imgs/smoothscale.jpg");
					imge = ImageIO.read(tFile);
					rgb_buffer = new int[3][imge.getHeight()][imge.getWidth()];
					sequentialProcess(imge,rgb_buffer);

					for (int i = 1; i < imge.getHeight() -1; i++) {
						for (int j = 1; j <imge.getWidth() -1; j++) {
		
							int r = rgb_buffer[0][i-1][j-1]+
							rgb_buffer[0][i-1][j]+
							rgb_buffer[0][i-1][j+1]+

							rgb_buffer[0][i][j-1]+
							rgb_buffer[0][i][j]+
							rgb_buffer[0][i][j+1]+

							rgb_buffer[0][i+1][j-1]+
							rgb_buffer[0][i+1][j]+
							rgb_buffer[0][i+1][j+1];


					int g = rgb_buffer[1][i-1][j-1]+
							rgb_buffer[1][i-1][j]+
							rgb_buffer[1][i-1][j+1]+

							rgb_buffer[1][i][j-1]+
							rgb_buffer[1][i][j]+
							rgb_buffer[1][i][j+1]+

							rgb_buffer[1][i+1][j-1]+
							rgb_buffer[1][i+1][j]+
							rgb_buffer[1][i+1][j+1];

					int b = rgb_buffer[2][i-1][j-1]+
							rgb_buffer[2][i-1][j]+
							rgb_buffer[2][i-1][j+1]+

							rgb_buffer[2][i][j-1]+
							rgb_buffer[2][i][j]+
							rgb_buffer[2][i][j+1]+

							rgb_buffer[2][i+1][j-1]+
							rgb_buffer[2][i+1][j]+
							rgb_buffer[2][i+1][j+1];

					Color nc = new Color(r/9,g/9,b/9);
					imge.setRGB(j,i,nc.getRGB());
						
					}
				}
					ImageIO.write(imge, "jpg", this.outputFile);	
				}
				catch (Exception e) {
					System.out.print("Exception: "+e.toString());
				}		
			}
			System.out.println("#Termino!;");
		
			
		


	}

	public File getOutputFile(){
			
		return this.outputFile;
	}
}