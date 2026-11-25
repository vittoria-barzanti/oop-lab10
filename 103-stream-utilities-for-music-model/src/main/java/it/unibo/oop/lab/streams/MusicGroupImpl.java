package it.unibo.oop.lab.streams;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.stream.Stream;

/**
 * This class implements the MusicGroup interface, representing a music group with albums and songs.
 */
public final class MusicGroupImpl implements MusicGroup {

    private final Map<String, Integer> albums = new LinkedHashMap<>();
    private final Set<Song> songs = new LinkedHashSet<>();

    @Override
    public void addAlbum(final String albumName, final int year) {
        this.albums.put(albumName, year);
    }

    @Override
    public void addSong(final String songName, final Optional<String> albumName, final double duration) {
        if (albumName.isPresent() && !this.albums.containsKey(albumName.get())) {
            throw new IllegalArgumentException("invalid album name");
        }
        this.songs.add(new MusicGroupImpl.Song(songName, albumName, duration));
    }

    @Override
    public Stream<String> orderedSongNames() {
        final Set<String> songsName = new LinkedHashSet<>();
        songs.forEach(el -> songsName.add(el.getSongName()));
        //final Stream<String> songSorted = songsName.stream().sorted();
        return songsName.stream().sorted();
    }

    @Override
    public Stream<String> albumNames() {
        final Set<String> albumNamesSet = new LinkedHashSet<>();
        albums.forEach((name, year) -> albumNamesSet.add(name));
        //final Stream<String> albumNames = albumNamesSet.stream();
        return albumNamesSet.stream();
    }

    @Override
    public Stream<String> albumInYear(final int year) {
        final Set<String> albumInYearSet = new LinkedHashSet<>();
        albums.forEach((name, yearA) -> {
            if (yearA.equals(year)) {
                albumInYearSet.add(name);
            }
        });
        //final Stream<String> albumInYear = albumInYearSet.stream();
        return albumInYearSet.stream();
    }

    @Override
    public int countSongs(final String albumName) {
        final Optional<String> albumNameOp = Optional.of(albumName);
        final int[] nSongs = {0};
        songs.forEach(el -> {
            if (el.getAlbumName().equals(albumNameOp)) {
                nSongs[0]++;
            }
        });
        return nSongs[0];
    }

    @Override
    public int countSongsInNoAlbum() {
        final int[] nSongs = {0};
        songs.forEach(el -> {
            if (el.getAlbumName().isEmpty()) {
                nSongs[0]++;
            }
        });
        return nSongs[0];
    }

    @Override
    public OptionalDouble averageDurationOfSongs(final String albumName) {
        final double[] durationMedia = {0, 0, 0};
        final Optional<String> albumNameOp = Optional.of(albumName);
        songs.forEach(el -> {
            if (el.getAlbumName().equals(albumNameOp)) {
                durationMedia[0] += el.getDuration();
                durationMedia[1]++;
            }
        });
        durationMedia[2] = durationMedia[0] / durationMedia[1];
        //final OptionalDouble durationMediaOp = OptionalDouble.of(durationMedia[2]);
        return OptionalDouble.of(durationMedia[2]);
    }

    @Override
    public Optional<String> longestSong() {
        final List<Optional<String>> longestSong = new ArrayList<>();
        longestSong.add(Optional.empty());
        final double[] duration = {0.0};
        songs.forEach(el -> {
            if (el.getDuration() > duration[0]) {
                longestSong.add(0, Optional.of(el.getSongName()));
                duration[0] = el.getDuration();
            }
        });
        return longestSong.getFirst();
    }

    @Override
    public Optional<String> longestAlbum() {
        final List<Optional<String>> longestAlbum = new ArrayList<>();
        longestAlbum.add(Optional.empty());
        final double[] duration = {0.0};
        albums.forEach((name, year) -> {
            final Optional<String> albumOp = Optional.of(name);
            final double[] albumDuration = {0};
            songs.forEach(song -> {
                if (song.getAlbumName().equals(albumOp)) {
                    albumDuration[0] += song.getDuration();
                }
            });
            if (albumDuration[0] > duration[0]) {
                longestAlbum.add(0, albumOp);
            }
        });
        return longestAlbum.getFirst();
    }

    private static final class Song {

        private final String songName;
        private final Optional<String> albumName;
        private final double duration;
        private int hash;

        Song(final String name, final Optional<String> album, final double len) {
            super();
            this.songName = name;
            this.albumName = album;
            this.duration = len;
        }

        String getSongName() {
            return songName;
        }

        Optional<String> getAlbumName() {
            return albumName;
        }

        double getDuration() {
            return duration;
        }

        @Override
        public int hashCode() {
            if (hash == 0) {
                hash = Objects.hash(songName, albumName, duration);
            }
            return hash;
        }

        @Override
        public boolean equals(final Object obj) {
            return obj instanceof Song other
                && albumName.equals(other.albumName)
                && songName.equals(other.songName)
                && duration == other.duration;
        }

        @Override
        public String toString() {
            return "Song [songName=" + songName + ", albumName=" + albumName + ", duration=" + duration + "]";
        }

    }

}
