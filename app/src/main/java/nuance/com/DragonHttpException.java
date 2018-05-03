package nuance.com;

import org.apache.http.HttpException;
import org.apache.http.StatusLine;

class DragonHttpException extends HttpException {
    private static final long serialVersionUID = 1;
    private int mCode;

    protected DragonHttpException() {
    }

    public DragonHttpException(String reason) {
        super(reason);
        this.mCode = 500;
    }

    public DragonHttpException(int code, String reason) {
        super(reason);
        this.mCode = code;
    }

    public DragonHttpException(StatusLine statusLine) {
        super(statusLine.getReasonPhrase());
        this.mCode = statusLine.getStatusCode();
    }

    public int getCode() {
        return this.mCode;
    }
}
