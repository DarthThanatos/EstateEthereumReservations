import os
import shutil


BOOT_N = 3
N = 3

#C:\Users\Admin\AppData\Roaming\Ethereum
#eth.getBalance(eth.accounts[0])

# bob's account: 8d50318432fd10fe29035c1736e60808b5cceed2, key: b5d577dc9ce59725e29886632e69ecdf3b6ca49c0a14f4315a2404fc1508672d
# alice's account: 43382647044a6003d6077e489698f10ab35935bc, key: a7dcef9aef26202fce82a7c7d6672afb3a149db207d90a07e437d5abc7fc99ed
# main's account: e47691a1d66ae4131cd880ca8b3cc93d21117c3a, key: 15c4a824ab2728a3f5e73b7a29bd6167ff6170b70b2e870974add0ba85e12624

START_BOOT_PORT = 30300
START_RPC_PORT = 8100

# BOOT_ADDR_1 = "192.168.0.100"
# BOOT_ADDR_2 = "192.168.0.104"
# BOOT_ADDR_3 = "192.168.0.101"

BOOT_ADDR_1 = "127.0.0.1"
BOOT_ADDR_2 = "127.0.0.1"
BOOT_ADDR_3 = "127.0.0.1"

ETH_NODE_BUT =\
	"geth --datadir=\"{}\" --verbosity 3 --ipcdisable --port {}" +\
	" --keystore C:\Users\Robert\AppData\Roaming\Ethereum\keystore " +\
	" --networkid 3765 --rpcport {} console  " +\
	" --bootnodes {} " +\
	" --netrestrict 127.0.0.0/24 " +\
	"\n\n"
	# " --netrestrict 192.168.0.0/24 " +\
	# --mine --minerthreads=1 --etherbase 0xe47691a1d66ae4131cd880ca8b3cc93d21117c3a 

all_boot_nodes = ""
for i in range(BOOT_N):
	with open("boot_addr{}".format(i), "r+") as f:
		content = f.read().replace("\n","")
		enode_1 = "enode://{}@{}:{}".format(content, BOOT_ADDR_1, START_BOOT_PORT - i)
		enode_2 = "enode://{}@{}:{}".format(content, BOOT_ADDR_2, START_BOOT_PORT - i)
		enode_3 = "enode://{}@{}:{}".format(content, BOOT_ADDR_3, START_BOOT_PORT - i)
		all_boot_nodes += enode_1 + ","
		all_boot_nodes += enode_2 + ","
		all_boot_nodes += enode_3 + ("," if i != BOOT_N -1 else " ")

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
		START_RPC_PORT + i,
		all_boot_nodes
	)
	print cmd
	os.system("geth --keystore C:\Users\Robert\AppData\Roaming\Ethereum\keystore init genesis.json --datadir " + directory )
	os.system("start cmd /K " + cmd)

