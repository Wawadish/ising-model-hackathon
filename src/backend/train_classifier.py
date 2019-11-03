import keras
import numpy as np

epochs = 3
batch_size = 50
num_classes = 2
data = np.load('test.npy')
train_x = data[0]
train_y = data[1]

cutoff = int(0.25*len(train_x) )
validation_x = train_x[:cutoff]
train_x = train_x[cutoff:]
validation_y = train_y[:cutoff]
train_y = train_y[cutoff:]

model = keras.models.Sequential()
model.add(keras.layers.Flatten())
model.add(keras.layers.Dense(40, activation='sigmoid'))
model.add(keras.layers.Dense(40, activation='sigmoid'))
model.add(keras.layers.Dense(2, activation='softmax'))

model.compile(loss='categorical_crossentropy',
              optimizer='Adadelta',
              metrics=['accuracy'])

model.fit(train_x, train_y, epochs, batch_size)

loss_and_metrics = model.evaluate(validation_x, validation_y, batch_size)






