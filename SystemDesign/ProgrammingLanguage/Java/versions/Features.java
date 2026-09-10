package SystemDesign.ProgrammingLanguage.Java.versions;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

public class Features {

    <E> void streamOfNullable(Collection<E> collection) {
        Stream.ofNullable(collection)
                .takeWhile(Objects::nonNull)
                .dropWhile(Objects::isNull)
                .findAny();
    }

    public static void main(String[] args) {

    }
}