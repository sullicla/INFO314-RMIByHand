import java.io.Serializable;

public class RemoteMethod implements Serializable {
    private String methodName;
    private Object[] args;

    public RemoteMethod(String methodName, Object[] args) {
        this.methodName = methodName;
        this.args = args;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getArguments() {
        return args;
    }

}
