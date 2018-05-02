package cli;

import jline.console.completer.Completer;

import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import static java.util.Arrays.asList;

public class StringsCompleter implements Completer {

    private final SortedSet<String> strings = new TreeSet<>();

    private StringsCompleter(Collection<String> strings) {
        this.strings.addAll(strings);
    }

    StringsCompleter(String... strings) {
        this(asList(strings));
    }

    @Override
    public int complete(String buffer, int cursor, List<CharSequence> candidates) {
        if (buffer == null) {
            candidates.addAll(strings);
        } else {
            for (String match : strings.tailSet(buffer)) {
                if (!match.startsWith(buffer)) {
                    break;
                }
                candidates.add(match);
            }
        }
        if (candidates.size() == 1) {
            candidates.set(0, candidates.get(0) + " ");
        }
        return candidates.isEmpty() ? -1 : 0;
    }
}