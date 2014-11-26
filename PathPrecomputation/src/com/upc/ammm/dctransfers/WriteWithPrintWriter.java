package com.upc.ammm.dctransfers;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;

import com.upc.ammm.dctransfers.models.Graph;
import com.upc.ammm.dctransfers.models.Pair;

public class WriteWithPrintWriter {
	
	private final String fFilePath;
	private final Charset ENCODING = StandardCharsets.UTF_8;
	private Graph graph;
	private ArrayList<Pair> transmissions;
	
	public WriteWithPrintWriter(Graph g, ArrayList<Pair> t) throws URISyntaxException, IOException {
		this.graph = g;
		this.transmissions = t;
		
		fFilePath = "/Users/gaby/Documents/MIRI/workspace/PrecomputeNetworkPaths/output.txt";
		processWriteLineByLine();
	}
	
	public void processWriteLineByLine() throws IOException {
		PrecomputePathsWithTransmissions precomputer = new PrecomputePathsWithTransmissions(graph);
		
		try (PrintWriter writer = new PrintWriter(fFilePath, ENCODING.name())) {
			for (Pair t : transmissions) {
				precomputer.precumputePathsFromTransmission(t.getSource(), t.getDestination());
				
				ArrayList<LinkedList<String>> paths = precomputer.getPaths();
				
				writer.write(paths.size() + " ");
				writer.write("(" + t.getSource() + "," + t.getDestination() + ")\n");
				
				String pathLine;
				
				for (LinkedList<String> path : paths) {
					pathLine = "";
					
					for (String node : path) {
						pathLine += node + " ";
					}
					
					writer.write(pathLine + "\n");
				}
			} 
			
			writer.close();
		}
	}
}
