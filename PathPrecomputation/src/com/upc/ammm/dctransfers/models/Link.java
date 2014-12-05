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
        } else {
            this.nodeA = nodeB;
            this.nodeB = nodeA;
        }
    }
}
