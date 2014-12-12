package com.upc.ammm.dctransfers;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.upc.ammm.dctransfers.models.Graph;
import com.upc.ammm.dctransfers.models.Link;
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

        File fileParent= new File(System.getProperty("user.dir"));
        fFilePath = new File(fileParent,"output.txt").getPath();
		processWriteLineByLine();
	}
	
	public void processWriteLineByLine() throws IOException {
		PrecomputePathsWithTransmissions precomputer = new PrecomputePathsWithTransmissions(graph);
		
		try (PrintWriter writer = new PrintWriter(fFilePath, ENCODING.name())) {
			List<Pair> foundPaths = new ArrayList<Pair>();			
			Set<String> nodes = graph.getNodes();
			Pair auxPair;
			ArrayList<Path> paths;
			ArrayList<Path> allPaths = new ArrayList<Path>();
			String pathLine;
			
			for (String s : nodes) {
				for (String d : nodes) {
					if (!s.equals(d)) {
						auxPair = new Pair(s, d);

						System.out.println("FOUND PATHS: ");
						
						for (Pair pair : foundPaths) {
							System.out.print("(" + pair.getSource() + ", " + pair.getDestination() + ") ");
						}
						
						System.out.print("\n");
						
						if (!Pair.containsPair(foundPaths, auxPair)) {
							System.out.println("COMPUTING PATHS BETWEEN (" + s + ", " + d + ")");
							
							precomputer.precumputePathsFromTransmission(s, d);
							
							paths = precomputer.getPaths();
							
							System.out.println("------------ PATHS BETWEEN (" + s + ", " + d + ") OR (" + d + ", " + s + ") ------------");
							System.out.println("------------ TOTAL: " + paths.size());
							
							for (Path p : paths) {
								pathLine = "";
								
								for (NodePair node : p.getPath()) {
									pathLine += node.getName() + " ";
								}
								
								System.out.println(pathLine);								
							}
							
							allPaths.addAll(precomputer.getPaths());
							
							foundPaths.add(new Pair(s, d));
							foundPaths.add(new Pair(d, s));
						}
					}
				}
			}
			
			System.out.println("TOTAL PATHS IN WHOLE GRAPH: " + allPaths.size());
			
			Set<Link> links = graph.getLinks();
			int linksSize = links.size();
			int allPathsSize = allPaths.size();
			int auxCounter1;
			int auxCounter2 = 1;
			
			writer.write("paths=[\n");
			
			for (Path p : allPaths) {
				auxCounter1 = 1;
				
				writer.write("[");
				
				for (Link l : links) {
					if (p.hasLink(l)) {
						writer.write("1");
					}
					else {
						writer.write("0");
					}
					
					if (auxCounter1 < linksSize) {
						writer.write(", ");
					}
					
					auxCounter1++;
				}
				
				writer.write("]");
				
				if (auxCounter2 < allPathsSize) {
					writer.write(",\n");
				}
				
				auxCounter2++;				
			}
			
			writer.write("]\n");
			
//			for (Pair t : transmissions) {
//				precomputer.precumputePathsFromTransmission(t.getSource(), t.getDestination());
//				
//				ArrayList<Path> paths = precomputer.getPaths();
//				
//				writer.write(paths.size() + " ");
//				writer.write("(" + t.getSource() + "," + t.getDestination() + ")\n");
//				
//				String pathLine;
//				
//				for (Path path : paths) {
//					pathLine = "";
//					
//					for (NodePair node : path.getPath()) {
//						pathLine += node.getName() + " ";
//					}
//					
//					writer.write(path.getCost() + " " + pathLine + "\n");
//				}
//			} 
//			
//			writer.write("paths=[");
//			
//			Map<Link, Integer> identifiers;
//			Set<Link> links;
//			ArrayList<Path> paths;
//			
//			for (Pair t : transmissions) {
//				writer.write("[");
//				
//				precomputer.precumputePathsFromTransmission(t.getSource(), t.getDestination());
//				
//				paths = precomputer.getPaths();
//				
//				identifiers = graph.getLinksIdentifier();
//				links = identifiers.keySet();				
//				
//				for (Path path : paths) {
//					System.out.println(path.toString());
//					
//					writer.write("[");				
//					
//					for (Link l : links) {
//						if (path.hasLink(l)) {
//							writer.write("1, ");
//						}
//						else {
//							writer.write("0, ");
//						}
//					}
//					
//					writer.write("], \n");
//				}
//				
//				writer.write("], \n");
//			}
//			
//			writer.write("]\n");

			writer.close();
		}
	}
}
