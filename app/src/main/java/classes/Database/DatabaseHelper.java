package classes.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.teamenigma.factthisshoot.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import classes.BitmapBytesConverter;

/**
 * Created by Rgee on 12/10/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper implements Serializable{

    private static final String DATABASE_NAME = "Facts.db";
    private static final String TABLE_NAME = "facts";
    private static final String col_ID = "ID";
    private static final String col_NAME = "NAME";
    private static final String col_IMAGE = "IMAGE";
    private static final String col_CATEGORY = "CATEGORY";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                                            "("+ col_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                + col_NAME + " TEXT, "
                                                + col_IMAGE + " BLOB, "
                                                + col_CATEGORY + " TEXT)";

    public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static final String SELECT_ALL = "SELECT * FROM " + TABLE_NAME;


    /**
     * Create a helper object to create, open, and/or manage a database.
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
    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
        onCreate(this.getWritableDatabase());

    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(DELETE_TABLE);
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
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(DELETE_TABLE);//Deletes currently existing table.
        onCreate(db);//Creates a new, updated version of the table
    }


    /*
    Inserts the data of the picture into the database
    @param name The name of the picture (Earth, Cannabis etc.)
    @param image The image file
    @param category The category the picture is in
     */
    public boolean insertData(String name, byte[] image, String category) throws SQLiteException
    {
        SQLiteDatabase db = getWritableDatabase(); //Get database to write data on

        ContentValues cv = new ContentValues(); //Initialize container cv where data values wil be put in

        cv.put(col_NAME, name);//Put name in the cv
        cv.put(col_IMAGE, image);//Put image in the cv
        cv.put(col_CATEGORY, category);//Put the category in the cv

        long result = db.insert(TABLE_NAME, null, cv);//Insert the cv into the database. Retrieve result, determining success of insert

        if(result == -1) return false;//If result is -1, database insert is unsuccessful
        else
        {
            Log.d("SQL STATEMENT", "Insertion successful");
            return true; //Else, the database insert is successful
        }

    }
    /*
    Retrieves ALL of the data within a category.
    @param category The category of the data (planets, flowers, dogs)
     */
    public Cursor getCategoryData(String category)
    {
        SQLiteDatabase db = getWritableDatabase();

        //Retrieve all data in a category (dogs, plants etc.)
        Cursor categoryData = db.rawQuery(SELECT_ALL + " WHERE " + col_CATEGORY + " = '"+category+"'", null);


        return categoryData;

    }

    /*
    Function used by the ChooseCategory class.
    Selects and returns a random, non-repeated wrong answer.

    @param invalidAnswerIDs the IDs of the answers that cannot be selected and returned again.
    @param categoryName the category of the data the wrong answer will be selected from
    @return the ID of the wrong answer

     */
    public int getWrongAnswerID(List<Integer> invalidAnswerIDs, String categoryName)
    {
        SQLiteDatabase db = getWritableDatabase();

        Cursor allItems = getCategoryData(categoryName);//Get all of the data within the specified category

        Random r = new Random();//Initialize random generator for selecting the ID of the wrong answer


        boolean isValidID = false;//Boolean to check whether the ID that is randomly selected has not been selected before.
        int randomPos = 0;//Initialize the position of the tuple of the randomly selected wrong answer

        while(!isValidID)
        {
            randomPos = r.nextInt(allItems.getCount()) + 1;//Select a random value between 1 and the total number of items in the data
            if(isValidID(randomPos, invalidAnswerIDs)) isValidID = true;//Checks whether the random ID has not been selected before.
        }

        allItems.moveToPosition(randomPos-1);//Go to the tuple where the random ID is


        return allItems.getInt(0);//Return the ID


    }

    public String getName(int pos)
    {
        SQLiteDatabase db = getWritableDatabase();

        Cursor tuple = db.rawQuery(SELECT_ALL + " WHERE ID = " + pos, null);

        tuple.moveToFirst();

        return tuple.getString(1);

    }

    private boolean isValidID(int randomID, List<Integer>invalidAnswerIDs)
    {
        for(int i = 0; i < invalidAnswerIDs.size(); i++)
        {
            if(randomID == invalidAnswerIDs.get(i)) return false;
        }
        return true;
    }
}
