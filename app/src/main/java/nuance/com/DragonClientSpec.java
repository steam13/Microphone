package nuance.com;

import android.net.Uri;
import android.net.Uri.Builder;

class DragonClientSpec {
    private static final String sDefaultBaseAddress = "http://host:51001/DragonService/";
    private final String mSpeaker;
    private final Uri mUriBaseAddress;

    DragonClientSpec(String host, int port, String speaker) {
        Builder builder = Uri.parse(sDefaultBaseAddress).buildUpon();
        builder.encodedAuthority(new StringBuilder(String.valueOf(host)).append(":").append(String.valueOf(port)).toString());
        this.mUriBaseAddress = builder.build();
        this.mSpeaker = speaker;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof DragonClientSpec)) {
            return false;
        }
        DragonClientSpec that = (DragonClientSpec) obj;
        if (this == that) {
            return true;
        }
        if (getUriBaseAddress().equals(that.getUriBaseAddress()) && getSpeaker().equals(that.getSpeaker())) {
            return true;
        }
        return false;
    }

    public String getUriBaseAddress() {
        return this.mUriBaseAddress.toString();
    }

    public String getSpeaker() {
        return this.mSpeaker;
    }
}
