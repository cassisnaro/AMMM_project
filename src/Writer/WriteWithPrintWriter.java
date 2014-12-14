package Writer;

import dctransfers.PrecomputePathsWithTransmissions;
import parameters.Edge;
import parameters.Graph;
import parameters.Path;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WriteWithPrintWriter {
	
	private final String fFilePath;
	private final Charset ENCODING = StandardCharsets.UTF_8;
	private Graph graph;
	private ArrayList<Path> transmissions;
	
	public WriteWithPrintWriter(Graph g, ArrayList<Path> t) throws URISyntaxException, IOException {
		this.graph = g;
		this.transmissions = t;

        File fileParent= new File(System.getProperty("user.dir"));
        fFilePath = new File(fileParent,"output.txt").getPath();
		processWriteLineByLine();
	}
	
	public void processWriteLineByLine() throws IOException {
		PrecomputePathsWithTransmissions precomputer = new PrecomputePathsWithTransmissions(graph);
		
		try (PrintWriter writer = new PrintWriter(fFilePath, ENCODING.name())) {
			List<Edge> foundLinks = new ArrayList<Edge>();			
			Set<String> nodes = graph.getNodes();
			Edge auxPair;
//			ArrayList<Path> paths;
			ArrayList<Path> allPaths = new ArrayList<Path>();
//			String pathLine;
			
			for (String s : nodes) {
				for (String d : nodes) {
					if (!s.equals(d)) {
						auxPair = new Edge(s, d);

//						System.out.println("FOUND PATHS: ");
//						
//						for (Edge pair : foundLinks) {
//							System.out.print("(" + pair.getSource() + ", " + pair.getDestination() + ") ");
//						}
//						
//						System.out.print("\n");
						
						if (!Edge.containsPair(foundLinks, auxPair)) {
//							System.out.println("COMPUTING PATHS BETWEEN (" + s + ", " + d + ")");
							
							precomputer.precumputePathsFromTransmission(s, d);
							
//							paths = precomputer.getPaths();
//							
//							System.out.println("------------ PATHS BETWEEN (" + s + ", " + d + ") OR (" + d + ", " + s + ") ------------");
//							System.out.println("------------ TOTAL: " + paths.size());
//							
//							for (Path p : paths) {
//								pathLine = "";
//								
//								for (NodePair node : p.getPath()) {
//									pathLine += node.getName() + " ";
//								}
//								
//								System.out.println(pathLine);								
//							}
							
							allPaths.addAll(precomputer.getPaths());
							
							foundLinks.add(new Edge(s, d));
							foundLinks.add(new Edge(d, s));
						}
					}
				}
			}
			
//			System.out.println("TOTAL PATHS IN WHOLE GRAPH: " + allPaths.size());
			
			Set<Edge> edges = graph.getEdges();
			int linksSize = edges.size();
			int allPathsSize = allPaths.size();
			int auxCounter1;
			int auxCounter2 = 1;
			
			writer.write("paths=[\n");
			
			for (Path p : allPaths) {				
				auxCounter1 = 1;
				
				writer.write("[");
				
				for (Edge e : edges) {
					if (p.hasEdge(e)) {
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
			
			writer.write("]\n\n");

            String requestedTransferStart=new String("a");
            String requestedTransferEnd=new String("d");
            ArrayList<Path> pathsForRequestedTransfer = new ArrayList<Path>();
            precomputer.precumputePathsFromTransmission(requestedTransferStart,requestedTransferEnd);
            pathsForRequestedTransfer.addAll(precomputer.getPaths());


            edges = graph.getEdges();
            linksSize = edges.size();
            allPathsSize = allPaths.size();
            auxCounter2 = 1;

            writer.write("nP="+pathsForRequestedTransfer.size()+";\n");
            writer.write("rho_pe=[\n");

            for (Path p : pathsForRequestedTransfer) {
                auxCounter1 = 1;

                writer.write("[");

                for (Edge e : edges) {
                    if (p.hasEdge(e)) {
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

                if (auxCounter2 < pathsForRequestedTransfer.size()) {
                    writer.write(",\n");
                }

                auxCounter2++;
            }

            writer.write("];\n\n");
			
			auxCounter2 = 1;
			
			writer.write("rho_re=[\n");
			
			for (Path t : transmissions) {				
				auxCounter1 = 1;
				
				writer.write("[");
				
				for (Edge e : edges) {
					if (t.hasEdge(e)) {
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
			
			writer.write("];\n");

            writer.write("nE="+edges.size()+";");
			
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
