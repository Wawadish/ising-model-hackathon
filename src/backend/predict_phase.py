import math
import numpy as np
import keras
import os

class Object(object):
        pass

dirpath = os.path.dirname(os.path.abspath(__file__))

MODEL = dirpath + '/../models/model_20191103-095924'

params = Object()
params.num_cols = 100
params.num_rows = 100
state = [[0 for j in range(params.num_cols)] for i in range(params.num_rows)]

f = open(dirpath+'/temp_ai', 'r')
for row in range(params.num_rows):
        row_str = f.readline()[:-1]
        for col, val in enumerate(row_str):
                state[row][col] = 1 if val == '1' else -1

test_x = np.array(state).reshape((1, 100*100,))
print(test_x.shape)

model = keras.models.load_model(MODEL)
pred_y = model.predict([test_x])
output = np.argmax(pred_y)
print("Prediction {}".format(output))
        

