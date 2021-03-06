package psidev.psi.mi.calimocho.solr.converter;

import java.util.Set;
import org.apache.solr.common.SolrInputDocument;
import org.hupo.psi.calimocho.key.CalimochoKeys;
import org.hupo.psi.calimocho.model.Field;

/**
 * TODO comment this
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>29/05/12</pre>
 */

public class TextFieldConverter implements SolrFieldConverter{

    public SolrInputDocument indexFieldValues(Field field, SolrFieldName name, SolrInputDocument doc, Set<String> uniques) {

        String db = field.get(CalimochoKeys.DB);
        String value = field.get(CalimochoKeys.VALUE);
        String text = field.get(CalimochoKeys.TEXT);
        String nameField = name.toString();

        if (name.toString().equalsIgnoreCase(SolrFieldName.ftypeA.toString()) || name.toString().equalsIgnoreCase(SolrFieldName.ftypeB.toString())) { //ftypeA or ftypeB
            if (db != null && !uniques.contains(db)){
                doc.addField(nameField, db);
                doc.addField(nameField+"_s", db);
                uniques.add(db);
            }
        } else {
            if (value != null && !uniques.contains(value)) {
                doc.addField(nameField, value);
                uniques.add(value);
            }
            if (db != null && !uniques.contains(db)) {
                doc.addField(nameField, db);
                uniques.add(db);
            }
            if (db != null && value != null && !uniques.contains(db + ":" + value)) {
                doc.addField(nameField, db + ":" + value);
                doc.addField(nameField + "_s", db + ":" + value);
                uniques.add(db + ":" + value);
            }
            if (text != null && !uniques.contains(text)) {
                doc.addField(nameField, text);
                uniques.add(text);
            }
//            if (db != null && value != null && text != null && !uniques.contains(db + ":" + value + "(" + text + ")")) {
//                doc.addField(nameField, db + ":" + value + "(" + text + ")");
//                doc.addField(nameField + "_s", db + ":" + value + "(" + text + ")");
//                uniques.add(db + ":" + value + "(" + text + ")");
//            }
        }

        return doc;

    }
    
}
