package gui.common;

public interface TableRecord {
    /** Creates a new instance of BasicTableRecord */
    public Object getFieldAt( int fieldIndex);
    public int getNumberOfField();
    public void setFieldAt(int fieldIndex, Object field);
    public Object getFiledbyKey(String key);
}