import matplotlib.pyplot as plt

data = []
file = open("SimpleSimulator/New_Output.txt","r")
for line in file.readlines():
    data.append(int(line))

x = []
y = []

for i in range(data[0]):
    x.append(i)
    y.append(i)

plt.plot(x,y)

plt.xlabel("No of Cycles")
plt.ylabel("Memory Address")

plt.show()