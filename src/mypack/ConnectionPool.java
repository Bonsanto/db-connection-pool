package mypack;

import org.omg.SendingContext.RunTime;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Vector;

/**
 * Created by Bonsanto on 9/9/2014.
 */
public class ConnectionPool {
    private ArrayList<DBConnection> free;
    private ArrayList<DBConnection> used;
    private String driver, db,  usr, pw, url;
    private int minsize, maxsize, stepsize;

    private static ConnectionPool pool = null;

    private ConnectionPool(){

        try {
            ResourceBundle rb = ResourceBundle.getBundle("mypack.connectionpool");
            driver = rb.getString("driver");
            url = rb.getString("url");
            db = rb.getString("db");
            usr = rb.getString("usr");
            pw = rb.getString("pw");
            minsize = Integer.parseInt(rb.getString("minsize"));
            maxsize = Integer.parseInt(rb.getString("maxsize"));
            stepsize = Integer.parseInt(rb.getString("stepsize"));

            free = new ArrayList<>();
            used = new ArrayList<>();
            instantiate(minsize);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void instantiate(int size){
        try{
            DBConnection con;

            for (int i = 0; i < size; i++) {
                con = new DBConnection();
                con.connect(driver, url, db, usr, pw);
                free.add(con);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized boolean createMoreConnections(){
        int current = free.size() - used.size();
        int n = Math.min(maxsize - current, stepsize); //We find the smaller number

        if (n > 0){
            System.out.println("Creating " + n + " new connections...");
            instantiate(n); //Interesting way to instantiate more connections
        }
        return n > 0;
    }

    public synchronized boolean availableConnections(){
        return (free.size() > 0 || maxsize > used.size());
    }

    public String toString(){
        return "Free connections: " + free.size() +
                ", used connections: " + used.size();
    }

    public synchronized static ConnectionPool getPool(){
        pool = (pool == null) ? new ConnectionPool() : pool;
        return pool;
    }

    public synchronized DBConnection getDBConnection(){
        if(free.size() == 0){ //If there aren't an free connections
            if(!createMoreConnections()){
               throw new RuntimeException("Not enough connections");
            }
        }
        DBConnection dbc = free.remove(0);
        used.add(dbc);
        System.out.println(toString());
        return dbc;
    }

    public synchronized void releaseConnection(DBConnection dbc){
        boolean ok = used.remove(dbc);

        if(ok){
            if (free.size() >= minsize){
                dbc.close();
                System.out.println("Deleting 1 connection...");
            } else {
                free.add(dbc);
            }
            System.out.println(toString());
        } else {
            throw new RuntimeException("It returns back a connection that is not mine...");
        }
    }

    public synchronized void close(){
        try{
            for(DBConnection dbc : free)
                dbc.close();
            for(DBConnection dbc : used)
                dbc.close();
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
