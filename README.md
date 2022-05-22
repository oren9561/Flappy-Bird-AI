
# Flappy Bird AI

This is an AI for the game "Flappy bird" written in Java & Processing.  

I used the processing IDE to make this project (for no real reason).  
This is the first ever AI I built on my own. It wasn't easy, but it was sure as hell worth it.  
I'm extremely interested in AI technology and I hope to continue exploring it in the future.


## How to Install and Run the Project

* Download and install [processing](https://processing.org/download).
* Download the ZIP file for the project from my [GitHub Page](https://github.com/oren9561/Flappy-Bird-AI).  
* Launch the game and watch as the birds thrive
* If you want to review the code, launch the sketch through processing.

## How to play the simulation
### Keys

* There are none.

### How the simulation works

* Once the simulation begins a population of birds spawn. These birds jump according to their activation function (which is based on random values).
* Each bird gets a set of random values called "genes" which determine whether or not the bird will jump at a given moment depending on it's distance from the pipes. These calculations are made in the activation function.
* When all the birds die, the next generation spawns. If the last generation made progress, it's genes will be forwarded to the next.
* If no progress has been made, the next generation spawns with the best set of genes so far.

## Notes
* The collision mechanism in the game might look like it's not 100% accurate, but this is due to the high framerate - there's a small gap between the front to the back end so the user might see a collision when in actuality there wasn't one.
* It might initially seem like the birds make no progress whatsoever, but I assure you they'll get there eventually.
[<img align="Center" alt="Proof It Works" src="https://i.gyazo.com/9b4fa920eefacb905577905810c33fee.png"/>]

## Credits

- [@oren9561](https://github.com/oren9561)

### Connect with me:

[![website](https://i.gyazo.com/7c244728088109ecda95a87017e30012.png)](https://www.linkedin.com/in/oren9561/)
[![website](https://i.gyazo.com/01810428375ef3b58190c80979bda9a9.png)](https://github.com/oren9561)


# **ENJOY**