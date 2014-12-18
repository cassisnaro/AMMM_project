package parameters;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import utils.PathComparator;

public class Path {
	private LinkedList<NodePair> path;
	private int cost;
	
	public Path() {
		
	}
	
	public Path(LinkedList<NodePair> path, int cost) {
		this.path = path;
		this.cost = cost;
	}

	public LinkedList<NodePair> getPath() {
		return path;
	}

	public void setPath(LinkedList<NodePair> path) {
		this.path = path;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}
	
	public List<Edge> getEdges() {
		List<Edge> edges = new ArrayList<Edge>();
		Edge auxEdge;
		int pathSize = path.size();
		
		for (int i = 1; i < pathSize; i++) {
			auxEdge = new Edge(path.get(i-1).getName(), path.get(i).getName());
			edges.add(auxEdge);
		}
		
		return edges;
	}
 	
	public boolean hasEdge(Edge e) {
		boolean hasLink = false;
		int pathSize = path.size();
		String previousNode = path.get(0).getName(); 	
		
		for (int i = 1; i < pathSize; i++) {
//			System.out.println("COMPARING: (" + previousNode + "," + path.get(i).getName() + ") WITH (" + e.getSource() + "," + e.getDestination() + ")");
			if (e.isEqual(new Edge(previousNode, path.get(i).getName()))) {
//				System.out.println("IS EQUAL!");
				hasLink = true;
				break;
			}
			
			previousNode = path.get(i).getName();
		}
		
		return hasLink;
	}
	 
	@Override
	public String toString() {
		String pathString = "[";
		int pathSize = path.size();
		int last = pathSize - 1;
		
		for (int i = 0; i < pathSize; i++) {
			pathString += path.get(i).getName();
			
			if (i != last) {
				pathString += ", ";
			}
		}
		
		pathString += "]";
		
		return pathString;
	} 
	
	public static List<Path> getPathsExamples() {
		List<Path> paths = new ArrayList<Path>();
		
		LinkedList<NodePair> auxNodePairList;
		Path auxPath;
		NodePair auxNodePair;
		
		// Path #1
		auxNodePairList = new LinkedList<NodePair>();
		
		auxNodePair = new NodePair("1", 0);
		auxNodePairList.add(auxNodePair);
		
		auxNodePair = new NodePair("2", 0);
		auxNodePairList.add(auxNodePair);
		
		auxNodePair = new NodePair("3", 0);
		auxNodePairList.add(auxNodePair);
		
		auxNodePair = new NodePair("4", 0);
		auxNodePairList.add(auxNodePair);
		
		auxPath = new Path(auxNodePairList, 0);
		paths.add(auxPath);
		
		// Path #2
		auxNodePairList = new LinkedList<NodePair>();
		
		auxNodePair = new NodePair("1", 0);
		auxNodePairList.add(auxNodePair);
		
		auxNodePair = new NodePair("2", 0);
		auxNodePairList.add(auxNodePair);
		
		auxNodePair = new NodePair("3", 0);
		auxNodePairList.add(auxNodePair);
		
		auxPath = new Path(auxNodePairList, 0);
		paths.add(auxPath);
		
		// Path #3
		auxNodePairList = new LinkedList<NodePair>();
		
		auxNodePair = new NodePair("1", 0);
		auxNodePairList.add(auxNodePair);
		
		auxNodePair = new NodePair("2", 0);
		auxNodePairList.add(auxNodePair);
		
		auxNodePair = new NodePair("3", 0);
		auxNodePairList.add(auxNodePair);
		
		auxNodePair = new NodePair("4", 0);
		auxNodePairList.add(auxNodePair);
		
		auxNodePair = new NodePair("5", 0);
		auxNodePairList.add(auxNodePair);
		
		auxNodePair = new NodePair("6", 0);
		auxNodePairList.add(auxNodePair);
		
		auxNodePair = new NodePair("7", 0);
		auxNodePairList.add(auxNodePair);
		
		auxNodePair = new NodePair("8", 0);
		auxNodePairList.add(auxNodePair);
		
		auxNodePair = new NodePair("9", 0);
		auxNodePairList.add(auxNodePair);
		
		auxNodePair = new NodePair("10", 0);
		auxNodePairList.add(auxNodePair);
		
		auxPath = new Path(auxNodePairList, 0);
		paths.add(auxPath);
		
		// Path #4
		auxNodePairList = new LinkedList<NodePair>();
		
		auxNodePair = new NodePair("1", 0);
		auxNodePairList.add(auxNodePair);
		
		auxNodePair = new NodePair("2", 0);
		auxNodePairList.add(auxNodePair);
		
		auxNodePair = new NodePair("3", 0);
		auxNodePairList.add(auxNodePair);
		
		auxPath = new Path(auxNodePairList, 0);
		paths.add(auxPath);
		
		// Path #5
		auxNodePairList = new LinkedList<NodePair>();
		
		auxNodePair = new NodePair("1", 0);
		auxNodePairList.add(auxNodePair);
		
		auxNodePair = new NodePair("2", 0);
		auxNodePairList.add(auxNodePair);
		
		auxNodePair = new NodePair("3", 0);
		auxNodePairList.add(auxNodePair);
		
		auxNodePair = new NodePair("4", 0);
		auxNodePairList.add(auxNodePair);
		
		auxNodePair = new NodePair("5", 0);
		auxNodePairList.add(auxNodePair);
		
		auxNodePair = new NodePair("6", 0);
		auxNodePairList.add(auxNodePair);
		
		auxNodePair = new NodePair("7", 0);
		auxNodePairList.add(auxNodePair);
		
		auxPath = new Path(auxNodePairList, 0);
		paths.add(auxPath);
		
		// Path #6
		auxNodePairList = new LinkedList<NodePair>();
		
		auxNodePair = new NodePair("1", 0);
		auxNodePairList.add(auxNodePair);
		
		auxPath = new Path(auxNodePairList, 0);
		paths.add(auxPath);
		
		return paths;
	}
	
//	static Function<Path, Integer> numberOfHops = new Function<Path, Integer>() {
//	    @Override
//	    public Integer apply(Path p) {
//	        return p.getPath().size();
//	    }
//	};
//	
//	public static List<Path> getRCLPaths(List<Path> paths) {
//		double alpha = 0.3;
//		
//		List<Path> sortedPaths = new ArrayList<Path>(paths);
//		sortedPaths.sort(new PathComparator());
//		
//		System.out.println("################## SORTED PATHS");
//		for (Path p : sortedPaths) {
//			System.out.println(p.toString());
//		}
//		
//		int qmin = sortedPaths.get(0).getPath().size();
//		int qmax = sortedPaths.get(sortedPaths.size() - 1).getPath().size();
//		
//		int costFunctionValue = (int) Math.ceil((double)qmin + alpha * ((double)qmax - (double)qmin)); 
//		
//		Predicate<Path> rclPathsPredicate = Predicates.compose(Predicates.equalTo(costFunctionValue), numberOfHops);
//		
//		Iterable<Path> itRCLPaths = Iterables.filter(sortedPaths, rclPathsPredicate);
//		
//		List<Path> rclPaths = Lists.newArrayList(itRCLPaths);
//		
//		System.out.println("################## RCL PATHS");
//		for (Path p : rclPaths) {
//			System.out.println(p.toString());
//		}
//		
//		System.out.println(rclPaths.size());
//		
//		return rclPaths;
//	}
	
	public static int costFunctionValue = -1;
	
	static Function<Path, Boolean> numberOfHops = new Function<Path, Boolean>() {
	    @Override
	    public Boolean apply(Path p) {
	        return p.getPath().size() <= costFunctionValue;
	    }
	};
	
	public static List<Path> getRCLPaths(List<Path> paths) {
		double alpha = 0.3;
		
		List<Path> sortedPaths = new ArrayList<Path>(paths);
		sortedPaths.sort(new PathComparator());
		
//		System.out.println("################## SORTED PATHS");
//		for (Path p : sortedPaths) {
//			System.out.println(p.toString());
//		}
		
		int qmin = sortedPaths.get(0).getPath().size();
		int qmax = sortedPaths.get(sortedPaths.size() - 1).getPath().size();	
		
		costFunctionValue = (int) Math.ceil((double)qmin + alpha * ((double)qmax - (double)qmin));
		
		Predicate<Path> rclPathsPredicate = Predicates.compose(Predicates.equalTo(true), numberOfHops);
		
		Iterable<Path> itRCLPaths = Iterables.filter(sortedPaths, rclPathsPredicate);
		
		List<Path> rclPaths = Lists.newArrayList(itRCLPaths);
		
		return rclPaths;
	}
	
	public static Path getRandomPath(List<Path> paths) {
		Random random = new Random();
		int randomPos;
		
		if (paths.size() != 1) {
			randomPos = random.nextInt(paths.size() - 1);
		}
		else {
			randomPos = 0;
		}
		
		return paths.get(randomPos);
	}
	
	public static void main(String[] args) {
		List<Path> pathss = getPathsExamples();
		List<Path> rclPaths;
		Path auxPath;
		
		while (pathss.size() != 0) {
			rclPaths = getRCLPaths(pathss);
			auxPath = getRandomPath(rclPaths);					
			pathss.remove(auxPath);
		}
	}
}
