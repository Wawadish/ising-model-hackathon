import argparse

parser = argparse.ArgumentParser(description='Process some integers.')
parser.add_argument('rows', type=int, help='Number of rows')
parser.add_argument('cols', type=int, help='Number of columns')
parser.add_argument('gamma', type=float, help='The gamma ratio')
parser.add_argument('data', type=str, help='el data')

args = parser.parse_args()
print('Rows: {}\nColumns: {}\nGamma: {}\nData: {}\n'
		.format(args.rows, args.cols, args.gamma, args.data))
		
num_rows = args.rows
num_cols = args.cols
gamma = args.gamma
raw_data = args.data

grid = [[0 for j in range(num_cols)] for i in range(num_rows)]

for idx, val in enumerate(raw_data):
	if val != '1':
		continue

	row = idx // num_cols
	col = idx % num_cols
	grid[row][col] = 1

for row in grid:
	print(''.join(str(x) for x in row))