import keras
import numpy as np
import random
import time

num_classes = 2
for i in range(1, 10):
    data = np.load(f'../TrainingData/training_data_0{i}.npy', allow_pickle = True)
    train_x = data[0]
    train_y = data[1]
    temp = list(zip(train_x, train_y))
    random.shuffle(temp)
    train_x, train_y =  zip(*temp)

    labels = [[0, 1], [1, 0]]

    train_x = np.array([np.reshape(config, 100*100) for config in train_x])
    train_y = np.array([labels[y] for y in train_y])
    print(train_x.shape)
    print(train_y.shape)

    cutoff = int(0.25*len(train_x) )
    validation_x = train_x[:cutoff]
    train_x = train_x[cutoff:]
    validation_y = train_y[:cutoff]
    train_y = train_y[cutoff:]

    model = keras.models.Sequential()
    model.add(keras.layers.Dense(40, activation='sigmoid', input_shape=(100*100,)))
    model.add(keras.layers.Dense(40, activation='sigmoid'))
    model.add(keras.layers.Dense(2, activation='softmax'))

    model.compile(loss='categorical_crossentropy',
              optimizer='Adadelta',
              metrics=['accuracy'])

    model.fit(train_x, train_y, epochs=10, batch_size=1000, shuffle=True)

    loss_and_metrics = model.evaluate(validation_x, validation_y, batch_size=50)

    print(loss_and_metrics)

timestr = time.strftime("%Y%m%d-%H%M%S")
model.save(f'../models/model_{timestr}')







