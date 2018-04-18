package com.mango.contentproviderandresolverdemo;

import android.net.Uri;

/**
 * class
 * package com.mango.contentproviderandresolverdemo
 *
 * @author swd1
 * Date 18-4-10
 */
public class Articles {

    /**
     * Data Field
     */
    public static final String ID = "_id";
    public static final String TITLE = "_title";
    public static final String ABSTRACT = "_abstract";
    public static final String URL = "_url";

    /**Default sort order*/
    public static final String DEFAULT_SORT_ORDER = "_id asc";

    /**Call method*/
    public static final String METHOD_GET_ITEM_COUNT = "METHOD_GET_ITEM_COUNT";
    public static final String KEY_ITEM_COUNT = "KEY_ITEM_COUNT";

    /**Authority*/
    public static final String AUTHORITY = "com.mango.providers.articles";

    /**
     * Match Code*/
    public static final int ITEM = 1;
    public static final int ITEM_ID = 2;
    public static final int ITEM_POS = 3;

    /**MIME*/
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.mango.article";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.mango.article";

    /**Content URI*/
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/item");
    public static final Uri CONTENT_POS_URI = Uri.parse("content://" + AUTHORITY + "/pos");


}
