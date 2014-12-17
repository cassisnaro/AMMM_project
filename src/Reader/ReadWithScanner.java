package Reader;

import parameters.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class ReadWithScanner {
	
	private final File fFilePath;
	private final Charset ENCODING = StandardCharsets.UTF_8; 
	private Graph graph = new Graph();
//	private ArrayList<Path> transmissions = new ArrayList<Path>();
	private RequestedTransfer requestedTransfer;
	private int numSlices;
	private TransferCollections transferCollections;

	public ReadWithScanner() throws URISyntaxException, IOException {
        File fileParent= new File(System.getProperty("user.dir"));
		fFilePath = new File(fileParent,"inputTestIntersections.txt");
		processReadLineByLine();
	}
	
	public void printGraph() {
		System.out.println("Grafo: " + graph.toString());
	}
	
	public Graph getGraph() {
		return graph;
	}
	
	public int getNumSlices(){
		return numSlices;
	}
	
	public RequestedTransfer getRequestedTransfer() {
		return requestedTransfer;
	}
	
	public TransferCollections getTransferCollections() {
		return transferCollections;
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
			
			List<Transfer> transfers = transferCollections.getTransfers();
			Set<Edge> edges = graph.getEdges();
			
			for (Edge edge : edges) {
				for (Transfer transfer : transfers) {
					if (transfer.getPath().hasEdge(edge)) {
						edge.addTransfer(transfer);
					}
				}
			}
			
			scanner.close();
		}
	}

	public void processReadLine(String lineIdentifier, String line) {
		if (lineIdentifier.equals("S")) {
			numSlices = Integer.parseInt(line);
			
			transferCollections = new TransferCollections(numSlices);
		}
		else if (lineIdentifier.equals("N")) {
			String[] nodes = line.split(" ");
			
			for (String node : nodes) {
				graph.addNode(node);
			}
		}
		else if (lineIdentifier.equals("G")) {
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
			String[] data = line.split(" ");
			boolean foundSeparator = false;
			int separatorIndex = 0;
			Transfer auxTransfer;
			int auxNodeOrigin;
			int auxNodeDestination;
			int auxTimeCompletion;
			int auxDataAmount;
			int auxMinCurrentSlices;
			int auxMaxCurrentSlices;
			int auxCurrentMaxTime;

			for (String d : data) {
				if (d.equals("/")) {
					foundSeparator = true;
					
					continue;
				}
				
				if (!foundSeparator) {
					transmissionPath.add(new NodePair(d, 0));
				}			
				else {
					break;
				}
				
				separatorIndex++;
			}
			
			auxNodeOrigin = graph.getNodeIdentifier(transmissionPath.get(0).getName());
			auxNodeDestination = graph.getNodeIdentifier(transmissionPath.get(transmissionPath.size() - 1).getName());
			auxTimeCompletion = Integer.parseInt(data[separatorIndex + 1]);
			auxDataAmount = Integer.parseInt(data[separatorIndex + 2]);
			auxMinCurrentSlices = Integer.parseInt(data[separatorIndex + 3]);
			auxMaxCurrentSlices = Integer.parseInt(data[separatorIndex + 4]);
			auxCurrentMaxTime = Integer.parseInt(data[separatorIndex + 5]);
			
			auxTransfer = new Transfer(auxNodeOrigin, auxNodeDestination, auxTimeCompletion, auxDataAmount);
			auxTransfer.setCurrentAllocation(auxMinCurrentSlices, auxMaxCurrentSlices, auxCurrentMaxTime);
			auxTransfer.setPath(new Path(transmissionPath, 0));
			
			transferCollections.add_transfer(auxTransfer);
		}
		else if (lineIdentifier.substring(0, 1).equals("R")) {			
			String[] requestedTransferInfo = line.split(" ");

			requestedTransfer = new RequestedTransfer(graph.getNodeIdentifier(requestedTransferInfo[0]), graph.getNodeIdentifier(requestedTransferInfo[1]), Integer.parseInt(requestedTransferInfo[2]), Integer.parseInt(requestedTransferInfo[3]));
		}
	}
}
