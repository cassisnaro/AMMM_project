package utils;

import java.util.Comparator;

import parameters.Path;

public class PathComparator implements Comparator<Path> {
	@Override
    public int compare(Path p1, Path p2) {
		Integer p1Size = new Integer(p1.getPath().size());
		Integer p2Size = new Integer(p2.getPath().size());
		
		return p1Size.compareTo(p2Size);
    }
}
