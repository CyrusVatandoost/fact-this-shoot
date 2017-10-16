package classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Rgee on 12/10/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Facts.db";
    private static final String TABLE_NAME = "facts";
    private static final String col_ID = "ID";
    private static final String col_NAME = "NAME";
    private static final String col_IMAGE = "IMAGE";
    private static final String col_CATEGORY = "CATEGORY";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                                            " ("+ col_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                + col_NAME + " TEXT, "
                                                + col_IMAGE + " TEXT, "
                                                + col_CATEGORY + " TEXT)";

    public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static final String SELECT_ALL = "SELECT * FROM " + TABLE_NAME;

    /**
     * Create a helper object to create, open, an d/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use to open or create the database
     * //@param name    of the database file, or null for an in-memory database
     * //@param factory to use for creating cursor objects, or null for the default
     * //@param version number of the database (starting at 1); if the database is older,
     *                {@link #onUpgrade} will be used to upgrade the database; if the database is
     *                newer, {@link #onDowngrade} will be used to downgrade the database
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_TABLE);//Deletes currently existing table.
        onCreate(db);//Creates a new, updated version of the table
    }

    /**
    Inserts the data of the picture into the database
    @param name The name of the picture (Earth, Cannabis etc.)
    @param imagePath The filename of the picture
    @param category The category the picture is in
     */
    public boolean insertData(String name, String imagePath, String category) {
        SQLiteDatabase db = getWritableDatabase(); //Get database to write data on
        ContentValues cv = new ContentValues(); //Initialize container cv where data values wil be put in

        cv.put(col_NAME, name);//Put name in the cv
        cv.put(col_IMAGE, imagePath);//Put image path in the cv
        cv.put(col_CATEGORY, category);//Put the category in the cv

        long result = db.insert(TABLE_NAME, null, cv);//Insert the cv into the database. Retrieve result, determining success of insert

        if(result == -1) return false;//If result is -1, database insert is unsuccessful
        else return true;//Else, the database insert is successful

    }

    /**
    Retrieves ALL of the data within a category.
    @param category The category of the data (planets, flowers, dogs)
     */
    public Cursor getCategoryData(String category) {
        SQLiteDatabase db = getWritableDatabase();

        //Retrieve all data in a category (dogs, plants etc.)
        Cursor categoryData = db.rawQuery(SELECT_ALL + " WHERE " + col_CATEGORY + "= '"+category+"'", null);

        return categoryData;

    }
}
