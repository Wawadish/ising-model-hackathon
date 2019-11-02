import argparse
import random
import math

class Object(object):
    pass

k = 1
FLIPS_PER_EPOCH = 10

# Set up argument parser
parser = argparse.ArgumentParser(description='Process some integers.')
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

def compute_energy(configuration, params):
	contributions = 0
	for i in range(params.num_rows):
		for j in range(params.num_cols):
			spin0 = configuration[i][j]
			spin1 = configuration[i-1][j]
			spin2 = configuration[i][j-1]
			
			contributions += (spin0 * spin1)
			contributions += (spin0 * spin2)
	
	return params.j_constant * contributions
	
def compute_new_energy(flip, ising):
	x, y = flip
	state = ising.state
	
	spin0 = state[x][y]
	spin1 = state[x-1][y]
	spin2 = state[x][y-1]
	spin3 = state[(x+1)%ising.params.num_rows][y]
	spin4 = state[x][(y+1)%ising.params.num_cols]
	
	changes = 0
	def delta(a, b):
		return 4 * int(a == b) - 2
	
	changes += delta(spin0, spin1)
	changes += delta(spin0, spin2)
	changes += delta(spin0, spin3)
	changes += delta(spin0, spin4)
	
	return ising.energy + (ising.params.j_constant * changes)

def choose_potential_flips(params):
	num_flips = FLIPS_PER_EPOCH
	x_flips = random.choices(range(params.num_rows), k=num_flips)
	y_flips = random.choices(range(params.num_cols), k=num_flips)
	
	return set(zip(x_flips, y_flips))

def apply_flips(potential_flips, ising):
	changes = []
	gamma = 1/(k*ising.params.temperature)
	for flip in potential_flips:
		current_energy = ising.energy
		new_energy = compute_new_energy(flip, ising)
		accepted = new_energy < current_energy
		if not accepted:
			r = random.random()
			weight = math.exp(-gamma * (new_energy - current_energy))
			if r < weight:
				accepted = True
		
		if not accepted:
			continue
		
		x, y = flip
		changes.append(flip)
		ising.state[x][y] = -ising.state[x][y]
		ising.energy = new_energy
	
	return changes

def time_step(ising):
	flips = choose_potential_flips(ising.params)
	changes = apply_flips(flips, ising)
	
	for change in changes:
		print(change[0], change[1], end=' ')
		
	print(-1, end=' ')

initial_energy = compute_energy(initial_state, params)

ising_system = Object()
ising_system.state = initial_state
ising_system.energy = initial_energy
ising_system.params = params

for i in range(1_000_000):
	time_step(ising_system)