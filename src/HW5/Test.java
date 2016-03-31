package HW5;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
	static final String BASE_URL = "https://en.wikipedia.org";
	

	public static void main(String[] args) throws IOException {
		System.out.println(extractLinks("sample.txt"));
		
		URL url = new URL(BASE_URL+"/wiki/physics");
		InputStream is = url.openStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		
		int i = 10;
		while(i > 0){
			System.out.println(br.readLine());
			i--;
		}
		
	
		
	}

	public static ArrayList<String> extractLinks(String doc) throws IOException {
		File f = new File(doc);
		File toAnalyze = null;
		ArrayList<String> links = new ArrayList<String>();
		Scanner scan = new Scanner(f);
		

		// move the scanner to the first occurrence of <p>
		String firstOccurence = findFirstOccurrence(scan);
		
		
		if(firstOccurence != null){
			analyze(scan, links, firstOccurence);
		}
		

		return links;
	}

	private static String findFirstOccurrence(Scanner s) {
		
		while (s.hasNextLine()){
			String line = s.nextLine();
			if(line.contains("<p>") || line.contains("<P>")){
				return line;
			}
					
				
			}
		return null;
		}
	
	
	private static void analyze(Scanner scanner, ArrayList<String> arr, String firstLine){
		Matcher mTag, mLink;
		Pattern pTag = Pattern.compile("(?i)<a([^>]+)>(.+?)</a>");
		Pattern pLink = Pattern .compile("\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))");
		
		mTag = pTag.matcher(firstLine);
		while(mTag.find()){
			String href = mTag.group(1);
			
			System.out.println(href);
			
			mLink = pLink.matcher(href);
			
			while(mLink.find()){
				String link = mLink.group(1);
				
				if(link.contains("/wiki/") && !(link.contains("#") || link.contains(":"))){
					arr.add(link);
					
					System.out.println(link);
				}
			}
		}
		
		while(scanner.hasNextLine()){
			String line = scanner.nextLine();
	
			mTag  = pTag.matcher(line);
			mLink = pLink.matcher(line);
			
				while(mLink.find()){
					String link = mLink.group(1);
					
					if(link.contains("/wiki/") && !(link.contains("#") || link.contains(":"))){
						arr.add(link);
						
						System.out.println(link);
					}
				}
			}
			
			
		}

	}
	

