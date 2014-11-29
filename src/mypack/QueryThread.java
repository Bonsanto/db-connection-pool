package mypack;

import java.sql.ResultSet;

public class QueryThread extends Thread {
    private String id;
    private String query = "Select * from product"; //where id_product < 100000";
    private DBConnection dbc;

    public QueryThread(int id, DBConnection dbc) {
        this.id = String.valueOf(id);
        this.dbc = dbc;
    }

    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                ResultSet rs = dbc.open(query);
                System.out.println("Thread " + id + " executed the query for " + i + " times");
            }
            Main.pool.releaseConnection(dbc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DBConnection releaseDBConnection() {
        return dbc;
    }
}
