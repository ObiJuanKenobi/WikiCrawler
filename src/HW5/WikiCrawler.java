package HW5;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WikiCrawler {
	private int maxPages;
	private File graphFile;
	private File htmlFile;
	static final String BASE_URL = "https://en.wikipedia.org";

	public WikiCrawler(String seedURL, int max, String fileName) throws IOException {
		graphFile = new File(fileName);
		htmlFile = new File("html.txt");
		maxPages = max;
		downloadHtmlFile(seedURL);
	}

	public ArrayList<String> extractLinks(String doc) throws IOException {
		File f = new File(doc);
		ArrayList<String> links = new ArrayList<String>();
		Scanner scan = new Scanner(f);

		String firstOccurence = findFirstOccurrence(scan);

		if (firstOccurence != null) {
			analyze(links, scan, firstOccurence);
		} else {
			System.err.println("Could not find <p> or <P>");
			System.exit(-1);
		}

		return links;
	}

	public void crawl() {
		
	}
	
	private void BFS(String seedURL) throws IOException{
		Queue<String> q = new LinkedList<String>();
		ArrayList<String> found = new ArrayList<String>();
		ArrayList<String> extractedLinks;
		q.add(seedURL);
		found.add(seedURL);
		
		while(!(q.isEmpty())){
			String currentPage = (String) q.remove();
			downloadHtmlFile(currentPage);
			extractedLinks = extractLinks("html.txt");
			
			
			
			for(int i = 0; i < extractedLinks.size(); i++){
				if(!(found.contains(extractedLinks.get(i)))){
					q.add(extractedLinks.get(i));
					found.add(extractedLinks.get(i));
				}
			}
		}
		
	}
	
	private File downloadHtmlFile(String seedURL) throws IOException{
		URL url = new URL(BASE_URL + seedURL);
		InputStream is = url.openStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		PrintWriter p = new PrintWriter(htmlFile);
		String line;
		
		while((line = br.readLine()) != null){
			p.write(line);
		}
		
		return htmlFile;
	}

	private String findFirstOccurrence(Scanner s) {
		while (s.hasNextLine()) {
			String line = s.nextLine();
			if (line.contains("<p>") || line.contains("<P>")) {
				return line;
			}

		}
		return null;
	}

	private void analyze(ArrayList<String> arr, Scanner s, String startingLine) {
		String validLink = validate(startingLine);

		if (validLink != null) {
			arr.add(validLink);
		}

		while (s.hasNextLine()) {
			String line = s.nextLine();
			validLink = validate(line);

			if (validLink != null) {
				arr.add(line);
			}

		}
	}

	private String validate(String line) {
		Matcher mLink;
		Pattern pLink = Pattern.compile("\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))");

		mLink = pLink.matcher(line);

		while (mLink.find()) {
			String link = mLink.group(1);

			if (link.contains("/wiki/") && !(link.contains("#") || link.contains(":"))) {
				System.out.println(link);
				return link;
			}
		}

		return null;
	}

}
