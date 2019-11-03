import argparse
import random
import math
import numpy
import ising_utils

class Object(object):
    pass

FLIPS_PER_EPOCH = 10
INITIAL_WAIT = 5_000
SNAPSHOT_INTERVAL = 1000
SNAPSHOT_COUNT = 10
RUNS_COUNT = 100

# Set up argument parser
parser = argparse.ArgumentParser(description='Generate Ising Model data')
parser.add_argument('rows', type=int, help='Number of rows')
parser.add_argument('cols', type=int, help='Number of columns')
parser.add_argument('output_file', type=str, help='The output file path')

# Extract data
args = parser.parse_args()
params = Object()
params.num_rows = args.rows
params.num_cols = args.cols

def time_step(ising):
	flips = ising_utils.choose_potential_flips(ising.params, FLIPS_PER_EPOCH)
	changes = ising_utils.apply_flips(flips, ising)
	
def run_simulation(is_ferro, params, snapshots, labels):
	# Generate initial state
	initial_state = [random.choices([-1, 1], k=params.num_cols) for i in range(params.num_rows)]
	initial_energy = ising_utils.compute_energy(initial_state, params)

	# Prepare the system
	ising_system = Object()
	ising_system.state = initial_state
	ising_system.energy = initial_energy
	ising_system.params = params
	
	# Let the system run for the beginning to reach some more stable state
	for i in range(INITIAL_WAIT):
		time_step(ising_system)
	
	# Start taking snapshots
	for epoch in range((SNAPSHOT_COUNT-1) * SNAPSHOT_INTERVAL + 1):
		time_step(ising_system)
		
		if epoch % SNAPSHOT_INTERVAL == 0:
			labels.append(int(is_ferro))
			snapshots.append(ising_system.state.copy())
			
	return snapshots
	
snapshots = []
labels = []
critical_gamma = 0.5 * math.log(1 + math.sqrt(2))

# Iterate from 0.05 to 0.85
for i in range(1, 17):
	gamma = 0.05 * i
	params.gamma = gamma
	is_ferro = (gamma < critical_gamma)
	print("GENERATING FOR GAMMA {}. Snapshots: {}".format(gamma, len(snapshots)), flush=True)
	for runs in range(RUNS_COUNT):
		if (runs+1) % 25 == 0:
			print("Completing run {}".format(runs+1), flush=True)
		run_simulation(is_ferro, params, snapshots, labels)

numpy.save(args.output_file + ".npy", snapshots)
