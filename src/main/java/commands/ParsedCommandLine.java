package commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ParsedCommandLine{

    public boolean empty;

    public String command;
    List<String> args;

    public ParsedCommandLine parse(String commandLine){
        List<String> parts = Arrays.stream(commandLine.split(" ")).filter(s -> !s.equals("")).collect(Collectors.toList());
        empty = parts.isEmpty();
        if(empty) {
            return this;
        }
        command = trimmed(parts.get(0));
        args = parts.stream().skip(1).map(this::trimmed).collect(Collectors.toList());
        return this;
    }

    private String trimmed(String in){
        return in.replaceAll("\\s", "");
    }
}