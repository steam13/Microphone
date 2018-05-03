package nuance.com;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ProfileData implements Parcelable {
    public static final Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel in) {
            return new ProfileData(in);
        }

        @Override
        public ProfileData[] newArray(int size) {
            return new ProfileData[size];
        }
    };
    private String addresses;
    private boolean cb;
    private String dnsNames;
    private int port;
    private int profileId;
    private String profileName;

    public boolean isCb() {
        return this.cb;
    }

    public void setCb(boolean cb) {
        this.cb = cb;
    }

    public ProfileData(Parcel in) {
        readFromParcel(in);
    }

    public ProfileData(int profileId, String profileName, String dnsNames, String addresses, int port, boolean check) {
        this.profileName = profileName;
        this.dnsNames = dnsNames;
        this.addresses = addresses;
        this.port = port;
        this.cb = check;
        this.profileId = profileId;
    }

    public String toString() {
        return this.profileName + ":" + this.addresses + ":" + this.port + ":" + this.cb + ":" + this.profileId;
    }

    public String getProfileName() {
        return this.profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getDnsNames() {
        return this.dnsNames;
    }

    public void setDnsNames(String dnsNames) {
        this.dnsNames = dnsNames;
    }

    public String getAddresses() {
        return this.addresses;
    }

    public void setAddresses(String addresses) {
        this.addresses = addresses;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void cbToggle() {
        this.cb = !this.cb;
    }

    public int getProfileId() {
        return this.profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public void readFromParcel(Parcel in) {
        boolean z = true;
        this.profileId = in.readInt();
        this.profileName = in.readString();
        this.dnsNames = in.readString();
        this.addresses = in.readString();
        this.port = in.readInt();
        if (in.readByte() != (byte) 1) {
            z = false;
        }
        this.cb = z;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.profileId);
        dest.writeString(this.profileName);
        dest.writeString(this.dnsNames);
        dest.writeString(this.addresses);
        dest.writeInt(this.port);
        dest.writeByte((byte) (this.cb ? 1 : 0));
    }
}
