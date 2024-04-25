import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class InMemoryDB {
    private Map<String, Integer> db;
    private boolean transactionInProgress;
    private Map<String, Integer> transactionMap;

    public InMemoryDB() {
        this.db = new HashMap<>();
        this.transactionInProgress = false;
        this.transactionMap = new HashMap<>();
    }

    public Integer get(String key) {
        return db.get(key);
    }
    
    public void put(String key, int value) {
        if (transactionInProgress)
            transactionMap.put(key, value);
        else
            throw new IllegalStateException("No transaction in progress");


    }
    
    public void beginTransaction() {
        if (transactionInProgress)
            throw new IllegalStateException("Transaction already in progress");
        else{
            transactionInProgress = true;
            transactionMap = new HashMap<>(db);
        }

    }

    public void commit() {
        if (transactionInProgress) {
            db = new HashMap<>(transactionMap);
            transactionInProgress = false;            
        }
        else
            throw new IllegalStateException("No transaction in progress");

    }

    public void rollback() {
        if (transactionInProgress) {
            transactionInProgress = false;
            transactionMap.clear();            
        }
        else
            throw new IllegalStateException("No transaction in progress");

    }

    public static void main(String[] args) {
        InMemoryDB inMemoryDB = new InMemoryDB();
        Scanner scanner = new Scanner(System.in);
        String input;

        while (true) {
            System.out.println("Enter operation (begin, put, get, rollback, commit) or 'exit' to quit:");
            input = scanner.nextLine();

            switch (input) {
                case "begin":
                    try{
                        inMemoryDB.beginTransaction();
                        System.out.println("Beggining transaction");
                    }
                    catch(Exception e){
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case "put":
                    System.out.println("Enter key:");
                    String key = scanner.nextLine();
                    System.out.println("Enter value:");
                    int value = scanner.nextInt();
                    try{
                        inMemoryDB.put(key, value);
                    }
                    catch(Exception e){
                        System.out.println("Error: " + e.getMessage());
                    }
                    scanner.nextLine(); // Consume newline
                    break;
                case "get":
                    System.out.println("Enter key:");
                    key = scanner.nextLine();
                    System.out.println(inMemoryDB.get(key));
                    break;
                case "rollback":
                    try{
                        inMemoryDB.rollback();
                        System.out.println("Transaction rolled back.");
                    }
                    catch(Exception e){
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case "commit":
                    try{
                        inMemoryDB.commit();
                        System.out.println("Transaction committed.");
                    }
                    catch(Exception e){
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case "exit":
                    System.out.println("Exiting program.");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid operation. Please try again.");
                    break;
            }
        }
    }
}