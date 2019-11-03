import argparse
import random
import math

class Object(object):
    pass

def compute_energy(configuration, params):
	contributions = 0
	for i in range(params.num_rows):
		for j in range(params.num_cols):
			spin0 = configuration[i][j]
			spin1 = configuration[i-1][j]
			spin2 = configuration[i][j-1]
			
			contributions += (spin0 * spin1)
			contributions += (spin0 * spin2)
	
	return params.gamma * contributions
	
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
	
	return ising.energy + (ising.params.gamma * changes)

def choose_potential_flips(params, flips_per_epoch):
	num_flips = flips_per_epoch
	x_flips = random.choices(range(params.num_rows), k=num_flips)
	y_flips = random.choices(range(params.num_cols), k=num_flips)
	
	return set(zip(x_flips, y_flips))

def apply_flips(potential_flips, ising):
	changes = []
	for flip in potential_flips:
		current_energy = ising.energy
		new_energy = compute_new_energy(flip, ising)
		accepted = new_energy < current_energy
		if not accepted:
			r = random.random()
			weight = math.exp(-(new_energy - current_energy))
			if r < weight:
				accepted = True
		
		if not accepted:
			continue
		
		x, y = flip
		changes.append(flip)
		ising.state[x][y] = -ising.state[x][y]
		ising.energy = new_energy
	
	return changes