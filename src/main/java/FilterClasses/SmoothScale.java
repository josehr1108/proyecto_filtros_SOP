package FilterClasses;
import java.awt.*;
import java.awt.image.BufferedImage;

import java.io.*;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;



public class SmoothScale {
	File inputFile;
	File outputFile;
	private long elapsedMs;
	
	int w;
	int h;
	int[][][] rgbbuffer ;
	BufferedImage imge;
	
		//Propiedades de modo paralelo
		private static int[] srcRgbArray;
		private static int[] destRgbArray;

	public SmoothScale(File inputFile,String opMode) {
		this.inputFile = inputFile;
		if(opMode == "Paralelo"){
			this.SmoothMageParallel();
		}
		else{
			this.SmoothMage();
		}
	}
	private BufferedImage getIMG(){
		return this.imge;
	}
	private void setIMG(BufferedImage img){
		this.imge = img;
	}

	private File getInputFile(){
		return this.inputFile;
	}
	private File getoutputFile(){
		return this.outputFile;
	}

	private void setOutputFile(File output){
		this.outputFile = output;
	}
	
	private int getW(){
		return this.w;
	}
	private void setW(int w){
		this.w = w;
	}

	private int getH(){
		return this.h;
	}
	private void setH(int h){
		this.h = h;
	}

	private int[][][] getBuffer(){
		return this.rgbbuffer;
	}
	private void setBuffer(int[][][] rgb_buffer){
		this.rgbbuffer = rgb_buffer;
	}


	

	private void parallelProcess(BufferedImage imge,int[][][] rgb_buffer){
		try {

			for (int i = 0; i < imge.getHeight(); i++) {
				for (int j = 0; j < imge.getWidth(); j++) {

					Color c = new Color(imge.getRGB(j, i));
					rgb_buffer[0][i][j]=c.getRed();
					rgb_buffer[1][i][j]=c.getGreen();
					rgb_buffer[2][i][j]=c.getBlue();
					c = null;
				}
			}

		} catch (Exception e) {
			System.out.print("Exception: "+e.toString());
		}

	}

	private void sequentialProcess(BufferedImage imge,int[][][] rgb_buffer){
		try {

			for (int i = 0; i < imge.getHeight(); i++) {
				for (int j = 0; j < imge.getWidth(); j++) {

					Color c = new Color(imge.getRGB(j, i));
					rgb_buffer[0][i][j]=c.getRed();
					rgb_buffer[1][i][j]=c.getGreen();
					rgb_buffer[2][i][j]=c.getBlue();
					c = null;
				}
			}

		} catch (Exception e) {
			System.out.print("Exception: "+e.toString());
		}
	}


	private void SmoothMageParallel(){

	
		try {
			BufferedImage imge = ImageIO.read(getInputFile());
			setW(imge.getWidth());
			setH(imge.getWidth());
			setIMG(imge);
			

			int[][][] rgb_buffer = new int[3][getH()][getW()];
			setBuffer(rgb_buffer);
			parallelProcess(getIMG(),getBuffer());
			
			for (int i = 1; i < getH() -1; i++) {
				for (int j = 1; j <getW()-1; j++) {
					int r = getBuffer()[0][i-1][j-1]+
					getBuffer()[0][i-1][j]+
					getBuffer()[0][i-1][j+1]+

					getBuffer()[0][i][j-1]+
					getBuffer()[0][i][j]+
					getBuffer()[0][i][j+1]+

					getBuffer()[0][i+1][j-1]+
					getBuffer()[0][i+1][j]+
					getBuffer()[0][i+1][j+1];


					int g = getBuffer()[1][i-1][j-1]+
					getBuffer()[1][i-1][j]+
					getBuffer()[1][i-1][j+1]+

					getBuffer()[1][i][j-1]+
					getBuffer()[1][i][j]+
					getBuffer()[1][i][j+1]+
						
					getBuffer()[1][i+1][j-1]+
					getBuffer()[1][i+1][j]+
					getBuffer()[1][i+1][j+1];

					int b = getBuffer()[2][i-1][j-1]+
					getBuffer()[2][i-1][j]+
					getBuffer()[2][i-1][j+1]+

					getBuffer()[2][i][j-1]+
					getBuffer()[2][i][j]+
					getBuffer()[2][i][j+1]+

					getBuffer()[2][i+1][j-1]+
					getBuffer()[2][i+1][j]+
					getBuffer()[2][i+1][j+1];

					Color nc = new Color(r/9,g/9,b/9);
					this.imge.setRGB(j,i,nc.getRGB());
				} 
			}
				File nn = new File("src/main/java/imgs/smoothscale.jpg");
				setOutputFile(nn);
				ImageIO.write(getIMG(), "jpg",nn);	
			}
			catch (Exception e) {
				System.out.print("Exception: "+e.toString());
			}		
			
			long startTime = System.nanoTime();

			for (int n = 0 ;  n < 10; n++) {
				try {
					
					BufferedImage imme = ImageIO.read(getInputFile());
					setW(imme.getWidth());
					setH(imme.getWidth());
					setIMG(imme);
					
		
					int[][][] rgbuffer = new int[3][getH()][getW()];
					setBuffer(rgbuffer);
					parallelProcess(getIMG(),getBuffer());

					Thread t1 = new Thread(new Runnable(){
				
						@Override
						public void run() {	
							int t1W = (int) (getW() / 3);
							int t1H =  (int) (getH() / 3);

					
							for (int i = 1; i < t1H -1; i++) {
								for (int j = 1; j <t1W -1; j++) {
				
									int r = getBuffer()[0][i-1][j-1]+
									getBuffer()[0][i-1][j]+
									getBuffer()[0][i-1][j+1]+
	
									getBuffer()[0][i][j-1]+
									getBuffer()[0][i][j]+
									getBuffer()[0][i][j+1]+
	
									getBuffer()[0][i+1][j-1]+
									getBuffer()[0][i+1][j]+
									getBuffer()[0][i+1][j+1];
	
	
							int g =  getBuffer()[1][i-1][j-1]+
							getBuffer()[1][i-1][j]+
									getBuffer()[1][i-1][j+1]+
	
									getBuffer()[1][i][j-1]+
									getBuffer()[1][i][j]+
									getBuffer()[1][i][j+1]+
	
									getBuffer()[1][i+1][j-1]+
									getBuffer()[1][i+1][j]+
									getBuffer()[1][i+1][j+1];
	
							int b =  getBuffer()[2][i-1][j-1]+
							getBuffer()[2][i-1][j]+
							getBuffer()[2][i-1][j+1]+
	
							getBuffer()[2][i][j-1]+
							getBuffer()[2][i][j]+
							getBuffer()[2][i][j+1]+
	
							getBuffer()[2][i+1][j-1]+
							getBuffer()[2][i+1][j]+
							getBuffer()[2][i+1][j+1];
	
							Color nc = new Color(r/9,g/9,b/9);
							getIMG().setRGB(j,i,nc.getRGB());
							
							}
						}
				
								
						}
					});

					Thread t2 = new Thread(new Runnable(){
				
						@Override
						public void run() {	
							int t2W =  (int) (getW() /3);
							int t2H =  (int) (getH() /3);

							for (int i = t2H; i < (t2H+t2H) -1; i++) {
								for (int j = t2W; j < (t2W+t2W) -1; j++) {
				
									int r = getBuffer()[0][i-1][j-1]+
									getBuffer()[0][i-1][j]+
									getBuffer()[0][i-1][j+1]+
	
									getBuffer()[0][i][j-1]+
									getBuffer()[0][i][j]+
									getBuffer()[0][i][j+1]+
	
									getBuffer()[0][i+1][j-1]+
									getBuffer()[0][i+1][j]+
									getBuffer()[0][i+1][j+1];
	
	
							int g =  getBuffer()[1][i-1][j-1]+
							getBuffer()[1][i-1][j]+
									getBuffer()[1][i-1][j+1]+
	
									getBuffer()[1][i][j-1]+
									getBuffer()[1][i][j]+
									getBuffer()[1][i][j+1]+
	
									getBuffer()[1][i+1][j-1]+
									getBuffer()[1][i+1][j]+
									getBuffer()[1][i+1][j+1];
	
							int b =  getBuffer()[2][i-1][j-1]+
							getBuffer()[2][i-1][j]+
							getBuffer()[2][i-1][j+1]+
	
							getBuffer()[2][i][j-1]+
							getBuffer()[2][i][j]+
							getBuffer()[2][i][j+1]+
	
							getBuffer()[2][i+1][j-1]+
							getBuffer()[2][i+1][j]+
							getBuffer()[2][i+1][j+1];
	
							Color nc = new Color(r/9,g/9,b/9);
							getIMG().setRGB(j,i,nc.getRGB());
							
							}
						}
								
						}
					});

					Thread t3 = new Thread(new Runnable(){
				
						@Override
						public void run() {	
							int t3W =  (int) (getW() /3);
							int t3H =  (int) (getH() /3);

						
							for (int i = (t3H+t3H); i < (t3H+t3H+t3H) -1; i++) {
								for (int j = (t3W+t3W); j < (t3W+t3W+t3W) -1; j++) {
				
									int r = getBuffer()[0][i-1][j-1]+
									getBuffer()[0][i-1][j]+
									getBuffer()[0][i-1][j+1]+
	
									getBuffer()[0][i][j-1]+
									getBuffer()[0][i][j]+
									getBuffer()[0][i][j+1]+
	
									getBuffer()[0][i+1][j-1]+
									getBuffer()[0][i+1][j]+
									getBuffer()[0][i+1][j+1];
	
	
							int g =  getBuffer()[1][i-1][j-1]+
							getBuffer()[1][i-1][j]+
									getBuffer()[1][i-1][j+1]+
	
									getBuffer()[1][i][j-1]+
									getBuffer()[1][i][j]+
									getBuffer()[1][i][j+1]+
	
									getBuffer()[1][i+1][j-1]+
									getBuffer()[1][i+1][j]+
									getBuffer()[1][i+1][j+1];
	
							int b =  getBuffer()[2][i-1][j-1]+
							getBuffer()[2][i-1][j]+
							getBuffer()[2][i-1][j+1]+
	
							getBuffer()[2][i][j-1]+
							getBuffer()[2][i][j]+
							getBuffer()[2][i][j+1]+
	
							getBuffer()[2][i+1][j-1]+
							getBuffer()[2][i+1][j]+
							getBuffer()[2][i+1][j+1];
	
							Color nc = new Color(r/9,g/9,b/9);
							getIMG().setRGB(j,i,nc.getRGB());
							
							}
						}
					
						}
					});
					

					t1.start();
					t2.start();
					t3.start();

					File nn = new File("src/main/java/imgs/smoothscale.jpg");
					setOutputFile(nn);
					ImageIO.write(getIMG(), "jpg",nn);	

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
	long endTime = System.nanoTime();
	this.elapsedMs = TimeUnit.NANOSECONDS.toMillis(endTime-startTime);	
}

	private void SmoothMage(){
		BufferedImage imge;
		int[][][] rgb_buffer ;
		File tFile;
			try {
				imge = ImageIO.read(getInputFile());
				rgb_buffer = new int[3][imge.getHeight()][imge.getWidth()];

				System.out.println("WIDTH: " + imge.getWidth() + " HEIGHT: "+ imge.getHeight());
				System.out.println("# WIDTH: " + imge.getWidth()/3 + " HEIGHT: "+ imge.getHeight()/3);

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
			File nn = new File("src/main/java/imgs/smoothscale.jpg");
			setOutputFile(nn);
			ImageIO.write(imge, "jpg",nn);	
		}
		catch (Exception e) {
			System.out.print("Exception: "+e.toString());
		}		
		
		long startTime = System.nanoTime();
		for (int n = 0 ;  n < 30; n++) {
			
			try {

				
				imge = ImageIO.read(getOutputFile());
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
			File nn = getOutputFile();
				ImageIO.write(imge, "jpg", nn);	
			}
			catch (Exception e) {
				System.out.print("Exception: "+e.toString());
			}		
		}
			long endTime = System.nanoTime();
			this.elapsedMs = TimeUnit.NANOSECONDS.toMillis(endTime-startTime);	
}



	public long getElapsedMs(){
		return this.elapsedMs;
	}


	public File getOutputFile(){
			
		return this.outputFile;
	}

}