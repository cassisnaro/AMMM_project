package com.upc.ammm.dctransfers;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {

	public static void main(String... aArgs) throws IOException, URISyntaxException {
		ReadWithScanner parser = new ReadWithScanner();
		WriteWithPrintWriter writer = new WriteWithPrintWriter(parser.getGraph(), parser.getTransmissions());		
	}
}
