bootnode.exe -genkey bootnode3.key
bootnode.exe -nodekey bootnode3.key -writeaddress > boot_addr3
bootnode.exe -nodekey bootnode3.key -addr "192.168.0.100:30298" -verbosity 9