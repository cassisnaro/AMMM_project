package com.upc.ammm.dctransfers.models;

/**
 * Created by daniel on 30/11/14.
 */
public class Link {
    private String nodeA;
    private String nodeB;

    public Link(String nodeA, String nodeB) {
        if (nodeA.compareTo(nodeB)<=0) {
            this.nodeA = nodeA;
            this.nodeB = nodeB;
        }
        else {
            this.nodeA = nodeB;
            this.nodeB = nodeA;
        }
    }

	public String getNodeA() {
		return nodeA;
	}

	public String getNodeB() {
		return nodeB;
	}
	
	public boolean isEqual(Link l) {
		boolean equal = false;
		
		if (nodeA.equals(l.getNodeA()) && nodeB.equals(l.getNodeB())) {
			equal = true;
		}
		
		return equal;
	}	
}
