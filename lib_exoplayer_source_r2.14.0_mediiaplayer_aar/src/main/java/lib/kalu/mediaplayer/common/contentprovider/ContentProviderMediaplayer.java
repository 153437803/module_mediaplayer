package lib.kalu.mediaplayer.common.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

import lib.kalu.mediaplayer.common.util.LogUtil;

@Keep
public class ContentProviderMediaplayer extends ContentProvider {

    public static WeakReference<Context> weakReference = null;

    public static final Context getContextWeakReference() {
        try {
            return weakReference.get();
        } catch (Exception e) {
            LogUtil.log(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext().getApplicationContext();
        weakReference = new WeakReference<>(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}