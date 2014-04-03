package book;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.swing.JFrame;



public class ABCBookCreator {

	public static void main(String[] args) throws IOException{
		if(args.length != 3){
			System.out.println("wordsfile sentencesfile booktitle");
		}
		
		ABCBookCreator creator = new ABCBookCreator(new File(args[0]), new File(args[1]), args[2]);
		creator.generateBook(args[2]);
	}
	
	private String[] words;
	private String[] sentences;
	private final int NUM_LETTERS = 26;
	private final int NUM_SENTENCES = 27;
	private Set<Integer> usedImages;
	
	public ABCBookCreator(File wordFile, File sentenceFile, String topic){
		words = new String[NUM_LETTERS];
		sentences = new String[NUM_SENTENCES];
		usedImages = new TreeSet<Integer>();
		
		try {
			
			BufferedReader reader = new BufferedReader(new FileReader(wordFile));
			for(int wordIndex = 0; wordIndex < NUM_LETTERS; wordIndex++){
				words[wordIndex] = reader.readLine();
			}
			reader.close();
			
			reader = new BufferedReader(new FileReader(sentenceFile));
			for(int sentenceIndex = 0; sentenceIndex < NUM_SENTENCES; sentenceIndex++){
				sentences[sentenceIndex] = reader.readLine();
			}
			reader.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String generateImageFromGoogle(IllustrationGenerator generator, String word, String letter) throws IOException{
		String query = word.replaceAll(" ", "%20");
		if(!new File(word + ".jpg").isFile())
			this.downloadImage(word + ".jpg", getImageURL(query));
		generator.generateIllustration(resizeImage(ImageIO.read(new File(word + ".jpg"))), word + ".png", letter);
		return "<img src=\"" + word + ".png" + 
				"\" alt=\"" + word + "\" width=\"500\">";
	}
	
	private String generateImageFromDatabase(IllustrationGenerator generator, ImageDatabase database, String letter, String word,
		String theme, String sentences) throws IOException{
		//String imageName = this.chooseFinalImage(database, word, theme, sentences);
		String imageName = null;
		if(imageName == null){
			generateImageFromGoogle(generator,word, letter);
		}
		else{
			File file = new File(imageName+".jpg");
			System.out.println(imageName);
			BufferedImage image = ImageIO.read(file);
			generator.generateIllustration(image, word + ".png", letter);
		}
		return "<img src=\"" + word + ".png" + 
				"\" alt=\"" + word + "\" width=\"500\">";
	}
	
	private String chooseFinalImage(ImageDatabase database, String word, String theme, String sentences){
		word = word.replaceAll(" ", "");
		Set<Integer> imagePool = database.getImagePool(word);
		List<Integer> equivalentScores = new ArrayList<Integer>();
		if(imagePool != null && imagePool.size() != 0){
			double maxScore = 0;
			for(Integer imageNum:imagePool){
				
				if(usedImages.contains(imageNum)){
					continue;
				}
				
				double curScore = database.scoreImage(theme, word, sentences, imageNum);
				if(curScore > maxScore){
					maxScore = curScore;
					
					equivalentScores.clear();
					equivalentScores.add(imageNum);
				}
				else if(curScore == maxScore){
					equivalentScores.add(imageNum);
				}
			}
			
			Random random = new Random();
			Integer finalImage = equivalentScores.get(random.nextInt(equivalentScores.size()));
			usedImages.add(finalImage);
			String imageName = "images\\im"+finalImage;
			return imageName;
		}
		return null;
	}
	
	public void generateEPubBook(String title) throws FileNotFoundException, IOException{
		/*Book book = new Book();
		
		// Set the title
		book.getMetadata().addTitle(title);
		
		// Add an Author
		book.getMetadata().addAuthor(new Author("C.S.", "Seuss"));
		
		// Create EpubWriter
		EpubWriter epubWriter = new EpubWriter();
			
		// Write the Book as Epub
		epubWriter.write(book, new FileOutputStream(title +".epub"));*/
	}
	
	public void generateBook(String bookTitle) throws IOException{
		File book = new File(bookTitle + ".html");
		BufferedWriter bookWriter = new BufferedWriter(new FileWriter(book));
		JFrame frame = new JFrame();
		IllustrationGenerator generator = new IllustrationGenerator();
		ImageDatabase database = null;// new ImageDatabase("images.txt");
		
		frame.add(generator);
		frame.setSize(1000, 700);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		bookWriter.append("<html>");
		bookWriter.append("<h1>");
		bookWriter.append(bookTitle);
		bookWriter.append(generateImageFromDatabase(generator,database, bookTitle , bookTitle, bookTitle, ""));
		bookWriter.append("</h1>");
		
		for(char letter = 'A'; letter < 'Z'; letter ++){
			String word = words[letter - 'A'];
			
			String sentence = sentences[(letter + 1 - 'A')];
		
			bookWriter.append(generateImageFromDatabase(generator,database, letter + "", word, bookTitle, sentence));
		
			String sentence1 = letter + " is for " + word;
			bookWriter.append("<br>");
			bookWriter.append(sentence1);
		
			bookWriter.append("<br>");
			bookWriter.append(sentence);
			bookWriter.append("<br>");
			bookWriter.append("<br>");
			bookWriter.append("<br>");
		}
		bookWriter.append("</html>");
		bookWriter.close();
	}
	
	public String getImageURL(String query){
		try {
			URL url = new URL("https://ajax.googleapis.com/ajax/services/search/images?" +
					"safe=active&v=1.0&q=" + query);
			URLConnection connection = url.openConnection();
			String line;
			StringBuilder builder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while((line = reader.readLine()) != null) {
				builder.append(line);
			}
			
			JSONObject json = new JSONObject(builder.toString());
			JSONArray results = json.getJSONObject("responseData").getJSONArray("results");
			if(results.length() == 4)
				return results.getJSONObject(Math.abs(new Random().nextInt()%4)).getString("unescapedUrl");
			//return results.getJSONObject(0).getString("unescapedUrl");
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return null;
	}
	
	private BufferedImage resizeImage(BufferedImage image){
		int imageWidth = 700;
		Image resized = image.getScaledInstance(imageWidth, image.getHeight() * imageWidth/image.getWidth(), BufferedImage.TYPE_INT_RGB);
		image = new BufferedImage(resized.getWidth(null), resized.getHeight(null), BufferedImage.TYPE_INT_RGB);
		image.createGraphics();
		image.getGraphics().drawImage(resized, 0, 0, null);
		return image;
	}
	
	private void downloadImage(String fileLocation, String imageURL){
		    URL url;
		    try {
		        url = new URL(imageURL);
		        URLConnection uc = url.openConnection();

		        uc.setRequestProperty("Accept",
		                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		        uc.setRequestProperty("Accept-Charset",
		                "ISO-8859-1,utf-8;q=0.7,*;q=0.3");
		        uc.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
		        uc.setRequestProperty("Accept-Language", "en-US,en;q=0.8");
		        uc.setRequestProperty("Connection", "keep-alive");

		        uc.setRequestProperty("Referer", imageURL);
		        //"http://www.hookerfurniture.com/index.cfm/furniture/furniture-catalog.show-product/American-furniture/3005-75310/spindle-back-side-chair---ebony.cfm");

		        InputStream is = url.openStream();
		        OutputStream os = new FileOutputStream(fileLocation);

		        byte[] b = new byte[2048];
		        int length;

		        while ((length = is.read(b)) != -1) {
		            os.write(b, 0, length);
		        }

		        is.close();
		        os.close();
		    } catch (MalformedURLException e) {
		        e.printStackTrace();
		    } catch (FileNotFoundException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
    
	}
}
