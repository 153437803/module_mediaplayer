package lib.kalu.mediaplayer.core.kernel.exo;

import android.content.Context;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerLibraryInfo;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider;
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSource;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.rtsp.RtspMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import lib.kalu.mediaplayer.config.cache.CacheConfig;
import lib.kalu.mediaplayer.config.cache.CacheType;
import lib.kalu.mediaplayer.util.MediaLogUtil;

public final class ExoMediaSourceHelper {

    private final WeakHashMap<String, SimpleCache> mWHM = new WeakHashMap<>();

    private ExoMediaSourceHelper() {
    }

    private static final class Holder {
        private final static ExoMediaSourceHelper mInstance = new ExoMediaSourceHelper();
    }

    public static ExoMediaSourceHelper getInstance() {
        return Holder.mInstance;
    }

    /**
     * @param url     ??????url
     * @param headers ??????headers
     * @return
     */
    public MediaSource getMediaSource(@NonNull Context context, @NonNull boolean hasCache, @NonNull CharSequence url, @Nullable Map<String, String> headers, @NonNull CacheConfig config) {
        Uri contentUri = Uri.parse(url.toString());
        MediaLogUtil.log("getMediaSource => scheme = " + contentUri.getScheme() + ", hasCache = " + hasCache + ", url = " + url);
        // rtmp
        if ("rtmp".equals(contentUri.getScheme())) {
            RtmpDataSource.Factory factory = new RtmpDataSource.Factory();
            return new ProgressiveMediaSource.Factory(factory).createMediaSource(MediaItem.fromUri(contentUri));
        }
        // rtsp
        else if ("rtsp".equals(contentUri.getScheme())) {
            RtspMediaSource.Factory factory = new RtspMediaSource.Factory();
            return factory.createMediaSource(MediaItem.fromUri(contentUri));
        }
        // other
        else {
            // http
            DefaultHttpDataSource.Factory http = new DefaultHttpDataSource.Factory();
            http.setUserAgent("(Linux;Android " + Build.VERSION.RELEASE + ") " + ExoPlayerLibraryInfo.VERSION_SLASHY);
            http.setConnectTimeoutMs(DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS);
            http.setReadTimeoutMs(DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS);
            http.setAllowCrossProtocolRedirects(true);
            http.setKeepPostFor302Redirects(true);

            // head
            refreshHeaders(http, headers);

            // ????????????
            if (hasCache && null != context && null != config && config.getCacheType() == CacheType.DEFAULT) {
                MediaLogUtil.log("getMediaSource => ??????, ????????????");

                // cache
                int size;
                String dir;
                if (null != config) {
                    size = config.getCacheMaxMB();
                    dir = config.getCacheDir();
                } else {
                    size = 1024;
                    dir = "temp";
                }

                CacheDataSource.Factory factory = new CacheDataSource.Factory();

                // ?????????????????????
                if (null != context && null != config && config.getCacheType() == CacheType.DEFAULT) {
                    if (!mWHM.containsKey(dir)) {
                        File file = new File(context.getExternalCacheDir(), dir);
                        LeastRecentlyUsedCacheEvictor evictor = new LeastRecentlyUsedCacheEvictor(size * 1024 * 1024);
                        StandaloneDatabaseProvider provider = new StandaloneDatabaseProvider(context);
                        SimpleCache simpleCache = new SimpleCache(file, evictor, provider);
                        mWHM.put(dir, simpleCache);
                    }
                    factory.setCache(mWHM.get(dir));
                }

                factory.setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR);
                factory.setUpstreamDataSourceFactory(http);
                return createMediaSource(url, factory);
            }
            // ??????
            else {
                MediaLogUtil.log("getMediaSource => ??????, ?????????");
                DefaultDataSource.Factory factory = new DefaultDataSource.Factory(context, http);
                return createMediaSource(url, factory);
            }
        }
    }

    private final MediaSource createMediaSource(@NonNull CharSequence url, @NonNull DataSource.Factory factory) {

        int contentType;
        if (url.toString().toLowerCase().contains(".mpd")) {
            contentType = C.TYPE_DASH;
        } else if (url.toString().toLowerCase().contains(".m3u8")) {
            contentType = C.TYPE_HLS;
        } else if (url.toString().toLowerCase().matches(".*\\.ism(l)?(/manifest(\\(.+\\))?)?")) {
            contentType = C.TYPE_SS;
        } else {
            contentType = C.TYPE_OTHER;
        }

        // ??????
//        MediaItem.SubtitleConfiguration.Builder subtitle = new MediaItem.SubtitleConfiguration.Builder(srtUri);
//        subtitle.setMimeType(MimeTypes.APPLICATION_SUBRIP);
//        subtitle.setLanguage("en");
//        subtitle.setSelectionFlags(C.SELECTION_FLAG_AUTOSELECT);

//        MediaLogUtil.log("SRT => srtUri = " + srtUri);
        MediaItem.Builder builder = new MediaItem.Builder();
        builder.setUri(Uri.parse(url.toString()));
//        builder.setSubtitleConfigurations(Arrays.asList(subtitle.build()));
        MediaItem mediaItem = builder.build();
//        MediaItem.Subtitle subtitle = new MediaItem.Subtitle(
//                srtUri,
//                MimeTypes.APPLICATION_SUBRIP,
//                "en",
//                C.SELECTION_FLAG_DEFAULT);
////                C.SELECTION_FLAG_AUTOSELECT);


        switch (contentType) {
            case C.TYPE_DASH:
                MediaLogUtil.log("SRT => TYPE_DASH");
                return new DashMediaSource.Factory(factory).createMediaSource(mediaItem);
            case C.TYPE_SS:
                MediaLogUtil.log("SRT => TYPE_SS");
                return new SsMediaSource.Factory(factory).createMediaSource(mediaItem);
            case C.TYPE_HLS:
                MediaLogUtil.log("SRT => TYPE_HLS");
                return new HlsMediaSource.Factory(factory).createMediaSource(mediaItem);
            default:
                MediaLogUtil.log("SRT => TYPE_DEFAULT");
//                return new DefaultMediaSourceFactory(factory).createMediaSource(mediaItem);
                DefaultExtractorsFactory extractors = new DefaultExtractorsFactory();
                extractors.setConstantBitrateSeekingEnabled(true);
                return new ProgressiveMediaSource.Factory(factory, extractors).createMediaSource(mediaItem);
        }

//        // ??????
//        if (null != srtUri) {
//
//            MediaItem.Builder srtBuilder = new MediaItem.Builder().setUri(srtUri);
//            MediaItem.Subtitle subtitle = new MediaItem.Subtitle(srtUri,
//                    MimeTypes.TEXT_VTT,
//                    "en",
//                    C.SELECTION_FLAG_DEFAULT);
//            srtBuilder.setSubtitles(Arrays.asList(subtitle));
//
//            MediaItem srtItem = srtBuilder.build();
////            MediaSource srtSource = new DefaultMediaSourceFactory(factory).createMediaSource(srtItem);
//
////            MediaItem.SubtitleConfiguration.Builder builder = new MediaItem.SubtitleConfiguration.Builder(srtUri);
//////            builder.setMimeType(MimeTypes.APPLICATION_SUBRIP);
////            builder.setMimeType(MimeTypes.TEXT_VTT);
////            builder.setLanguage("en");
////            builder.setSelectionFlags(C.SELECTION_FLAG_DEFAULT);
////            MediaItem.SubtitleConfiguration subtitle = builder.build();
//            MediaSource textMediaSource = new SingleSampleMediaSource.Factory(factory).createMediaSource(subtitle, C.TIME_UNSET);
//////            textMediaSource.getMediaItem().mediaMetadata.subtitle.toString();
////            MediaLogUtil.log("SRT => " + subtitle);
//            return new MergingMediaSource(mediaSource, srtSource);
//        }
//        // ??????
//        else {
//            return mediaSource;
//        }
    }

    private void refreshHeaders(@NonNull HttpDataSource.Factory factory, @NonNull Map<String, String> map) {

        if (null == map || map.size() <= 0)
            return;

        String userAgent = null;
        HashMap<String, String> mapFormat = new HashMap<>();

        for (String temp : map.keySet()) {
            if (null == temp || temp.length() <= 0)
                continue;
            String value = mapFormat.get(temp);
            if (null == value || value.length() <= 0)
                continue;
            if ("User-Agent".equals(temp)) {
                userAgent = value;
            } else {
                mapFormat.put(temp, value);
            }
        }

        //????????????????????????header?????????UA???????????????HttpDataSourceFactory?????????userAgent????????????????????????
        if (null != userAgent) {
            try {
                Field userAgentField = factory.getClass().getDeclaredField("userAgent");
                userAgentField.setAccessible(true);
                userAgentField.set(factory, userAgent);
            } catch (Exception e) {
            }
        }

        // add
        factory.setDefaultRequestProperties(mapFormat);
    }
}
