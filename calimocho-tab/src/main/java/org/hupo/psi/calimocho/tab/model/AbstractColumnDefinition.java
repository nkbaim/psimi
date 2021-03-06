package org.hupo.psi.calimocho.tab.model;

import com.google.common.collect.Maps;
import org.hupo.psi.calimocho.model.AbstractDefined;
import org.hupo.psi.calimocho.tab.io.FieldFormatter;
import org.hupo.psi.calimocho.tab.io.FieldParser;

import java.util.Map;

/**
 * TODO document this !
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id$
 * @since 1.0
 */
public abstract class AbstractColumnDefinition extends AbstractDefined implements ColumnDefinition {

    private Integer position;
    private String key;
    private boolean allowsEmpty;
    private String emptyValue;
    private FieldParser fieldParser;
    private FieldFormatter fieldFormatter;
    private String fieldSeparator;
    private String fieldDelimiter;

    private Map<String,String> defaultValuesMap;

    public AbstractColumnDefinition(){
        this.allowsEmpty = true;
        defaultValuesMap = Maps.newHashMap();
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition( Integer position ) {
        this.position = position;
    }

    public String getKey() {
        return key;
    }

    public void setKey( String key ) {
        this.key = key;
    }

    public boolean isAllowsEmpty() {
        return allowsEmpty;
    }

    public void setAllowsEmpty( boolean allowsEmpty ) {
        this.allowsEmpty = allowsEmpty;
    }

    public String getEmptyValue() {
        return emptyValue;
    }

    public void setEmptyValue( String emptyValue ) {
        this.emptyValue = emptyValue;
    }

    public FieldParser getFieldParser() {
        return fieldParser;
    }

    public void setFieldParser(FieldParser fieldParser) {
        this.fieldParser = fieldParser;
    }

    public FieldFormatter getFieldFormatter() {
        return fieldFormatter;
    }

    public void setFieldFormatter(FieldFormatter fieldFormatter) {
        this.fieldFormatter = fieldFormatter;
    }

    public String getFieldSeparator() {
        return fieldSeparator;
    }

    public void setFieldSeparator( String fieldSeparator ) {
        this.fieldSeparator = fieldSeparator;
    }

    public String getFieldDelimiter() {
        return fieldDelimiter;
    }

    public boolean hasFieldDelimiter() {
        return fieldDelimiter != null;
    }

    // TODO when the need arise, it would be useful to allow setting of field cardinality (min, max)
    public boolean hasFieldSeparator() {
        return fieldSeparator != null;
    }

    public void setFieldDelimiter( String fieldDelimiter ) {
        this.fieldDelimiter = fieldDelimiter;
    }

    public Map<String, String> getDefaultValues() {
        return defaultValuesMap;
    }

    public void setDefaultValuesMap( Map<String, String> defaultValuesMap ) {
        if (defaultValuesMap != null){
            this.defaultValuesMap.clear();
            this.defaultValuesMap.putAll(defaultValuesMap);
        }
    }

    public void addDefaultValue(String key, String value) {
        getDefaultValues().put( key, value );
    }

    public String getDefaultValue(String key) {
        return getDefaultValues().get( key );
    }

    public int compareTo( ColumnDefinition cd ) {
        if (position != null) {
            return position.compareTo( cd.getPosition());
        }

        return 0;
    }
}
