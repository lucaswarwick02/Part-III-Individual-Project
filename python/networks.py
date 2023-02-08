import networkx as nx
import matplotlib.pyplot as plt
import math
from tqdm import tqdm, trange
import random


def number_of_stubs (nodes_per_prob):
    stubs = 0
    for i in range(0, len(nodes_per_prob)):
        stubs += (i + 1) * nodes_per_prob[i]
    return stubs


def poisson (k: int, z: float) -> float:
    return ((math.pow(z, k)) * (math.exp(-z))) / math.factorial(k)


def get_neighbours(node: int, number_of_nodes: int, n: int) -> list[int]:
    return random.sample([i for i in range(number_of_nodes) if i != node], n)


def generate_poisson_sequence (z: int, population: int, max_degree: int):
    probabilities = [poisson(k, z) for k in range(1, max_degree)]  # Poisson Distribution
    
    nodes_per_prob = [math.ceil(p * population) for p in probabilities]
    nodes_left = population - sum(nodes_per_prob)
    average_degree = round(sum([k * poisson(k, z) for k in range(1, max_degree)]))  # Poisson Distribution
    while nodes_left > 0:
        if (nodes_left == 1):
            if (number_of_stubs(nodes_per_prob) + average_degree) % 2 == 0:
                # No stubs left over
                nodes_per_prob[average_degree - 1] += 1
            else:
                # 1 Stub left over, add 1 to average degree
                nodes_per_prob[average_degree] += 1
        else:
            nodes_per_prob[average_degree - 1] += 1 # Add the node into the distribution
        nodes_left -= 1
        
    return nodes_per_prob


def generate_random_network(g, number_of_nodes=2500, max_degree=25):
    nodes_per_prob = generate_poisson_sequence(4, number_of_nodes, max_degree)

    g.add_nodes_from([i for i in range(number_of_nodes)])
    
    current_node = 0
    for i, count in enumerate(nodes_per_prob):
        degree = i + 1
        for j in range(count):
            current_node_neighbours = get_neighbours(current_node, number_of_nodes, degree)
            for n in current_node_neighbours:
                g.add_edge(current_node, n)
            current_node += 1
            
            
def polylogarithm (s: float, z: float, precision=100) -> float:
    power_sum = 0
    for k in range(1, precision):
        power_sum += math.pow(z, k) / math.pow(k, s)
    return power_sum
            

def powerlaw (k: int, gamma: float, kappa: float):
    return (math.pow(k, -gamma) * math.exp(-k / kappa)) / polylogarithm(gamma, math.exp(-1 / kappa))
            
            
def generate_powerlaw_sequence (kappa: int, population: int):
    probabilities = [powerlaw(k, gamma=2, kappa=kappa) for k in range(1, kappa)]  # Power-Law Distribution
    
    nodes_per_prob = [math.ceil(p * population) for p in probabilities]
    nodes_left = population - sum(nodes_per_prob)
    average_degree = round(sum([k * powerlaw(k, gamma=2, kappa=kappa) for k in range(1, kappa)]))  # Power-Law Distribution
    print(f'Nodes Left: {nodes_left}')
    while nodes_left > 0:
        if (nodes_left == 1):
            if (number_of_stubs(nodes_per_prob) + average_degree) % 2 == 0:
                # No stubs left over
                nodes_per_prob[average_degree - 1] += 1
            else:
                # 1 Stub left over, add 1 to average degree
                nodes_per_prob[average_degree] += 1
        else:
            nodes_per_prob[average_degree - 1] += 1 # Add the node into the distribution
        nodes_left -= 1
        
    return nodes_per_prob


def generate_scalefree_network(g, number_of_nodes=2500):
    nodes_per_prob = generate_powerlaw_sequence(25, number_of_nodes)

    g.add_nodes_from([i for i in range(number_of_nodes)])
    
    current_node = 0
    for i, count in enumerate(nodes_per_prob):
        degree = i + 1
        for j in range(count):
            current_node_neighbours = get_neighbours(current_node, number_of_nodes, degree)
            for n in current_node_neighbours:
                g.add_edge(current_node, n)
            current_node += 1


if __name__ == '__main__':
    r = nx.Graph()
    generate_random_network(r, number_of_nodes=500)
    nx.draw(r, node_size=1, with_labels=False)
    plt.show()
    
    sf = nx.Graph()
    generate_scalefree_network(sf, number_of_nodes=500)
    nx.draw(sf, node_size=1, with_labels=False)
    plt.show()
    
    auto = nx.scale_free_graph(500)
    nx.draw(auto, node_size=1, with_labels=False)
    plt.show()