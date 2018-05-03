package nuance.com;

import org.json.JSONException;
import org.json.JSONObject;

class ConnectDeviceData {
    private final String mDeviceName;
    private final String mSpeakerName;

    ConnectDeviceData(String speakerName, String deviceName) {
        this.mSpeakerName = speakerName;
        this.mDeviceName = deviceName;
    }

    String asJsonString() {
        JSONObject jobject = new JSONObject();
        try {
            jobject.put("DeviceName", this.mDeviceName);
            jobject.put("SpeakerName", this.mSpeakerName);
        } catch (JSONException e) {
        }
        return jobject.toString();
    }
}
