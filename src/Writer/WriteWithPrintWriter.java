package Writer;

import dctransfers.PrecomputePathsWithTransmissions;
import parameters.Edge;
import parameters.Graph;
import parameters.Path;
import parameters.RequestedTransfer;
import parameters.Transfer;
import parameters.TransferCollections;

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
	private TransferCollections transferCollections;
	private RequestedTransfer requestedTransfer;
	
	public WriteWithPrintWriter(Graph g, TransferCollections tc, RequestedTransfer rt) throws URISyntaxException, IOException {
		this.graph = g;
		this.transferCollections = tc;
		this.requestedTransfer = rt;

        File fileParent= new File(System.getProperty("user.dir"));
        fFilePath = new File(fileParent,"output_test_v1.txt").getPath();
		processWriteLineByLine();
	}
	
	public void processWriteLineByLine() throws IOException {
		PrecomputePathsWithTransmissions precomputer = new PrecomputePathsWithTransmissions(graph);
		
		try (PrintWriter writer = new PrintWriter(fFilePath, ENCODING.name())) {
			writer.write("nA="+transferCollections.getRequestedTransfer().getA().size()+";\n");
			writer.write("nA0="+transferCollections.getMaxA0()+";\n");
			writer.write("nA1="+transferCollections.getMaxA1()+";\n");
			writer.write("nS="+transferCollections.getMaxFreq()+";\n");
			writer.write("nT="+transferCollections.getMaxTime()+";\n");
			writer.write("nR="+transferCollections.getTransfers().size()+";\n");
			
			ArrayList<Path> pathsForRequestedTransfer = new ArrayList<Path>();
            precomputer.precumputePathsFromTransmission(graph.getNodeNameFromIdentifier(requestedTransfer.getNode_origin()), graph.getNodeNameFromIdentifier(requestedTransfer.getNode_destination()));
            pathsForRequestedTransfer.addAll(precomputer.getPaths());
            
            Set<Edge> edges = graph.getEdges();			
			List<Edge> foundLinks = new ArrayList<Edge>();			
			Set<String> nodes = graph.getNodes();
			Edge auxPair;
			ArrayList<Path> allPaths = new ArrayList<Path>();
			int linksSize = edges.size();
			int allPathsSize = allPaths.size();
			int auxCounter1;
			int auxCounter2 = 1;
			
			writer.write("nE="+edges.size()+";\n");
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

                writer.write("]\n");
            }

            writer.write("];\n\n");
			
			writer.write("rho_re=[\n");
			
			List<Transfer> transfers = transferCollections.getTransfers();
			
			for (Transfer t : transfers) {				
				auxCounter1 = 1;
				
				writer.write("[");
				
				for (Edge e : edges) {
					if (t.getPath().hasEdge(e)) {
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
				
				writer.write("]\n");	
			}
			
			writer.write("];\n");
			
			for (String s : nodes) {
				for (String d : nodes) {
					if (!s.equals(d)) {
						auxPair = new Edge(s, d);
						
						if (!Edge.containsPair(foundLinks, auxPair)) {							
							precomputer.precumputePathsFromTransmission(s, d);						
							
							allPaths.addAll(precomputer.getPaths());
							
							foundLinks.add(new Edge(s, d));
							foundLinks.add(new Edge(d, s));
						}
					}
				}
			}
			
			transferCollections.printTransferCollectionsParameters(writer);
			
			writer.write("\n");
			
//			writer.write("paths=[\n");
//			
//			for (Path p : allPaths) {
//				auxCounter1 = 1;
//				
//				writer.write("[");
//				
//				for (Edge e : edges) {
//					if (p.hasEdge(e)) {
//						writer.write("1");
//					}
//					else {
//						writer.write("0");
//					}
//					
//					if (auxCounter1 < linksSize) {
//						writer.write(", ");
//					}
//					
//					auxCounter1++;
//				}
//				
//				writer.write("]");
//				
//				if (auxCounter2 < allPathsSize) {
//					writer.write(",\n");
//				}
//				
//				auxCounter2++;				
//			}
//			
//			writer.write("]\n\n");

			writer.close();
		}
	}
}
