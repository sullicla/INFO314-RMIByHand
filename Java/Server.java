import java.net.ServerSocket;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newCachedThreadPool();

        try ( ServerSocket server = new ServerSocket(10314) ) {
            while (true) {
                Socket sock = server.accept();
                threadPool.execute(() -> respond(sock));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void respond(Socket sock) {
        try {
            InputStream request = sock.getInputStream();
            ObjectInputStream objInStream = new ObjectInputStream(request);
            RemoteMethod requestMethod = (RemoteMethod) objInStream.readObject();

            String methodName = requestMethod.getMethodName();
            Object[] args = requestMethod.getArgs();

            Method[] availableMethods = new Server().getClass().getMethods();
            Method callMe = null;
            for (Method method : availableMethods) {
                if (method.getName().equals(methodName)) {
                    callMe = method;
                    break;
                }
            }

            Object result = null;
            if (callMe == null) {
                result = new Exception("Couldn't find method " + methodName);
            } else {
                try {
                    result = callMe.invoke(null, args);
                } catch (InvocationTargetException e) {
                    result = e.getCause();
                }
            }

            OutputStream response = sock.getOutputStream();
            ObjectOutputStream objOutStream = new ObjectOutputStream(response);
            objOutStream.writeObject(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Do not modify any code below tihs line
    // --------------------------------------
    public static String echo(String message) {
        return "You said " + message + "!";
    }

    public static int add(int lhs, int rhs) {
        return lhs + rhs;
    }

    public static int divide(int num, int denom) {
        if (denom == 0)
            throw new ArithmeticException();

        return num / denom;
    }
}