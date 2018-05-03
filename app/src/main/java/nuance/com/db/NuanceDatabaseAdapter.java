package nuance.com.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import nuance.com.ProfileData;

public class NuanceDatabaseAdapter implements NuanceQueryConstants {
    private final Context context;
    private SQLiteDatabase db;
    private final DatabaseHelper dbHelper;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, NuanceQueryConstants.DATABASE_NAME, null, 1);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(NuanceQueryConstants.PROFILE);
        }

        public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
            db.execSQL("DROP TABLE IF EXISTS CUSTOMER");
            onCreate(db);
        }
    }

    public NuanceDatabaseAdapter(Context ctx) {
        this.context = ctx;
        dbHelper = new DatabaseHelper(this.context);
    }

    public long insertProfile(String profileName, String dnsName, String ipAddress, String port, boolean selection) {
        long retval = 0;
        try {
            ContentValues initialValues = new ContentValues();
            initialValues.put(NuanceQueryConstants.PROFILE_NAME, profileName);
            initialValues.put(NuanceQueryConstants.DNS_NAME, dnsName);
            initialValues.put(NuanceQueryConstants.IP_ADDRESS, ipAddress);
            initialValues.put("port", Integer.valueOf(Integer.parseInt(port)));
            initialValues.put(NuanceQueryConstants.SELECTION, Boolean.valueOf(selection));
            retval = this.db.insert(NuanceQueryConstants.DATABASE_TABLE_PROFILE, null, initialValues);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return retval;
    }

    public NuanceDatabaseAdapter open() throws SQLException {
        this.db = this.dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        this.dbHelper.close();
    }

    public Cursor selectRecordsFromDB(String query, String[] selectionArgs) {
        return this.db.rawQuery(query, selectionArgs);
    }

    public int deleteRecordsFromDB(String table, String whereClause, String[] whereArgs) {
        return this.db.delete(table, whereClause, whereArgs);
    }

    public Cursor selectRecordsFromDB(String tableName, String[] tableColumns, String whereClase, String[] whereArgs, String groupBy, String having, String orderBy) {
        return this.db.query(tableName, tableColumns, whereClase, whereArgs, groupBy, having, orderBy);
    }

    public boolean updateRecordInDB(String tableName, ContentValues initialValues, String whereClause, String[] whereArgs) {
        return this.db.update(tableName, initialValues, whereClause, whereArgs) > 0;
    }

    public SQLiteDatabase getDb() {
        return this.db;
    }

    public ProfileData selectCurrentProfile() {
        String whereClause = "profile_selection = ? ";
        Cursor c = this.db.query(NuanceQueryConstants.DATABASE_TABLE_PROFILE, new String[]{NuanceQueryConstants.PROFILE_ID, NuanceQueryConstants.PROFILE_NAME, NuanceQueryConstants.DNS_NAME, NuanceQueryConstants.IP_ADDRESS, "port", NuanceQueryConstants.SELECTION}, "profile_selection = ? ", new String[]{"1"}, null, null, null);
        if (c.getCount() > 0) {
            ProfileData p1 = null;
            while (c.moveToNext()) {
                int profileId = Integer.parseInt(c.getString(c.getColumnIndex(NuanceQueryConstants.PROFILE_ID)));
                String profileName = c.getString(c.getColumnIndex(NuanceQueryConstants.PROFILE_NAME));
                String dnsNames = c.getString(c.getColumnIndex(NuanceQueryConstants.DNS_NAME));
                String addresses = c.getString(c.getColumnIndex(NuanceQueryConstants.IP_ADDRESS));
                int port = Integer.parseInt(c.getString(c.getColumnIndex("port")));
                boolean check = c.getInt(c.getColumnIndex(NuanceQueryConstants.SELECTION)) > 0;
                if (check) {
                    p1 = new ProfileData(profileId, profileName, dnsNames, addresses, port, check);
                }
            }
            c.close();
            return p1;
        }
        c.close();
        return null;
    }
}
