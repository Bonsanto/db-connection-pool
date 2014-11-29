package mypack;
import java.sql.*;

/**
 * Created by Alberto Bonsanto on 17/07/2014.
 */
public class DBConnection {
    private Connection con = null;
    private Statement stmt = null;

    public void connect(String sgdb, String ip, String port, String db, String user, String key) {
        String sb = "jdbc:" + sgdb + "://" + ip + ":" + port + "/" + db;

        try {
            Class.forName ("org.postgresql.Driver");
            con = DriverManager.getConnection(sb, user, key);
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void connect(String driver, String url, String db, String user, String key) {

        try {
            Class.forName (driver);
            con = DriverManager.getConnection(url + db, user, key);
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet open(String query) throws NullPointerException{
        ResultSet ret = null;

        try {
            stmt.executeQuery(query);
            ret = stmt.getResultSet();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public void exec(String query){
        try {
            stmt.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public JSON getAsJSON(String query, String na){
        ResultSet rs = open(query);
        ResultSetMetaData rsmd;
        JSON[] row;
        JSON result = new JSON();
        int rowsNumber;
        int columnNumber;
        int type;
        int[] columnType;
        String name;
        String[] columnName;

        try {
            rs.last();
            rowsNumber = rs.getRow();
            rs.beforeFirst();
            rsmd = rs.getMetaData();
            columnNumber = rsmd.getColumnCount();
            columnType = new int[columnNumber];
            columnName = new String[columnNumber];
            row = new JSON[rowsNumber];

            for (int i = 0; i < columnNumber; i++) {
                columnType[i] = rsmd.getColumnType(i + 1);
                columnName[i] = rsmd.getColumnName(i + 1);
            }

            //TODO: FIX THE CASE WHEN BLOB.
            for (int j = 0; j < rowsNumber; j++) {
                rs.next();
                row[j] = new JSON();
                for (int i = 1; i <= columnNumber; i++) {
                    type = columnType[i - 1];
                    name = columnName[i - 1];
                    switch (type) {
                        case Types.FLOAT:
                            row[j].addAttribute(name, rs.getFloat(i));
                            break;
                        case Types.INTEGER:
                            row[j].addAttribute(name, rs.getInt(i));
                            break;
                        case Types.DOUBLE:
                            row[j].addAttribute(name, rs.getDouble(i));
                            break;
                        default:
                            row[j].addAttribute(name, rs.getString(i));
                            break;
                    }
                }
                row[j].build();
            }
            result.addAttribute(na, row);
            result.build();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public JSON getAsJSON(String query){
        ResultSet rs = open(query);
        ResultSetMetaData rsmd;
        JSON[] row;
        JSON result = new JSON();
        int rowsNumber;
        int columnNumber;
        int type;
        int[] columnType;
        String name;
        String[] columnName;

        try {
            rs.last();
            rowsNumber = rs.getRow();
            rs.beforeFirst();
            rsmd = rs.getMetaData();
            columnNumber = rsmd.getColumnCount();
            columnType = new int[columnNumber];
            columnName = new String[columnNumber];
            row = new JSON[rowsNumber];

            for (int i = 0; i < columnNumber; i++) {
                columnType[i] = rsmd.getColumnType(i + 1);
                columnName[i] = rsmd.getColumnName(i + 1);
            }

            //TODO: FIX THE CASE WHEN BLOB.
            for (int j = 0; j < rowsNumber; j++) {
                rs.next();
                row[j] = new JSON();
                for (int i = 1; i <= columnNumber; i++) {
                    type = columnType[i - 1];
                    name = columnName[i - 1];
                    switch (type) {
                        case Types.FLOAT:
                            row[j].addAttribute(name, rs.getFloat(i));
                            break;
                        case Types.INTEGER:
                            row[j].addAttribute(name, rs.getInt(i));
                            break;
                        case Types.DOUBLE:
                            row[j].addAttribute(name, rs.getDouble(i));
                            break;
                        default:
                            row[j].addAttribute(name, rs.getString(i));
                            break;
                    }
                }
                row[j].build();
            }
            result.addAttribute("rs", row);
            result.build();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
