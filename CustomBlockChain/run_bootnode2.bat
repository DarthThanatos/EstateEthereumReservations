bootnode.exe -genkey bootnode2.key
bootnode.exe -nodekey bootnode2.key -writeaddress > boot_addr2
bootnode.exe -nodekey bootnode2.key -addr "192.168.0.100:30299" -verbosity 9