package modules;

/*
    Response class is used to return an interaction response from the server to the client.
    It contains three properties:
    - isSuccess: a boolean value that indicates whether the operation was successful or not.
    - message: a string that contains a message that describes the result of the operation.
    - output: a generic object that contains the result of the operation.
 */
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
