package modules;

public class Response<T> {

    // Constructor

    public Response(boolean isSuccess, String message, T output)
    {
        this.isSuccess = isSuccess;
        this.message = message;
        this.output = output;
    }

    // Properties

    private boolean isSuccess;
    private String message;
    private T output;

    // Getters

    public boolean getIsSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public T getOutput() {
        return output;
    }

}
