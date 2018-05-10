geth --datadir="/tmp/eth/cluster/02" --verbosity 3 --ipcdisable --port 30302 --networkid 3765 --rpcport 8102 console init custom_genesis.json ^
 --bootnodes ^
 enode://041ad56e9d2fbccdff25e77a7d7d1feb7af1bca7d8c3d36135894d12f9369958a2e9295d2c257b9fdc354e9a6e1a94631e05ab945d805e30e13f8699bb64b5ec@192.168.0.100:30300^
 ,enode://31621309ef00b4ddb83a9daa29c5802e098896b569e168973d61cea287ccdb47045d79c994c405c9b3975651cb3c4a47679816a2267dc5393a4e21744ad4a624@192.168.0.101:30300^
  --netrestrict 192.168.0.0/24 