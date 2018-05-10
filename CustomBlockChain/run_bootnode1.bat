bootnode.exe -genkey bootnode1.key
bootnode.exe -nodekey bootnode1.key -writeaddress > boot_addr1
bootnode.exe -nodekey bootnode1.key -addr "192.168.0.100:30300" -verbosity 9