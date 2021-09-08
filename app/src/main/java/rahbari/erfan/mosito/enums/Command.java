package rahbari.erfan.mosito.enums;

public enum Command {
    Next(0), Back(1), Stop(2), Play(3), Pause(4), Seek(5), Toggle(6);

    private int val;

    Command(int i) {
        val = i;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }
    }
