package org.hupo.psi.calimocho.xgmml;

import org.hupo.psi.calimocho.io.DocumentConverter;
import org.hupo.psi.calimocho.model.DocumentDefinition;
import org.hupo.psi.calimocho.tab.model.ColumnBasedDocumentDefinition;
import org.hupo.psi.calimocho.tab.util.MitabDocumentDefinitionFactory;

import java.io.*;

public class Playground {

    public static void main(String[] args) throws Exception {
//        InputStream stream = Playground.class.getResourceAsStream("/META-INF/mitab/brca2.mitab25-ia.txt");
        final InputStream stream = new FileInputStream("/home/marine/Downloads/(Q86X80_Q7.txt");
        final String outFile = "/home/marine/Downloads/(Q86X80_Q7.xgmml";

        ColumnBasedDocumentDefinition mitabDefinition = MitabDocumentDefinitionFactory.mitab25();
        DocumentDefinition definition = new XGMMLDocumentDefinition();

        OutputStream os = new FileOutputStream(outFile);

        DocumentConverter converter = new DocumentConverter( mitabDefinition, definition );
        converter.convert( stream, os );

        os.flush();
        os.close();

        /*BufferedReader in = new BufferedReader(new FileReader(outFile));
        String str;
        while ((str = in.readLine()) != null) {
            System.out.println(str);
        }
        */
        stream.close();
    }

}
