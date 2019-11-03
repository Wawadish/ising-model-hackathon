import argparse
import random
import argparse
import random
import math
import ising_utils

class Object(object):
    pass

k = 1
FLIPS_PER_EPOCH = 10

# Set up argument parser
parser = argparse.ArgumentParser(description='Run Ising Model simulation.')
parser.add_argument('rows', type=int, help='Number of rows')
parser.add_argument('cols', type=int, help='Number of columns')
parser.add_argument('temperature', type=float, help='The temperature')
parser.add_argument('interaction_strength', type=float, help='The interaction strength constant')
parser.add_argument('data', type=str, help='el data')
parser.add_argument('--debug', help='Adds debug statements', action='store_true')

# Extract data
args = parser.parse_args()
raw_data = args.data
params = Object()
params.num_rows = args.rows
params.num_cols = args.cols
params.temperature = args.temperature
params.j_constant = args.interaction_strength
params.gamma = params.j_constant / (k*params.temperature)
params.debug = args.debug

if params.debug:
	print('Rows: {}\nColumns: {}\nTemperature: {}\nJ Constant: {}\nData: {}'
		.format(args.rows, args.cols, args.temperature, args.interaction_strength, args.data))

	critical = 2 * params.j_constant / (k * math.log(1+math.sqrt(2)))
	print('Critical Temperature: {:.3f}'.format(critical))

# Compute the initial state
initial_state = [[-1 for j in range(params.num_cols)] for i in range(params.num_rows)]
for idx, val in enumerate(raw_data):
	if val != '1':
		continue

	row = idx // params.num_cols
	col = idx % params.num_cols
	initial_state[row][col] = 1

# Print the initial state
if params.debug:
	print('\nInitial state: ')
	for row in initial_state:
		print(''.join('1' if x == 1 else '0' for x in row))

def time_step(ising):
	flips = ising_utils.choose_potential_flips(ising.params, FLIPS_PER_EPOCH)
	changes = ising_utils.apply_flips(flips, ising)
	
	for change in changes:
		print(change[0], change[1], end=' ')
		
	print(-1, end=' ')
		
initial_energy = ising_utils.compute_energy(initial_state, params)

ising_system = Object()
ising_system.state = initial_state
ising_system.energy = initial_energy
ising_system.params = params


for i in range(1_000_000):
	time_step(ising_system)