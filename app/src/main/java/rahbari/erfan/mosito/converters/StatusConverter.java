package rahbari.erfan.mosito.converters;

import androidx.room.TypeConverter;

import rahbari.erfan.mosito.enums.Status;

public class StatusConverter {
    @TypeConverter
    public static Status storedStringToProfile(int val) {
        switch (val) {
            case 0:
                return Status.PLAY;
            case 2:
                return Status.PREPARING;
            case 3:
                return Status.COMPLETED;
            default:
                return Status.PAUSE;
        }
    }

    @TypeConverter
    public static int ProfileToStoredString(Status status) {
        return status.getVal();
    }
}
