from vpython import *

# Set up the scene
scene = canvas(title='Ball Drop', width=800, height=600, center=vector(0, 50, 0), background=color.white)
floor = box(pos=vector(0, -0.5, 0), size=vector(100, 1, 100), color=color.gray(0.7))

# Set up the ball
ball = sphere(pos=vector(0, 100, 0), radius=5, color=color.red, make_trail=True)

# Set up the physics
g = 9.81 # acceleration due to gravity
dt = 0.1 # time step
t = 0 # initial time
v = vector(0, 0, 0) # initial velocity
a = vector(2, -g, 0) # acceleration vector

# Simulate the ball's motion
while ball.pos.y >= ball.radius:
    rate(100) # maximum 100 loops per second
    v = v + a * dt
    ball.pos = ball.pos + v * dt
    t += dt