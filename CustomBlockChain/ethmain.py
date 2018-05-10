import os
import shutil


BOOT_N = 3
N = 3

USER="Robert"

#C:\Users\User\AppData\Roaming\Ethereum
#eth.getBalance(eth.accounts[0])

# bob's account: 8d50318432fd10fe29035c1736e60808b5cceed2, key: b5d577dc9ce59725e29886632e69ecdf3b6ca49c0a14f4315a2404fc1508672d
# alice's account: 43382647044a6003d6077e489698f10ab35935bc, key: a7dcef9aef26202fce82a7c7d6672afb3a149db207d90a07e437d5abc7fc99ed
# main's account: e47691a1d66ae4131cd880ca8b3cc93d21117c3a, key: 15c4a824ab2728a3f5e73b7a29bd6167ff6170b70b2e870974add0ba85e12624

START_BOOT_PORT = 30300
START_RPC_PORT = 8100

BOOT_ADDRS = [
	# "127.0.0.1", 
	"192.168.0.100", 
	# "192.168.0.104",
	"192.168.0.101"
]

ETH_ADDR_CIDR =  "192.168.0.0/24" #"127.0.0.0/24"

ETH_NODE_BUT =\
	"geth --datadir=\"{}\" --verbosity 3 --ipcdisable --port {}" +\
	" --keystore C:\Users\{}\AppData\Roaming\Ethereum\keystore " +\
	" --networkid 3765 --rpcport {} console  " +\
	" --bootnodes {} " +\
	" --netrestrict {} " +\
	"\n\n"

	# --mine --minerthreads=1 --etherbase 0xe47691a1d66ae4131cd880ca8b3cc93d21117c3a 

for i in range(BOOT_N):
	with open("boot_addr{}".format(i), "r+") as f:
		content = f.read().replace("\n","")
		all_boot_nodes = ""
		for j,addr in enumerate(BOOT_ADDRS):
			all_boot_nodes += "enode://{}@{}:{}".format(content, addr, START_BOOT_PORT - i) + ("," if j != BOOT_ADDRS.__len__() -1 else "")

try:
	os.mkdir("/tmp")
except Exception:
	print "/tmp exists"

try:
	os.mkdir("/tmp/eth")
except Exception:
	print "/tmp/eth exists"

try:
	os.mkdir("/tmp/eth/cluster")
except Exception:
	print "/tmp/eth/cluster exists"

for i in range(N):
	directory = "/tmp/eth/cluster/{}".format(i)

	try:
		shutil.rmtree(directory)
	except Exception:
		print directory, "does not exist, skipping removing operation"

	try:
		os.mkdir(directory)
	except Exception:
		print "directory", directory, "already exists"

	cmd = ETH_NODE_BUT.format(
		directory,
		START_BOOT_PORT + i + 1,
		USER,
		START_RPC_PORT + i,
		all_boot_nodes,
		ETH_ADDR_CIDR
	)
	print cmd
	os.system("geth --keystore C:\Users\{}\AppData\Roaming\Ethereum\keystore init genesis.json --datadir ".format(USER) + directory )
	os.system("start cmd /K " + cmd)

