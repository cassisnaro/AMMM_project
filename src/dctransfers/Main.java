package dctransfers;

import Reader.ReadWithScanner;
import Writer.WriteWithPrintWriter;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {

	public static void main(String... aArgs) throws IOException, URISyntaxException {
		long startILPconversion= System.currentTimeMillis();
		ReadWithScanner parser = new ReadWithScanner();
		WriteWithPrintWriter writer = new WriteWithPrintWriter(parser.getGraph(), parser.getTransferCollections(), parser.getRequestedTransfer());
		System.out.println("Conversion time: "+(System.currentTimeMillis()-startILPconversion));
	}
}
