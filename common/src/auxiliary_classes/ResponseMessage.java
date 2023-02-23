package auxiliary_classes;

import java.io.Serializable;

public class ResponseMessage<T> implements Serializable {
    private T responseData;

    public ResponseMessage(String classname, String commandName, T commandData) {

        this.responseData = commandData;
    }


    public T getResponseData() {
        return responseData;
    }


    public void setResponseData(T responseData) {
        this.responseData = responseData;
    }
}
