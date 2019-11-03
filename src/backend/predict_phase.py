import os
import math
import numpy as np
import keras
import traceback

class Object(object):
        pass

dirpath = os.path.dirname(os.path.abspath(__file__))
MODEL = dirpath + '/../models/model_20191103-095924'
model = keras.models.load_model(MODEL)
params = Object()
params.num_cols = 100
params.num_rows = 100
state = [[0 for j in range(params.num_cols)] for i in range(params.num_rows)]

while True:
	with open(dirpath+'/temp_ai', 'r')as f2:
		for row in range(params.num_rows):
			row_str = f2.readline()[:-1]
			for col, val in enumerate(row_str):
				state[row][col] = 1 if val == '1' else -1
	
	test_x = np.array(state).reshape((1, 100*100,))
	pred_y = model.predict([test_x])
	output = np.argmax(pred_y)
	print("Prediction {}".format(output), flush = True)
	
	
	
        
        

