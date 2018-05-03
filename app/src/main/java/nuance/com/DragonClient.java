package nuance.com;

import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;

import nuance.com.MicView.MicState;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

class DragonClient extends RestClient implements IDNSClient {
    private static /* synthetic */ int[] $SWITCH_TABLE$nuance$com$MicView$MicState = null;
    private static final int CONNECTION_TIMEOUT = 10000;
    private static final int SOCKET_TIMEOUT = 10000;
    private static final String sConnectionKEEP_ALIVE = "Keep-Alive";
    private static final String sConnectionKey = "Connection";
    private static final String sContentBINARY = "application/octet-stream";
    private static final String sContentJSON = "application/json; charset=utf-8";
    private static final String sContentTypeKey = "Content-Type";
    private static final String sJsonSpec = "?format=json";
    private static final String sThis = ":DragonClient:";
    private final DragonClientSpec mClientSpec;
    private final DefaultHttpClient mHttpClient;

    static int[] $SWITCH_TABLE$nuance$com$MicView$MicState() {
        int[] iArr = $SWITCH_TABLE$nuance$com$MicView$MicState;
        if (iArr == null) {
            iArr = new int[MicState.values().length];
            try {
                iArr[MicState.DISCONNECTED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[MicState.OFF.ordinal()] = 3;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[MicState.ON.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[MicState.ONPAUSE.ordinal()] = 7;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[MicState.PAUSE.ordinal()] = 4;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[MicState.QUIT.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[MicState.RESUME.ordinal()] = 8;
            } catch (NoSuchFieldError e7) {
            }
            try {
                iArr[MicState.SLEEPING.ordinal()] = 5;
            } catch (NoSuchFieldError e8) {
            }
            $SWITCH_TABLE$nuance$com$MicView$MicState = iArr;
        }
        return iArr;
    }

    DragonClient(DragonClientSpec clientSpec) {
        this.mClientSpec = clientSpec;
        ClientConnectionManager mgr = new DefaultHttpClient().getConnectionManager();
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
        HttpConnectionParams.setSoTimeout(httpParameters, 10000);
        this.mHttpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(httpParameters, mgr.getSchemeRegistry()), httpParameters);
    }

    DragonClientSpec getClientSpec() {
        return this.mClientSpec;
    }

    void close() {
        this.mHttpClient.getConnectionManager().shutdown();
    }

    public double Ver() throws DragonHttpException {
        String sWho = ":DragonClient:Ver:";
        try {
            HttpResponse response = this.mHttpClient.execute(new HttpGet(getUriCommand("Ver")));
            validateAndLogResponse(response, ":DragonClient:Ver:");
            return Double.valueOf(EntityUtils.toString(response.getEntity()));
        } catch (DragonHttpException e) {
            throw e;
        } catch (Exception e2) {
            throw new DragonHttpException(":DragonClient:Ver:" + e2.getMessage());
        }
    }

    public int SampleRate() throws DragonHttpException {
        String sWho = ":DragonClient:SampleRate:";
        try {
            HttpResponse response = this.mHttpClient.execute(new HttpGet(getUriCommand("SampleRate")));
            // validateAndLogResponse(response, ":DragonClient:SampleRate:");
            return Integer.valueOf(EntityUtils.toString(response.getEntity()));
        } catch (DragonHttpException e) {
            throw e;
        } catch (Exception e2) {
            throw new DragonHttpException(":DragonClient:SampleRate:" + e2.getMessage());
        }
    }

    public String Speaker() throws DragonHttpException {
        String sWho = ":DragonClient:Speaker:";
        String sSpeakerName = "";
        try {
            HttpResponse response = this.mHttpClient.execute(new HttpGet(getUriCommand("Speaker")));
            validateAndLogResponse(response, ":DragonClient:Speaker:");
            sSpeakerName = EntityUtils.toString(response.getEntity());
            sSpeakerName = sSpeakerName.replaceAll("\"", "");
            return sSpeakerName;
        } catch (DragonHttpException e) {
            throw e;
        } catch (ClientProtocolException e2) {
            throw new DragonHttpException(":DragonClient:Speaker:ClientProtocolException");
        } catch (IOException e3) {
            throw new DragonHttpException(":DragonClient:Speaker:IOException");
        } catch (Exception e4) {
            throw new DragonHttpException(":DragonClient:Speaker:" + e4.getMessage());
        }
    }

    public String ConnectDevice() throws DragonHttpException {
        return ConnectDevice(new ConnectDeviceData(this.mClientSpec.getSpeaker(), Build.MODEL));
    }

    public String ConnectDevice(ConnectDeviceData devData) throws DragonHttpException {
        String sWho = ":DragonClient:ConnectDevice:";
        String sessionId = "";
        try {
            HttpPost httppost = new HttpPost(getUriCommand("ConnectDevice"));
            httppost.setHeader(sContentTypeKey, sContentJSON);
            httppost.setEntity(new StringEntity(devData.asJsonString(), "UTF-8"));
            HttpResponse response = this.mHttpClient.execute(httppost);
            validateAndLogResponse(response, ":DragonClient:ConnectDevice:");
            sessionId = EntityUtils.toString(response.getEntity());
            sessionId = sessionId.replaceAll("\"", "");
            return sessionId;
        } catch (DragonHttpException e) {
            throw e;
        } catch (Exception e2) {
            throw new DragonHttpException(":DragonClient:ConnectDevice:" + e2.getMessage());
        }
    }

    public void Disconnect(String sessionId) throws DragonHttpException {
        String sWho = ":DragonClient:Disconnect:";
        try {
            validateAndLogResponse(this.mHttpClient.execute(new HttpPost(getUriCommand(new StringBuilder(String.valueOf(sessionId)).append("/Disconnect").toString()))), ":DragonClient:Disconnect:");
        } catch (DragonHttpException e) {
            throw e;
        } catch (Exception e2) {
            throw new DragonHttpException(":DragonClient:Disconnect:" + e2.getMessage());
        }
    }

    public MicState SetMicState(String sessionId, MicState micState) throws DragonHttpException {
        String sWho = ":DragonClient:SetMicState:";
        try {
            HttpResponse response = this.mHttpClient.execute(new HttpPost(getUriCommand(new StringBuilder(String.valueOf(sessionId)).append("/Mic=").append(micStateToString(micState)).toString())));
            validateAndLogResponse(response, ":DragonClient:SetMicState:");
            return stringToMicState(EntityUtils.toString(response.getEntity()));
        } catch (DragonHttpException e) {
            throw e;
        } catch (Exception e2) {
            throw new DragonHttpException(":DragonClient:SetMicState:" + e2.getMessage());
        }
    }

    public MicState MicAudio(String sessionId, byte[] buffer) throws DragonHttpException {
        String sWho = ":DragonClient:MicAudio:";
        MicState micState = MicState.DISCONNECTED;
        try {
            HttpPost httppost = new HttpPost(getUriCommand(new StringBuilder(String.valueOf(sessionId)).append("/MicAudio").toString()));
            httppost.setHeader(sContentTypeKey, sContentBINARY);
            httppost.setHeader(sConnectionKey, sConnectionKEEP_ALIVE);
            httppost.setEntity(new ByteArrayEntity(buffer));
            HttpResponse response = this.mHttpClient.execute(httppost);
            validateAndLogResponse(response, ":DragonClient:MicAudio:");
            return convertResponseToMicState(response);
        } catch (DragonHttpException e) {
            throw e;
        } catch (SocketTimeoutException e2) {
            throw new DragonHttpException(410, ":DragonClient:MicAudio:" + e2.getMessage());
        } catch (Exception e3) {
            throw new DragonHttpException(410, ":DragonClient:MicAudio:" + e3.getMessage());
        }
    }

    protected MicState convertResponseToMicState(HttpResponse resp) {
        String strMicState = "";
        try {
            strMicState = EntityUtils.toString(resp.getEntity());
        } catch (Exception e) {
            Log.d(sThis, "MIC STATE ERROR");
        }
        MicState micState = MicState.DISCONNECTED;
        if (strMicState.contains("OFF")) {
            return MicState.OFF;
        }
        if (strMicState.contains("ON")) {
            return MicState.ON;
        }
        if (strMicState.contains("PAUSE")) {
            return MicState.PAUSE;
        }
        if (strMicState.contains("SLEEPING")) {
            return MicState.SLEEPING;
        }
        return micState;
    }

/*    protected static String micStateToString(MicState micState) {
        String strMicState = "UNKNOWN";
        switch ($SWITCH_TABLE$nuance$com$MicView$MicState()[micState.ordinal()]) {
            case R.styleable.States_state_OFF:
                return "DISCONNECTED";
            case R.styleable.States_state_PAUSE:
                return "ON";
            case R.styleable.States_state_ON *//*3*//*:
                return "OFF";
            case R.styleable.States_state_SLEEPING:
                return "PAUSE";
            case R.styleable.States_state_QUIT *//*5*//*:
                return "SLEEPING";
            default:
                return strMicState;
        }
    }*/

    protected static String micStateToString(MicView.MicState micstate) {
        switch ($SWITCH_TABLE$nuance$com$MicView$MicState()[micstate.ordinal()]) {
            case 5: // '\005'
                return "SLEEPING";

            case 4: // '\004'
                return "PAUSE";

            case 2: // '\002'
                return "ON";

            case 3: // '\003'
                return "OFF";

            case 1: // '\001'
                return "DISCONNECTED";
        }
        return "UNKNOWN";
    }

    protected static MicState stringToMicState(String strMicState) {
        MicState micState = MicState.DISCONNECTED;
        if (strMicState.contains("OFF")) {
            return MicState.OFF;
        }
        if (strMicState.contains("ON")) {
            return MicState.ON;
        }
        if (strMicState.contains("PAUSE")) {
            return MicState.PAUSE;
        }
        if (strMicState.contains("SLEEPING")) {
            return MicState.SLEEPING;
        }
        return micState;
    }

    protected void validateAndLogResponse(HttpResponse resp, String sWho) throws DragonHttpException {
        Logger.logMessage(new StringBuilder(String.valueOf(sWho)).append("Response:      <= ").append(resp.getStatusLine().toString()).toString());
        if (resp.getStatusLine().getStatusCode() != 200) {
            throw new DragonHttpException(resp.getStatusLine());
        }
    }

    protected URI getUriCommand(String command) throws Exception {
        return new URI(new StringBuilder(String.valueOf(this.mClientSpec.getUriBaseAddress())).append(command).append(sJsonSpec).toString());
    }
}
