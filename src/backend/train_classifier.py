import keras
import numpy as np
import random
import time

num_classes = 2
train_x = np.load('../training_data/unified_train_x.npy', allow_pickle = True)
train_y = np.load('../training_data/unified_train_y.npy', allow_pickle = True)

model = keras.models.Sequential()
model.add(keras.layers.Dense(40, activation='sigmoid', input_shape=(100*100,)))
model.add(keras.layers.Dense(40, activation='sigmoid'))
model.add(keras.layers.Dense(2, activation='softmax'))

model.compile(loss='categorical_crossentropy',
		  optimizer='Adadelta',
		  metrics=['accuracy'])
	
cutoff = int(0.25*len(train_x) )
validation_x = train_x[:cutoff]
train_x = train_x[cutoff:]
validation_y = train_y[:cutoff]
train_y = train_y[cutoff:]

model.fit(train_x, train_y, epochs=3, batch_size=1000, shuffle=True)
loss_and_metrics = model.evaluate(validation_x, validation_y, batch_size=50)
print(loss_and_metrics)

timestr = time.strftime("%Y%m%d-%H%M%S")
model.save(f'../models/model_{timestr}')







