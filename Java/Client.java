public class Client {

    /**
     * This method name and parameters must remain as-is
     */
    public static int add(int lhs, int rhs) {

        RemoteMethod add = new RemoteMethod("add", new Object[]{lhs, rhs});
        return (int) makeRequest(add);

        // connect to server
        // create an instance of the RemoteMethod
        // RemoteMethod add = new RemoteMethod("add", new Objects[]{lhs, rhs});
//        RemoteMethod add = new RemoteMethod("add", new Objects[]{lhs, rhs});
        // ObjectOutputStream to serialize the add instance
        // OutputStream os = socket.getOutputStream();
//        return -1;
    }

    /**
     * This method name and parameters must remain as-is
     */
    public static int divide(int num, int denom) throws ArithmeticException {

        RemoteMethod div = new RemoteMethod("divide", new Object[]{num, denom});

        Object response = makeRequest(div);
        if (response instanceof ArithmeticException) throw (ArithmeticException) response;
        return (int) response;
    }
    /**
     * This method name and parameters must remain as-is
     */
    public static String echo(String message) {

        RemoteMethod echo = new RemoteMethod("echo", new Object[]{message});
        return (String) makeRequest(echo);

    }

    private static Object makeRequest(RemoteMethod requestMethod) {
        try (Socket sock = new Socket("localhost", PORT)) {
            // Create output stream and write the request method object to it
            OutputStream sockOut = sock.getOutputStream();
            ObjectOutputStream objOut = new ObjectOutputStream(sockOut);
            objOut.writeObject(requestMethod);

            // Create input stream and read the response object from it
            InputStream sockIn = sock.getInputStream();
            ObjectInputStream objIn = new ObjectInputStream(sockIn);
            Object response = objIn.readObject();

            // Check if the response object is an exception and throw it if so
            if (response instanceof Exception) {
                throw (Exception) response;
            }

            // Return the response object
            return response;
        } catch (ConnectException e) {
            // If a ConnectException is caught, print an error message and return it
            System.out.println("The server is down! I repeat, the server is down!!! (Can't reach localhost:" + PORT + ")");
            return e;
        } catch (Exception e) {
            // If any other exception is caught, return it
            return e;
        }
    }


    // Do not modify any code below this line
    // --------------------------------------
    String server = "localhost";
    public static final int PORT = 10314;

    public static void main(String... args) {
        // All of the code below this line must be uncommented
        // to be successfully graded.
        System.out.print("Testing... ");

        if (add(2, 4) == 6)
            System.out.print(".");
        else
            System.out.print("X");

        try {
            divide(1, 0);
            System.out.print("X");
        } catch (ArithmeticException x) {
            System.out.print(".");
        }

        if (echo("Hello") == "You said Hello!")
            System.out.print(".");
        else
            System.out.print("X");

        System.out.println(" Finished");
    }
}