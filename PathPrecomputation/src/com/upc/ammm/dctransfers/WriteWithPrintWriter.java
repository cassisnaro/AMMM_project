package com.upc.ammm.dctransfers;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import com.upc.ammm.dctransfers.models.Graph;
import com.upc.ammm.dctransfers.models.NodePair;
import com.upc.ammm.dctransfers.models.Pair;
import com.upc.ammm.dctransfers.models.Path;

public class WriteWithPrintWriter {
	
	private final String fFilePath;
	private final Charset ENCODING = StandardCharsets.UTF_8;
	private Graph graph;
	private ArrayList<Pair> transmissions;
	
	public WriteWithPrintWriter(Graph g, ArrayList<Pair> t) throws URISyntaxException, IOException {
		this.graph = g;
		this.transmissions = t;
		
		fFilePath = "/Users/gaby/Documents/MIRI/Algorithmic Methods for Mathematical Models/project/implementation/AMMM_project/PathPrecomputation/output.txt";
		processWriteLineByLine();
	}
	
	public void processWriteLineByLine() throws IOException {
		PrecomputePathsWithTransmissions precomputer = new PrecomputePathsWithTransmissions(graph);
		
		try (PrintWriter writer = new PrintWriter(fFilePath, ENCODING.name())) {
			for (Pair t : transmissions) {
				precomputer.precumputePathsFromTransmission(t.getSource(), t.getDestination());
				
				ArrayList<Path> paths = precomputer.getPaths();
				
				writer.write(paths.size() + " ");
				writer.write("(" + t.getSource() + "," + t.getDestination() + ")\n");
				
				String pathLine;
				
				for (Path path : paths) {
					pathLine = "";
					
					for (NodePair node : path.getPath()) {
						pathLine += node.getName() + " ";
					}
					
					writer.write(path.getCost() + " " + pathLine + "\n");
				}
			} 
			
			writer.close();
		}
	}
}
