package org.hupo.psi.calimocho.tab.io.formatter;

import org.hupo.psi.calimocho.io.IllegalFieldException;
import org.hupo.psi.calimocho.key.CalimochoKeys;
import org.hupo.psi.calimocho.model.Field;
import org.hupo.psi.calimocho.model.Row;
import org.hupo.psi.calimocho.tab.io.FieldFormatter;

/**
 * TODO document this !
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id$
 * @since 1.0
 */
public class BooleanFieldFormatter implements FieldFormatter {

    public String format( Field field ) throws IllegalFieldException {

        String value = field.get( CalimochoKeys.VALUE );

        try {
            Boolean.parseBoolean( value );
        } catch ( Exception e ) {
            throw new IllegalFieldException( "Boolean expected, found: "+value );
        }

        return value;
    }

    public String format( Field field, Row row ) throws IllegalFieldException {
        return format(field);
    }
}
