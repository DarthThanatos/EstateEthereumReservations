package ethtest;

import java.util.List;
import java.util.stream.Collectors;

class EventHandler <T extends  SolEvent>{

    private int i = 0;
    private AccountsManager accountsManager;

    EventHandler(AccountsManager accountsManager){
        this.accountsManager = accountsManager;
    }

    void handle(T event){
        Object[] translatedAdresses =
                event.addressesToTranslate().stream()
                        .map(s -> accountsManager.getReadableNameFromHexForm(s)).collect(Collectors.toList()).toArray();
        if(CustomTest.DEVEL_PHASE){
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