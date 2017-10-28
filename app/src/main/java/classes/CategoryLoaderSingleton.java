package classes;

import android.util.Log;

/**
 * Created by user on 28 Oct 2017.
 */

public class CategoryLoaderSingleton {

    private static CategoryLoaderSingleton instance = null;
    private static CategoryLoader loader = null;

    protected CategoryLoaderSingleton() {}

    public static CategoryLoaderSingleton getInstance(CategoryLoader categoryLoader) {
        if(instance == null) {
            instance = new CategoryLoaderSingleton();
            Log.i("CategoryLoaderSingleton", "CategoryLoaderSingleton created successfully");
            if(loader == null) {
                loader = categoryLoader;
                Log.i("CategoryLoader", "CategoryLoader created successfully.");
            }
            Log.i("CategoryLoader", "CategoryLoader is not null.");
        }
        return instance;
    }

    public CategoryLoader getCategoryLoader() {
        if(loader == null)
            Log.e("CategoryLoader", "CategoryLoader is NULL.");
        return loader;
    }

}
