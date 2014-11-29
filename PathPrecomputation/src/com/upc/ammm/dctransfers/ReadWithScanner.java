package com.upc.ammm.dctransfers;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

import com.upc.ammm.dctransfers.models.Graph;
import com.upc.ammm.dctransfers.models.Pair;

public class ReadWithScanner {
	
	private final File fFilePath;
	private final Charset ENCODING = StandardCharsets.UTF_8; 
	private Graph graph = new Graph();
	private ArrayList<Pair> transmissions = new ArrayList<Pair>();	

	public ReadWithScanner() throws URISyntaxException, IOException {
		fFilePath = new File("/Users/gaby/Documents/MIRI/Algorithmic Methods for Mathematical Models/project/implementation/AMMM_project/PathPrecomputation/input.txt");
		processReadLineByLine();
	}
	
	public void printGraph() {
		System.out.println("Grafo: " + graph.toString());
	}
	
	public Graph getGraph() {
		return graph;
	}
	
	public ArrayList<Pair> getTransmissions() {
		return transmissions;
	}

	public void processReadLineByLine() throws IOException {
		String lineIdentifier;
		String line;
		
		try (Scanner scanner = new Scanner(fFilePath, ENCODING.name())) {
			while (scanner.hasNextLine()) {
				lineIdentifier = scanner.nextLine();
				line = scanner.nextLine();
				processReadLine(lineIdentifier, line);
			}
			
			scanner.close();
		}
	}

	public void processReadLine(String lineIdentifier, String line) {
		if (lineIdentifier.equals("L")) {
			String[] links = line.split(" ");
			String n1;
			String n2;
			int cost;

			for (String l : links) {
				n1 = l.substring(1, 2);
				n2 = l.substring(3, 4);
				cost = Integer.parseInt(l.substring(5, 6));
				
				graph.addTwoWayVertex(n1, cost, n2, cost);
			}
		}
		else if (lineIdentifier.equals("LT")) {
			String[] links = line.split(" ");
			String n1;
			String n2;

			for (String l : links) {
				n1 = l.substring(1, 2);
				n2 = l.substring(3, 4);
				
				transmissions.add(new Pair(n1, n2));
			}
		}
	}
}

