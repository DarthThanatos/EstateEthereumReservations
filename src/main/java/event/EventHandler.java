package event;

import main.Main;
import ethereum.AccountsManager;

import java.util.Arrays;
import java.util.stream.Collectors;

public class EventHandler <T extends SolEvent>{

    private int i = 0;
    private AccountsManager accountsManager;

    public EventHandler(AccountsManager accountsManager){
        this.accountsManager = accountsManager;
    }

    public void handle(T event){
        Object[] translatedAdresses =
                event.addressesToTranslate().stream()
                        .map(s -> accountsManager.getReadableNameFromHexForm(s)).collect(Collectors.toList()).toArray();
        if(Main.DEVEL_PHASE){
            if(i == 0) {
                System.out.println(
                        String.format(
                                event.toString(),
                                translatedAdresses
                        )
                );
                i = 1;
            }
            else i = 0;

        }
        else{
            System.out.println(
                    String.format(
                            event.toString(),
                            translatedAdresses
                    )
            );
        }
    }
}