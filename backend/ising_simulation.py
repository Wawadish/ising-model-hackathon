import argparse 

parser = argparse.ArgumentParser(description='Process some integers.')
parser.add_argument('rows', type=int, help='Number of rows')
parser.add_argument('cols', type=int, help='Number of columns')
parser.add_argument('gamma', type=float, help='The gamma ratio')
parser.add_argument('data', type=str, help='el data')

args = parser.parse_args()
print('Rows: {}\n Columns: {}\n Gamma: {}\n Data: {}"
		.format(args.rows, args.cols, args.gamma, args.data))