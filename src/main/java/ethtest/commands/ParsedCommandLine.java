package ethtest.commands;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ParsedCommandLine{

    public boolean empty;

    public String command;
    List<String> args;

    public ParsedCommandLine parse(String commandLine){
        String[] parts = commandLine.split(" ");
        empty = parts.length == 0;
        if(empty) {
            return this;
        }
        command = parts[0];
        args = Arrays.stream(parts).skip(1).collect(Collectors.toList());
        return this;
    }
}