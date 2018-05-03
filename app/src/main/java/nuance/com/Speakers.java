package nuance.com;

public class Speakers {
    private String dragonClientSpeaker;
    private String dragonSpSpeaker;

    public String getDragonClientSpeaker() {
        return this.dragonClientSpeaker;
    }

    public void setDragonClientSpeaker(String dragonClientSpeaker) {
        this.dragonClientSpeaker = dragonClientSpeaker;
    }

    public String getDragonSpSpeaker() {
        return this.dragonSpSpeaker;
    }

    public void setDragonSpSpeaker(String dragonSpSpeaker) {
        this.dragonSpSpeaker = dragonSpSpeaker;
    }

    public Speakers(String dragonClientSpeaker, String dragonSpSpeaker) {
        this.dragonClientSpeaker = dragonClientSpeaker;
        this.dragonSpSpeaker = dragonSpSpeaker;
    }

    public String formSpeakerError(String error) {
        String[] errorMsg = error.split("@");
        StringBuilder errorMsgBuilder = new StringBuilder();
        errorMsgBuilder.append(errorMsg[0]);
        errorMsgBuilder.append(getDragonSpSpeaker());
        errorMsgBuilder.append(errorMsg[1]);
        errorMsgBuilder.append(getDragonClientSpeaker());
        errorMsgBuilder.append(errorMsg[2]);
        return errorMsgBuilder.toString();
    }
}
