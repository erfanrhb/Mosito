package rahbari.erfan.mosito.enums;

public enum Status {
    PLAY(0), PAUSE(1), PREPARING(2), COMPLETED(3);

    private int val;

    Status(int i) {
        val = i;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }
}
