package Reader;

import parameters.Graph;
import parameters.NodePair;
import parameters.Path;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class ReadWithScanner {
	
	private final File fFilePath;
	private final Charset ENCODING = StandardCharsets.UTF_8; 
	private Graph graph = new Graph();
	private ArrayList<Path> transmissions = new ArrayList<Path>();
	private Path requestedTransmission;

	public ReadWithScanner() throws URISyntaxException, IOException {
        File fileParent= new File(System.getProperty("user.dir"));
		fFilePath = new File(fileParent,"input_gaby_v1.txt");
		processReadLineByLine();
	}
	
	public void printGraph() {
		System.out.println("Grafo: " + graph.toString());
	}
	
	public Graph getGraph() {
		return graph;
	}
	
	public ArrayList<Path> getTransmissions() {
		return transmissions;
	}
	
	public Path getRequestedTransmission() {
		return requestedTransmission;
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
		if (lineIdentifier.equals("G")) {
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
		else if (lineIdentifier.substring(0, 1).equals("T")) {			
			LinkedList<NodePair> transmissionPath = new LinkedList<NodePair>();
			String[] nodes = line.split(" ");

			for (String node : nodes) {				
				transmissionPath.add(new NodePair(node, 0));
			}
			
			transmissions.add(new Path(transmissionPath, 0));
		}
		else if (lineIdentifier.substring(0, 1).equals("R")) {			
			LinkedList<NodePair> transmissionPath = new LinkedList<NodePair>();
			String[] nodes = line.split(" ");

			for (String node : nodes) {				
				transmissionPath.add(new NodePair(node, 0));
			}
			
			requestedTransmission = new Path(transmissionPath, 0);
		}
	}
}

