import math
import numpy
import keras

MODEL = '....//////'

state = [[0 for j in range(params.num_cols)] for i in range(params.num_rows)]

f = open(dirpath+'/temp_ai', 'r')
for row in range(params.num_rows):
	row_str = f.readline()[:-1]
	for col, val in enumerate(row_str):
		state[row][col] = 1 if val == '1' else -1

test_x = np.reshape(state, (100*100))

model = keras.models.load_model(MODEL)
pred_y = model.predict(test_x)
output = np.argmax(pred_y)
print(output)