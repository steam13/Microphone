package nuance.com;

import nuance.com.MicView.MicState;

public interface IDNSClient {
    String ConnectDevice(ConnectDeviceData connectDeviceData) throws DragonHttpException;

    void Disconnect(String str) throws DragonHttpException;

    MicState MicAudio(String str, byte[] bArr) throws DragonHttpException;

    int SampleRate() throws DragonHttpException;

    MicState SetMicState(String str, MicState micState) throws DragonHttpException;

    String Speaker() throws DragonHttpException;

    double Ver() throws DragonHttpException;
}
