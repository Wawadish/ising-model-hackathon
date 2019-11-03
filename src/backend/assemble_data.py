import random
import numpy as np

train_x = []
train_y = []
labels = [[0, 1], [1, 0]]
for i in range(1, 10):
	print('Assembling {}'.format(i), flush=True)
	data = np.load('../TrainingData/training_data_0{}.npy'.format(i), allow_pickle = True)
	for x, y in zip(data[0], data[1]):
		train_x.append(np.reshape(x, 100*100))
		train_y.append(labels[y])

temp = list(zip(train_x, train_y))
random.shuffle(temp)
train_x, train_y = zip(*temp)

np.save('unified_train_x.npy', train_x)
np.save('unified_train_y.npy', train_y)
