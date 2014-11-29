package mypack;


import java.util.Collection;

public class Main {
    public static ConnectionPool pool = ConnectionPool.getPool();

    public static void main(String[] args) {
        final int SIZE = 10;
        QueryThread[] threads = new QueryThread[SIZE];
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < SIZE; i++) {
            while (!pool.availableConnections()) {
                try {
                    Thread.sleep(5000);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            threads[i] = new QueryThread(i, pool.getDBConnection());
            threads[i].start();
        }

        while(!allAlive(threads)){
            try {
                Thread.sleep(500);
                //System.out.print(".");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        startTime = System.currentTimeMillis() - startTime;
        System.out.println(startTime);

        /*
        final int SIZE = 18;
        long startTime;
        ConnectionPool connectionPool;
        QueryThread t[] = new QueryThread[SIZE];
        DBConnection dbc[] = new DBConnection[SIZE];


       // for (int j = 10; j < 1000; j++) {
            startTime = System.currentTimeMillis();
            for (int i = 0; i < t.length; i++){
                dbc[i] = new DBConnection();
                dbc[i].connect("postgresql","localhost","5432","products","postgres","masterkey");
                t[i] = new QueryThread(i, dbc[i]);
                t[i].start();
            }
        //}

        while(!allAlive(t)){
            try {
                Thread.sleep(500);
                System.out.print(".");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        startTime = System.currentTimeMillis() - startTime;
        System.out.println(startTime);
        */
    }


    public static boolean allAlive(QueryThread t[]){
        boolean status = true;
        for (QueryThread th : t){
            if(th.isAlive()){
                status = false;
                break;
            }
        }
        return  status;
    }

}
