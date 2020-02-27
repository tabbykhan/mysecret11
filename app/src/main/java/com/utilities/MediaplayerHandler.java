package com.utilities;

import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Manish Kumar
 * <p>
 * MediaplayerHandler
 * <p>
 * <p>
 * This class is use for  handle {@link MediaPlayer} in application
 * <p>
 */
public class MediaplayerHandler {

    static MediaplayerHandler mediaplayerHandler;

    Map<String, MediaModal> mediaPlayersMap;

    Context _context;


    private MediaplayerHandler (Context _context) {
        this._context = _context;
        mediaPlayersMap = new HashMap<>();
    }


    public static MediaplayerHandler getInstance (Context _context) {
        if (mediaplayerHandler == null) {
            mediaplayerHandler = new MediaplayerHandler(_context);
        }
        return mediaplayerHandler;
    }

    public void stopAll () {
        if (mediaPlayersMap != null && mediaPlayersMap.size() > 0) {
            for (String key : mediaPlayersMap.keySet()) {
                stopPlayer(key);
            }
        }
    }


    public void stopPlayer (String key) {
        if (mediaPlayersMap != null && mediaPlayersMap.containsKey(key)) {
            MediaModal mediaModal = mediaPlayersMap.get(key);
            mediaModal.stopPlayer();
        }
    }

    public void pauseAll () {
        if (mediaPlayersMap != null && mediaPlayersMap.size() > 0) {
            for (String key : mediaPlayersMap.keySet()) {
                pausePlayer(key);
            }
        }
    }

    public void pausePlayer (String key) {
        if (mediaPlayersMap != null && mediaPlayersMap.containsKey(key)) {
            MediaModal mediaModal = mediaPlayersMap.get(key);
            mediaModal.pausePlayer();
        }
    }

    public void resumeAll () {
        if (mediaPlayersMap != null && mediaPlayersMap.size() > 0) {
            for (String key : mediaPlayersMap.keySet()) {
                resumePlayer(key);
            }
        }
    }

    public void resumePlayer (String key) {
        if (mediaPlayersMap != null && mediaPlayersMap.containsKey(key)) {
            MediaModal mediaModal = mediaPlayersMap.get(key);
            mediaModal.resumePlayer();
        }
    }

    public boolean hasPlayer (String key) {
        if (mediaPlayersMap != null && mediaPlayersMap.containsKey(key)) {
            return true;
        }
        return false;
    }

    public Uri getUriFromResource (int rawId) {

        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                _context.getPackageName() + "/" + rawId);

    }

    public void playSound (String key, int rawId, boolean repeat) {
        playSound(key, getUriFromResource(rawId), repeat);
    }


    public void playSound (String key, Uri source, boolean repeat) {
        stopPlayer(key);

        MediaModal mediaModal = new MediaModal(key, new MediaPlayer());
        try {
            mediaModal.setPlayer(source, repeat);
            mediaPlayersMap.put(key, mediaModal);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    class MediaModal implements
            MediaPlayer.OnErrorListener,
            MediaPlayer.OnCompletionListener,
            MediaPlayer.OnPreparedListener {

        MediaPlayer mediaPlayer;
        String key;

        public MediaModal (String key, MediaPlayer mediaPlayer) {
            this.key = key;
            this.mediaPlayer = mediaPlayer;
        }

        private void pausePlayer () {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
        }

        private void resumePlayer () {
            if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
        }

        private void stopPlayer () {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            mediaPlayersMap.remove(key);
        }

        private void setPlayer (Uri source, boolean repeat) throws IOException, IllegalStateException, IllegalArgumentException, SecurityException {
            if (mediaPlayer == null) {
                throw new IllegalStateException("MediaPlayer is null.");
            }
            mediaPlayer.reset();
            mediaPlayer.setDataSource(_context, source);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setLooping(repeat);
            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnCompletionListener(this);

        }

        @Override
        public void onCompletion (MediaPlayer mp) {
            if (!mediaPlayer.isLooping()) {
                stopPlayer();
            }
        }

        @Override
        public boolean onError (MediaPlayer mp, int what, int extra) {
            stopPlayer();
            return false;
        }

        @Override
        public void onPrepared (MediaPlayer mp) {
            try {
                mp.start();
            } catch (IllegalStateException e) {
                stopPlayer();
            }
        }
    }
}
