package book;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;


public class IllustrationGenerator extends JComponent {
	
	private static final long serialVersionUID = 1L;
	private List<Point> edgePoints;
	private List<Cluster> regions;
	private final int NUM_CLUSTERS = 5;
	private final int LIMIT = 2000000;
	private final int BIN_SIZE = 5;
	private final int WINDOW_SIZE = 4;
	private final int CLUSTER_ITERATIONS = 1;
	private Color[] colorScheme;
	String curLetter;
	
	public IllustrationGenerator(){
		edgePoints = new ArrayList<Point>();
		regions = new ArrayList<Cluster>();
	}
	
	public void generateIllustration(BufferedImage image, String fileName, String letter){
		curLetter = letter;
		generateIllustration(image);
		this.outputImageFile(fileName);
	}
	
	public void generateIllustration(BufferedImage image){
		
		this.setSize(image.getWidth(), image.getHeight());
		image = blurImage(image);
		
		double edgePercent = 0;
		double limit = LIMIT;
		
		do{
			findEdgePoints(image, (int)limit);
			edgePercent = (double)edgePoints.size()/(image.getWidth() * image.getHeight());
			limit = limit * 1.1;
		}while(edgePercent > .2);
		
		repaint();
		image = blurImage(image);
		fill(image);
		assignColorScheme(image);
		repaint();
		System.out.println("Done");
	}
	
	private void findEdgePoints(BufferedImage image, int limit){
		edgePoints.clear();
		for(int y = 1; y < image.getHeight() - 1; y++){
			for(int x = 1; x < image.getWidth() - 1; x++){
				int hPixelSum = 0;
				hPixelSum += 1 * image.getRGB(x-1, y-1);
				hPixelSum += -1 * image.getRGB(x-1, y+1);
				hPixelSum += 2 * image.getRGB(x, y-1);
				hPixelSum += -2 * image.getRGB(x, y+1);
				hPixelSum += 1 * image.getRGB(x+1, y-1);
				hPixelSum += -1 * image.getRGB(x+1, y-1);
				
				int vPixelSum = 0;
				vPixelSum += -1 * image.getRGB(x-1, y-1);
				vPixelSum += -2 * image.getRGB(x-1, y);
				vPixelSum += -1 * image.getRGB(x-1, y+1);
				vPixelSum += 1 * image.getRGB(x+1, y-1);
				vPixelSum += 2 * image.getRGB(x+1, y);
				vPixelSum += 1 * image.getRGB(x+1, y-1);
				
				int dPixelSum = 0;
				dPixelSum+= -2 * image.getRGB(x - 1, y-1);
				dPixelSum+= -1 * image.getRGB(x - 1, y);
				dPixelSum+= -1 * image.getRGB(x, y - 1);
				dPixelSum+= 2 * image.getRGB(x + 1, y  + 1);
				dPixelSum+= 1 * image.getRGB(x + 1, y);
				dPixelSum+= 1 * image.getRGB(x, y + 1);
				
				int dPixelSum2 = 0;
				dPixelSum2+= -2 * image.getRGB(x - 1, y + 1);
				dPixelSum2+= -1 * image.getRGB(x - 1, y);
				dPixelSum2+= -1 * image.getRGB(x, y + 1);
				dPixelSum2+= 2 * image.getRGB(x + 1, y  - 1);
				dPixelSum2+= 1 * image.getRGB(x + 1, y);
				dPixelSum2+= 1 * image.getRGB(x, y - 1);
				
				
				if(Math.abs(hPixelSum) > limit){
					edgePoints.add(new Point(x,y));
				}
				if(Math.abs(vPixelSum) > limit){
					edgePoints.add(new Point(x,y));
				}
				if(Math.abs(dPixelSum) > limit){
					edgePoints.add(new Point(x,y));
				}
				if(Math.abs(dPixelSum2) > limit){
					edgePoints.add(new Point(x,y));
				}
			}
		}
	}
	
	private void assignColorScheme(BufferedImage image){
		if(colorScheme != null){
			
			Color[] colorSchemeToChange = new Color[NUM_CLUSTERS];
			for(int i = 0; i < NUM_CLUSTERS; i++){
				colorSchemeToChange[i] = regions.get(i).getColor();
			}
			sortByValue(colorSchemeToChange);
			
			for(int color = 0; color < colorScheme.length; color++){
				for(int cluster = 0; cluster < regions.size(); cluster++){
					if(regions.get(cluster).getColor().equals(colorSchemeToChange[color])){
						Color newScheme = colorScheme[color];
						//Color oldScheme = colorSchemeToChange[color];
						//int red = (int)(.5 * newScheme.getRed() + .5 * oldScheme.getRed());
						//int green = (int)(.5 *newScheme.getGreen() + .5 * oldScheme.getGreen());
						//int blue = (int)(.5 *newScheme.getBlue() + .5 * oldScheme.getBlue());
						//regions.get(cluster).setColor(new Color(red, green, blue));
						regions.get(cluster).setColor(newScheme);
						break;
					}
				}
			}
			
		}
		else{
			colorScheme = new Color[NUM_CLUSTERS];
			for(int i = 0; i < NUM_CLUSTERS; i++){
				colorScheme[i] = regions.get(i).getColor();
			}
			sortByValue(colorScheme);
		}
	}
	
	private void sortByValue(Color[] colorScheme){
		for(int color = 0; color < colorScheme.length; color++){
			Color minColor = colorScheme[color];
			for(int nextMin = color + 1; nextMin < colorScheme.length; nextMin++){
				Color nextColor = colorScheme[nextMin];
				int minValue = Math.max(Math.max(minColor.getRed(), minColor.getBlue()), minColor.getGreen());
				int nextValue = Math.max(Math.max(nextColor.getRed(), nextColor.getBlue()), nextColor.getGreen());
				if(nextValue > minValue){
					colorScheme[color] = nextColor;
					colorScheme[nextMin] = minColor;
					minColor = nextColor;
				}
			}
		}
	}
	
	private void fill(BufferedImage image){
		Random random = new Random();
		regions.clear();
		for(int i = 0; i < NUM_CLUSTERS; i++){
			regions.add(new Cluster(new Point(random.nextInt(image.getWidth()), random.nextInt(image.getHeight()))));
		}
		
		for(int i = 0; i < CLUSTER_ITERATIONS; i++){
			assignToClusters(image);
		}
	}
	
	private BufferedImage blurImage(BufferedImage image){
		
		BufferedImage blurred = new BufferedImage(image.getWidth() - WINDOW_SIZE,
				image.getHeight() - WINDOW_SIZE, BufferedImage.TYPE_INT_RGB);
		ColorModel model = ColorModel.getRGBdefault();
		
		for(int row = WINDOW_SIZE/2; row < blurred.getHeight() - WINDOW_SIZE/2; row++){
			for(int col = WINDOW_SIZE/2; col < blurred.getWidth() - WINDOW_SIZE/2; col++){
				int red = 0;
				int green = 0;
				int blue = 0;
				for(int x = col - WINDOW_SIZE/2; x < col + WINDOW_SIZE/2; x++){
					for(int y = row - WINDOW_SIZE/2; y < row + WINDOW_SIZE/2; y++){
						int pixel = image.getRGB(x,y);
						red += model.getRed(pixel);
						green += model.getGreen(pixel);
						blue += model.getBlue(pixel);
					}
				}
				red /= WINDOW_SIZE * WINDOW_SIZE;
				green /= WINDOW_SIZE * WINDOW_SIZE;
				blue /= WINDOW_SIZE * WINDOW_SIZE;
				blurred.setRGB(col, row,getColor(red, green, blue));
			}
		}
		return blurred;
	}
	
	private int getColor(int red, int green, int blue){
		int rgb = red;
		rgb = (rgb << 8) + green;
		rgb = (rgb << 8) + blue;
		return rgb;
	}
	
	private void assignToClusters(BufferedImage image){
		
		for(Cluster cluster: regions){
			cluster.clear();
		}
		
		for(int row = 0; row < image.getHeight(); row++){
			for(int col = 0; col < image.getWidth(); col++){
				
				Point point = new Point(col, row);
				Cluster bestCluster = null;
				double minDistance = Double.MAX_VALUE;
				
				for(Cluster cluster: regions){
					
					double distance = cluster.distance(point, cluster.getCentroid(), image);
					
					if(distance < minDistance){
						minDistance = distance;
						bestCluster = cluster;
					}
					
				}
				
				bestCluster.addPoint(point);
			}
		}
		
		for(Cluster cluster: regions){
			cluster.recomputeCentroid(image);
		}
	}
	
	private class Cluster{
		private Point centroid;
		private List<Point> points;
		private Color color;
		
		public Cluster(Point centroid ){
			this.setCentroid(centroid);
			this.points = new ArrayList<Point>();
		}
		
		public void setColor(Color color) {
			this.color = color;
		}

		public void setCentroid(Point centroid){
			this.centroid = centroid;
		}
		
		public Point getCentroid(){
			return centroid;
		}
		
		public Color getColor(){
			return color;
		}
		
		public Point getPoint(int i){
			return points.get(i);
		}
		
		public void clear(){
			points.clear();
		}
		
		public void recomputeCentroid(BufferedImage image){
			double x = 0;
			double y = 0;
			
			for(Point p: points){
				x += p.x;
				y += p.y;
			}
			
			centroid = new Point((int)x/points.size(), (int)y/points.size());
			recomputeColor(image);
		}
		
		private void recomputeColor(BufferedImage image){
			this.color = getMostFrequentColor(generateColorHistogram(image, ColorModel.getRGBdefault()));
		}
		
		private Color getMostFrequentColor(int[][][]histogram){
			int maxRed = 0, maxGreen = 0, maxBlue = 0;
			int max = 0;
			for(int red = 0; red < histogram.length;red++){
				for(int green = 0;green < histogram.length; green++){
					for(int blue = 0;blue < histogram.length; blue++){
						if(histogram[red][green][blue] > max){
							max = histogram[red][green][blue];
							maxRed = red * BIN_SIZE;
							maxBlue = blue * BIN_SIZE;
							maxGreen = green * BIN_SIZE;
						}
					}
				}
			}
			return new Color(maxRed, maxGreen, maxBlue);
		}
		
		private int[][][] generateColorHistogram(BufferedImage image, ColorModel model){
			final int WIDTH = 255/BIN_SIZE + 1;
			int[][][] histogram = new int[WIDTH][WIDTH][WIDTH];
			for(Point p: points){
				int pixel = image.getRGB(p.x, p.y);
				histogram[model.getRed(pixel)/BIN_SIZE][model.getGreen(pixel)/BIN_SIZE][model.getBlue(pixel)/BIN_SIZE]++;
			}
			return histogram;
		}
		
		public double distance(Point p1, Point p2, BufferedImage image){
			return p1.distance(p2) + Math.abs(image.getRGB(p1.x, p1.y) - image.getRGB(p2.x, p2.y))/1000;
		}
		
		public void addPoint(Point p){
			points.add(p);
		}
		
		public int size(){
			return points.size();
		}
	}
	
	
	@Override
	public void paint(Graphics g){
	
		this.setBackground(new Color(0, 255, 255, 255));
		
		for(Cluster cluster: regions){
			int xOffset = 0;//random.nextInt(20) - 10;
			int yOffset = 0;//random.nextInt(20) - 10;
			g.setColor(cluster.getColor());
			for(int i = 0; i < cluster.size(); i++){
				Point p = cluster.getPoint(i);
				g.drawLine(p.x- xOffset, p.y - yOffset, p.x - xOffset, p.y - yOffset);
			}
		}
		
		if(colorScheme == null)
			g.setColor(new Color(25,25,25,150));
		else
			g.setColor(colorScheme[0].darker());
		for(int i = 0; i < edgePoints.size(); i++){
			Point p = edgePoints.get(i);
			g.drawLine((int)p.getX(), (int)p.getY(), (int)p.getX(), (int)p.getY());
		}
		
		g.setColor(new Color(255, 255, 255, 150));
		g.setFont(g.getFont().deriveFont(600f).deriveFont(Font.BOLD));
		g.drawString(curLetter + "", this.getWidth()/2,this.getHeight()/2 + 200);
	}

	public void outputImageFile(String fileName){
		File file = new File(fileName);
		BufferedImage image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
		paint(image.getGraphics());
		try {
			ImageIO.write(image, "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		JFrame frame = new JFrame();
		IllustrationGenerator generator = new IllustrationGenerator();
		frame.add(generator);
		frame.setSize(1000, 700);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		try {
			generator.generateIllustration(ImageIO.read(new File("fruit.jpg")));
			generator.outputImageFile("topic.png");
			for(char letter = 'a'; letter < 'd'; letter++){
				generator.generateIllustration(ImageIO.read(new File(letter + ".jpg")));
				generator.outputImageFile(letter + ".png");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
